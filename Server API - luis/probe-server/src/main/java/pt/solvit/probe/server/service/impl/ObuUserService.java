/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.ObuUserRole;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IObuUserRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.exception.impl.ObuUserAlreadyExistsException;
import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.model.ObuUserDao;
import pt.solvit.probe.server.service.api.IObuUserService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.*;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaRita
 */
@Service
public class ObuUserService implements IObuUserService {

    private static final Logger LOGGER = Logger.getLogger(ObuUserService.class.getName());

    @Autowired
    private IUserService userService;
    @Autowired
    private IObuUserRepository obuUserRepository;



    @Override
    public long createObuUserRegistry(ObuUser obuUser, User loggedInUser) {
        LOGGER.log(Level.INFO, "Creating new obu_user registry.");

        checkIfObuUserRegistryAlreadyExists(obuUser);

        //check permissions
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();


        User userToCheck = userService.getUser(obuUser.getUserID(), loggedInUser);

        //if normal_user
        if ( ! userService.checkUserPermissions(userToCheck, UserProfile.SUPER_USER))
            //if is EDITOR
            if (obuUser.getRole() == ObuUserRole.EDITOR)
                throw new NormalObuUserEditorException(userToCheck.getUserName());

        return obuUserRepository.add(ObuUser.transformToObuUserDao(obuUser));
    }


    /*
    //not implemented
    @Override
    public ObuUser getObuUserByID(long obuID, long userID, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding registry with obu ID {0} and user ID {1}", new long[]{obuID, userID});

        checkLoggedInUserHasPermissions(userID, loggedInUser);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        ObuDao obuDao = obuRepository.findById(obuID, userID);
        return ObuDao.transformToObu(obuDao);
    }
    */

    @Override
    public List<ObuUser> getAllObuUserByUserID(long userID, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding registry with user ID {0}", userID);

        //check permissions
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        //check logged in user has more permissions than user to get
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            if (userID != loggedInUser.getId()) {
                User userToCheck = userService.getUser(userID, loggedInUser);
                userService.checkLoggedInUserPermissionsHigherThanUser(loggedInUser, userToCheck);
            }
        }


        List<ObuUserDao> obuUserDaoList = obuUserRepository.findAllByUserId(userID);
        return ServiceUtil.map(obuUserDaoList, ObuUserDao::transformToObuUser);
    }

    /*
    @Override
    public List<ObuUser> getAllObuUserByObuID(long obuId, User loggedInUser) {
        return null;
    }
    */

    @Override
    public List<ObuUser> getAllObuUser(User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding all registries");

        //check permissions
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        List<ObuUserDao> obuUserDaoList = obuUserRepository.findAll();
        return ServiceUtil.map(obuUserDaoList, ObuUserDao::transformToObuUser);
    }

    @Override
    public void updateObuUser(ObuUser obuUser, User loggedInUser) {
        LOGGER.log(Level.INFO, "Updating obu_user registry with obu ID {0} and user ID {1}", new long[]{obuUser.getObuID(), obuUser.getUserID()});

        //not at least superuser
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) )
            throw new PermissionException();

        //if self update
        if ( obuUser.getUserID().equals(loggedInUser.getId()) )
            throw new ObuUserSelfRoleChangeException();


        //get user to update
        User userToCheck = userService.getUser(obuUser.getUserID(), loggedInUser);

        //if user to update is admin
        if ( userService.checkUserPermissions(userToCheck, UserProfile.ADMIN) )
            throw new AdminNeedsNoRegistryException(userToCheck.getUserName());

        //if self permission <= user to update permission
        if ( ! userService.checkLoggedInUserPermissionsHigherThanUser(loggedInUser, userToCheck) )
            throw new PermissionException();


        //if self is not admin
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            //if this doesn't throw an exception it's because logged in user has connection to OBU
            ObuUserDao selfObuUserDao = obuUserRepository.findById(obuUser.getObuID(), loggedInUser.getId());

            //if self is only a viewer of the obu
            if (ObuUserRole.valueOf(selfObuUserDao.getRole()) == ObuUserRole.VIEWER)
                throw new ObuUserInsufficientRoleException();
        }


        obuUserRepository.update(ObuUser.transformToObuUserDao(obuUser));
    }

    @Override
    public void deleteObuUser(long obuID, long userID, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if obu_user registry with obu ID {0} and user ID {1} exists", new long[]{obuID,userID});

        //not at least superuser
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        //if not self delete
        if ( userID != loggedInUser.getId() ) {

            //if self not admin
            if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) )
                //if this doesn't throw an exception it's because logged in user has connection to OBU
                /*ObuUserDao selfObuUserDao = */obuUserRepository.findById(obuID, loggedInUser.getId());


            User userToCheck = userService.getUser(userID, loggedInUser);

            //check logged in user has more permissions than user to delete
            if ( ! userService.checkLoggedInUserPermissionsHigherThanUser(loggedInUser, userToCheck) )
                throw new PermissionException();

            if ( userService.checkUserPermissions(userToCheck, UserProfile.ADMIN) )
                throw new AdminNeedsNoRegistryException(userToCheck.getUserName());
        }


        LOGGER.log(Level.INFO, "Deleting obu_user registry with obu ID {0} and user ID {1}", new long[]{obuID,userID});
        obuUserRepository.deleteById(obuID,userID);
    }






    private void checkIfObuUserRegistryAlreadyExists(ObuUser obuUser) {
        LOGGER.log(Level.INFO, "Checking if bu_user registry already exists.");
        try {
            obuUserRepository.findById(obuUser.getObuID(), obuUser.getUserID());
            throw new ObuUserAlreadyExistsException();
        } catch (EntityWithIdNotFoundException ex) {
            //ignore
        }
    }

}
