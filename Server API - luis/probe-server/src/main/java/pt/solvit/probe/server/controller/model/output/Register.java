/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Register", description = "Register data tranfer object")
public class Register {

    @JsonProperty("obuId")
    private long obuId;

    @JsonProperty("password")
    private String password;

    public Register(long obuId, String password) {
        this.obuId = obuId;
        this.password = password;
    }

    @ApiModelProperty(required = true, value = "Obu identification")
    public long getObuId() {
        return obuId;
    }

    @ApiModelProperty(required = true, value = "Obu password")
    public String getPassword() {
        return password;
    }
}
