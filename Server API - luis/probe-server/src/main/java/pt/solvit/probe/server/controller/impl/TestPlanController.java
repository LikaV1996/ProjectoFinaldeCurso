/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import pt.solvit.probe.server.controller.api.ITestPlanController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.controller.model.input.testplan.InputTestPlan;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.TestPlan;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.ITestPlanService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.util.DateUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestPlanController implements ITestPlanController {

    @Autowired
    private ITestPlanService testPlanService;
    @Autowired
    private ISetupService setupService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<TestPlan>> getAllTestPlans(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<TestPlan> testPlanList = testPlanService.getAllTestPlans();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_TESTPLAN);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(testPlanList);
    }

    @Override
    public ResponseEntity<TestPlan> createTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputTestPlan body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        TestPlan testPlan = transformToTestPlan(body, user.getUserName());
        long testPlanId = testPlanService.createTestPlan(testPlan);

        testPlan = testPlanService.getTestPlan(testPlanId);
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_TESTPLAN);
        //serverLogService.createServerLog(serverLog);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_TESTPLAN_ID, testPlanId);

        return ResponseEntity.created(createdURI).body(testPlan);
    }

    @Override
    public ResponseEntity<TestPlan> getTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        TestPlan testPlan = testPlanService.getTestPlan(testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_ID, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(testPlan);
    }

    @Override
    public ResponseEntity<Void> deleteTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId) {

        User user = (User) request.getAttribute("user");

        testPlanService.deleteTestPlan(testPlanId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_ID, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Setup>> getAllSetupsFromTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        List<Setup> setupList = setupService.getAllTestPlanSetups(testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_SETUP, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(setupList);
    }

    @Override
    public ResponseEntity<Void> removeAllSetupsFromTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        setupService.removeAllSetupsFromTestPlan(testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_SETUP, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addSetupToTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {

        //User user = userService.checkUserCredentials(authorization);

        setupService.addSetupToTestPlan(testPlanId, setupId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_SETUP_ID, testPlanId, setupId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Setup> getSetupFromTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {

        //User user = userService.checkUserCredentials(authorization);

        Setup setup = setupService.getTestPlanSetup(testPlanId, setupId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_SETUP_ID, testPlanId, setupId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(setup);
    }

    @Override
    public ResponseEntity<Void> removeSetupFromTestPlan(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId) {

        //User user = userService.checkUserCredentials(authorization);

        setupService.removeSetupFromTestPlan(testPlanId, setupId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_TESTPLAN_SETUP_ID, testPlanId, setupId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ObuTestPlan>> getAllTestPlansFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        List<ObuTestPlan> obuTestPlanList = testPlanService.getAllObuTestPlans(obuId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuTestPlanList);
    }

    @Override
    public ResponseEntity<Void> removelAllTestPlansFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        testPlanService.removeAllTestPlansFromObu(obuId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addTestPlanToObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        testPlanService.addTestPlanToObu(obuId, testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN_ID, obuId, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ObuTestPlan> getTestPlanFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        ObuTestPlan obuTestPlan = testPlanService.getObuTestPlan(obuId, testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN_ID, obuId, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuTestPlan);
    }

    @Override
    public ResponseEntity<Void> cancelTestPlanFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        boolean result = testPlanService.cancelTestPlanFromObu(obuId, testPlanId);

        if (!result){
            //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.PATCH, HttpStatus.ACCEPTED, AppConfiguration.URL_OBU_TESTPLAN_ID_CANCEL, obuId, testPlanId);
            //serverLogService.createServerLog(serverLog);

            return ResponseEntity.accepted().build();
        }
                
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.PATCH, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN_ID_CANCEL, obuId, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeTestPlanFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId) {

        //User user = userService.checkUserCredentials(authorization);

        testPlanService.removeTestPlanFromObu(obuId, testPlanId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_OBU_TESTPLAN_ID, obuId, testPlanId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    private TestPlan transformToTestPlan(InputTestPlan inputTestPlan, String creator) {
        List<Setup> setupList = null;
        if (inputTestPlan.getSetups() != null) {
            setupList = new ArrayList();
            for (InputSetup curInputSetup : inputTestPlan.getSetups()) {
                setupList.add(ControllerUtil.transformToSetup(curInputSetup, creator));
            }
        }

        LocalDateTime startDate = DateUtil.getDateFromIsoString(inputTestPlan.getStartDate());
        LocalDateTime stopDate = DateUtil.getDateFromIsoString(inputTestPlan.getStopDate());

        return new TestPlan(null, inputTestPlan.getTestplanName(), startDate, stopDate,
                inputTestPlan.getTriggerCoordinates(), inputTestPlan.getPeriod(), setupList,
                inputTestPlan.getMaxRetries(), inputTestPlan.getRetryDelay(), inputTestPlan.getRedialTriggers(),
                creator, LocalDateTime.now(), null, null);
    }
}
