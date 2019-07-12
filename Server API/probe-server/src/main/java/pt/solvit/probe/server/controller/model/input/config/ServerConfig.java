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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.ServerInterface;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "ServerConfig", description = "Server configuration data tranfer object")
@Validated
public class ServerConfig {

    private static final Logger LOGGER = Logger.getLogger(ServerConfig.class.getName());

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("serverInterface")
    @NotNull(message = "A serverInterface must be provided.")
    private String serverInterface;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("registrationRetryDelay")
    @NotNull(message = "A registrationRetryDelay must be provided.")
    private Long registrationRetryDelay;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("serverAddress")
    @NotNull(message = "A serverAddress must be provided.")
    private String serverAddress;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("serverUser")
    @NotNull(message = "A serverUser must be provided.")
    private String serverUser;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("serverPassword")
    @NotNull(message = "A serverPassword must be provided.")
    private String serverPassword;

    @ApiModelProperty(example = "MODEM_GSMR", required = true, value = "Network interface", allowableValues = "MODEM_GSMR, PPP_GSMR")
    public ServerInterface getServerInterface() {
        try {
            return ServerInterface.valueOf(serverInterface);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match NetworkInterface enum", serverInterface);
            return null;
        }
    }

    @ApiModelProperty(example = "60", required = true, value = "Registration retry delay")
    public Long getRegistrationRetryDelay() {
        return registrationRetryDelay;
    }

    @ApiModelProperty(example = "000.000.00.00:8080", required = true, value = "Server address")
    public String getServerAddress() {
        return serverAddress;
    }

    @ApiModelProperty(example = "username", required = true, value = "Server username")
    public String getServerUser() {
        return serverUser;
    }

    @ApiModelProperty(example = "password", required = true, value = "Server password")
    public String getServerPassword() {
        return serverPassword;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (serverInterface == null) {
            throw new BadRequestException("Invalid configuration.", "Server: serverInterface is null.", "/probs/serverconfig-null-params", "about:blank");
        }
        if (getServerInterface() == null) {
            throw new BadRequestException("Invalid configuration.", "Server: invalid networkInterface.", "/probs/serverconfig-invalid-networkinterface", "about:blank");
        }
        if (registrationRetryDelay == null) {
            throw new BadRequestException("Invalid configuration.", "Server: registrationRetryDelay is null.", "/probs/serverconfig-null-params", "about:blank");
        }
        if (serverAddress == null) {
            throw new BadRequestException("Invalid configuration.", "Network: serverAddress is null.", "/probs/serverconfig-null-params", "about:blank");
        }
        if (serverUser == null) {
            throw new BadRequestException("Invalid configuration.", "Network: serverUser is null.", "/probs/serverconfig-null-params", "about:blank");
        }
        if (serverPassword == null) {
            throw new BadRequestException("Invalid configuration.", "Network: serverPassword is null.", "/probs/serverconfig-null-params", "about:blank");
        }
    }
}
