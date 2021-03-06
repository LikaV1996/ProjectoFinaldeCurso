/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.UserProfile;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "User", description = "User data tranfer object")
@Validated
public class InputUser {

    private static final Logger LOGGER = Logger.getLogger(InputUser.class.getName());

    @JsonProperty("userName")
    @NotNull(message = "An userName must be provided.")
    private String userName;

    @JsonProperty("userPassword")
    @NotNull(message = "An userPassword must be provided.")
    private String userPassword;

    @JsonProperty("userProfile")
    @NotNull(message = "An userProfile must be provided.")
    private String userProfile;

    @JsonProperty("suspended")
    @NotNull(message = "An userProfile must be provided.")
    private Boolean suspended;


    @ApiModelProperty(example = "username", required = true, value = "User name")
    public String getUserName() {
        return userName;
    }

    @ApiModelProperty(example = "password", required = true, value = "user password")
    public String getUserPassword() {
        return userPassword;
    }

    @ApiModelProperty(example = "NORMAL_USER", required = true, value = "User profile", allowableValues = "ADMIN, SUPER_USER, NORMAL_USER")
    public UserProfile getUserProfile() {
        try {
            return UserProfile.valueOf(userProfile);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match UserProfile enum", userProfile);
            return null;
        }
    }

    @ApiModelProperty(example = "true", required = true, value = "Suspended", allowableValues = "true, false")
    public Boolean getSuspended() {
        return suspended;
    }

    @ApiModelProperty(hidden = true)
    public void validateForCreate() {
        if (userName == null) {
            throw new BadRequestException("Invalid user.", "UserName is null.", "/probs/user-null-params", "about:blank");
        }
        if (userPassword == null) {
            throw new BadRequestException("Invalid user.", "UserPassword is null.", "/probs/user-null-params", "about:blank");
        }
        if (userProfile == null) {
            throw new BadRequestException("Invalid user.", "UserProfile is null.", "/probs/user-null-params", "about:blank");
        }
        if (getUserProfile() == null) {
            throw new BadRequestException("Invalid user.", "Invalid userProfile.", "/probs/user-invalid-userprofile", "about:blank");
        }
    }

    @ApiModelProperty(hidden = true)
    public void validateForUpdate() {
        if (userName == null && userPassword == null && userProfile == null && suspended == null) {
            throw new BadRequestException("Invalid user.", "All fields are null.", "/probs/user-null-params", "about:blank");
        }
        if (userProfile != null && getUserProfile() == null) {
            throw new BadRequestException("Invalid user.", "Invalid userProfile.", "/probs/user-invalid-userprofile", "about:blank");
        }
    }
}
