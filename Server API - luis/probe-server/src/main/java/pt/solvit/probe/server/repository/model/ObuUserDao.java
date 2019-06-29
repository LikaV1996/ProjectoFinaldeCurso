/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.model.enums.ObuUserRole;
import pt.solvit.probe.server.model.properties.ObuProperties;

import java.sql.Timestamp;

import static pt.solvit.probe.server.util.ServerUtil.GSON;


public class ObuUserDao{

    private long userID;
    private String userName;
    private long obuID;
    private String obuName;
    private String role;

    public ObuUserDao(){ super(); }

    public ObuUserDao(long userID, String userName, long obuID, String obuName, String role) {
        this.userID = userID;
        this.userName = userName;
        this.obuID = obuID;
        this.obuName = obuName;
        this.role = role;
    }

    public long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public long getObuID() {
        return obuID;
    }

    public String getObuName() {
        return obuName;
    }

    public String getRole() {
        return role;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setObuID(long obuID) {
        this.obuID = obuID;
    }

    public void setObuName(String obuName) {
        this.obuName = obuName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static ObuUser transformToObuUser(ObuUserDao obuUserDao) {
        return new ObuUser(obuUserDao.getUserID(), obuUserDao.getUserName(), obuUserDao.getObuID(), obuUserDao.getObuName(), ObuUserRole.valueOf( obuUserDao.getRole()) );
    }
}
