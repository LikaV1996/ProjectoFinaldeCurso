/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.model.properties.ObuProperties;

import java.sql.Timestamp;

import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class ObuDao extends CreatorDao{

    private Long id;
    private long hardwareId;
    private String obuState;
    private Long currentConfigId;
    private Long currentTestPlanId;
    private String obuName;
    private String obuPassword;
    private String properties;

    public ObuDao(){
        super();
    }

    public ObuDao(Long id, long hardwareId, String obuState, Long currentConfigId, Long currentTestPlanId,
                  String obuName, String obuPassword, String properties,
                  String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.hardwareId = hardwareId;
        this.obuState = obuState;
        this.currentConfigId = currentConfigId;
        this.currentTestPlanId = currentTestPlanId;
        this.obuName = obuName;
        this.obuPassword = obuPassword;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public long getHardwareId() {
        return hardwareId;
    }

    public String getObuState() {
        return obuState;
    }

    public void setObuState(String obuState) {
        this.obuState = obuState;
    }

    public Long getCurrentConfigId() {
        return currentConfigId;
    }

    public void setCurrentConfigId(Long currentConfigId) {
        this.currentConfigId = currentConfigId;
    }

    public Long getCurrentTestPlanId() {
        return currentTestPlanId;
    }

    public void setCurrentTestPlanId(Long currentTestPlanId) {
        this.currentTestPlanId = currentTestPlanId;
    }

    public String getObuName() {
        return obuName;
    }

    public String getObuPassword() {
        return obuPassword;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHardwareId(long hardwareId) {
        this.hardwareId = hardwareId;
    }

    public void setObuName(String obuName) {
        this.obuName = obuName;
    }

    public void setObuPassword(String obuPassword) {
        this.obuPassword = obuPassword;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public static Obu transformToObu(ObuDao obuDao) {
        ObuState obuState = ObuState.valueOf(obuDao.getObuState());

        ObuProperties properties = GSON.fromJson(obuDao.getProperties(), ObuProperties.class);

        return new Obu(obuDao.getId(), obuDao.getHardwareId(), obuState, obuDao.getObuName(), obuDao.getObuPassword(),
                properties.getSims(), obuDao.getCurrentConfigId(), obuDao.getCurrentTestPlanId(), //properties.getFactoryConfig(),
                properties.isAuthenticate(), properties.isUploadRequest(), properties.isClearAlarmsRequest(),
                properties.isResetRequest(), properties.isShutdownRequest(),
                obuDao.getCreator(), obuDao.getCreationDate().toLocalDateTime(),
                obuDao.getModifier(), (obuDao.getModifiedDate() != null ? obuDao.getModifiedDate().toLocalDateTime() : null));
    }
}
