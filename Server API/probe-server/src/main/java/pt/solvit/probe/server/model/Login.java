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


@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Component", description = "Component data tranfer object")
public class Login {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("token")
    private String token;

    public Login(String token) {
        this.token = token;
    }

    @ApiModelProperty(required = true, value = "token")
    public String getToken() {
        return token;
    }
}
