/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.model.enums.ObuUserRole;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IObuConfigRepository;
import pt.solvit.probe.server.repository.api.IObuTestPlanRepository;
import pt.solvit.probe.server.repository.api.IObuUserRepository;
import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.model.ObuUserDao;
import pt.solvit.probe.server.service.api.IObuService;
import pt.solvit.probe.server.service.exception.impl.*;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.service.api.IUserService;

/**
 *
 * @author AnaRita
 */
@Service
public class ObuService implements IObuService {

    private static final Logger LOGGER = Logger.getLogger(ObuService.class.getName());

    @Autowired
    private IUserService userService;
    @Autowired
    private IObuRepository obuRepository;
    @Autowired
    private IObuConfigRepository obuConfigRepository;
    @Autowired
    private IObuTestPlanRepository obuTestPlanRepository;
    @Autowired
    private IObuUserRepository obuUserRepository;


    @Override
    public long createObu(Obu obu, User loggedInUser) {
        LOGGER.log(Level.INFO, "Creating new obu");

        if (!userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        return obuRepository.add(Obu.transformToObuDao(obu));
    }

    /*
    @Override
    public Obu getObuByID(long obuId) {
        ObuDao obuDao = obuRepository.findById(obuId, null);
        return ObuDao.transformToObu(obuDao);
    }
    */

    @Override
    public Obu getObuByID(long obuId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding obu with ID {0}", obuId);

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        ObuDao obuDao = obuRepository.findById(obuId, userID);
        return ObuDao.transformToObu(obuDao);
    }

    @Override
    public List<Obu> getObusWithHardware(long hardwareId) {
        LOGGER.log(Level.INFO, "Finding obus with hardware ID {0}", hardwareId);
        List<ObuDao> obuDaoList = obuRepository.findByHardwareId(hardwareId);
        return ServiceUtil.map(obuDaoList, ObuDao::transformToObu);
    }

    @Override
    public List<Obu> getAllObus(User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding all obus");

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        List<ObuDao> obuDaoList = obuRepository.findAll(userID);
        return ServiceUtil.map(obuDaoList, ObuDao::transformToObu);
    }

    @Override
    public void updateObu(Obu obu, User loggedInUser) {
        LOGGER.log(Level.INFO, "Updating obu with id {0}", obu.getId());

        //not at least superuser
        LOGGER.log(Level.INFO, "Checking user permissions for obu update");
        if (!userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        //check if superuser is editor or viewer
        if (!userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            checkObuUserRoleIsEDITOR(obu.getId(), loggedInUser.getId());

        obuRepository.update(Obu.transformToObuDao(obu));
    }

    @Override
    public void deleteObu(long obuId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if obu {0} exists", obuId);

        //not at least superuser
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        ObuDao obuDao = obuRepository.findById(obuId, null);


        //check if superuser...
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN)) {
            //...is editor or viewer
            checkObuUserRoleIsEDITOR(obuId, loggedInUser.getId());

            //...owns obu
            userOwnsObu(obuDao, loggedInUser);
        }

        

        //check if obu has config
        if ( ! obuConfigRepository.findByObuId(obuId).isEmpty())
            throw new ObuHasConfigException();

        //check if obu has testplans
        if ( ! obuTestPlanRepository.findByObuId(obuId).isEmpty())
            throw new ObuHasTestPlanException();

        //obu not active    TODO check ObuState for delete
        if ( ! obuDao.getObuState().equals(ObuState.READY.name()))
            throw new ObuActiveException();

        LOGGER.log(Level.INFO, "Deleting obu {0}", obuId);
        obuRepository.deleteById(obuId);
    }


    private void userOwnsObu(ObuDao obuDao, User user) {
        LOGGER.log(Level.INFO, "Checking if user {0} owns obu {1}", new String[]{user.getUserName(), String.valueOf(obuDao.getId())});
        if (!obuDao.getCreator().equals(user.getUserName())) {
            throw new EntityOwnershipException(EntityType.OBU);
        }
    }


    private Long getUserIdIfNotAdmin(User loggedInUser){
        return userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ? null : loggedInUser.getId();
    }

    private void checkObuUserRoleIsEDITOR(long obuID, long userID) {
        ObuUserDao obuUserDao = obuUserRepository.findById(obuID, userID);
        if (obuUserDao.getRole().equals(ObuUserRole.VIEWER.toString()))
            throw new PermissionException();
    }

}
