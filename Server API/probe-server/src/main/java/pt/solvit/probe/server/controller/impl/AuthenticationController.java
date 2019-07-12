/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.solvit.probe.server.controller.api.IAuthenticationController;
import pt.solvit.probe.server.controller.model.input.InputLogin;
import pt.solvit.probe.server.model.Login;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IAuthenticationService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController implements IAuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @Override
    public ResponseEntity<Login> getLoginToken(HttpServletRequest request, @RequestBody InputLogin body) {

        body.validate();

        User loggedInUser = new User(body.getUsername());
        request.setAttribute("user", loggedInUser); //TODO for logging, is it worth logging the failed logins to this user?

        Login login = authenticationService.login(body.getUsername(), body.getPassword());

        return ResponseEntity.ok().body(login);
    }

    @Override
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok().body(user);
    }

}
