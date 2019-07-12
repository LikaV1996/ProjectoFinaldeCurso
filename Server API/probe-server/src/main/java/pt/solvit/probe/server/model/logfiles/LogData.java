/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.solvit.probe.server.model.enums.LogDataType;

/**
 *
 * @author AnaRita
 */
public class LogData {

    @JsonProperty("type")
    private LogDataType type;
    @JsonProperty("text")
    private String text;

    public LogData(LogDataType type, String text) {
        this.type = type;
        this.text = text;
    }

    public LogDataType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
