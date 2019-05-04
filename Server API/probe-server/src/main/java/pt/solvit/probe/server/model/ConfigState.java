/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.ConfigStateEnum;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ConfigState", description = "Configuration state data tranfer object")
public class ConfigState {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("state")
    private ConfigStateEnum state;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("date")
    private LocalDateTime date;

    public ConfigState(ConfigStateEnum state, LocalDateTime date) {
        this.state = state;
        this.date = date;
    }

    @ApiModelProperty(value = "Configuration state")
    public ConfigStateEnum getState() {
        return state;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getLocalDateTime() {
        return date;
    }

    @ApiModelProperty(value = "State date")
    public String getDate() {
        return ISO8601_DATE_FORMATTER.format(date);
    }
}
