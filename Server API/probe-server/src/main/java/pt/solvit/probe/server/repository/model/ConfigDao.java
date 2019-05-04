/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.properties.ConfigProperties;

import java.sql.Timestamp;

import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class ConfigDao extends CreatorDao{

    private Long id;
    private Timestamp activationDate;
    private String properties;


    public ConfigDao(Long id, Timestamp activationDate, String properties,
            String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.activationDate = activationDate;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getActivationDate() {
        return activationDate;
    }

    public String getProperties() {
        return properties;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setActivationDate(Timestamp activationDate) {
        this.activationDate = activationDate;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public static Config transformToConfig(ConfigDao configDao) {
        ConfigProperties properties = GSON.fromJson(configDao.getProperties(), ConfigProperties.class);

        return new Config(configDao.getId(), configDao.getActivationDate().toLocalDateTime(), properties.getArchive(),
                properties.getControlConnection(), properties.getCore(), properties.getData(), properties.getDownload(),
                properties.getScanning(), properties.getServer(), properties.getTestPlan(), properties.getUpload(),
                properties.getVoice(), configDao.getCreator(), configDao.getCreationDate().toLocalDateTime(),
                configDao.getModifier(), configDao.getModifiedDate().toLocalDateTime());
    }
}
