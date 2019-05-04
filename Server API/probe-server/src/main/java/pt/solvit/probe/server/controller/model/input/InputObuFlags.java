/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ObuFlags", description = "Obu flags data tranfer object")
@Validated
public class InputObuFlags {

    @JsonProperty("authenticate")
    private Boolean authenticate;
    @JsonProperty("uploadRequest")
    private Boolean uploadRequest;
    @JsonProperty("clearAlarmsRequest")
    private Boolean clearAlarmsRequest;
    @JsonProperty("resetRequest")
    private Boolean resetRequest;
    @JsonProperty("shutdownRequest")
    private Boolean shutdownRequest;

    public Boolean isAuthenticate() {
        return authenticate;
    }

    public Boolean isUploadRequest() {
        return uploadRequest;
    }

    public Boolean isClearAlarmsRequest() {
        return clearAlarmsRequest;
    }

    public Boolean isResetRequest() {
        return resetRequest;
    }

    public Boolean isShutdownRequest() {
        return shutdownRequest;
    }
}
