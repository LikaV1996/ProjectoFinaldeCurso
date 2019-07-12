/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Test;

/**
 *
 * @author AnaRita
 */
public class VoiceTestProperties {

    private String[] destination;
    private Long duration;
    private String priority;

    public VoiceTestProperties(Test test) {
        this.destination = test.getDestination();
        this.duration = test.getDuration();
        this.priority = test.getPriority();
    }

    public String[] getDestination() {
        return destination;
    }

    public Long getDuration() {
        return duration;
    }

    public String getPriority() {
        return priority;
    }
}
