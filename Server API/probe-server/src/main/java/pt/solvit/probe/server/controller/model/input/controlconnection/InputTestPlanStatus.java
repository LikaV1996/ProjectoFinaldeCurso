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
@ApiModel(value = "TestPlanStatus", description = "Test plan status data tranfer object")
@Validated
public class InputTestPlanStatus {

    @JsonProperty("id")
    @NotNull(message = "An id must be provided.")
    private Long id;

    @JsonProperty("stateList")
    @Valid
    private List<InputTestPlanState> stateList;

    @ApiModelProperty(required = true, value = "Test plan identifier")
    public long getId() {
        return id;
    }

    @ApiModelProperty(required = true, value = "Test plan state list")
    public List<InputTestPlanState> getStateList() {
        return stateList;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (id == null) {
            throw new BadRequestException("Invalid testPlanStatus.", "Id is null.", "string", "about:blank");
        }
        if (stateList == null) {
            throw new BadRequestException("Invalid testPlanStatus.", "StateList is null.", "string", "about:blank");
        }
        for (InputTestPlanState curState : stateList) {
            curState.validate();
        }
    }
}
