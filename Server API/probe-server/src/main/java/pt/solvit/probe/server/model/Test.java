/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.properties.DataTestProperties;
import pt.solvit.probe.server.model.properties.VoiceTestProperties;
import pt.solvit.probe.server.model.enums.TestType;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Test {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("index")
    private long index;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("type")
    private TestType type;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("delay")
    private Long delay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("destination")
    private String[] destination;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("duration")
    private Long duration;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("message")
    private String message;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("priority")
    private String priority;

    public Test(Long id, long index, TestType type, Long delay, String[] destination, Long duration, String message, String priority) {
        this.id = id;
        this.index = index;
        this.type = type;
        this.delay = delay;
        this.destination = destination;

        this.duration = duration;
        this.message = message;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public long getIndex() {
        return index;
    }

    public TestType getType() {
        return type;
    }

    public long getDelay() {
        return delay != null ? delay : 0;
    }

    public String[] getDestination() {
        return destination;
    }

    public Long getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    @JsonIgnore
    public String getPropertiesString() {
        if (type == TestType.SMS) {
            return GSON.toJson(new DataTestProperties(this));
        }
        return GSON.toJson(new VoiceTestProperties(this));
    }
}
