/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.solvit.probe.server.util.HeaderEnums.LogHeader;

/**
 *
 * @author AnaRita
 */
public class Coordinates {

    private static final Logger LOGGER = Logger.getLogger(Coordinates.class.getName());

    private double lat;
    private double lon;
    private double height;
    private double heading;
    private double speed;

    public Coordinates() {
    }

    public Coordinates(double lat, double lon, double height, double heading, double speed) {
        this.lat = lat;
        this.lon = lon;
        this.height = height;
        this.heading = heading;
        this.speed = speed;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getHeight() {
        return height;
    }

    public double getHeading() {
        return heading;
    }

    public double getSpeed() {
        return speed;
    }

    public void parseCoordinates(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case LATITUDE:
                    LOGGER.log(Level.FINE, "Coordinates parse: latitude ({0})", data);
                    this.lat = Double.parseDouble(data);
                    break;
                case LONGITUDE:
                    LOGGER.log(Level.FINE, "Coordinates parse: longitude ({0})", data);
                    this.lon = Double.parseDouble(data);
                    break;
                case HEIGHT:
                    LOGGER.log(Level.FINE, "Coordinates parse: height ({0})", data);
                    this.height = Double.parseDouble(data);
                    break;
                case HEADING:
                    LOGGER.log(Level.FINE, "Coordinates parse: heading ({0})", data);
                    this.heading = Double.parseDouble(data);
                    break;
                case SPEED:
                    LOGGER.log(Level.FINE, "Coordinates parse: speed ({0})", data);
                    this.speed = Double.parseDouble(data);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Coordinates parse: unknown field ({0})", data);
                //ignore
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing coordinates ({0})", e.getMessage());
        }
    }
}
