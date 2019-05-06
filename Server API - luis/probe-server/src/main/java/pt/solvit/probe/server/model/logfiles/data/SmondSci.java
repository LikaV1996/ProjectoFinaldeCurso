/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles.data;

/**
 *
 * @author AnaRita
 */
public class SmondSci {

    /*<MCC>,<MNC>,<LAC>,<cell>,<BSIC>,<chann>,<RxLev>,<RxLev>Full,<RxLev>Sub,<RxQual>,<RxQual>Full,<RxQual>Sub,<Timeslot>
    If no serving cell is found, unavailable values are omitted:" ,,,,,,<RxLev>,,,0,,,0*/
    private String mcc, mnc, lac, cell, bsic, chann, rxLev_full, rxLev_sub, rxQual_full, rxQual_sub;
    private int rxLev, rxQual, timeslot;

    public SmondSci() {
    }

    public SmondSci(String mcc, String mnc, String lac, String cell, String bsic, String chann, int rxLev, String rxLev_full, String rxLev_sub, int rxQual, String rxQual_full, String rxQual_sub, int timeslot) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cell = cell;
        this.bsic = bsic;
        this.chann = chann;
        this.rxLev = rxLev;
        this.rxLev_full = rxLev_full;
        this.rxLev_sub = rxLev_sub;
        this.rxQual = rxQual;
        this.rxQual_full = rxQual_full;
        this.rxQual_sub = rxQual_sub;
        this.timeslot = timeslot;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getBsic() {
        return bsic;
    }

    public String getChann() {
        return chann;
    }

    public int getRxLev() {
        return rxLev;
    }

    public String getRxLev_full() {
        return rxLev_full;
    }

    public String getRxLev_sub() {
        return rxLev_sub;
    }

    public int getRxQual() {
        return rxQual;
    }

    public String getRxQual_full() {
        return rxQual_full;
    }

    public String getRxQual_sub() {
        return rxQual_sub;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public String getLac() {
        return lac;
    }

    public String getCell() {
        return cell;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public void setBsic(String bsic) {
        this.bsic = bsic;
    }

    public void setChann(String chann) {
        this.chann = chann;
    }

    public void setRxLev(int rxLev) {
        this.rxLev = rxLev;
    }

    public void setRxLev_full(String rxLev_full) {
        this.rxLev_full = rxLev_full;
    }

    public void setRxLev_sub(String rxLev_sub) {
        this.rxLev_sub = rxLev_sub;
    }

    public void setRxQual(int rxQual) {
        this.rxQual = rxQual;
    }

    public void setRxQual_full(String rxQual_full) {
        this.rxQual_full = rxQual_full;
    }

    public void setRxQual_sub(String rxQual_sub) {
        this.rxQual_sub = rxQual_sub;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }
}
