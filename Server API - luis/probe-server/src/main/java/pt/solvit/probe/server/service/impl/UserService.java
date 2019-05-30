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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.model.UserDao;
import pt.solvit.probe.server.repository.exception.impl.UserAlreadyExistsException;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.AdminNotDeletableException;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.repository.api.IUserRepository;

/**
 *
 * @author AnaRita
 */
@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private IUserRepository userRepository;

    @Override
    public long createUser(InputUser input, User loggedInUser) {

        checkIfUserAlreadyExists(input.getUserName());

        User addUser = User.makeUser(input, loggedInUser.getUserName());

        if( !checkLoggedInUserPermissions(loggedInUser, UserProfile.ADMIN) &&
                !checkLoggedInUserPermissionsLowerThanUser(loggedInUser, addUser) )
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Creating new user");
        UserDao userDao = User.transformToUserDao(addUser);
        return userRepository.add(userDao);
    }

    @Override
    public void updateUser(User userToUpdate, InputUser input, User loggedInUser) {
        if( !checkLoggedInUserPermissionsLowerThanUser(loggedInUser, userToUpdate) )
            throw new PermissionException();

        if ( input.getUserName() != null && !input.getUserName().equals( userToUpdate.getUserName() ) )
            checkIfUserAlreadyExists(userToUpdate.getUserName());

        userToUpdate = User.updateUser(userToUpdate, input, loggedInUser);

        LOGGER.log(Level.INFO, "Updating user {0}", userToUpdate.getUserName());
        userRepository.updateByID( User.transformToUserDao(userToUpdate) );
    }

    @Override
    public User getUser(long userId, User loggedInUser) {
        if( !checkLoggedInUserPermissions(loggedInUser, UserProfile.SUPER_USER) )
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Finding user {0}", userId);
        UserDao userDao = userRepository.findById(userId);
        return UserDao.transformToUser(userDao);

    }

    @Override
    public List<User> getAllUsers(User loggedInUser) {
        if( !checkLoggedInUserPermissions(loggedInUser, UserProfile.SUPER_USER) )
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Finding all users");
        List<UserDao> userList = userRepository.findAll();
        return ServiceUtil.map(userList, UserDao::transformToUser); //make sure doesn't blow up (previously -> this::transformToUser     function was in this class)
    }

    @Override
    public void deleteUser(long userId, User loggedInUser) {
        if( !checkLoggedInUserPermissions(loggedInUser, UserProfile.ADMIN) )
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Checking if user {0} exists", userId);
        UserDao userDao = userRepository.findById(userId);

        if (UserProfile.valueOf(userDao.getUserProfile()) == UserProfile.ADMIN) {
            throw new AdminNotDeletableException();
        }

        LOGGER.log(Level.INFO, "Deleting user {0}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void suspendUser(User userToSuspend, User loggedInUser) {
        if( !checkLoggedInUserPermissionsLowerThanUser(loggedInUser, userToSuspend) )
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Suspending user {0}", userToSuspend.getId());

        userToSuspend = User.updateUser(userToSuspend, null, loggedInUser);
        userToSuspend.setSuspended( !userToSuspend.getSuspended() );

        userRepository.updateByID( User.transformToUserDao(userToSuspend) );
    }


    /*
    @Override
    public User checkUserCredentials(String authorization) {
        LOGGER.log(Level.INFO, "Checking user credentials");

        LOGGER.log(Level.FINE, "Header Received: {0}", authorization);
        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                StandardCharsets.UTF_8);
        LOGGER.log(Level.FINE, "Header Decoded: {0}", credentials);
        String[] values = credentials.split(":", 2); // credentials = username:password

        LOGGER.log(Level.INFO, "Checking if user {0} exists", values[0]);
        UserDao userDao;
        try {
            userDao = userRepository.findByName(values[0]);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UnauthorizedException("You are not authorized to make that request.", "Your credentials are invalid.", "string", "about:blank");
        }

        LOGGER.log(Level.INFO, "Validating user credentials");
        if (!values[1].equals(userDao.getUserPassword())) {
            throw new UnauthorizedException("You are not authorized to make that request.", "Your credentials are invalid.", "string", "about:blank");
        }
        LOGGER.log(Level.INFO, "Authentication completed");

        return UserDao.transformToUser(userDao);
    }
    */

    //@Override
    //public void checkUserPermissionsForUpdate(User user, User loggedInUser) {
    //    LOGGER.log(Level.INFO, "Checking user {0} permissions", user.getUserName());
    //    if (user.getUserProfile().getLevel() >= loggedInUser.getUserProfile().getLevel()) {
    //        throw new PermissionException();
    //    }
    //}

    @Override
    public boolean checkLoggedInUserPermissions(User loggedInUser, UserProfile requiredProfile) {
        LOGGER.log(Level.INFO, "Checking user {0} permissions", loggedInUser.getUserName());
        return loggedInUser.getUserProfile().getLevel() >= requiredProfile.getLevel();
    }

    @Override
    public boolean checkLoggedInUserPermissionsLowerThanUser(User loggedInUser, User user) {
        LOGGER.log(Level.INFO, "Checking user {0} permissions", loggedInUser.getUserName());
        return loggedInUser.getUserProfile().getLevel() > user.getUserProfile().getLevel();
    }

    private void checkIfUserAlreadyExists(String userName) {
        LOGGER.log(Level.INFO, "Checking if user name already exists");
        try {
            userRepository.findByName(userName);
            throw new UserAlreadyExistsException();
        } catch (IncorrectResultSizeDataAccessException e) {
            //Ignore
        }
    }



}
