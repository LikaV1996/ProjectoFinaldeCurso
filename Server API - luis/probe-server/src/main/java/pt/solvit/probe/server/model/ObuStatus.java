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
import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.controlconnection.NetworkInterface;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ObuStatus", description = "Obu status data tranfer object")
public class ObuStatus {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("date")
    private LocalDateTime date;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("location")
    private Location location;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("storage")
    private Storage storage;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("alarms")
    private Alarms alarms;

    @JsonView(Profile.ExtendedView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("temperature")
    private Float temperature;

    @JsonView(Profile.ExtendedView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("networkInterfaces")
    private List<NetworkInterface> networkInterfaces;

    public ObuStatus(LocalDateTime date, Float temperature, Location location, Storage storage, Alarms alarms, List<NetworkInterface> networkInterfaces) {
        this.date = date;
        this.temperature = temperature;
        this.location = location;
        this.storage = storage;
        this.alarms = alarms;
        this.networkInterfaces = networkInterfaces;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getLocalDateTime() {
        return date;
    }

    @ApiModelProperty(value = "Information date (ISO 8601)")
    public String getDate() {
        return ISO8601_DATE_FORMATTER.format(date);
    }

    public Location getLocation() {
        return location;
    }

    public Storage getStorage() {
        return storage;
    }

    public Alarms getAlarms() {
        return alarms;
    }

    public float getTemperature() {
        return temperature;
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    @JsonIgnore
    public String getNetworkInterfacesString() {
        return GSON.toJson(networkInterfaces);
    }
}
