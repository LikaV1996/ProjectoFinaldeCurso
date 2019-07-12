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
@ApiModel(value = "DownloadConfig", description = "Download configuration data tranfer object")
@Validated
public class DownloadConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("retryDelay")
    @NotNull(message = "A retryDelay must be provided.")
    private Long retryDelay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxRetries")
    @NotNull(message = "A maxRetries must be provided.")
    private Integer maxRetries;

    @ApiModelProperty(example = "120", required = true, value = "Download retry delay")
    public Long getRetryDelay() {
        return retryDelay;
    }

    @ApiModelProperty(example = "3", required = true, value = "Maximum number of download retries")
    public Integer getMaxRetries() {
        return maxRetries;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (retryDelay == null) {
            throw new BadRequestException("Invalid configuration.", "Download: retryDelay is null.", "/probs/downloadconfig-null-params", "about:blank");
        }
        if (maxRetries == null) {
            throw new BadRequestException("Invalid configuration.", "Download: maxRetries is null.", "/probs/downloadconfig-null-params", "about:blank");
        }
    }
}
