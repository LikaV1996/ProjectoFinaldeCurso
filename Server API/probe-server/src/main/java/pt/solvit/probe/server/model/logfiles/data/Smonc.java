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
public class Smonc {

    private static final Logger LOGGER = Logger.getLogger(Smonc.class.getName());

    /*3 digits, e.g. 232
    000 Not decoded*/
    private String mcc, mnc, bsic;
    /*4 hexadecimal digits, e.g. 4EAF
    0000 Not decoded*/
    String lac, cell;
    /*ARFCN (Absolute Frequency Channel Number)
    0 Not decoded. In this case, all remaining parameters related to the same channel are neither decoded.*/
    private int chann;
    /*Received signal level of the BCCH carrier (0..63).*/
    private int rssi;
    /*Coefficient for base station reselection, e.g. 30. In dedicated mode, under certain conditions the parameter cannot be updated. In such cases a '-' is presented.*/
    String c1, c2;

    public Smonc() {
    }

    public Smonc(String mcc, String mnc, String lac, String cell, String bsic, int chann, int rssi, String c1, String c2) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cell = cell;
        this.bsic = bsic;
        this.chann = chann;
        this.rssi = rssi;
        this.c1 = c1;
        this.c2 = c2;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getLac() {
        return lac;
    }

    public String getCell() {
        return cell;
    }

    public String getBsic() {
        return bsic;
    }

    public int getChann() {
        return chann;
    }

    public int getRssi() {
        return rssi;
    }

    public String getC1() {
        return c1;
    }

    public String getC2() {
        return c2;
    }

    private void setMcc(String mcc) {
        this.mcc = mcc;
    }

    private void setMnc(String mnc) {
        this.mnc = mnc;
    }

    private void setBsic(String bsic) {
        this.bsic = bsic;
    }

    private void setChann(int chann) {
        this.chann = chann;
    }

    private void setRssi(int rssi) {
        this.rssi = rssi;
    }

    private void setC1(String c1) {
        this.c1 = c1;
    }

    private void setC2(String c2) {
        this.c2 = c2;
    }

    private void setLac(String lac) {
        this.lac = lac;
    }

    private void setCell(String cell) {
        this.cell = cell;
    }

    public void parseSmonc(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case SMONC_MCC:
                    LOGGER.log(Level.FINE, "Smonc parse: MCC ({0})", data);
                    setMcc(data);
                    break;
                case SMONC_MNC:
                    LOGGER.log(Level.FINE, "Smonc parse: MNC ({0})", data);
                    setMnc(data);
                    break;
                case SMONC_LAC:
                    LOGGER.log(Level.FINE, "Smonc parse: LAC ({0})", data);
                    setLac(data);
                    break;
                case SMONC_cell:
                    LOGGER.log(Level.FINE, "Smonc parse: cell ({0})", data);
                    setCell(data);
                    break;
                case SMONC_BSIC:
                    LOGGER.log(Level.FINE, "Smonc parse: BSIC ({0})", data);
                    setBsic(data);
                    break;
                case SMONC_chann:
                    LOGGER.log(Level.FINE, "Smonc parse: chann ({0})", data);
                    setChann(Integer.parseInt(data));
                    break;
                case SMONC_RSSI:
                    LOGGER.log(Level.FINE, "Smonc parse: rssi ({0})", data);
                    setRssi(Integer.parseInt(data));
                    break;
                case SMONC_C1:
                    LOGGER.log(Level.FINE, "Smonc parse: C1 ({0})", data);
                    setC1(data);
                    break;
                case SMONC_C2:
                    LOGGER.log(Level.FINE, "Smonc parse: C2 ({0})", data);
                    setC2(data);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Smonc parse: unknown field ({0})", data);
                //ignore       
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Smonc ({0})", e.getMessage());
        }
    }
}
