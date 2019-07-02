/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.util.DateUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Config", description = "Configuration data tranfer object")
public class InputConfig {

    @JsonProperty("configName")
    @NotNull(message = "A configName must be provided.")
    private String configName;

    @JsonProperty("activationDate")
    //@NotNull(message = "An activationDate must be provided.")
    private String activationDate;

    @JsonProperty("archive")
    @Valid
    private ArchiveConfig archive;

    @JsonProperty("controlConnection")
    @Valid
    private ControlConnectionConfig controlConnection;

    @JsonProperty("core")
    @Valid
    private CoreConfig core;

    @JsonProperty("data")
    @Valid
    private DataConfig data;

    @JsonProperty("download")
    @Valid
    private DownloadConfig download;

    @JsonProperty("scanning")
    @Valid
    private ScanningConfig scanning;

    @JsonProperty("server")
    @Valid
    private ServerConfig server;

    @JsonProperty("testPlan")
    @Valid
    private TestPlanConfig testPlan;

    @JsonProperty("upload")
    @Valid
    private UploadConfig upload;

    @JsonProperty("voice")
    @Valid
    private VoiceConfig voice;



    @JsonIgnore
    @ApiModelProperty(required = true, value = "Config name")
    public String getConfigName() {
        return configName;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getActivationLocalDateTime() {
        return DateUtil.getDateFromIsoString(activationDate);
    }

    @ApiModelProperty(example = "2019-01-01T10:00:00", required = true, value = "Activation date (ISO 8601)")
    public String getActivationDate() {
        return activationDate;
    }

    @ApiModelProperty(required = true, value = "Archive configuration")
    public ArchiveConfig getArchive() {
        return archive;
    }

    @ApiModelProperty(required = true, value = "Control connection configuration")
    public ControlConnectionConfig getControlConnection() {
        return controlConnection;
    }

    @ApiModelProperty(required = true, value = "Control connection configuration")
    public CoreConfig getCore() {
        return core;
    }

    @ApiModelProperty(required = true, value = "Data service configuration")
    public DataConfig getData() {
        return data;
    }

    @ApiModelProperty(required = true, value = "Upload configuration")
    public DownloadConfig getDownload() {
        return download;
    }

    @ApiModelProperty(required = true, value = "Scanning service configuration")
    public ScanningConfig getScanning() {
        return scanning;
    }

    @ApiModelProperty(required = true, value = "Server configuration")
    public ServerConfig getServer() {
        return server;
    }

    @ApiModelProperty(required = true, value = "Test plan configuration")
    public TestPlanConfig getTestPlan() {
        return testPlan;
    }

    @ApiModelProperty(required = true, value = "Upload configuration")
    public UploadConfig getUpload() {
        return upload;
    }

    @ApiModelProperty(required = true, value = "Voice service configuration")
    public VoiceConfig getVoice() {
        return voice;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (configName == null) {
            throw new BadRequestException("Invalid configuration.", "configName is null.", "string", "about:blank");
        }
        /*
        if (activationDate == null) {
            throw new BadRequestException("Invalid configuration.", "ActivationDate is null.", "string", "about:blank");
        }
        if (getActivationLocalDateTime() == null) {
            throw new BadRequestException("Invalid configuration.", "ActivationDate is not on ISO format.", "string", "about:blank");
        }
        */
        if (activationDate != null) {
            if (getActivationLocalDateTime() == null) {
                throw new BadRequestException("Invalid configuration.", "ActivationDate is not on ISO format.", "string", "about:blank");
            }
        }

        if (archive == null) {
            throw new BadRequestException("Invalid configuration.", "Archive is null.", "string", "about:blank");
        }
        if (controlConnection == null) {
            throw new BadRequestException("Invalid configuration.", "ControlConnection is null.", "string", "about:blank");
        }
        if (core == null) {
            throw new BadRequestException("Invalid configuration.", "Core is null.", "string", "about:blank");
        }
        if (data == null) {
            throw new BadRequestException("Invalid configuration.", "Data is null.", "string", "about:blank");
        }
        if (download == null) {
            throw new BadRequestException("Invalid configuration.", "Download is null.", "string", "about:blank");
        }
        if (scanning == null) {
            throw new BadRequestException("Invalid configuration.", "Scanning is null.", "string", "about:blank");
        }
        if (server == null) {
            throw new BadRequestException("Invalid configuration.", "Server is null.", "string", "about:blank");
        }
        if (testPlan == null) {
            throw new BadRequestException("Invalid configuration.", "TestPlan is null.", "string", "about:blank");
        }
        if (upload == null) {
            throw new BadRequestException("Invalid configuration.", "Upload is null.", "string", "about:blank");
        }
        if (voice == null) {
            throw new BadRequestException("Invalid configuration.", "Voice is null.", "string", "about:blank");
        }


        archive.validate();
        controlConnection.validate();
        core.validate();
        data.validate();
        download.validate();
        scanning.validate();
        server.validate();
        testPlan.validate();
        upload.validate();
        voice.validate();
    }
}
