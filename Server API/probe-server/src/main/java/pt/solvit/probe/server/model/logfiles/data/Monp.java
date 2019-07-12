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
public class Monp {

    private static final Logger LOGGER = Logger.getLogger(Monp.class.getName());

    private String mcc, mnc, bcc, c1, c2;
    private int chann, rs, dBm;

    public Monp() {
    }

    public Monp(int chann, int rs, int dBm, String mcc, String mnc, String bcc, String c1, String c2) {
        this.chann = chann;
        this.rs = rs;
        this.dBm = dBm;
        this.mcc = mcc;
        this.mnc = mnc;
        this.bcc = bcc;
        this.c1 = c1;
        this.c2 = c2;
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

    public String getBcc() {
        return bcc;
    }

    public String getC1() {
        return c1;
    }

    public String getC2() {
        return c2;
    }

    public void parseMonp(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case MONP_chann:
                    LOGGER.log(Level.FINE, "Monp parse: chann ({0})", data);
                    this.chann = Integer.parseInt(data);
                    break;
                case MONP_rs:
                    LOGGER.log(Level.FINE, "Monp parse: rs ({0})", data);
                    this.rs = Integer.parseInt(data);
                    break;
                case MONP_dBm:
                    LOGGER.log(Level.FINE, "Monp parse: dBm ({0})", data);
                    this.dBm = Integer.parseInt(data);
                    break;
                case MONP_MCC:
                    LOGGER.log(Level.FINE, "Monp parse: MCC ({0})", data);
                    this.mcc = data;
                    break;
                case MONP_MNC:
                    LOGGER.log(Level.FINE, "Monp parse: MNC ({0})", data);
                    this.mnc = data;
                    break;
                case MONP_BCC:
                    LOGGER.log(Level.FINE, "Monp parse: BCC ({0})", data);
                    this.bcc = data;
                    break;
                case MONP_C1:
                    LOGGER.log(Level.FINE, "Monp parse: C1 ({0})", data);
                    this.c1 = data;
                    break;
                case MONP_C2:
                    LOGGER.log(Level.FINE, "Monp parse: C2 ({0})", data);
                    this.c2 = data;
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Monp parse: unknown field ({0})", data);
                //ignore   
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Monp ({0})", e.getMessage());
        }
    }
}
