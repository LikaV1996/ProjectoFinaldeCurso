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
public class TestPlanDao extends CreatorDao{

    private Long id;
    private Timestamp startDate;
    private Timestamp stopDate;
    private String properties;


    public TestPlanDao(Long id, Timestamp startDate, Timestamp stopDate, String properties,
                       String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getStopDate() {
        return stopDate;
    }

    public String getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setStopDate(Timestamp stopDate) {
        this.stopDate = stopDate;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
