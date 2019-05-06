/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.ComponentType;
import pt.solvit.probe.server.model.enums.ModemType;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Component", description = "Component data tranfer object")
public class Component {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("serialNumber")
    private String serialNumber;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("componentType")
    private ComponentType componentType;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("manufacturer")
    private String manufacturer;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("model")
    private String model;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("modemType")
    private ModemType modemType;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("imei")
    private String imei;

    public Component(String serialNumber, ComponentType componentType, String manufacturer, String model, ModemType modemType, String imei) {
        this.serialNumber = serialNumber;
        this.componentType = componentType;
        this.manufacturer = manufacturer;
        this.model = model;
        this.modemType = modemType;
        this.imei = imei;
    }

    @ApiModelProperty(required = true, value = "Component serial number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @ApiModelProperty(required = true, value = "Component type")
    public ComponentType getComponentType() {
        return componentType;
    }

    @ApiModelProperty(required = true, value = "Component manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @ApiModelProperty(required = true, value = "Component model")
    public String getModel() {
        return model;
    }

    @ApiModelProperty(value = "Modem type")
    public ModemType getModemType() {
        return modemType;
    }

    @ApiModelProperty(value = "Modem imei")
    public String getImei() {
        return imei;
    }
}
