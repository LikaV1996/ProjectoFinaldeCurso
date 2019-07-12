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
public class MoniServingCell {

    private String mcc, mnc, ncc, lac, cell, c1, bcc;
    private int chann, rs, dBm, pwr, rxLev;

    private String servingCellInfo;

    public MoniServingCell() {
    }

    public MoniServingCell(String servingCellInfo) {
        this.servingCellInfo = servingCellInfo;
    }

    public MoniServingCell(int chann, int rs, int dBm, String mcc, String mnc, String lac, String cell, String ncc, String bcc, int pwr, int rxLev, String c1) {
        this.chann = chann;
        this.rs = rs;
        this.dBm = dBm;
        this.lac = lac;
        this.cell = cell;
        this.mcc = mcc;
        this.mnc = mnc;
        this.ncc = ncc;
        this.bcc = bcc;
        this.pwr = pwr;
        this.rxLev = rxLev;
        this.c1 = c1;
    }

    public int getChann() {
        return chann;
    }

    public int getRs() {
        return rs;
    }

    public int getdBm() {
        return dBm;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getNcc() {
        return ncc;
    }

    public String getBcc() {
        return bcc;
    }

    public int getPwr() {
        return pwr;
    }

    public int getRxLev() {
        return rxLev;
    }

    public String getC1() {
        return c1;
    }

    public String getLac() {
        return lac;
    }

    public String getCell() {
        return cell;
    }

    public String getServingCellInfo() {
        return servingCellInfo;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public void setdBm(int dBm) {
        this.dBm = dBm;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public void setNcc(String ncc) {
        this.ncc = ncc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void setPwr(int pwr) {
        this.pwr = pwr;
    }

    public void setRxLev(int rxLev) {
        this.rxLev = rxLev;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public void setChann(int chann) {
        this.chann = chann;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public void setServingCellInfo(String servingCellInfo) {
        this.servingCellInfo = servingCellInfo;
    }
}
