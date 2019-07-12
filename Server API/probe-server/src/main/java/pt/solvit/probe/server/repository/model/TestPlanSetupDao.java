/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

/**
 *
 * @author AnaRita
 */
public class TestPlanSetupDao {

    private long testPlanId;
    private long setupId;

    public TestPlanSetupDao() {
    }

    public TestPlanSetupDao(long testPlanId, long setupId) {
        this.testPlanId = testPlanId;
        this.setupId = setupId;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public long getSetupId() {
        return setupId;
    }

    public void setTestPlanId(long testPlanId) {
        this.testPlanId = testPlanId;
    }

    public void setSetupId(long setupId) {
        this.setupId = setupId;
    }
}
