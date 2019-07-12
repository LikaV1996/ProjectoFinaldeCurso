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
@ApiModel(value = "ArchiveConfig", description = "Archive configuration data tranfer object")
@Validated
public class ArchiveConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("expiration")
    @NotNull(message = "A expiration must be provided.")
    private Long expiration;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("period")
    @NotNull(message = "A period must be provided.")
    private Long period;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("referenceDate")
    @NotNull(message = "A referenceDate must be provided.")
    private String referenceDate;

    @ApiModelProperty(example = "2592000", required = true, value = "Expiration time")
    public long getExpiration() {
        return expiration;
    }

    @ApiModelProperty(example = "86400", required = true, value = "Scan period")
    public long getPeriod() {
        return period;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getReferenceLocalDateTime() {
        return DateUtil.getDateFromIsoString(referenceDate);
    }

    @ApiModelProperty(example = "2019-01-01T00:00:00", required = true, value = "Reference date")
    public String getReferenceDate() {
        return referenceDate;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (expiration == null) {
            throw new BadRequestException("Invalid configuration.", "Archive: expiration is null.", "/probs/archiveconfig-null-params", "about:blank");
        }
        if (period == null) {
            throw new BadRequestException("Invalid configuration.", "Archive: period is null.", "/probs/archiveconfig-null-params", "about:blank");
        }
        if (referenceDate == null) {
            throw new BadRequestException("Invalid configuration.", "Archive: referenceDate is null.", "/probs/archiveconfig-null-params", "about:blank");
        }
        if (getReferenceLocalDateTime() == null) {
            throw new BadRequestException("Invalid configuration.", "Archive: referenceDate is not on ISO format.", "/probs/archiveconfig-invalid-referencedate", "about:blank");
        }
    }
}
