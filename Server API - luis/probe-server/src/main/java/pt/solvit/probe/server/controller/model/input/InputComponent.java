/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.ComponentType;
import pt.solvit.probe.server.model.enums.ModemType;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Component", description = "Component data tranfer object")
@Validated
public class InputComponent {

    private static final Logger LOGGER = Logger.getLogger(InputComponent.class.getName());

    @JsonProperty("serialNumber")
    @NotNull(message = "A serialNumber must be provided.")
    private String serialNumber;

    @JsonProperty("componentType")
    @NotNull(message = "A componentType must be provided.")
    private String componentType;

    @JsonProperty("manufacturer")
    @NotNull(message = "A manufacturer must be provided.")
    private String manufacturer;

    @JsonProperty("model")
    @NotNull(message = "A model must be provided.")
    private String model;

    @JsonProperty("modemType")
    private String modemType;

    @JsonProperty("imei")
    private String imei;

    @ApiModelProperty(example = "serial number", required = true, value = "Serial Number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @ApiModelProperty(example = "MODEM", required = true, value = "Component type", allowableValues = "MODEM, GPS, LEDS, POWER_SUPPLY, MOTHERBOARD, POWER_BOARD")
    public ComponentType getComponentType() {
        try {
            return ComponentType.valueOf(componentType);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ComponentType enum", componentType);
            return null;
        }
    }

    @ApiModelProperty(example = "manufacturer", required = true, value = "Component manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @ApiModelProperty(example = "model", required = true, value = "Component model")
    public String getModel() {
        return model;
    }

    @ApiModelProperty(example = "GSMR", required = true, value = "Modem type", allowableValues = "PLMN, GSMR")
    public ModemType getModemType() {
        try {
            return modemType == null ? null : ModemType.valueOf(modemType);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ModemType enum", modemType);
            return null;
        }
    }

    @ApiModelProperty(example = "111111111111111", value = "International Mobile Equipment Identity")
    public String getImei() {
        return imei;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (componentType == null) {
            throw new BadRequestException("Invalid component.", "ComponentType is null.", "/probs/component-null-params", "about:blank");
        }
        if (manufacturer == null) {
            throw new BadRequestException("Invalid component.", "Manufacturer is null.", "/probs/component-null-params", "about:blank");
        }
        if (model == null) {
            throw new BadRequestException("Invalid component.", "Model is null.", "/probs/component-null-params", "about:blank");
        }
        ComponentType componentTypeEnum = getComponentType();
        if (componentTypeEnum == null) {
            throw new BadRequestException("Invalid component.", "Invalid componentType.", "/probs/component-invalid-componenttype", "about:blank");
        }
        if (componentTypeEnum == ComponentType.MODEM) {
            if (modemType == null) {
                throw new BadRequestException("Invalid component.", "ModemType is null.", "/probs/component-null-params", "about:blank");
            }
            if (imei == null) {
                throw new BadRequestException("Invalid component.", "Imei is null.", "/probs/component-null-params", "about:blank");
            }
        } else if (serialNumber == null) {
            throw new BadRequestException("Invalid component.", "SerialNumber is null.", "/probs/component-null-params", "about:blank");
        }
    }
}
