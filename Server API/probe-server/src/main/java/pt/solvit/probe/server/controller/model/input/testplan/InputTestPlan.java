/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.testplan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.util.DateUtil;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.model.input.controlconnection.InputCoordinates;
import pt.solvit.probe.server.model.enums.RedialTrigger;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "TestPlan", description = "Test plan data tranfer object")
@Validated
public class InputTestPlan {

    private static final Logger LOGGER = Logger.getLogger(InputTestPlan.class.getName());

    @JsonProperty("testplanName")
    @NotNull(message = "A tesplanName must be provided.")
    private String testplanName;

    @JsonProperty("startDate")
    @NotNull(message = "A startDate must be provided.")
    private String startDate;

    @JsonProperty("stopDate")
    @NotNull(message = "A stopDate must be provided.")
    private String stopDate;

    @JsonProperty("triggerCoordinates")
    private List<InputCoordinates> triggerCoordinates;

    @JsonProperty("period")
    private String period;

    /*  //removed
    @JsonProperty("setups")
    @Valid
    private List<InputSetup> setups;
    */

    @JsonProperty("maxRetries")
    private Long maxRetries;

    @JsonProperty("retryDelay")
    private Long retryDelay;

    @JsonProperty("redialTriggers")
    private List<String> redialTriggers;

    @ApiModelProperty(required = true, value = "Testplan name")
    public String getTestplanName() {
        return testplanName;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getStartDateLocalDateTime() {
        return DateUtil.getDateFromIsoString(startDate);
    }

    @ApiModelProperty(example = "2019-01-01T00:00:00", required = true, value = "Start date (ISO 8601)")
    public String getStartDate() {
        return startDate;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getStopDateLocalDateTime() {
        return DateUtil.getDateFromIsoString(stopDate);
    }

    @ApiModelProperty(example = "2019-02-01T00:00:00", required = true, value = "Stop date (ISO 8601)")
    public String getStopDate() {
        return stopDate;
    }

    @ApiModelProperty(value = "Trigger coordinates")
    public List<InputCoordinates> getTriggerCoordinates() {
        return triggerCoordinates;
    }

    @ApiModelProperty(example = "P1D", value = "Period (ISO 8601)")
    public Duration getPeriod() {
        if (period == null) {
            return null;
        }
        try {
            return Duration.parse(period);
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.SEVERE, "{0} is not on ISO format", period);
            return null;
        }
    }

    /*  //removed
    @ApiModelProperty(value = "Setup list")
    public List<InputSetup> getSetups() {
        return setups;
    }
    */

    @ApiModelProperty(example = "3", value = "Maximum number of retries")
    public Long getMaxRetries() {
        return maxRetries;
    }

    @ApiModelProperty(example = "60", value = "Retry delay")
    public Long getRetryDelay() {
        return retryDelay;
    }

    @ApiModelProperty(example = "DROPPED", value = "Redial triggers", allowableValues = "BLOCKED, DROPPED, BUSY, NO_CARRIER")
    public List<RedialTrigger> getRedialTriggers() {
        List<RedialTrigger> redialTriggersList = null;
        if (redialTriggers != null) {
            redialTriggersList = new ArrayList();
            for (String curRedialTrigger : redialTriggers) {
                try {
                    redialTriggersList.add(RedialTrigger.valueOf(curRedialTrigger));
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.SEVERE, "{0} does not match RedialTrigger enum", curRedialTrigger);
                    return null;
                }
            }
        }
        return redialTriggersList;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (testplanName == null) {
            throw new BadRequestException("Invalid test plan.", "testplanName is null.", "/probs/testplan-null-params", "about:blank");
        }
            if (startDate == null) {
            throw new BadRequestException("Invalid test plan.", "StartDate is null.", "/probs/testplan-null-params", "about:blank");
        }
        if (stopDate == null) {
            throw new BadRequestException("Invalid test plan.", "StopDate is null.", "/probs/testplan-null-params", "about:blank");
        }
        if (DateUtil.getDateFromIsoString(startDate) == null) {
            throw new BadRequestException("Invalid test plan.", "StartDate is not on ISO format.", "/probs/testplan-invalid-startdate", "about:blank");
        }
        if (DateUtil.getDateFromIsoString(stopDate) == null) {
            throw new BadRequestException("Invalid test plan.", "StopDate is not on ISO format.", "/probs/testplan-invalid-stopdate", "about:blank");
        }
        if (triggerCoordinates != null) {
            if (triggerCoordinates.size() != 2) {
                throw new BadRequestException("Invalid test plan.", "TriggerCoordinates size is not 2.", "/probs/testplan-invalid-triggercoordinates", "about:blank");
            }
            for (InputCoordinates curCoordinates : triggerCoordinates) {
                curCoordinates.validate();
            }
        } else if (period != null && getPeriod() == null) {
            throw new BadRequestException("Invalid test plan.", "Invalid period.", "/probs/testplan-invalid-period", "about:blank");
        }
        /*  //removed
        if (setups != null) {
            for (InputSetup curSetup : setups) {
                curSetup.validateForCreate();
            }
            int i = 0;
            for (InputSetup curSetup1 : setups) {
                int j = 0;
                for (InputSetup curSetup2 : setups) {
                    if (i != j) {
                        if (curSetup1.getModemType().equals(curSetup2.getModemType())) {
                            throw new BadRequestException("Invalid test plan.", "Different setups can not have same modemType.", "/probs/testplan-setup-rip", "about:blank");
                        }
                    }
                    j++;
                }
                i++;
            }
        }
        */
        if (redialTriggers != null && getRedialTriggers() == null) {
            throw new BadRequestException("Invalid test plan.", "Invalid redialTriggers.", "/probs/testplan-invalid-redialtriggers", "about:blank");
        }
    }
}
