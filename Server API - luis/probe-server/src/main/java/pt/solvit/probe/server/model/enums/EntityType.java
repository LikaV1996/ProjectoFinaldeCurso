/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.enums;

/**
 *
 * @author AnaRita
 */
public enum EntityType {
    USER("user"),
    HARDWARE("hardware"),
    OBU("obu"),
    COMPONENT("component"),
    CONFIG("configuration"),
    TESTPLAN("test plan"),
    SETUP("setup"),
    TEST("test"),
    TESTLOG("test log"),
    SYSLOG("system log"),
    SERVERLOG("server log"),
    TEMPFILE("temp file");

    private final String name;

    private EntityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
