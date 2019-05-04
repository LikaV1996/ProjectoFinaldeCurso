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
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.GpsFix;
import pt.solvit.probe.server.model.properties.LocationProperties;
import pt.solvit.probe.server.util.DateUtil;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Location", description = "Location data tranfer object")
public class Location {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("date")
    private String date;
    
    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("lat")
    private Double lat;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("lon")
    private Double lon;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("heightAboveEllipsoid")
    private Double heightAboveEllipsoid;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("heightAboveMSL")
    private Double heightAboveMSL;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("groundSpeed")
    private Double groundSpeed;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("heading")
    private Double heading;
    
    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("gpsFix")
    private GpsFix gpsFix;

    public Location() {
    }

    public Location(String date, double lat, double lon, Double heightAboveEllipsoid, Double heightAboveMSL, Double groundSpeed, Double heading, GpsFix gpsFix) {
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.heightAboveEllipsoid = heightAboveEllipsoid;
        this.heightAboveMSL = heightAboveMSL;
        this.groundSpeed = groundSpeed;
        this.heading = heading;
        this.gpsFix = gpsFix;
    }
    
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getLocalDateTime() {
        return DateUtil.getDateFromIsoString(date);
    }

    @ApiModelProperty(required = true, value = "Date of acquisition (ISO 8601))")
    public String getDate() {
        return date;
    }
    
    @ApiModelProperty(required = true, value = "Latitude")
    public Double getLat() {
        return lat;
    }

    @ApiModelProperty(required = true, value = "Longitude")
    public Double getLon() {
        return lon;
    }

    @ApiModelProperty(required = true, value = "Height Above Ellipsoid [m]")
    public Double getHeightAboveEllipsoid() {
        return heightAboveEllipsoid;
    }

    @ApiModelProperty(required = true, value = "Height Above MSL (Mean See Level) [m]")
    public Double getHeightAboveMSL() {
        return heightAboveMSL;
    }

    @ApiModelProperty(required = true, value = "Ground Speed [km/h]")
    public Double getGroundSpeed() {
        return groundSpeed;
    }

    @ApiModelProperty(required = true, value = "Heading [º]")
    public Double getHeading() {
        return heading;
    }
    
    @ApiModelProperty(required = true, value = "GpsFix")
    public GpsFix getGpsFix() {
        return gpsFix;
    }

    @JsonIgnore
    public String getLocationPropertiesString() {
        return GSON.toJson(new LocationProperties(this));
    }
}
