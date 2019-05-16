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

    /*
    @JsonProperty("obuPassword")
    @Valid
    private String obuPassword;
    */

    @ApiModelProperty(example = "1", required = true, value = "Hardware identifier")
    public Long getHardwareId() {
        return hardwareId;
    }

    @ApiModelProperty(value = "SIM list")
    public List<SimCard> getSims() {
        return sims;
    }

    @ApiModelProperty(value = "Obu name")
    public String getObuName() {
        return obuName;
    }

    /*
    @ApiModelProperty(value = "Obu password")
    public String getObuPassword() {
        return obuPassword;
    }
    */

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (hardwareId == null) {
            throw new BadRequestException("Invalid obu.", "HardwareId is null.", "string", "about:blank");
        }
        if (sims != null) {
            for (SimCard curSim : sims) {
                curSim.validate();
            }
        }
        if(obuName == null){
            throw new BadRequestException("Invalid obu.", "obuName is null.", "string", "about:blank");
        }
        /*
        if(obuPassword == null){
            throw new BadRequestException("Invalid obu.", "obuPassword is null.", "string", "about:blank");
        }
        */

    }
}
