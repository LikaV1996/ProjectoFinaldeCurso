/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import pt.solvit.probe.server.model.enums.FileType;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "File", description = "File data tranfer object")
public class ObuFile {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("obuId")
    private long obuId;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("closeDate")
    private LocalDateTime closeDate;
    @JsonProperty("uploadDate")
    private LocalDateTime uploadDate;
    @JsonProperty("fileType")
    private FileType fileType;

    @JsonIgnore
    private byte[] fileData;

    public ObuFile(Long id, long obuId, String fileName, FileType fileType, LocalDateTime closeDate, LocalDateTime uploadDate, byte[] fileData) {
        this.id = id;
        this.obuId = obuId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.closeDate = closeDate;
        this.uploadDate = uploadDate;
        this.fileData = fileData;
    }

    @ApiModelProperty(required = true, value = "File identifier")
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ApiModelProperty(required = true, value = "Obu identifier")
    public long getObuId() {
        return obuId;
    }

    @ApiModelProperty(required = true, value = "File name")
    public String getFileName() {
        return fileName;
    }

    @ApiModelProperty(required = true, value = "File type")
    public FileType getFileType() {
        return fileType;
    }

    @JsonIgnore
    public LocalDateTime getUploadLocalDateTime() {
        return uploadDate;
    }

    @ApiModelProperty(required = true, value = "Upload date")
    public String getUploadDate() {
        return ISO8601_DATE_FORMATTER.format(uploadDate);
    }

    @JsonIgnore
    public LocalDateTime getCloseLocalDateTime() {
        return closeDate;
    }

    @ApiModelProperty(required = true, value = "Close date")
    public String getCloseDate() {
        return ISO8601_DATE_FORMATTER.format(closeDate);
    }

    public byte[] getFileData() {
        return fileData;
    }
}
