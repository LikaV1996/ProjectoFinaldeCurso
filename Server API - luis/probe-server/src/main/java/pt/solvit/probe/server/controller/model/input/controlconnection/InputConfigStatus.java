/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ConfigStatus", description = "Config status data tranfer object")
@Validated
public class InputConfigStatus {

    @JsonProperty("id")
    @NotNull(message = "An id must be provided.")
    private Long id;

    @JsonProperty("stateList")
    @Valid
    private List<InputConfigState> stateList;

    @ApiModelProperty(required = true, value = "Config identifier")
    public long getId() {
        return id;
    }

    @ApiModelProperty(required = true, value = "Config state list")
    public List<InputConfigState> getStateList() {
        return stateList;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (id == null) {
            throw new BadRequestException("Invalid configStatus.", "Id is null.", "/probs/configstatus-null-params", "about:blank");
        }
        if (stateList == null) {
            throw new BadRequestException("Invalid configStatus.", "StateList is null.", "/probs/configstatus-null-params", "about:blank");
        }
        for (InputConfigState curState : stateList) {
            curState.validate();
        }
    }
}
