/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.logfiles.TestLog;

/**
 *
 * @author AnaRita
 */
public class FileProperties {

    private Long testPlanId;
    private Long setupId;

    public FileProperties(TestLog testLog) {
        this.testPlanId = testLog.getTestPlanId();
        this.setupId = testLog.getSetupId();
    }

    public Long getTestPlanId() {
        return testPlanId;
    }

    public Long getSetupId() {
        return setupId;
    }
}
