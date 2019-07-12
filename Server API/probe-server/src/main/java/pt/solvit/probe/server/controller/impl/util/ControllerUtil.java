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
import pt.solvit.probe.server.controller.model.input.InputComponent;
import pt.solvit.probe.server.controller.model.input.InputHardware;
import pt.solvit.probe.server.controller.model.input.InputObuUser;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputConfigState;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputConfigStatus;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputTestPlanState;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputTestPlanStatus;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.controller.model.input.testplan.InputTest;
import pt.solvit.probe.server.controller.model.input.testplan.InputTestPlan;
import pt.solvit.probe.server.model.*;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.model.enums.CancelState;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
public class ControllerUtil {

    //Config
    public static Config transformToConfig(InputConfig inputConfig, String creator) {
        return new Config(null, inputConfig.getConfigName(),
                inputConfig.getActivationDate() != null ? inputConfig.getActivationLocalDateTime() : null,
                inputConfig.getArchive(), inputConfig.getControlConnection(), inputConfig.getCore(), inputConfig.getData(),
                inputConfig.getDownload(), inputConfig.getScanning(), inputConfig.getServer(), inputConfig.getTestPlan(),
                inputConfig.getUpload(), inputConfig.getVoice(), creator, LocalDateTime.now(),
                null, null);
    }

    //TestPlan
    public static TestPlan transformToTestPlan(InputTestPlan inputTestPlan, String creator) {
        //  removed
        //List<Setup> setupList = transformToSetupList(inputTestPlan.getSetups(), creator);

        LocalDateTime startDate = DateUtil.getDateFromIsoString(inputTestPlan.getStartDate());
        LocalDateTime stopDate = DateUtil.getDateFromIsoString(inputTestPlan.getStopDate());

        return new TestPlan(null, inputTestPlan.getTestplanName(), startDate, stopDate,
                inputTestPlan.getTriggerCoordinates(), inputTestPlan.getPeriod(), //setupList,
                inputTestPlan.getMaxRetries(), inputTestPlan.getRetryDelay(), inputTestPlan.getRedialTriggers(),
                creator, LocalDateTime.now(), null, null);
    }

    //SetupList
    public static List<Setup> transformToSetupList(List<InputSetup> inputSetupList, String creator){
        List<Setup> setupList = null;
        if (inputSetupList != null) {
            setupList = new ArrayList();
            for (InputSetup curInputSetup : inputSetupList) {
                setupList.add(ControllerUtil.transformToSetup(curInputSetup, creator));
            }
        }
        return setupList;
    }

    //Setup
    public static Setup transformToSetup(InputSetup inputSetup, String creator) {
        List<Test> testList = transformToTestList(inputSetup.getTests());

        return new Setup(null, inputSetup.getSetupName(), inputSetup.getModemType(), inputSetup.getScanning(), testList, creator, LocalDateTime.now(), null, null);
    }

    //TestList
    public static List<Test> transformToTestList(List<InputTest> inputTestList){
        List<Test> testList = null;
        if (inputTestList != null) {
            testList = new ArrayList();
            for (InputTest curInputTest : inputTestList) {
                testList.add(transformToTest(curInputTest));
            }
        }
        return testList;
    }

    //Test
    private static Test transformToTest(InputTest inputTest) {
        return new Test(null, inputTest.getIndex(), inputTest.getType(), inputTest.getDelay(), inputTest.getDestination(),
                inputTest.getDuration(), inputTest.getMessage(), inputTest.getPriority());
    }



    //ObuConfig
    public static ObuConfig transformToObuConfig(long obuId, InputConfigStatus inputConfigStatus) {
        List<ConfigState> stateList = transformToConfigStateList(inputConfigStatus.getStateList());

        return new ObuConfig(obuId, inputConfigStatus.getId(), CancelState.NONE, stateList, null);
    }

    //ConfigStateList
    private static List<ConfigState> transformToConfigStateList(List<InputConfigState> inputConfigStateList){
        List<ConfigState> stateList = null;
        if (inputConfigStateList != null) {
            stateList = new ArrayList();
            for (InputConfigState curInputConfigState : inputConfigStateList) {
                stateList.add(transformToConfigState(curInputConfigState));
            }
        }
        return stateList;
    }

    //ConfigState
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

    //TestPlanState
    private static TestPlanState transformToTestPlanState(InputTestPlanState inputTestPlanState) {
        LocalDateTime date = DateUtil.getDateFromIsoString(inputTestPlanState.getDate());

        return new TestPlanState(inputTestPlanState.getState(), date);
    }



    //ServerLog         not used
    public static ServerLog transformToServerLog(String username, RequestMethod requestMethod, HttpStatus httpStatus, String url, Object... values) {
        String accessURI = requestMethod + ": " + UriBuilder.buildUri(url, values).toString();
        String httpStatusStr = httpStatus.value() + " " + httpStatus.getReasonPhrase();

        return new ServerLog(null, LocalDateTime.now(), AccessType.USER, accessURI, username, LocalDateTime.now(),
                httpStatusStr, null);
    }



    //Hardware
    public static Hardware transformToHardware(InputHardware inputHardware, String creator) {

        List<Component> componentList = transformToComponentList(inputHardware.getComponents());

        return new Hardware(null, inputHardware.getSerialNumber(), componentList, creator, LocalDateTime.now(), null, null);
    }

    //ComponentList
    public static List<Component> transformToComponentList(List<InputComponent> inputComponentList){
        List<Component> componentList = null;
        if (inputComponentList != null) {
            componentList = new ArrayList();
            for (InputComponent curInputComponent : inputComponentList) {
                componentList.add(transformToComponent(curInputComponent));
            }
        }
        return componentList;
    }

    //Component
    private static Component transformToComponent(InputComponent inputComponent) {
        return new Component(inputComponent.getSerialNumber(), inputComponent.getComponentType(),
                inputComponent.getManufacturer(), inputComponent.getModel(), inputComponent.getModemType(), inputComponent.getImei());
    }



    public static ObuUser transformToObuUser(InputObuUser body) {
        return new ObuUser(body.getUserID(), null, body.getObuID(), null, body.getRole());
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
