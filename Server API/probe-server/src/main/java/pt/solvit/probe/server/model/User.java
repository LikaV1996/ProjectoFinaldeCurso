/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    //@JsonIgnore
    private String userPassword;

    @JsonProperty("userProfile")
    private UserProfile userProfile;

    @JsonProperty("suspended")
    private Boolean suspended;

    public User(String userName){
        super(null,null,null,null);
        this.userName = userName;
    }

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


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }



    public static User makeUserFromInput(InputUser inputUser, String creator){
        return new User(null, inputUser.getUserName(), inputUser.getUserPassword(), inputUser.getUserProfile(),
                creator, LocalDateTime.now(), null, null, false);
    }

    public static User updateUser(User updatedUser, InputUser updatedFields, User modifier){
        if(updatedFields != null) {
            if (updatedFields.getUserName() != null)
                updatedUser.setUserName(updatedFields.getUserName());

            if (updatedFields.getUserPassword() != null)
                updatedUser.setUserPassword(updatedFields.getUserPassword());

            if (updatedFields.getUserProfile() != null)
                updatedUser.setUserProfile(updatedFields.getUserProfile());
        }

        updatedUser.setModifier( modifier.getUserName() );

        return updatedUser;
    }


    public static UserDao transformToUserDao(User user) {
        return new UserDao(user.getId(), user.getUserName(), user.getUserPassword(), user.getUserProfile().name(),
                null, user.getCreator(), Timestamp.valueOf(user.getCreationLocalDateTime()),
                user.getModifier(), user.getModifiedLocalDateTime() != null ? Timestamp.valueOf(user.getModifiedLocalDateTime()) : null,
                user.getSuspended()
        );
    }
}
