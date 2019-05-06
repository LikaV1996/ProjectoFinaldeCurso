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
public class Moni {

    private static final Logger LOGGER = Logger.getLogger(Moni.class.getName());

    private final MoniServingCell servingCell;
    private final MoniDedicatedChannel dedicatedChannel;

    public Moni() {
        this.servingCell = new MoniServingCell();
        this.dedicatedChannel = new MoniDedicatedChannel();
    }

    public Moni(MoniServingCell servingCell, MoniDedicatedChannel dedicatedChannel) {
        this.servingCell = servingCell;
        this.dedicatedChannel = dedicatedChannel;
    }

    public MoniServingCell getServingCell() {
        return servingCell;
    }

    public MoniDedicatedChannel getDedicatedChannel() {
        return dedicatedChannel;
    }

    public void parseMoniServingCell(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case MONI_SC_chann:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: chann ({0})", data);
                    servingCell.setChann(Integer.parseInt(data));
                    break;
                case MONI_SC_rs:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: rs ({0})", data);
                    servingCell.setRs(Integer.parseInt(data));
                    break;
                case MONI_SC_dBm:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: dBm ({0})", data);
                    servingCell.setdBm(Integer.parseInt(data));
                    break;
                case MONI_SC_MCC:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: MCC ({0})", data);
                    servingCell.setMcc(data);
                    break;
                case MONI_SC_MNC:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: MNC ({0})", data);
                    servingCell.setMnc(data);
                    break;
                case MONI_SC_LAC:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: LAC ({0})", data);
                    servingCell.setLac(data);
                    break;
                case MONI_SC_cell:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: cell ({0})", data);
                    servingCell.setCell(data);
                    break;
                case MONI_SC_NCC:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: NCC ({0})", data);
                    servingCell.setNcc(data);
                    break;
                case MONI_SC_BCC:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: BCC ({0})", data);
                    servingCell.setBcc(data);
                    break;
                case MONI_SC_PWR:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: PWR ({0})", data);
                    servingCell.setPwr(Integer.parseInt(data));
                    break;
                case MONI_SC_RXLev:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: C2 ({0})", data);
                    servingCell.setRxLev(Integer.parseInt(data));
                    break;
                case MONI_SC_C1:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: C1 ({0})", data);
                    servingCell.setC1(data);
                    break;
                case MONI_SC_info:
                    LOGGER.log(Level.FINE, "Moni Serving Cell parse: info ({0})", data);
                    servingCell.setServingCellInfo(data);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Moni Serving Cell parse: unknown field ({0})", data);
                //ignore   
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Moni Serving Cell ({0})", e.getMessage());
        }
    }

    public void parseMoniDedicatedChannel(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case MONI_DC_chann:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: chann ({0})", data);
                    dedicatedChannel.setChann(Integer.parseInt(data));
                    break;
                case MONI_DC_TS:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: TS ({0})", data);
                    dedicatedChannel.setTs(Integer.parseInt(data));
                    break;
                case MONI_DC_timAdv:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: timeAdv ({0})", data);
                    dedicatedChannel.setTimAdv(Integer.parseInt(data));
                    break;
                case MONI_DC_PWR:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: PWR ({0})", data);
                    dedicatedChannel.setPwr(Integer.parseInt(data));
                    break;
                case MONI_DC_dBm:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: dBm ({0})", data);
                    dedicatedChannel.setdBm(Integer.parseInt(data));
                    break;
                case MONI_DC_Q:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: Q ({0})", data);
                    dedicatedChannel.setQ(Integer.parseInt(data));
                    break;
                case MONI_DC_ChMod:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: ChMod ({0})", data);
                    dedicatedChannel.setChMod(data);
                    break;
                case MONI_DC_info:
                    LOGGER.log(Level.FINE, "Moni Dedicated Channel parse: info ({0})", data);
                    dedicatedChannel.setDedicatedChannelInfo(data);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Moni Dedicated Channel parse: unknown field ({0})", data);
                //ignore    
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Moni Dedicated Channel ({0})", e.getMessage());
        }
    }
}
