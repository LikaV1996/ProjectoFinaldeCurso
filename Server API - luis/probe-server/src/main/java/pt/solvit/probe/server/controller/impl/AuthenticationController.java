/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

//import jdk.nashorn.internal.objects.NativeJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IAuthenticationController;
import pt.solvit.probe.server.controller.api.IUserController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputLogin;
import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.Login;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.service.api.IAuthenticationService;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AuthenticationController implements IAuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<Login> getLoginToken(HttpServletRequest request, @RequestBody InputLogin body) {

        body.validate();

        User loggedInUser = new User(body.getUsername());
        request.setAttribute("user", loggedInUser); //TODO for logging, is it worth logging the failed logins to this user?

        Login login = authenticationService.login(body.getUsername(), body.getPassword());

        //ServerLog serverLog = ControllerUtil.transformToServerLog(body.getUsername(), RequestMethod.POST, HttpStatus.OK, AppConfiguration.URL_LOGIN);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(login);
    }

    @Override
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        //User user = authenticationService.getLoggedInUsethe decline" de nofxr(userID);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_GET_LOGGEDIN_USER);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(user);
    }

}
