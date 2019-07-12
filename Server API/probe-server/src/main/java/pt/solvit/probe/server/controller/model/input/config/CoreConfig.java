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
@ApiModel(value = "CoreConfig", description = "Core configuration data tranfer object")
@Validated
public class CoreConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxSystemLogSize")
    @NotNull(message = "A maxSystemLogSize must be provided.")
    private Long maxSystemLogSize;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("storageMonitorPeriod")
    @NotNull(message = "A storageMonitorPeriod must be provided.")
    private Long storageMonitorPeriod;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("storageWarningThreshold")
    @NotNull(message = "A storageWarningThreshold must be provided.")
    private Long storageWarningThreshold;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("storageCriticalThreshold")
    @NotNull(message = "A storageCriticalThreshold must be provided.")
    private Long storageCriticalThreshold;

    @ApiModelProperty(example = "10485760", required = true, value = "Core maximum system log size")
    public long getMaxSystemLogSize() {
        return maxSystemLogSize;
    }

    @ApiModelProperty(example = "300", required = true, value = "Core storage monitor period")
    public long getStorageMonitorPeriod() {
        return storageMonitorPeriod;
    }

    @ApiModelProperty(example = "524288000", required = true, value = "Core storage warning threshold")
    public long getStorageWarningThreshold() {
        return storageWarningThreshold;
    }

    @ApiModelProperty(example = "209715200", required = true, value = "Core storage critical threshold")
    public long getStorageCriticalThreshold() {
        return storageCriticalThreshold;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (maxSystemLogSize == null) {
            throw new BadRequestException("Invalid configuration.", "Core: maxSystemLogSize is null.", "/probs/coreconfig-null-params", "about:blank");
        }
        if (storageMonitorPeriod == null) {
            throw new BadRequestException("Invalid configuration.", "Core: storageMonitorPeriod is null.", "/probs/coreconfig-null-params", "about:blank");
        }
        if (storageWarningThreshold == null) {
            throw new BadRequestException("Invalid configuration.", "Core: storageWarningThreshold is null.", "/probs/coreconfig-null-params", "about:blank");
        }
        if (storageCriticalThreshold == null) {
            throw new BadRequestException("Invalid configuration.", "Core: storageCriticalThreshold is null.", "/probs/coreconfig-null-params", "about:blank");
        }
    }
}
