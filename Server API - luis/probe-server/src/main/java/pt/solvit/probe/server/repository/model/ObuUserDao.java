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
    private long obuID;
    private String role;

    public ObuUserDao(){ super(); }

    public ObuUserDao(long userID, long obuID, String role) {
        this.userID = userID;
        this.obuID = obuID;
        this.role = role;
    }

    public long getUserID() {
        return userID;
    }

    public long getObuID() {
        return obuID;
    }

    public String getRole() {
        return role;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setObuID(long obuID) {
        this.obuID = obuID;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static ObuUser transformToObuUser(ObuUserDao obuUserDao) {
        return new ObuUser(obuUserDao.getUserID(), obuUserDao.getObuID(), ObuUserRole.valueOf( obuUserDao.getRole()) );
    }
}
