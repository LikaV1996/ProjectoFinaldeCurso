/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputConfigState;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputConfigStatus;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputTestPlanState;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputTestPlanStatus;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.controller.model.input.testplan.InputTest;
import pt.solvit.probe.server.model.ConfigState;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.Test;
import pt.solvit.probe.server.model.TestPlanState;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.model.enums.CancelState;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
public class ControllerUtil {

    //Setup
    public static Setup transformToSetup(InputSetup inputSetup, String creator) {
        List<Test> testList = null;
        if (inputSetup.getTests() != null) {
            testList = new ArrayList();
            for (InputTest curInputTest : inputSetup.getTests()) {
                testList.add(transformToTest(curInputTest));
            }
        }

        return new Setup(null, inputSetup.getSetupName(), inputSetup.getModemType(), inputSetup.getScanning(), testList, creator, LocalDateTime.now(), null, null);
    }

    private static Test transformToTest(InputTest inputTest) {
        return new Test(null, inputTest.getIndex(), inputTest.getType(), inputTest.getDelay(), inputTest.getDestination(),
                inputTest.getDuration(), inputTest.getMessage(), inputTest.getPriority());
    }

    //ObuConfig
    public static ObuConfig transformToObuConfig(long obuId, InputConfigStatus inputConfigStatus) {
        List<ConfigState> stateList = null;
        if (inputConfigStatus.getStateList() != null) {
            stateList = new ArrayList();
            for (InputConfigState curInputConfigState : inputConfigStatus.getStateList()) {
                stateList.add(transformToConfigState(curInputConfigState));
            }
        }

        return new ObuConfig(obuId, inputConfigStatus.getId(), CancelState.NONE, stateList, null);
    }

    private static ConfigState transformToConfigState(InputConfigState inputConfigState) {
        LocalDateTime date = DateUtil.getDateFromIsoString(inputConfigState.getDate());

        return new ConfigState(inputConfigState.getState(), date);
    }

    //ObuTestPlan
    public static ObuTestPlan transformToObuTestPlan(long obuId, InputTestPlanStatus inputTestPlanStatus) {
        List<TestPlanState> stateList = null;
        if (inputTestPlanStatus.getStateList() != null) {
            stateList = new ArrayList();
            for (InputTestPlanState curInputTestPlanState : inputTestPlanStatus.getStateList()) {
                stateList.add(transformToTestPlanState(curInputTestPlanState));
            }
        }

        return new ObuTestPlan(obuId, inputTestPlanStatus.getId(), CancelState.NONE, stateList, null);
    }

    private static TestPlanState transformToTestPlanState(InputTestPlanState inputTestPlanState) {
        LocalDateTime date = DateUtil.getDateFromIsoString(inputTestPlanState.getDate());

        return new TestPlanState(inputTestPlanState.getState(), date);
    }

    //ServerLog    
    public static ServerLog transformToServerLog(String username, RequestMethod requestMethod, HttpStatus httpStatus, String url, Object... values) {
        String accessURI = requestMethod + ": " + UriBuilder.buildUri(url, values).toString();
        String httpStatusStr = httpStatus.value() + " " + httpStatus.getReasonPhrase();

        return new ServerLog(null, LocalDateTime.now(), AccessType.USER, accessURI, username, LocalDateTime.now(),
                httpStatusStr, null);
    }

    public static String getAlphaNumeric(int len) {
        char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        char[] c = new char[len];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            c[i] = ch[random.nextInt(ch.length)];
        }
        return new String(c);
    }

    public static boolean validatePriority(String priority) {
        switch (priority) {
            case "A":
            case "B":
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                return true;
            default:
                return false;
        }
    }
}
