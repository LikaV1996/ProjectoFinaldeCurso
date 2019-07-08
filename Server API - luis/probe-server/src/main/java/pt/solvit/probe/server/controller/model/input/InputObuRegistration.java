/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ObuRegistration", description = "Obu registration data tranfer object")
@Validated
public class InputObuRegistration {

    @JsonProperty("serialNumber")
    @NotNull(message = "A serialNumber must be provided.")
    private String serialNumber;

    @JsonProperty("factoryConfig")
    @Valid
    private InputConfig factoryConfig;

    @ApiModelProperty(required = true, value = "Serial number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @ApiModelProperty(required = true, value = "Factory configuration")
    public InputConfig getFactoryConfig() {
        return factoryConfig;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (serialNumber == null) {
            throw new BadRequestException("Invalid obu.", "SerialNumber is null.", "/probs/oburegistration-null-params", "about:blank");
        }
        if (factoryConfig == null) {
            throw new BadRequestException("Invalid obu.", "FactoryConfig is null.", "/probs/oburegistration-null-params", "about:blank");
        }
        factoryConfig.validate();
    }
}
