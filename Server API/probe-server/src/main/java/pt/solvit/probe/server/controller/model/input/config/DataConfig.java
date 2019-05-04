/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.Profile;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "DataConfig", description = "Data service configuration data tranfer object")
@Validated
public class DataConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("defaultMessage")
    @NotNull(message = "A defaultMessage must be provided.")
    private String defaultMessage;

    @ApiModelProperty(example = "Default message", required = true, value = "Default message")
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (defaultMessage == null) {
            throw new BadRequestException("Invalid configuration.", "Data: defaultMessage is null.", "string", "about:blank");
        }
    }
}
