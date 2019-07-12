/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;

import java.sql.Timestamp;

/**
 *
 * @author AnaRita
 */
public class UserDao extends CreatorDao{

    private Long id;
    private String userName;
    private String userPassword;
    private String userProfile;
    private String properties;
    private Boolean suspended;

    public UserDao(){
        super();
    }

    public UserDao(Long id, String userName, String userPassword, String userProfile, String properties,
            String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate, Boolean suspended) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userProfile = userProfile;
        this.properties = properties;
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

    public String getUserProfile() {
        return userProfile;
    }

    public String getProperties() {
        return properties;
    }

    public Boolean getSuspended(){
        return suspended;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    public void setUserProfile(String userProfile){
        this.userProfile = userProfile;
    }

    public void setProperties(String properties){
        this.properties = properties;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }



    public static User transformToUser(UserDao userDao) {
        UserProfile userProfile = UserProfile.valueOf(userDao.getUserProfile());

        return new User(userDao.getId(), userDao.getUserName(), userDao.getUserPassword(), userProfile,
                userDao.getCreator(), userDao.getCreationDate().toLocalDateTime(),
                userDao.getModifier(), (userDao.getModifiedDate() != null ? userDao.getModifiedDate().toLocalDateTime() : null),
                userDao.getSuspended()
        );
    }
}
