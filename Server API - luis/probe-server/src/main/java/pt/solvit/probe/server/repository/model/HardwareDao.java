/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.properties.HardwareProperties;

import java.sql.Timestamp;

import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class HardwareDao extends CreatorDao{

    private Long id;
    private String serialNumber;
    private String properties;

    public HardwareDao(){
        super();
    }

    public HardwareDao(Long id, String serialNumber, String properties,
                       String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.serialNumber = serialNumber;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public static Hardware transformToHardware(HardwareDao hardwareDao) {
        HardwareProperties properties = GSON.fromJson(hardwareDao.getProperties(), HardwareProperties.class);

        return new Hardware(hardwareDao.getId(), hardwareDao.getSerialNumber(), properties.getComponents(),
                hardwareDao.getCreator(), hardwareDao.getCreationDate().toLocalDateTime(),
                hardwareDao.getModifier(), hardwareDao.getModifiedDate() != null ? hardwareDao.getModifiedDate().toLocalDateTime() : null);
    }
}
