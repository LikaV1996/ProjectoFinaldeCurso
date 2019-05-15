/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IUserController;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController implements IUserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        List<User> userList = userService.getAllUsers(user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_GET_USERS);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(userList);
    }

    @Override
    public ResponseEntity<User> createUser(HttpServletRequest request, @RequestBody InputUser body) {

        User user = (User) request.getAttribute("user");

        body.validateForCreate();
        long userId = userService.createUser(body, user);

        User addUser = userService.getUser(userId, user);
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_GET_USERS);
        //serverLogService.createServerLog(serverLog);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_GET_USER_BY_ID, userId);

        return ResponseEntity.created(createdURI).body(addUser);
    }

    @Override
    public ResponseEntity<User> updateUser(HttpServletRequest request, @PathVariable("user-id") long userId, @RequestBody InputUser body) {

        User user = (User) request.getAttribute("user");

        body.validateForUpdate();
        User updateUser = userService.getUser(userId, user);

        userService.updateUser(updateUser, body, user);

        updateUser = userService.getUser(userId, user);
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_GET_USERS);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(updateUser);
    }

    @Override
    public ResponseEntity<User> getUser(HttpServletRequest request, @PathVariable("user-id") long userId) {

        User user = (User) request.getAttribute("user");

        User getUser = userService.getUser(userId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_GET_USER_BY_ID, userId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(getUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, @PathVariable("user-id") long userId) {

        User user = (User) request.getAttribute("user");

        userService.deleteUser(userId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_GET_USER_BY_ID, userId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<User> suspendUser(HttpServletRequest request, @PathVariable("user-id") long userId){

        User user = (User) request.getAttribute("user");

        User suspendedUser = userService.getUser(userId, user);

        userService.suspendUser(suspendedUser, user);

        suspendedUser = userService.getUser(userId, user);
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_GET_USER_BY_ID, userId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(suspendedUser);
    }

}
