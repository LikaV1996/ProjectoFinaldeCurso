/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.controller.exception.UnauthorizedException;
import pt.solvit.probe.server.model.Login;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IAuthenticationRepository;
import pt.solvit.probe.server.repository.api.IUserRepository;
import pt.solvit.probe.server.repository.exception.impl.UserAlreadyExistsException;
import pt.solvit.probe.server.repository.model.UserDao;
import pt.solvit.probe.server.service.api.IAuthenticationService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.AdminNotDeletableException;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaRita
 */
@Service
public class AuthenticationService implements IAuthenticationService {


    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    @Autowired
    private IAuthenticationRepository authenticationRepository;


    @Override
    public Login login(String username, String password) {
        LOGGER.log(Level.INFO, "Logging in user/Getting login token");

        //UserDao userDao =
        authenticationRepository.findByNameAndPassword(username,password);

        String token = username + ":" + password;
        return new Login(new String(Base64.getEncoder().encode( token.getBytes() )));    //encoded token
    }

    @Override
    public User getLoggedInUser(long userID) {
        LOGGER.log(Level.INFO, "Getting logged in user");

        UserDao userDao = authenticationRepository.findById(userID);

        return transformToUser(userDao);
    }

    @Override
    public User getAuthenticatedUser(String username, String password) {
        LOGGER.log(Level.INFO, "Getting authenticated user");

        UserDao userDao = authenticationRepository.findByNameAndPassword(username, password);

        return transformToUser(userDao);
    }


    private User transformToUser(UserDao userDao) {
        UserProfile userProfile = UserProfile.valueOf(userDao.getUserProfile());

        return new User(userDao.getId(), userDao.getUserName(), userDao.getUserPassword(), userProfile,
                userDao.getCreator(), userDao.getCreationDate().toLocalDateTime(),
                userDao.getModifier(), userDao.getModifiedDate() != null ? userDao.getModifiedDate().toLocalDateTime() : null,
                userDao.getSuspended()
        );
    }
}





