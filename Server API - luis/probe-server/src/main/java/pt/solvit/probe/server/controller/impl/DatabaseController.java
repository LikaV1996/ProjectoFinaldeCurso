/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IDatabaseController;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IDatabaseService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DatabaseController implements IDatabaseController {

    @Autowired
    private IDatabaseService databaseService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<Void> reset(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        databaseService.resetDb(user);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> factoryReset(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        databaseService.factoryResetDb(user);


        return ResponseEntity.ok().build();
    }
}
