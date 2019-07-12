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
public class SetupDao extends CreatorDao{

    private Long id;
    private String setupName;
    private String properties;

    public SetupDao(){
        super();
    }

    public SetupDao(Long id, String setupName, String properties, String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.setupName = setupName;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public String getSetupName() {
        return setupName;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSetupName(String setupName) {
        this.setupName = setupName;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
