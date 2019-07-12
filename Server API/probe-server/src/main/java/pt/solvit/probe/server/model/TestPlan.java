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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputCoordinates;
import pt.solvit.probe.server.model.properties.TestPlanProperties;
import pt.solvit.probe.server.model.enums.RedialTrigger;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "TestPlan", description = "Test plan data transfer object")
public class TestPlan extends CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("testplanName")
    private String testplanName;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("startDate")
    private LocalDateTime startDate;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("stopDate")
    private LocalDateTime stopDate;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("triggerCoordinates")
    private List<InputCoordinates> triggerCoordinates;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("period")
    private Duration period;

    /*  //removed
    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("setups")
    private List<Setup> setups;
    */

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxRetries")
    private Long maxRetries;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("retryDelay")
    private Long retryDelay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("redialTriggers")
    private List<RedialTrigger> redialTriggers;

    public TestPlan(Long id, String testplanName, LocalDateTime startDate, LocalDateTime stopDate,
                    List<InputCoordinates> triggerCoordinates, Duration period, //List<Setup> setups,
                    Long maxRetries, Long retryDelay, List<RedialTrigger> redialTriggers,
                    String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate
        ) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.testplanName = testplanName;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.triggerCoordinates = triggerCoordinates;
        this.period = period;

        //  removed
        //this.setups = setups;

        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
        this.redialTriggers = redialTriggers;
    }

    @ApiModelProperty(required = true, value = "Test plan identifier")
    public Long getId() {
        return id;
    }

    @ApiModelProperty(required = true, value = "Test plan name")
    public String getTestplanName() {
        return testplanName;
    }

    public void setTestplanName(String testplanName) {
        this.testplanName = testplanName;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getStartDateLocalDateTime() {
        return startDate;
    }

    @ApiModelProperty(required = true, value = "Start date (ISO 8601)")
    public String getStartDate() {
        return ISO8601_DATE_FORMATTER.format(startDate);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getStopDateLocalDateTime() {
        return stopDate;
    }

    @ApiModelProperty(required = true, value = "Stop date (ISO 8601)")
    public String getStopDate() {
        return ISO8601_DATE_FORMATTER.format(stopDate);
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }

    @ApiModelProperty(value = "Trigger coordinates")
    public List<InputCoordinates> getTriggerCoordinates() {
        return triggerCoordinates;
    }

    public void setTriggerCoordinates(List<InputCoordinates> triggerCoordinates) {
        this.triggerCoordinates = triggerCoordinates;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public Duration getPeriodDuration() {
        return period;
    }

    @ApiModelProperty(value = "Period (ISO 8601)")
    public String getPeriod() {
        if (period == null) {
            return null;
        }
        return period.toString();
    }

    public void setPeriod(Duration period) {
        this.period = period;
    }

    /*  //removed
    @ApiModelProperty(value = "Setup list")
    public List<Setup> getSetups() {
        return setups;
    }

    public void setSetups(List<Setup> setups) {
        this.setups = setups;
    }
    */

    @ApiModelProperty(value = "Maximum number of retries")
    public Long getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Long maxRetries) {
        this.maxRetries = maxRetries;
    }

    @ApiModelProperty(value = "Retry delay")
    public Long getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(Long retryDelay) {
        this.retryDelay = retryDelay;
    }

    @ApiModelProperty(value = "Redial triggers", allowableValues = "BLOCKED, DROPPED, BUSY, NO_CARRIER")
    public List<RedialTrigger> getRedialTriggers() {
        return redialTriggers;
    }

    public void setRedialTriggers(List<RedialTrigger> redialTriggers) {
        this.redialTriggers = redialTriggers;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public String getPropertiesString() {
        return GSON.toJson(new TestPlanProperties(this));
    }
}
