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
public class SysLogDao {

    private Long id;
    private long obuId;
    private String fileName;
    private Timestamp closeDate;
    private Timestamp uploadDate;
    private byte[] fileData;
    private String properties;

    public SysLogDao() {
    }

    public SysLogDao(Long id, long obuId, String fileName, Timestamp closeDate, Timestamp uploadDate, byte[] fileData, String properties) {
        this.id = id;
        this.obuId = obuId;
        this.fileName = fileName;
        this.closeDate = closeDate;
        this.uploadDate = uploadDate;
        this.fileData = fileData;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public long getObuId() {
        return obuId;
    }

    public String getFileName() {
        return fileName;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setObuId(long obuId) {
        this.obuId = obuId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
