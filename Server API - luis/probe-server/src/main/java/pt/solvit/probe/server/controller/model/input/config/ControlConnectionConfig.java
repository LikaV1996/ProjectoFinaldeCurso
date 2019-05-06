/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ControlConnectionConfig", description = "Control connection configuration data tranfer object")
@Validated
public class ControlConnectionConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("referenceDate")
    @NotNull(message = "A referenceDate must be provided.")
    private String referenceDate;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("period")
    @NotNull(message = "A period must be provided.")
    private Long period;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("retryDelay")
    @NotNull(message = "A retryDelay must be provided.")
    private Long retryDelay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxRetries")
    @NotNull(message = "A maxRetries must be provided.")
    private Integer maxRetries;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getReferenceLocalDateTime() {
        return DateUtil.getDateFromIsoString(referenceDate);
    }

    @ApiModelProperty(example = "2019-01-01T00:00:00", required = true, value = "Control connection reference date")
    public String getReferenceDate() {
        return referenceDate;
    }

    @ApiModelProperty(example = "600", required = true, value = "Control connection period")
    public long getPeriod() {
        return period;
    }

    @ApiModelProperty(example = "60", required = true, value = "Control connection retry delay")
    public long getRetryDelay() {
        return retryDelay;
    }

    @ApiModelProperty(example = "3", required = true, value = "Control connection maximum number of retries")
    public int getMaxRetries() {
        return maxRetries;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (referenceDate == null) {
            throw new BadRequestException("Invalid configuration.", "Control Connection: referenceDate is null.", "string", "about:blank");
        }
        if (period == null) {
            throw new BadRequestException("Invalid configuration.", "Control Connection: period is null.", "string", "about:blank");
        }
        if (retryDelay == null) {
            throw new BadRequestException("Invalid configuration.", "Control Connection: retryDelay is null.", "string", "about:blank");
        }
        if (maxRetries == null) {
            throw new BadRequestException("Invalid configuration.", "Control Connection: maxRetries is null.", "string", "about:blank");
        }
        if (getReferenceLocalDateTime() == null) {
            throw new BadRequestException("Invalid configuration.", "Control Connection: referenceDate is not on ISO format.", "string", "about:blank");
        }
    }
}
