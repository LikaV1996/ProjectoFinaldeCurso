package pt.solvit.probe.server.repository.model;

import java.sql.Timestamp;

public class CreatorDao {

    private String creator;
    private Timestamp creationDate;
    private String modifier;
    private Timestamp modifiedDate;

    CreatorDao(){
    }

    CreatorDao(String creator, Timestamp creationDate, String modifier, Timestamp modifiedDate){
        this.creator = creator;
        this.creationDate = creationDate;
        this.modifier = modifier;
        this.modifiedDate = modifiedDate;
    }

    public String getCreator() {
        return creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public String getModifier() {
        return modifier;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setCreator(String creator){
        this.creator = creator;
    }

    public void setCreationDate(Timestamp creationDate){
        this.creationDate = creationDate;
    }

    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public void setModifiedDate(Timestamp modifiedDate){
        this.modifiedDate = modifiedDate;
    }
}
