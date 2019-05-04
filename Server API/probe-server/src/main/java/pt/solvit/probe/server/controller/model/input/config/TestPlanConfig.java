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
@ApiModel(value = "TestPlanConfig", description = "Test plan configuration data tranfer object")
@Validated
public class TestPlanConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("defaultMaxRetries")
    @NotNull(message = "A defaultMaxRetries must be provided.")
    private Integer defaultMaxRetries;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("defaultRetryDelay")
    @NotNull(message = "A defaultRetryDelay must be provided.")
    private Long defaultRetryDelay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxLogSize")
    @NotNull(message = "A maxLogSize must be provided.")
    private Long maxLogSize;

    @ApiModelProperty(example = "3", required = true, value = "Default maximum number of retries")
    public int getDefaultMaxRetries() {
        return defaultMaxRetries;
    }

    @ApiModelProperty(example = "60", required = true, value = "Default retry delay")
    public long getDefaultRetryDelay() {
        return defaultRetryDelay;
    }

    @ApiModelProperty(example = "10485760", required = true, value = "Default retry delay")
    public long getMaxLogSize() {
        return maxLogSize;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (defaultMaxRetries == null) {
            throw new BadRequestException("Invalid configuration.", "Test Plan: defaultMaxRetries null.", "string", "about:blank");
        }
        if (defaultRetryDelay == null) {
            throw new BadRequestException("Invalid configuration.", "Test Plan: defaultRetryDelay null.", "string", "about:blank");
        }
        if (maxLogSize == null) {
            throw new BadRequestException("Invalid configuration.", "Test Plan: maxLogSize null.", "string", "about:blank");
        }
    }
}
