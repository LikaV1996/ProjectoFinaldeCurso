/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import pt.solvit.probe.server.util.HeaderEnums.EventLevel;

/**
 *
 * @author AnaRita
 */
public class EventProperties {

    private Integer eventLevel;

    private String modemType;
    private String retryCount;
    private String cause;
    private String requestOrigin;

    private String networkInterface;
    private String downloadType;
    private String id;
    private String fileName;

    private String callType;
    private String originNumber;
    private String destinationNumber;
    private String establishmentTime;
    private String duration;
    private String releaseCause;
    private String message;

    public EventProperties(Event event) {
        this.eventLevel = event.getEventLevel();

        this.modemType = event.getModemType();
        this.retryCount = event.getRetryCount();
        this.cause = event.getCause();
        this.requestOrigin = event.getRequestOrigin();

        this.networkInterface = event.getNetworkInterface();
        this.downloadType = event.getDownloadType();
        this.id = event.getId();
        this.fileName = event.getFileName();

        this.callType = event.getCallType();
        this.originNumber = event.getOriginNumber();
        this.destinationNumber = event.getDestinationNumber();
        this.establishmentTime = event.getEstablishmentTime();
        this.duration = event.getDuration();
        this.releaseCause = event.getReleaseCause();
        this.message = event.getMessage();
    }

    public int getEventLevel() {
        return eventLevel;
    }

    public EventLevel getEventLevelEnum() {
        return EventLevel.getEventLevel(eventLevel);
    }

    public String getModemType() {
        return modemType;
    }

    public String getRetryCount() {
        return retryCount;
    }

    public String getCause() {
        return cause;
    }

    public String getRequestOrigin() {
        return requestOrigin;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCallType() {
        return callType;
    }

    public String getOriginNumber() {
        return originNumber;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public String getEstablishmentTime() {
        return establishmentTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getReleaseCause() {
        return releaseCause;
    }

    public String getMessage() {
        return message;
    }
}
