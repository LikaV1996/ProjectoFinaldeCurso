/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.GpsFix;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Location", description = "Location data tranfer object")
@Validated
public class InputLocation {

    private static final Logger LOGGER = Logger.getLogger(InputLocation.class.getName());
    
    @JsonProperty("date")
    @NotNull(message = "A date must be provided.")
    private String date;

    @JsonProperty("coordinates")
    @Valid
    private InputCoordinates coordinates;

    @JsonProperty("heightAboveEllipsoid")
    private Double heightAboveEllipsoid;

    @JsonProperty("heightAboveMSL")
    private Double heightAboveMSL;

    @JsonProperty("groundSpeed")
    private Double groundSpeed;

    @JsonProperty("heading")
    private Double heading;
    
    @JsonProperty("gpsFix")
    private String gpsFix;

    
    @ApiModelProperty(required = true, value = "Date of acquisition (ISO 8601)")
    public String getDate() {
        return date;
    }

    @ApiModelProperty(required = true, value = "Latitude")
    public InputCoordinates getCoordinates() {
        return coordinates;
    }

    @ApiModelProperty(required = true, value = "Height above ellipsoid [m]")
    public Double getHeightAboveEllipsoid() {
        return heightAboveEllipsoid;
    }

    @ApiModelProperty(required = true, value = "Height above MSL (Mean Sea Level) [m]")
    public Double getHeightAboveMSL() {
        return heightAboveMSL;
    }

    @ApiModelProperty(required = true, value = "Ground speed (km/h)")
    public Double getGroundSpeed() {
        return groundSpeed;
    }

    @ApiModelProperty(required = true, value = "Heading [º]")
    public Double getHeading() {
        return heading;
    }
    
    @ApiModelProperty(required = true, value = "Gps Fix")
    public GpsFix getGpsFix() {
        try {
            return GpsFix.valueOf(gpsFix);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match GpsFix enum", gpsFix);
            return null;
        }
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (date == null) {
            throw new BadRequestException("Invalid location.", "Date is null.", "/probs/location-null-params", "about:blank");
        }
        if (DateUtil.getDateFromIsoString(date) == null) {
            throw new BadRequestException("Invalid location.", "Date is not on ISO format.", "/probs/location-invalide-date", "about:blank");
        }
        if (coordinates == null) {
            throw new BadRequestException("Invalid location.", "Coordinates is null.", "/probs/location-null-params", "about:blank");
        }
        coordinates.validate();
        if (groundSpeed == null) {
            throw new BadRequestException("Invalid location.", "GroundSpeed is null.", "/probs/location-null-params", "about:blank");
        }
    }
}
