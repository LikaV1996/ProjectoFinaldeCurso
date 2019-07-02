/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.model.Config;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Obu", description = "Obu data tranfer object")
@Validated
public class InputObu {

    @JsonProperty("hardwareId")
    @NotNull(message = "A hardwareId must be provided.")
    private Long hardwareId;

    @JsonProperty("sims")
    @Valid
    private List<SimCard> sims;

    @JsonProperty("obuName")
    @Valid
    private String obuName;

    /*  //removed (read-only ; not updatable ; not user created)
    @JsonProperty("factoryConfig")
    private InputConfig factoryConfig;
    */

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



    @ApiModelProperty(example = "1", value = "Hardware identifier")
    public Long getHardwareId() {
        return hardwareId;
    }

    /*  //removed
    @ApiModelProperty(value = "Factory config")
    public InputConfig getFactoryConfig() {
        return factoryConfig;
    }
    */

    @ApiModelProperty(value = "SIM list")
    public List<SimCard> getSims() {
        return sims;
    }

    @ApiModelProperty(value = "Obu name")
    public String getObuName() {
        return obuName;
    }


    @ApiModelProperty(value = "Flag authenticate")
    public Boolean isAuthenticate() { return authenticate; }    //used on update
    @ApiModelProperty(value = "Flag uploadRequest")
    public Boolean isUploadRequest() { return uploadRequest; }  //used on update
    @ApiModelProperty(value = "Flag clearAlarmsRequest")
    public Boolean isClearAlarmsRequest() { return clearAlarmsRequest; }  //used on update
    @ApiModelProperty(value = "Flag resetRequest")
    public Boolean isResetRequest() { return resetRequest; }  //used on update
    @ApiModelProperty(value = "Flag shutdownRequest")
    public Boolean isShutdownRequest() { return shutdownRequest; }  //used on update


    @ApiModelProperty(hidden = true)
    public void validateForCreate() {
        if(obuName == null){
            throw new BadRequestException("Invalid obu.", "obuName is null.", "string", "about:blank");
        }
        if (hardwareId == null) {
            throw new BadRequestException("Invalid obu.", "HardwareId is null.", "string", "about:blank");
        }
        if (sims != null) {
            for (SimCard curSim : sims) {
                curSim.validate();
            }
        }
        /*
        if(obuPassword == null){
            throw new BadRequestException("Invalid obu.", "obuPassword is null.", "string", "about:blank");
        }
        */
    }

    @ApiModelProperty(hidden = true)
    public void validateForUpdate() {
        boolean updatingFlags = authenticate != null || uploadRequest != null || clearAlarmsRequest != null || resetRequest != null || shutdownRequest != null;
        if (hardwareId == null && sims == null && obuName == null && !updatingFlags) { //&& factoryConfig == null) {
            throw new BadRequestException("Invalid obu.", "Nothing fields to update.", "string", "about:blank");
        }
        /*  //removed
        if (factoryConfig != null) {
            factoryConfig.validate();
        }
        */
        if (sims != null) {
            for (SimCard curSim : sims) {
                curSim.validate();
            }
        }

    }
}
