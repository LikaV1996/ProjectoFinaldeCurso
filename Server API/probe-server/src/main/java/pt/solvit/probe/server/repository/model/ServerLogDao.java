/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import java.sql.Timestamp;

/**
 *
 * @author AnaRita
 */
public class ServerLogDao {

    private Long id;
    private Timestamp logDate;
    private String accessPath;
    private String accessType;
    private String accessUser;
    private Timestamp responseDate;
    private String status;
    private String detail;

    public ServerLogDao() {
    }

    public ServerLogDao(Long id, Timestamp logDate, String accessType, String accessPath, String accessUser,
            Timestamp responseDate, String status, String detail) {
        this.id = id;
        this.logDate = logDate;
        this.accessType = accessType;
        this.accessPath = accessPath;
        this.accessUser = accessUser;
        this.responseDate = responseDate;
        this.status = status;
        this.detail = detail;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getLogDate() {
        return logDate;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String getAccessUser() {
        return accessUser;
    }

    public Timestamp getResponseDate() {
        return responseDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
    }

    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public void setAccessUser(String accessUser) {
        this.accessUser = accessUser;
    }

    public void setResponseDate(Timestamp responseDate) {
        this.responseDate = responseDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
