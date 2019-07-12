/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pt.solvit.probe.server.controller.impl.util.Profile;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Alarms", description = "Alarms data tranfer object")
public class Alarms {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("critical")
    private int critical;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("major")
    private int major;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("warning")
    private int warning;

    public Alarms() {
        this.critical = 0;
        this.major = 0;
        this.warning = 0;
    }

    public Alarms(int critical, int major, int warning) {
        this.critical = critical;
        this.major = major;
        this.warning = warning;
    }

    @ApiModelProperty(required = true, value = "Number of critical alarms")
    public int getCritical() {
        return critical;
    }

    @ApiModelProperty(required = true, value = "Number of major alarms")
    public int getMajor() {
        return major;
    }

    @ApiModelProperty(required = true, value = "Number of warning alarms")
    public int getWarning() {
        return warning;
    }
}
