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
public class ObuTestPlanDao {

    private long obuId;
    private long testPlanId;
    private String properties;

    public ObuTestPlanDao() {
    }

    public ObuTestPlanDao(long obuId, long testPlanId, String properties) {
        this.obuId = obuId;
        this.testPlanId = testPlanId;
        this.properties = properties;
    }

    public long getObuId() {
        return obuId;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public String getProperties() {
        return properties;
    }

    public void setObuId(long obuId) {
        this.obuId = obuId;
    }

    public void setTestPlanId(long testPlanId) {
        this.testPlanId = testPlanId;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
