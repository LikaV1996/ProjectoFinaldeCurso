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
import pt.solvit.probe.server.model.enums.ConfigStateEnum;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ConfigState", description = "Config state data tranfer object")
@Validated
public class InputConfigState {

    private static final Logger LOGGER = Logger.getLogger(InputConfigState.class.getName());

    @JsonProperty("state")
    @NotNull(message = "A state must be provided.")
    private String state;

    @JsonProperty("date")
    @NotNull(message = "A date must be provided.")
    private String date;

    @ApiModelProperty(required = true, value = "Config State", allowableValues = "DOWNLOADED, REJECTED, SCHEDULED, CANCELED, ACTIVE, REPLACED, ERROR")
    public ConfigStateEnum getState() {
        try {
            return ConfigStateEnum.valueOf(state);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ConfigState enum", state);
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
            throw new BadRequestException("Invalid configState.", "State is null.", "/probs/configstate-null-params", "about:blank");
        }
        if (date == null) {
            throw new BadRequestException("Invalid configState.", "Date is null.", "/probs/configstate-null-params", "about:blank");
        }
        if (getState() == null) {
            throw new BadRequestException("Invalid configState.", "Invalid state.", "/probs/configstate-invalid-state", "about:blank");
        }
        if (DateUtil.getDateFromIsoString(date) == null) {
            throw new BadRequestException("Invalid configState.", "Date is not on ISO format.", "/probs/configstate-invalid-date", "about:blank");
        }
    }
}
