/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.model.logfiles.data.Coordinates;
import pt.solvit.probe.server.model.logfiles.data.Csq;
import pt.solvit.probe.server.model.logfiles.data.Moni;
import pt.solvit.probe.server.model.logfiles.data.Monp;
import pt.solvit.probe.server.model.logfiles.data.Smonc;
import pt.solvit.probe.server.model.logfiles.data.Smond;

/**
 *
 * @author AnaRita
 */
public class ScanData {

    private LocalDateTime date;
    private Coordinates coordinates;
    private Csq csq;
    private Moni moni;
    private List<Monp> monpList;
    private List<Smonc> smoncList;
    private Smond smond;

    public ScanData(LocalDateTime date, Coordinates coordinates, Csq csq, Moni moni, List<Monp> monpList, List<Smonc> smoncList, Smond smond) {
        this.date = date;
        this.coordinates = coordinates;
        this.csq = csq;
        this.moni = moni;
        this.monpList = monpList;
        this.smoncList = smoncList;
        this.smond = smond;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Csq getCsq() {
        return csq;
    }

    public Moni getMoni() {
        return moni;
    }

    public List<Monp> getMonpList() {
        return monpList;
    }

    public List<Smonc> getSmoncList() {
        return smoncList;
    }

    public Smond getSmond() {
        return smond;
    }
}
