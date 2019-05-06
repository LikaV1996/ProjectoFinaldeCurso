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
public class TestDao {

    private Long id;
    private long testIndex;
    private String testType;
    private long delay;
    private long setupId;
    private String properties;

    public TestDao() {
    }

    public TestDao(Long id, long testIndex, String testType, long delay, long setupId, String properties) {
        this.id = id;
        this.testIndex = testIndex;
        this.testType = testType;
        this.delay = delay;
        this.setupId = setupId;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public long getTestIndex() {
        return testIndex;
    }

    public String getTestType() {
        return testType;
    }

    public long getDelay() {
        return delay;
    }

    public long getSetupId() {
        return setupId;
    }

    public void setSetupId(long setupId) {
        this.setupId = setupId;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTestIndex(long testIndex) {
        this.testIndex = testIndex;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
