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
public class SmondNci {

    /*Neighbour cell information for neighbour cell 1 through 6 (comma-separated, no cr/lf included)
    <MCC>1,<MNC>1,<LAC>1,<cell>1,<BSIC>1,<chann>1,<RxLev>1, (these parameters repeated for neighbour cells 2 through 6 with no CR/LF): 
    ... <MCC>6,<MNC>6,<LAC>6,<cell>6,<BSIC>6,<chann>6,<RxLev>6
    An unavailable cell appears as follows: " ,,,,,,0*/
    private int rxLev;
    private String mcc, mnc, lac, cell, bsic, chann;

    public SmondNci() {
    }

    public SmondNci(String mcc, String mnc, String lac, String cell, String bsic, String chann, int rxLev) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cell = cell;
        this.bsic = bsic;
        this.chann = chann;
        this.rxLev = rxLev;
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

    public void setLac(String lac) {
        this.lac = lac;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }
}
