/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Location;
import pt.solvit.probe.server.model.enums.GpsFix;

/**
 *
 * @author AnaRita
 */
public class LocationProperties {

    private String date;
    private Double heightAboveEllipsoid;
    private Double heightAboveMSL;
    private Double heading;
    private GpsFix gpsFix;

    public LocationProperties(Location location) {
        this.date = location.getDate();
        this.heightAboveEllipsoid = location.getHeightAboveEllipsoid();
        this.heightAboveMSL = location.getHeightAboveMSL();
        this.heading = location.getHeading();
        this.gpsFix = location.getGpsFix();
    }

    public String getDate() {
        return date;
    }
    
    public Double getHeightAboveEllipsoid() {
        return heightAboveEllipsoid;
    }

    public Double getHeightAboveMSL() {
        return heightAboveMSL;
    }

    public Double getHeading() {
        return heading;
    }
    
    public GpsFix getGpsFix() {
        return gpsFix;
    }
}
