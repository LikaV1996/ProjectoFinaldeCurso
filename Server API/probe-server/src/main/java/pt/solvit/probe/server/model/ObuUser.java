/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.ObuUserRole;
import pt.solvit.probe.server.repository.model.ObuUserDao;

import java.time.LocalDateTime;
import java.util.List;

import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

//not used atm
@ApiModel(value = "ObuUser", description = "Obu user data tranfer object")
public class ObuUser {

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("userID")
    private Long userID;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("userName")
    private String userName;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("obuID")
    private Long obuID;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("obuName")
    private String obuName;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("role")
    private ObuUserRole role;

    public ObuUser(Long userID, String userName, Long obuID, String obuName, ObuUserRole role) {
        this.userID = userID;
        this.userName = userName;
        this.obuID = obuID;
        this.obuName = obuName;
        this.role = role;
    }

    public static ObuUserDao transformToObuUserDao(ObuUser obuUser) {
        return new ObuUserDao(obuUser.getUserID(), obuUser.getUserName(), obuUser.getObuID(), obuUser.getUserName(), obuUser.getRole().toString());
    }


    public Long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public Long getObuID() {
        return obuID;
    }

    public String getObuName() {
        return obuName;
    }

    public ObuUserRole getRole() {
        return role;
    }
}
