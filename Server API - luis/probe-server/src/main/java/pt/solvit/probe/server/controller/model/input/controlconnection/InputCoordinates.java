/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Coordinates", description = "Coordinates data tranfer object")
@Validated
public class InputCoordinates {

    @JsonProperty("lat")
    @NotNull(message = "A latitude must be provided.")
    private Double lat;

    @JsonProperty("lon")
    @NotNull(message = "A longitude must be provided.")
    private Double lon;

    @ApiModelProperty(example = "38.687918099", required = true, value = "Latitude")
    public double getLat() {
        return lat;
    }

    @ApiModelProperty(example = "-9.316346627", required = true, value = "Longitude")
    public double getLon() {
        return lon;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (lat == null) {
            throw new BadRequestException("Invalid coordinates.", "Lat is null.", "/probs/coordinates-null-params", "about:blank");
        }
        if (lon == null) {
            throw new BadRequestException("Invalid coordinates.", "Lon is null.", "/probs/coordinates-null-params", "about:blank");
        }
    }
}
