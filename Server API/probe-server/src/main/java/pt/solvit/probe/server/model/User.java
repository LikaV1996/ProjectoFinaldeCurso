/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.model.UserDao;

/**
 *
 * @author AnaRita
 */
public class User extends CreatorModel {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("userPassword")
    private String userPassword;
    @JsonProperty("userProfile")
    private UserProfile userProfile;
    @JsonProperty("suspended")
    private Boolean suspended;

    public User(Long id, String userName, String userPassword, UserProfile userProfile, String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate, Boolean suspended) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userProfile = userProfile;
        this.suspended = suspended;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Boolean getSuspended() {
        return suspended;
    }


    public static User makeUser(InputUser inputUser, String creator){
        return new User(null, inputUser.getUserName(), inputUser.getUserPassword(), inputUser.getUserProfile(),
                creator, LocalDateTime.now(), null, null, false);
    }


    public static UserDao transformToUserDao(User user) {
        return new UserDao(user.getId(), user.getUserName(), user.getUserPassword(), user.getUserProfile().name(),
                null, user.getCreator(), Timestamp.valueOf(user.getCreationLocalDateTime()),
                user.getModifier(), user.getModifiedDate()!= null ? Timestamp.valueOf(user.getModifiedDate()) : null,
                user.getSuspended()
        );
    }
}
