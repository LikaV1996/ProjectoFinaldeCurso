/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.ObuUserRole;
import pt.solvit.probe.server.model.enums.UserProfile;

import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "User", description = "User data tranfer object")
@Validated
public class InputObuUser {

    private static final Logger LOGGER = Logger.getLogger(InputObuUser.class.getName());

    @JsonProperty("userID")
    @NotNull(message = "An userID must be provided.")
    private Long userID;

    @JsonProperty("obuID")
    @NotNull(message = "An obuID must be provided.")
    private Long obuID;

    @JsonProperty("role")
    @NotNull(message = "A role must be provided.")
    private String role;



    @ApiModelProperty(example = "userID", required = true, value = "ID of user")
    public Long getUserID() {
        return userID;
    }

    @ApiModelProperty(example = "obuID", required = true, value = "ID of obu")
    public Long getObuID() {
        return obuID;
    }

    @ApiModelProperty(example = "VIEWER", required = true, value = "role of user for that obu")
    public ObuUserRole getRole() {
        try {
            return ObuUserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ObuUserRole enum", role);
            return null;
        }
    }


    @ApiModelProperty(hidden = true)
    public void validate() {
        if (userID == null)
            throw new BadRequestException("Invalid obu_user registry.", "Invalid userID.", "string", "about:blank");
        if (obuID == null)
            throw new BadRequestException("Invalid obu_user registry.", "Invalid obuID.", "string", "about:blank");
        if (role == null || getRole() == null )
            throw new BadRequestException("Invalid obu_user registry.", "Invalid role.", "string", "about:blank");

    }
}
