/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.solvit.probe.server.util.HeaderEnums.LogHeader;

/**
 *
 * @author AnaRita
 */
public class Smond {

    private static final Logger LOGGER = Logger.getLogger(Smond.class.getName());

    private SmondSci sci;
    private List<SmondNci> nciList;
    private int ta, rssi, ber;

    public Smond() {
        sci = new SmondSci();
        nciList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            nciList.add(new SmondNci());
        }
    }

    public Smond(SmondSci sci, List<SmondNci> nciList, int ta, int rssi, int ber) {
        this.sci = sci;
        this.nciList = nciList;
        this.ta = ta;
        this.rssi = rssi;
        this.ber = ber;
    }

    public SmondSci getSci() {
        return sci;
    }

    public List<SmondNci> getNciList() {
        return nciList;
    }

    public int getTa() {
        return ta;
    }

    public int getRssi() {
        return rssi;
    }

    public int getBer() {
        return ber;
    }

    private void setTa(int ta) {
        this.ta = ta;
    }

    private void setRssi(int rssi) {
        this.rssi = rssi;
    }

    private void setBer(int ber) {
        this.ber = ber;
    }

    public void parseSmond(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case SMOND_TA:
                    LOGGER.log(Level.FINE, "Smond parse: TA ({0})", data);
                    setTa(Integer.parseInt(data));
                    break;
                case SMOND_RSSI:
                    LOGGER.log(Level.FINE, "Smond parse: RSSI ({0})", data);
                    setRssi(Integer.parseInt(data));
                    break;
                case SMOND_BER:
                    LOGGER.log(Level.FINE, "Smond parse: BER ({0})", data);
                    setBer(Integer.parseInt(data));
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Smond parse: unknown field ({0})", data);
                //ignore     
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Smond ({0})", e.getMessage());
        }
    }

    public void parseSmondSci(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case SMOND_SC_MCC:
                    LOGGER.log(Level.FINE, "Smond Sci parse: MCC ({0})", data);
                    sci.setMcc(data);
                    break;
                case SMOND_SC_MNC:
                    LOGGER.log(Level.FINE, "Smond Sci parse: MNC ({0})", data);
                    sci.setMnc(data);
                    break;
                case SMOND_SC_LAC:
                    LOGGER.log(Level.FINE, "Smond Sci parse: LAC ({0})", data);
                    sci.setLac(data);
                    break;
                case SMOND_SC_cell:
                    LOGGER.log(Level.FINE, "Smond Sci parse: cell ({0})", data);
                    sci.setCell(data);
                    break;
                case SMOND_SC_BSIC:
                    LOGGER.log(Level.FINE, "Smond Sci parse: BSIC ({0})", data);
                    sci.setBsic(data);
                    break;
                case SMOND_SC_chann:
                    LOGGER.log(Level.FINE, "Smond Sci parse: chann ({0})", data);
                    sci.setChann(data);
                    break;
                case SMOND_SC_RxLev:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxLev ({0})", data);
                    sci.setRxLev(Integer.parseInt(data));
                    break;
                case SMOND_SC_RxLev_Full:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxLev Full ({0})", data);
                    sci.setRxLev_full(data);
                    break;
                case SMOND_SC_RxLev_Sub:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxLev Sub ({0})", data);
                    sci.setRxLev_sub(data);
                    break;
                case SMOND_SC_RxQual:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxQual ({0})", data);
                    sci.setRxQual(Integer.parseInt(data));
                    break;
                case SMOND_SC_RxQual_Full:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxQual Full ({0})", data);
                    sci.setRxQual_full(data);
                    break;
                case SMOND_SC_RxQual_Sub:
                    LOGGER.log(Level.FINE, "Smond Sci parse: RxQual Sub ({0})", data);
                    sci.setRxQual_sub(data);
                    break;
                case SMOND_SC_Timeslot:
                    LOGGER.log(Level.FINE, "Smond Sci parse: Timeslot ({0})", data);
                    sci.setTimeslot(Integer.parseInt(data));
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Smond Sci parse: unknown field ({0})", data);
                //ignore  
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Smond Sci ({0})", e.getMessage());
        }
    }

    public void parseSmondNciList(LogHeader logHeader, String data, int i) {
        int j = 0;
        try {
            for (SmondNci curSmondNci : nciList) {
                if (i == j) {
                    switch (logHeader) {
                        case SMOND_NC_MCC:
                            LOGGER.log(Level.FINE, "Smond Nci parse: MCC ({0})", data);
                            curSmondNci.setMcc(data);
                            break;
                        case SMOND_NC_MNC:
                            LOGGER.log(Level.FINE, "Smond Nci parse: MNC ({0})", data);
                            curSmondNci.setMnc(data);
                            break;
                        case SMOND_NC_LAC:
                            LOGGER.log(Level.FINE, "Smond Nci parse: LAC ({0})", data);
                            curSmondNci.setLac(data);
                            break;
                        case SMOND_NC_cell:
                            LOGGER.log(Level.FINE, "Smond Nci parse: cell ({0})", data);
                            curSmondNci.setCell(data);
                            break;
                        case SMOND_NC_BSIC:
                            LOGGER.log(Level.FINE, "Smond Nci parse: BSIC ({0})", data);
                            curSmondNci.setBsic(data);
                            break;
                        case SMOND_NC_chann:
                            LOGGER.log(Level.FINE, "Smond Nci parse: chann ({0})", data);
                            curSmondNci.setChann(data);
                            break;
                        case SMOND_NC_RxLev:
                            LOGGER.log(Level.FINE, "Smond Nci parse: RxLev ({0})", data);
                            curSmondNci.setRxLev(Integer.parseInt(data));
                            break;
                        default:
                            LOGGER.log(Level.WARNING, "Smond Nci parse: unknown field ({0})", data);
                        //ignore      
                    }
                }
                j++;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Smond Nci ({0})", e.getMessage());
        }
    }
}
