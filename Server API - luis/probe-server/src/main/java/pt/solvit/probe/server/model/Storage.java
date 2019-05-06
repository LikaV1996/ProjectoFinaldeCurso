/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import pt.solvit.probe.server.controller.impl.util.Profile;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Storage {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("free")
    private Long free;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("usable")
    private Long usable;

    public Storage(long free, long usable) {
        this.free = free;
        this.usable = usable;
    }

    @ApiModelProperty(value = "Free storage")
    public Long getFree() {
        return free;
    }

    @ApiModelProperty(value = "Usable storage")
    public Long getUsable() {
        return usable;
    }
}
