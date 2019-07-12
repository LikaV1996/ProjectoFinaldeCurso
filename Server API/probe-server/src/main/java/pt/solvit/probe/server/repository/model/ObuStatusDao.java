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
public class ObuStatusDao {

    private Long id;
    private long obuId;
    private Timestamp statusDate;
    private Double lat;
    private Double lon;
    private Double speed;
    private String locationProperties;
    private long usableStorage;
    private long freeStorage;
    private int criticalAlarms;
    private int majorAlarms;
    private int warningAlarms;
    private double temperature;
    private String networkInterfaces;

    public ObuStatusDao() {
    }

    public ObuStatusDao(Long id, long obuId, Timestamp statusDate, Double lat, Double lon, Double speed,
            String locationProperties, long usableStorage, long freeStorage, int criticalAlarms, int majorAlarms,
            int warningAlarms, double temperature, String networkInterfaces) {
        this.id = id;
        this.obuId = obuId;
        this.statusDate = statusDate;
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.locationProperties = locationProperties;
        this.usableStorage = usableStorage;
        this.freeStorage = freeStorage;
        this.criticalAlarms = criticalAlarms;
        this.majorAlarms = majorAlarms;
        this.warningAlarms = warningAlarms;
        this.temperature = temperature;
        this.networkInterfaces = networkInterfaces;
    }

    public Long getId() {
        return id;
    }

    public long getObuId() {
        return obuId;
    }

    public Timestamp getStatusDate() {
        return statusDate;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getSpeed() {
        return speed;
    }

    public String getLocationProperties() {
        return locationProperties;
    }

    public long getUsableStorage() {
        return usableStorage;
    }

    public long getFreeStorage() {
        return freeStorage;
    }

    public int getCriticalAlarms() {
        return criticalAlarms;
    }

    public int getMajorAlarms() {
        return majorAlarms;
    }

    public int getWarningAlarms() {
        return warningAlarms;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setObuId(long obuId) {
        this.obuId = obuId;
    }

    public void setStatusDate(Timestamp statusDate) {
        this.statusDate = statusDate;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setLocationProperties(String locationProperties) {
        this.locationProperties = locationProperties;
    }

    public void setUsableStorage(long usableStorage) {
        this.usableStorage = usableStorage;
    }

    public void setFreeStorage(long freeStorage) {
        this.freeStorage = freeStorage;
    }

    public void setCriticalAlarms(int criticalAlarms) {
        this.criticalAlarms = criticalAlarms;
    }

    public void setMajorAlarms(int majorAlarms) {
        this.majorAlarms = majorAlarms;
    }

    public void setWarningAlarms(int warningAlarms) {
        this.warningAlarms = warningAlarms;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setNetworkInterfaces(String networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }
}
