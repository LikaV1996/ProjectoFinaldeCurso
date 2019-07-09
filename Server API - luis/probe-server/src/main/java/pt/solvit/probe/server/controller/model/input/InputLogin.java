package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.UserProfile;

import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.UserProfile;


@ApiModel(value = "Login", description = "Login data transfer object")
@Validated
public class InputLogin {

    private static final Logger LOGGER = Logger.getLogger(InputLogin.class.getName());

    @JsonProperty("username")
    @NotNull(message = "An username must be provided.")
    private String username;

    @JsonProperty("password")
    @NotNull(message = "A password must be provided.")
    private String password;

    @ApiModelProperty(example = "username", required = true, value = "username")
    public String getUsername() {
        return username;
    }

    @ApiModelProperty(example = "password", required = true, value = "password")
    public String getPassword() {
        return password;
    }


    @ApiModelProperty(hidden = true)
    public void validate() {
        if (username == null) {
            throw new BadRequestException("Invalid login.", "Username is null.", "/probs/login-null-params", "about:blank");
        }
        if (password == null) {
            throw new BadRequestException("Invalid login.", "Password is null.", "/probs/login-null-params", "about:blank");
        }
    }
}
