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
import pt.solvit.probe.server.controller.model.input.config.ArchiveConfig;
import pt.solvit.probe.server.controller.model.input.config.ControlConnectionConfig;
import pt.solvit.probe.server.controller.model.input.config.CoreConfig;
import pt.solvit.probe.server.controller.model.input.config.DataConfig;
import pt.solvit.probe.server.controller.model.input.config.DownloadConfig;
import pt.solvit.probe.server.controller.model.input.config.ServerConfig;
import pt.solvit.probe.server.controller.model.input.config.ScanningConfig;
import pt.solvit.probe.server.controller.model.input.config.TestPlanConfig;
import pt.solvit.probe.server.controller.model.input.config.UploadConfig;
import pt.solvit.probe.server.controller.model.input.config.VoiceConfig;
import pt.solvit.probe.server.model.properties.ConfigProperties;
import pt.solvit.probe.server.repository.model.ConfigDao;

import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Config", description = "Configuration data tranfer object")
public class Config extends CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ShortView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("activationDate")
    private LocalDateTime activationDate;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("archive")
    private ArchiveConfig archive;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("controlConnection")
    private ControlConnectionConfig controlConnection;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("core")
    private CoreConfig core;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("data")
    private DataConfig data;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("download")
    private DownloadConfig download;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("scanning")
    private ScanningConfig scanning;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("server")
    private ServerConfig server;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("testPlan")
    private TestPlanConfig testPlan;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("upload")
    private UploadConfig upload;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("voice")
    private VoiceConfig voice;

    public Config(Long id, LocalDateTime activationDate, ArchiveConfig archive, ControlConnectionConfig controlConnection, CoreConfig core,
            DataConfig data, DownloadConfig download, ScanningConfig scanning, ServerConfig server, TestPlanConfig testPlan, 
            UploadConfig upload, VoiceConfig voice, String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.activationDate = activationDate;
        this.archive = archive;
        this.controlConnection = controlConnection;
        this.core = core;
        this.data = data;
        this.download = download;
        this.scanning = scanning;
        this.server = server;
        this.testPlan = testPlan;
        this.upload = upload;
        this.voice = voice;
    }

    @ApiModelProperty(value = "Configuration identifier")
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public LocalDateTime getActivationLocalDateTime() {
        return activationDate;
    }

    @ApiModelProperty(example = "2019-01-01T10:00:00", value = "Activation date (ISO 8601)")
    public String getActivationDate() {
        return activationDate != null ?ISO8601_DATE_FORMATTER.format(activationDate) : null;
    }

    @ApiModelProperty(value = "Archive configuration")
    public ArchiveConfig getArchive() {
        return archive;
    }

    @ApiModelProperty(value = "Control connection configuration")
    public ControlConnectionConfig getControlConnection() {
        return controlConnection;
    }

    @ApiModelProperty(value = "Core configuration")
    public CoreConfig getCore() {
        return core;
    }

    @ApiModelProperty(value = "Data service configuration")
    public DataConfig getData() {
        return data;
    }

    @ApiModelProperty(value = "Download configuration")
    public DownloadConfig getDownload() {
        return download;
    }

    @ApiModelProperty(value = "Scanning service configuration")
    public ScanningConfig getScanning() {
        return scanning;
    }

    @ApiModelProperty(value = "Server configuration")
    public ServerConfig getServer() {
        return server;
    }

    @ApiModelProperty(value = "Test plan configuration")
    public TestPlanConfig getTestPlan() {
        return testPlan;
    }

    @ApiModelProperty(value = "Upload configuration")
    public UploadConfig getUpload() {
        return upload;
    }

    @ApiModelProperty(value = "Voice service configuration")
    public VoiceConfig getVoice() {
        return voice;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public String getPropertiesString() {
        return GSON.toJson(new ConfigProperties(this));
    }


    public static ConfigDao transformToConfigDao(Config config) {
        return new ConfigDao(null, Timestamp.valueOf(config.getActivationLocalDateTime()), config.getPropertiesString(),
                config.getCreator(), Timestamp.valueOf(config.getCreationLocalDateTime()), config.getModifier(), Timestamp.valueOf(config.getModifiedDate()));
    }
}
