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
@ApiModel(value = "ControlConnection", description = "Control connection data tranfer object")
@Validated
public class InputControlConnection {

    @JsonProperty("currentConfigId")
    @NotNull(message = "A currentConfigId must be provided.")
    private Long currentConfigId;

    @JsonProperty("currentTestPlanId")
    private Long currentTestPlanId;

    @JsonProperty("configList")
    @Valid
    private List<InputConfigStatus> configList;

    @JsonProperty("testPlanList")
    @Valid
    private List<InputTestPlanStatus> testPlanList;

    @JsonProperty("status")
    @Valid
    private InputObuStatus status;

    @JsonProperty("networkInterfaces")
    @Valid
    private List<NetworkInterface> networkInterfaces;

    @ApiModelProperty(required = true, value = "Current config identifier")
    public long getCurrentConfigId() {
        return currentConfigId;
    }

    @ApiModelProperty(value = "Current test plan identifier")
    public Long getCurrentTestPlanId() {
        return currentTestPlanId;
    }

    @ApiModelProperty(value = "Config list")
    public List<InputConfigStatus> getConfigList() {
        return configList;
    }

    @ApiModelProperty(value = "Test plan list")
    public List<InputTestPlanStatus> getTestPlanList() {
        return testPlanList;
    }

    @ApiModelProperty(value = "Status")
    public InputObuStatus getStatus() {
        return status;
    }

    @ApiModelProperty(value = "Network Interfaces")
    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (currentConfigId == null) {
            throw new BadRequestException("Invalid controlConnection.", "CurrentConfigId is null.", "/probs/controlconnection-null-params", "about:blank");
        }
        if (configList != null) {
            for (InputConfigStatus curStatus : configList) {
                curStatus.validate();
            }
        }
        if (testPlanList != null) {
            for (InputTestPlanStatus curStatus : testPlanList) {
                curStatus.validate();
            }
        }
        if (status == null) {
            throw new BadRequestException("Invalid controlConnection.", "Status is null.", "/probs/controlconnection-null-params", "about:blank");
        }
        status.validate();
    }
}
