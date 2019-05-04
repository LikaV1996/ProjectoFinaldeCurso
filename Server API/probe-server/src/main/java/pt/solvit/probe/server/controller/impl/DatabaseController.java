/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IDatabaseController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.service.api.IDatabaseService;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DatabaseController implements IDatabaseController {

    @Autowired
    private IDatabaseService databaseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<Void> reset(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        User user = (User) request.getAttribute("user");

        databaseService.resetDb(user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_RESET_DB);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> factoryReset(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        User user = (User) request.getAttribute("user");

        databaseService.factoryResetDb(user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_FACTORY_RESET_DB);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }
}
