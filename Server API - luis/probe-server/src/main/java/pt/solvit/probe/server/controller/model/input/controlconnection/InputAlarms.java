/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Alarms", description = "Alarms data tranfer object")
@Validated
public class InputAlarms {

    @JsonProperty("critical")
    @NotNull(message = "A critical must be provided.")
    private Integer critical;

    @JsonProperty("major")
    @NotNull(message = "A major must be provided.")
    private Integer major;

    @JsonProperty("warning")
    @NotNull(message = "A warning must be provided.")
    private Integer warning;

    @ApiModelProperty(required = true, value = "Number of critical alarms")
    public int getCritical() {
        return critical;
    }

    @ApiModelProperty(required = true, value = "Number of major alarms")
    public int getMajor() {
        return major;
    }

    @ApiModelProperty(required = true, value = "Number of warning alarms")
    public int getWarning() {
        return warning;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (critical == null) {
            throw new BadRequestException("Invalid alarmCount.", "Critical is null.", "/probs/alarms-null-params", "about:blank");
        }
        if (major == null) {
            throw new BadRequestException("Invalid alarmCount.", "Major is null.", "/probs/alarms-null-params", "about:blank");
        }
        if (warning == null) {
            throw new BadRequestException("Invalid alarmCount.", "Warning is null.", "/probs/alarms-null-params", "about:blank");
        }
    }
}
