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
public class Csq {

    private static final Logger LOGGER = Logger.getLogger(Csq.class.getName());

    /*rssi:
    0       -113 dBm or less
    1       -111 dBm
    2..30   -109... -53 dBm
    31      -51 dBm or greater
    99      not known or not detectable*/
    private int rssi;
    /*ber:
    0..7    as RXQUAL values in the table in GSM 05.08 section 8.2.4.
    99      not known or not detectable*/
    private int ber;

    public Csq() {
    }

    public Csq(int rssi, int ber) {
        this.rssi = rssi;
        this.ber = ber;
    }

    public int getRssi() {
        return rssi;
    }

    public int getBer() {
        return ber;
    }

    public void parseCsq(LogHeader logHeader, String data) {
        try {
            switch (logHeader) {
                case CSQ_RSSI:
                    LOGGER.log(Level.FINE, "Csq parse: rssi ({0})", data);
                    this.rssi = Integer.parseInt(data);
                    break;
                case CSQ_BER:
                    LOGGER.log(Level.FINE, "Csq parse: ber ({0})", data);
                    this.ber = Integer.parseInt(data);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Csq parse: unknown field ({0})", data);
                //ignore
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing Csq ({0})", e.getMessage());
        }
    }
}
