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
import pt.solvit.probe.server.controller.api.ITestPlanController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.testplan.InputTestPlan;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.TestPlan;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.ITestPlanService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestPlanController implements ITestPlanController {

    @Autowired
    private ITestPlanService testPlanService;
    @Autowired
    private ISetupService setupService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<List<TestPlan>> getAllTestPlans(HttpServletRequest request) {


        List<TestPlan> testPlanList = testPlanService.getAllTestPlans();


        return ResponseEntity.ok(testPlanList);
    }

    @Override
    public ResponseEntity<TestPlan> createTestPlan(HttpServletRequest request, @RequestBody InputTestPlan body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        TestPlan testPlan = ControllerUtil.transformToTestPlan(body, user.getUserName());
        long testPlanId = testPlanService.createTestPlan(testPlan);

        testPlan = testPlanService.getTestPlan(testPlanId);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_TESTPLAN_ID, testPlanId);

        return ResponseEntity.created(createdURI).body(testPlan);
    }

    @Override
    public ResponseEntity<TestPlan> getTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId) {


        TestPlan testPlan = testPlanService.getTestPlan(testPlanId);


        return ResponseEntity.ok(testPlan);
    }

    @Override
    public ResponseEntity<TestPlan> updateTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId, @RequestBody InputTestPlan body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        TestPlan testPlan = testPlanService.getTestPlan(testPlanId);

        updateTestPlan(body, testPlan, user.getUserName());

        testPlanService.updateTestPlan(testPlan);

        testPlan = testPlanService.getTestPlan(testPlanId);


        return ResponseEntity.ok().body(testPlan);
    }

    @Override
    public ResponseEntity<Void> deleteTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId) {

        User user = (User) request.getAttribute("user");

        testPlanService.deleteTestPlan(testPlanId, user);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Setup>> getAllSetupsFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId) {


        List<Setup> setupList = setupService.getAllTestPlanSetups(testPlanId);


        return ResponseEntity.ok(setupList);
    }

    @Override
    public ResponseEntity<Void> removeAllSetupsFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId) {


        setupService.removeAllSetupsFromTestPlan(testPlanId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addSetupToTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {


        setupService.addSetupToTestPlan(testPlanId, setupId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Setup> getSetupFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {


        Setup setup = setupService.getTestPlanSetup(testPlanId, setupId);


        return ResponseEntity.ok(setup);
    }

    @Override
    public ResponseEntity<Void> removeSetupFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {


        setupService.removeSetupFromTestPlan(testPlanId, setupId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ObuTestPlan>> getAllTestPlansFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId) {


        List<ObuTestPlan> obuTestPlanList = testPlanService.getAllObuTestPlans(obuId);


        return ResponseEntity.ok(obuTestPlanList);
    }

    @Override
    public ResponseEntity<Void> removelAllTestPlansFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId) {


        testPlanService.removeAllTestPlansFromObu(obuId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addTestPlanToObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {


        //TODO which users can add testplans? EDITORS? VIEWERS?

        testPlanService.addTestPlanToObu(obuId, testPlanId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ObuTestPlan> getTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {


        ObuTestPlan obuTestPlan = testPlanService.getObuTestPlan(obuId, testPlanId);


        return ResponseEntity.ok(obuTestPlan);
    }

    @Override
    public ResponseEntity<Void> cancelTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {


        boolean result = testPlanService.cancelTestPlanFromObu(obuId, testPlanId);


        return !result ? ResponseEntity.accepted().build() : ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {


        testPlanService.removeTestPlanFromObu(obuId, testPlanId);


        return ResponseEntity.ok().build();
    }


    private void updateTestPlan(InputTestPlan inputTestPlan, TestPlan testPlan, String modifier){
        testPlan.setTestplanName(inputTestPlan.getTestplanName());
        testPlan.setStartDate( inputTestPlan.getStartDateLocalDateTime() );
        testPlan.setStopDate( inputTestPlan.getStopDateLocalDateTime() );
        testPlan.setPeriod(inputTestPlan.getPeriod());
        testPlan.setRedialTriggers(inputTestPlan.getRedialTriggers());

        //  removed
        //testPlan.setSetups( ControllerUtil.transformToSetupList(inputTestPlan.getSetups(), testPlan.getCreator()) );

        testPlan.setMaxRetries(inputTestPlan.getMaxRetries());
        testPlan.setRetryDelay(inputTestPlan.getRetryDelay());
        testPlan.setTriggerCoordinates(inputTestPlan.getTriggerCoordinates());

        testPlan.setModifier(modifier);
    }
}
