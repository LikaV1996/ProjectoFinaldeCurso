/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.testplan;

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
@ApiModel(value = "Scanning", description = "Scanning data tranfer object")
@Validated
public class Scanning {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableScanning")
    @NotNull(message = "A enableScanning must be provided.")
    private Boolean enableScanning;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableCsq")
    @NotNull(message = "A enableCsq must be provided.")
    private Boolean enableCsq;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableMoni")
    @NotNull(message = "A enableMoni must be provided.")
    private Boolean enableMoni;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableMonp")
    @NotNull(message = "A enableMonp must be provided.")
    private Boolean enableMonp;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableSmond")
    @NotNull(message = "A enableSmond must be provided.")
    private Boolean enableSmond;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("enableSmonc")
    @NotNull(message = "A enableSmonc must be provided.")
    private Boolean enableSmonc;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("sampleTime")
    @NotNull(message = "A sampleTime must be provided.")
    private Long sampleTime;

    @ApiModelProperty(example = "true", required = true, value = "Enable scanning")
    public boolean isEnableScanning() {
        return enableScanning;
    }

    @ApiModelProperty(example = "false", value = "Enable Csq acquisition")
    public boolean isEnableCsq() {
        return enableCsq;
    }

    @ApiModelProperty(example = "false", value = "Enable Moni acquisition")
    public boolean isEnableMoni() {
        return enableMoni;
    }

    @ApiModelProperty(example = "false", value = "Enable Monp acquisition")
    public boolean isEnableMonp() {
        return enableMonp;
    }

    @ApiModelProperty(example = "true", value = "Enable Smond acquisition")
    public boolean isEnableSmond() {
        return enableSmond;
    }

    @ApiModelProperty(example = "false", value = "Enable Smonc acquisition")
    public boolean isEnableSmonc() {
        return enableSmonc;
    }

    @ApiModelProperty(example = "2", value = "Sample time")
    public long getSampleTime() {
        return sampleTime;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (enableScanning == null) {
            throw new BadRequestException("Invalid setup.", "Scanning: enableScanning is null.", "/probs/scanning-null-params", "about:blank");
        }
        if (enableScanning == true) {
            if (sampleTime == null) {
                throw new BadRequestException("Invalid setup.", "Scanning: sampleTime is null.", "/probs/scanning-null-params", "about:blank");
            }
            if (enableCsq == null && enableMoni == null && enableMonp == null && enableSmond == null && enableSmonc == null) {
                throw new BadRequestException("Invalid setup.", "Scanning: enableCsq and enableMoni and enableMonp and enableSmond and enableSmonc are all null.", "/probs/scanning-null-params", "about:blank");
            }
            if ((enableCsq != null && enableCsq == true) || (enableMoni != null && enableMoni == true)
                    || (enableMonp != null && enableMonp == true) || (enableSmond != null && enableSmond == true)
                    || (enableSmonc != null && enableSmonc == true)) {
                return;
            }
            throw new BadRequestException("Invalid setup.", "Scanning: enableCsq and enableMoni and enableMonp and enableSmond and enableSmonc are all false or null.", "/probs/scanning-invalid-enableflags", "about:blank");
        }
    }
}
