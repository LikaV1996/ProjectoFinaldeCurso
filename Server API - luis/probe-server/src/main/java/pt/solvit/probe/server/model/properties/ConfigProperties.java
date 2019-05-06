/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.controller.model.input.config.ArchiveConfig;
import pt.solvit.probe.server.controller.model.input.config.ControlConnectionConfig;
import pt.solvit.probe.server.controller.model.input.config.CoreConfig;
import pt.solvit.probe.server.controller.model.input.config.DataConfig;
import pt.solvit.probe.server.controller.model.input.config.DownloadConfig;
import pt.solvit.probe.server.controller.model.input.config.ScanningConfig;
import pt.solvit.probe.server.controller.model.input.config.ServerConfig;
import pt.solvit.probe.server.controller.model.input.config.TestPlanConfig;
import pt.solvit.probe.server.controller.model.input.config.UploadConfig;
import pt.solvit.probe.server.controller.model.input.config.VoiceConfig;

/**
 *
 * @author AnaRita
 */
public class ConfigProperties {

    private ArchiveConfig archive;
    private ControlConnectionConfig controlConnection;
    private CoreConfig core;
    private DataConfig data;
    private DownloadConfig download;
    private ScanningConfig scanning;
    private ServerConfig server;
    private TestPlanConfig testPlan;
    private UploadConfig upload;
    private VoiceConfig voice;

    public ConfigProperties(Config config) {
        this.archive = config.getArchive();
        this.controlConnection = config.getControlConnection();
        this.core = config.getCore();
        this.data = config.getData();
        this.download = config.getDownload();
        this.scanning = config.getScanning();
        this.server = config.getServer();
        this.testPlan = config.getTestPlan();
        this.upload = config.getUpload();
        this.voice = config.getVoice();
    }

    public ArchiveConfig getArchive() {
        return archive;
    }

    public ControlConnectionConfig getControlConnection() {
        return controlConnection;
    }

    public CoreConfig getCore() {
        return core;
    }

    public DataConfig getData() {
        return data;
    }

    public DownloadConfig getDownload() {
        return download;
    }

    public ScanningConfig getScanning() {
        return scanning;
    }

    public ServerConfig getServer() {
        return server;
    }

    public TestPlanConfig getTestPlan() {
        return testPlan;
    }

    public UploadConfig getUpload() {
        return upload;
    }

    public VoiceConfig getVoice() {
        return voice;
    }
}
