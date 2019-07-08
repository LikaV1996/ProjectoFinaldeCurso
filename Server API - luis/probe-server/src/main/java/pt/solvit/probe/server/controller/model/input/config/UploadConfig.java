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
@ApiModel(value = "UploadConfig", description = "Upload configuration data tranfer object")
@Validated
public class UploadConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("autoUpload")
    @NotNull(message = "An autoUpload must be provided.")
    private Boolean autoUpload;

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

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("maxUploadSize")
    @NotNull(message = "A maxUploadSize must be provided.")
    private Long maxUploadSize;

    @ApiModelProperty(example = "true", required = true, value = "Enable automated upload")
    public boolean isAutoUpload() {
        return autoUpload;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getReferenceLocalDateTime() {
        return DateUtil.getDateFromIsoString(referenceDate);
    }

    @ApiModelProperty(example = "2019-01-01T00:00:00", required = true, value = "Upload reference date")
    public String getReferenceDate() {
        return referenceDate;
    }

    @ApiModelProperty(example = "86400", required = true, value = "Upload period")
    public long getPeriod() {
        return period;
    }

    @ApiModelProperty(example = "60", required = true, value = "Upload retry delay")
    public long getRetryDelay() {
        return retryDelay;
    }

    @ApiModelProperty(example = "3", required = true, value = "Maximum number of upload retries")
    public int getMaxRetries() {
        return maxRetries;
    }

    @ApiModelProperty(example = "5242880", required = true, value = "Maximum upload size")
    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (autoUpload == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: autoUpload is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (referenceDate == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: referenceDate is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (period == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: period is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (retryDelay == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: retryDelay is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (maxRetries == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: maxRetries is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (maxUploadSize == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: maxUploadSize is null.", "/probs/uploadconfig-null-params", "about:blank");
        }
        if (getReferenceLocalDateTime() == null) {
            throw new BadRequestException("Invalid configuration.", "Upload: referenceDate is not on ISO format.", "/probs/uploadconfig-invalid-referencedate", "about:blank");
        }
    }
}
