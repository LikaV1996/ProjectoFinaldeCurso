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

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "NetworkInterface", description = "Network interface tranfer object")
@Validated
public class NetworkInterface {

    @JsonProperty("name")
    @NotNull(message = "A name must be provided.")
    private String name;

    @JsonProperty("ip")
    @NotNull(message = "A ip must be provided.")
    private String ip;

    @ApiModelProperty(required = true, value = "Name")
    public String getName() {
        return name;
    }

    @ApiModelProperty(required = true, value = "IP address")
    public String getIP() {
        return ip;
    }
}
