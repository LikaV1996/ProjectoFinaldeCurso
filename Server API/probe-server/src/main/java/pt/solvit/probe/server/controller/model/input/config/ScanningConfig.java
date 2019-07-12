/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.Profile;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ScanningConfig", description = "Scanning configuration data tranfer object")
@Validated
public class ScanningConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableMonitor")
    @NotNull(message = "An enableMonitor must be provided.")
    private Boolean enableMonitor;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("sampleTime")
    @NotNull(message = "A sampleTime must be provided.")
    private Long sampleTime;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableCsq")
    private Boolean enableCsq = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableMoni")
    private Boolean enableMoni = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableMonp")
    private Boolean enableMonp = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableSmond")
    private Boolean enableSmond = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableSmonc")
    private Boolean enableSmonc = false;

    @ApiModelProperty(example = "false", required = true, value = "Enable monitor")
    public boolean isEnableMonitor() {
        return enableMonitor;
    }

    @ApiModelProperty(example = "60", value = "Sample time")
    public long getSampleTime() {
        return sampleTime;
    }

    @ApiModelProperty(example = "true", required = true, value = "Enable Csq acquisition")
    public boolean isEnableCsq() {
        return enableCsq;
    }

    @ApiModelProperty(example = "true", required = true, value = "Enable Moni acquisition")
    public boolean isEnableMoni() {
        return enableMoni;
    }

    @ApiModelProperty(example = "true", value = "Enable Monp acquisition")
    public boolean isEnableMonp() {
        return enableMonp;
    }

    @ApiModelProperty(example = "true", value = "Enable Smond acquisition")
    public boolean isEnableSmond() {
        return enableSmond;
    }

    @ApiModelProperty(example = "true", value = "Enable Smonc acquisition")
    public boolean isEnableSmonc() {
        return enableSmonc;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (enableMonitor == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableMonitor is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (sampleTime == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: sampleTime is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (enableCsq == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableCsq is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (enableMoni == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableMoni is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (enableMonp == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableMonp is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (enableSmond == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableSmond is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
        if (enableSmonc == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning: enableSmonc is null.", "/probs/scanningconfig-null-params", "about:blank");
        }
    }
}
