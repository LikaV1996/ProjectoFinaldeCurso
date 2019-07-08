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
@ApiModel(value = "VoiceConfig", description = "Voice configuration data tranfer object")
@Validated
public class VoiceConfig {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("defaultCallDuration")
    @NotNull(message = "A defaultCallDuration must be provided.")
    private Long defaultCallDuration;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("incomingCallTimeout")
    @NotNull(message = "A incomingCallTimeout must be provided.")
    private Long incomingCallTimeout;

    @ApiModelProperty(example = "300", required = true, value = "Default call duration")
    public long getDefaultCallDuration() {
        return defaultCallDuration;
    }

    @ApiModelProperty(example = "3600", required = true, value = "Incoming call timeout")
    public long getIncomingCallTimeout() {
        return incomingCallTimeout;
    }
   
    @ApiModelProperty(hidden = true)
    public void validate() {
        if (defaultCallDuration == null) {
            throw new BadRequestException("Invalid configuration.", "Voice: defaultCallDuration is null.", "/probs/voice-null-params", "about:blank");
        }
        if (incomingCallTimeout == null) {
            throw new BadRequestException("Invalid configuration.", "Voice: incomingCallTimeout is null.", "/probs/voice-null-params", "about:blank");
        }
    }
}
