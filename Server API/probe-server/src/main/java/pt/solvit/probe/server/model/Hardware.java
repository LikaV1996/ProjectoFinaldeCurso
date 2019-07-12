/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.properties.HardwareProperties;
import pt.solvit.probe.server.repository.model.HardwareDao;

import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Hardware", description = "Hardware data tranfer object")
public class Hardware extends CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("serialNumber")
    private String serialNumber;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("components")
    private List<Component> components;

    public Hardware(Long id, String serialNumber, List<Component> components, String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.serialNumber = serialNumber;
        this.components = components;
    }

    @ApiModelProperty(required = true, value = "Hardware identifier")
    public Long getId() {
        return id;
    }

    @ApiModelProperty(required = true, value = "Hardware serial number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @ApiModelProperty(required = true, value = "Component list")
    public List<Component> getComponents() {
        return components;
    }

    @JsonIgnore
    public String getPropertiesString() {
        return GSON.toJson(new HardwareProperties(this));
    }

    public static HardwareDao transformToHardwareDao(Hardware hardware) {
        return new HardwareDao(hardware.getId(), hardware.getSerialNumber(), hardware.getPropertiesString(),
                hardware.getCreator(), Timestamp.valueOf(hardware.getCreationLocalDateTime()),
                hardware.getModifier(), hardware.getModifiedDate() != null ? Timestamp.valueOf(hardware.getModifiedDate()) : null);
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

}
