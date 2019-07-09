/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

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
@ApiModel(value = "Hardware", description = "Hardware data tranfer object")
@Validated
public class InputHardware {

    @JsonProperty("serialNumber")
    @NotNull(message = "A serialNumber must be provided.")
    private String serialNumber;

    @JsonProperty("components")
    @Valid
    private List<InputComponent> components;

    @ApiModelProperty(example = "OBU001", value = "Serial number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @ApiModelProperty(value = "Components list")
    public List<InputComponent> getComponents() {
        return components;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (serialNumber == null) {
            throw new BadRequestException("Invalid hardware.", "SerialNumber is null.", "/probs/hardware-null-params", "about:blank");
        }
        if (components != null) {
            for (InputComponent curComponent : components) {
                curComponent.validate();
            }
        }
    }
}
