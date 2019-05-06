/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.model.enums.TestPlanStateEnum;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "TestPlanState", description = "Test plan state data tranfer object")
@Validated
public class InputTestPlanState {

    private static final Logger LOGGER = Logger.getLogger(InputTestPlanState.class.getName());

    @JsonProperty("state")
    @NotNull(message = "A state must be provided.")
    private String state;

    @JsonProperty("date")
    @NotNull(message = "A date must be provided.")
    private String date;

    @ApiModelProperty(required = true, value = "Test plan state", allowableValues = "DOWNLOADED, REJECTED, SCHEDULED, CANCELED, RUNNING, INTERRUPTED, ENDED, ERROR")
    public TestPlanStateEnum getState() {
        try {
            return TestPlanStateEnum.valueOf(state);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match TestPlanState enum", state);
            return null;
        }
    }

    @ApiModelProperty(required = true, value = "Date")
    public String getDate() {
        return date;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (state == null) {
            throw new BadRequestException("Invalid testPlanState.", "State is null.", "string", "about:blank");
        }
        if (date == null) {
            throw new BadRequestException("Invalid testPlanState.", "Date is null.", "string", "about:blank");
        }
        if (getState() == null) {
            throw new BadRequestException("Invalid testPlanState.", "Invalid state.", "string", "about:blank");
        }
        if (DateUtil.getDateFromIsoString(date) == null) {
            throw new BadRequestException("Invalid testPlanState.", "Date is not on ISO format.", "string", "about:blank");
        }
    }
}
