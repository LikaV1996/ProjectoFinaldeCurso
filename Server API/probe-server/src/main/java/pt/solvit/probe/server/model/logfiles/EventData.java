/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.solvit.probe.server.model.logfiles.data.Coordinates;
import pt.solvit.probe.server.model.enums.EventType;

/**
 *
 * @author AnaRita
 */
public class EventData {

    private static final Logger LOGGER = Logger.getLogger(EventData.class.getName());

    private LocalDateTime date;
    private Coordinates coordinates;
    private String eventType;
    private EventProperties properties;

    public EventData(LocalDateTime date, Coordinates coordinates, String eventType, EventProperties properties) {
        this.date = date;
        this.coordinates = coordinates;
        this.eventType = eventType;
        this.properties = properties;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getEventType() {
        return eventType;
    }

    public EventType getEventTypeEnum() {
        try {
            return EventType.valueOf(eventType);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match EventType enum", eventType);
            return null;
        }
    }

    public EventProperties getProperties() {
        return properties;
    }
}
