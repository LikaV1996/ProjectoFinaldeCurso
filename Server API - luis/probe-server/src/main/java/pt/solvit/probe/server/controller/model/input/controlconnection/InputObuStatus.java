/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ObuStatus", description = "Obu status data tranfer object")
@Validated
public class InputObuStatus {

    @JsonProperty("location")
    @Valid
    private InputLocation location;

    @JsonProperty("alarmCount")
    @Valid
    private InputAlarms alarmCount;

    @JsonProperty("storage")
    @Valid
    private InputStorage storage;

    @JsonProperty("temperature")
    @Valid
    private Float temperature;

    @ApiModelProperty(value = "Location")
    public InputLocation getLocation() {
        return location;
    }

    @ApiModelProperty(value = "Alarm Count")
    public InputAlarms getAlarmCount() {
        return alarmCount;
    }

    @ApiModelProperty(value = "Storage")
    public InputStorage getStorage() {
        return storage;
    }

    @ApiModelProperty(value = "Temperature")
    public Float getTemperature() {
        return temperature;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (location != null) {
            location.validate();
        }
        if (alarmCount == null) {
            throw new BadRequestException("Invalid status.", "AlarmCount is null.", "/probs/obustatus-null-params", "about:blank");
        }
        alarmCount.validate();
        if (storage == null) {
            throw new BadRequestException("Invalid status.", "Storage is null.", "/probs/obustatus-null-params", "about:blank");
        }
        storage.validate();
        if (temperature == null) {
            throw new BadRequestException("Invalid status.", "Temperature is null.", "/probs/obustatus-null-params", "about:blank");
        }
    }
}
