/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.ISetupController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SetupController implements ISetupController {

    @Autowired
    private ISetupService setupService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<Setup>> getAllSetups(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<Setup> setupList = setupService.getAllSetups();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_SETUP);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(setupList);
    }

    @Override
    public ResponseEntity<Void> createSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputSetup body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Setup setup = ControllerUtil.transformToSetup(body, user.getUserName());
        long setupId = setupService.createSetup(setup);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_SETUP);
        //serverLogService.createServerLog(serverLog);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_SETUP_ID, setupId);

        return ResponseEntity.created(createdURI).build();
    }

    @Override
    public ResponseEntity<Setup> getSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("setup-id") long setupId) {

        //User user = userService.checkUserCredentials(authorization);

        Setup setup = setupService.getSetup(setupId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_SETUP_ID, setupId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(setup);
    }

    @Override
    public ResponseEntity<Setup> updateSetup(HttpServletRequest request, long setupId, InputSetup body) {
        body.validate();
        Setup setup = setupService.getSetup(setupId);

        setup.setSetupName( body.getSetupName() );
        setup.setModemType( body.getModemType() );
        setup.setScanning( body.getScanning() );
        //TODO
        // setup.setTests( body.getTests() );

        //TODO
        // setupService.updateSetup(setup);

        setup = setupService.getSetup(setupId);

        return ResponseEntity.ok().body(setup);
    }

    @Override
    public ResponseEntity<Void> deleteSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("setup-id") long setupId) {

        User user = (User) request.getAttribute("user");

        setupService.deleteSetup(setupId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_SETUP_ID, setupId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }
}
