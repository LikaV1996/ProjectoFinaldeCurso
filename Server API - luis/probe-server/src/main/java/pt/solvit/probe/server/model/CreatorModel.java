/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import pt.solvit.probe.server.controller.impl.util.Profile;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Creator", description = "Creator data tranfer object")
public class CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("creator")
    private String creator;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("creationDate")
    private LocalDateTime creationDate;

    @JsonView(Profile.ExtendedView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("modifier")
    private String modifier;

    @JsonView(Profile.ExtendedView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("modifiedDate")
    private LocalDateTime modifiedDate;

    public CreatorModel(String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        this.creator = creator;
        this.creationDate = creationDate;
        this.modifier = modifier;
        this.modifiedDate = modifiedDate;
    }

    @ApiModelProperty(value = "Creator")
    public String getCreator() {
        return creator;
    }

    @JsonIgnore
    public LocalDateTime getCreationLocalDateTime() {
        return creationDate;
    }

    @ApiModelProperty(value = "Creation date")
    public String getCreationDate() {
        return ISO8601_DATE_FORMATTER.format(creationDate);
    }

    @ApiModelProperty(value = "Modifier")
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    @JsonIgnore
    public LocalDateTime getModifiedLocalDateTime() {
        return modifiedDate;
    }

    @ApiModelProperty(value = "Modified date")
    public String getModifiedDate() {
        return modifiedDate != null ? ISO8601_DATE_FORMATTER.format(modifiedDate) : null;
    }
}
