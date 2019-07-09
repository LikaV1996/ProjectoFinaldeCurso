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
import pt.solvit.probe.server.controller.api.ISetupController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SetupController implements ISetupController {

    @Autowired
    private ISetupService setupService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<List<Setup>> getAllSetups(HttpServletRequest request) {


        List<Setup> setupList = setupService.getAllSetups();


        return ResponseEntity.ok().body(setupList);
    }

    @Override
    public ResponseEntity<Void> createSetup(HttpServletRequest request, @RequestBody InputSetup body) {

        User user = (User) request.getAttribute("user");

        body.validateForCreate();
        Setup setup = ControllerUtil.transformToSetup(body, user.getUserName());
        long setupId = setupService.createSetup(setup, user);


        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_SETUP_ID, setupId);

        return ResponseEntity.created(createdURI).build();
    }

    @Override
    public ResponseEntity<Setup> getSetup(HttpServletRequest request, @PathVariable("setup-id") long setupId) {


        Setup setup = setupService.getSetup(setupId);

        return ResponseEntity.ok(setup);
    }

    @Override
    public ResponseEntity<Setup> updateSetup(HttpServletRequest request, @PathVariable("setup-id") long setupId, @RequestBody InputSetup body) {

        User user = (User) request.getAttribute("user");

        body.validateForUpdate();
        Setup setup = setupService.getSetup(setupId);

        updateSetup(body, setup, user.getUserName());

        setupService.updateSetup(setup, user);

        setup = setupService.getSetup(setupId);

        return ResponseEntity.ok().body(setup);
    }

    @Override
    public ResponseEntity<Void> deleteSetup(HttpServletRequest request, @PathVariable("setup-id") long setupId) {

        User user = (User) request.getAttribute("user");

        setupService.deleteSetup(setupId, user);


        return ResponseEntity.ok().build();
    }


    private void updateSetup(InputSetup inputSetup, Setup setup, String modifier){
        setup.setSetupName( inputSetup.getSetupName() );
        setup.setModemType( inputSetup.getModemType() );
        setup.setScanning( inputSetup.getScanning() );

        setup.setTests( ControllerUtil.transformToTestList( inputSetup.getTests() ) );

        setup.setModifier( modifier );
    }

}
