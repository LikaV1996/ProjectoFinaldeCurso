/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.TestPlan;
import java.time.Duration;
import java.util.List;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputCoordinates;
import pt.solvit.probe.server.model.enums.RedialTrigger;

/**
 *
 * @author AnaRita
 */
public class TestPlanProperties {

    private List<InputCoordinates> triggerCoordinates;
    private String period;
    private Long maxRetries;
    private Long retryDelay;
    private List<RedialTrigger> redialTriggers;

    public TestPlanProperties(TestPlan testPlan) {
        this.triggerCoordinates = testPlan.getTriggerCoordinates();
        this.period = testPlan.getPeriodDuration().toString();
        this.maxRetries = testPlan.getMaxRetries();
        this.retryDelay = testPlan.getRetryDelay();
        this.redialTriggers = testPlan.getRedialTriggers();
    }

    public List<InputCoordinates> getTriggerCoordinates() {
        return triggerCoordinates;
    }

    public Duration getPeriodDuration() {
        return Duration.parse(period);
    }

    public String getPeriod() {
        return period;
    }

    public Long getMaxRetries() {
        return maxRetries;
    }

    public Long getRetryDelay() {
        return retryDelay;
    }

    public List<RedialTrigger> getRedialTriggers() {
        return redialTriggers;
    }
}
