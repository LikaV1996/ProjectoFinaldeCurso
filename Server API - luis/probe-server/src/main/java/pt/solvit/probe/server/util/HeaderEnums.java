/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaRita
 */
public class HeaderEnums {

    private static final Logger LOGGER = Logger.getLogger(HeaderEnums.class.getName());

    public enum HeaderType {
        BASE, SCANNING;
    }

    public enum LogHeader {
        DATETIME("DateTime", HeaderType.BASE, "yyyy-MM-dd'T'HH:mm:ss.SSS"),
        LATITUDE("Latitude", HeaderType.BASE, "º"),
        LONGITUDE("Longitude", HeaderType.BASE, "º"),
        HEIGHT("Height", HeaderType.BASE, "mm"),
        HEADING("Heading", HeaderType.BASE, "º"),
        SPEED("Speed", HeaderType.BASE, "mm/s"),
        EVENT("Event", HeaderType.BASE),
        CSQ_RSSI("CSQ_RSSI", HeaderType.SCANNING, "dBm"),
        CSQ_BER("CSQ_BER", HeaderType.SCANNING, "dBm"),
        MONI_SC_chann("MONI_SC_chann", HeaderType.SCANNING),
        MONI_SC_rs("MONI_SC_rs", HeaderType.SCANNING, "dBm"),
        MONI_SC_dBm("MONI_SC_dBm", HeaderType.SCANNING, "dBm"),
        MONI_SC_MCC("MONI_SC_MCC", HeaderType.SCANNING),
        MONI_SC_MNC("MONI_SC_MNC", HeaderType.SCANNING),
        MONI_SC_LAC("MONI_SC_LAC", HeaderType.SCANNING),
        MONI_SC_cell("MONI_SC_cell", HeaderType.SCANNING),
        MONI_SC_NCC("MONI_SC_NCC", HeaderType.SCANNING),
        MONI_SC_BCC("MONI_SC_BCC", HeaderType.SCANNING),
        MONI_SC_PWR("MONI_SC_PWR", HeaderType.SCANNING, "dBm"),
        MONI_SC_RXLev("MONI_SC_RXLev", HeaderType.SCANNING, "dBm"),
        MONI_SC_C1("MONI_SC_C1", HeaderType.SCANNING),
        MONI_SC_info("MONI_SC_info", HeaderType.SCANNING),
        MONI_DC_chann("MONI_DC_chann", HeaderType.SCANNING),
        MONI_DC_TS("MONI_DC_TS", HeaderType.SCANNING),
        MONI_DC_timAdv("MONI_DC_timAdv", HeaderType.SCANNING),
        MONI_DC_PWR("MONI_DC_PWR", HeaderType.SCANNING),
        MONI_DC_dBm("MONI_DC_dBm", HeaderType.SCANNING, "dBm"),
        MONI_DC_Q("MONI_DC_Q", HeaderType.SCANNING),
        MONI_DC_ChMod("MONI_DC_ChMod", HeaderType.SCANNING),
        MONI_DC_info("MONI_DC_info", HeaderType.SCANNING),
        MONP_chann("MONP_chann", HeaderType.SCANNING),
        MONP_rs("MONP_rs", HeaderType.SCANNING, "dBm"),
        MONP_dBm("MONP_dBm", HeaderType.SCANNING, "dBm"),
        MONP_MCC("MONP_MCC", HeaderType.SCANNING),
        MONP_MNC("MONP_MNC", HeaderType.SCANNING),
        MONP_BCC("MONP_BCC", HeaderType.SCANNING),
        MONP_C1("MONP_C1", HeaderType.SCANNING),
        MONP_C2("MONP_C2", HeaderType.SCANNING),
        SMONC_MCC("SMONC_MCC", HeaderType.SCANNING),
        SMONC_MNC("SMONC_MNC", HeaderType.SCANNING),
        SMONC_LAC("SMONC_LAC", HeaderType.SCANNING),
        SMONC_cell("SMONC_cell", HeaderType.SCANNING),
        SMONC_BSIC("SMONC_BSIC", HeaderType.SCANNING),
        SMONC_chann("SMONC_chann", HeaderType.SCANNING),
        SMONC_RSSI("SMONC_RSSI", HeaderType.SCANNING, "dBm"),
        SMONC_C1("SMONC_C1", HeaderType.SCANNING),
        SMONC_C2("SMONC_C2", HeaderType.SCANNING),
        SMOND_TA("SMOND_TA", HeaderType.SCANNING, "dBm"),
        SMOND_RSSI("SMOND_RSSI", HeaderType.SCANNING),
        SMOND_BER("SMOND_BER", HeaderType.SCANNING),
        SMOND_SC_MCC("SMOND_SC_MCC", HeaderType.SCANNING),
        SMOND_SC_MNC("SMOND_SC_MNC", HeaderType.SCANNING),
        SMOND_SC_LAC("SMOND_SC_LAC", HeaderType.SCANNING),
        SMOND_SC_cell("SMOND_SC_cell", HeaderType.SCANNING),
        SMOND_SC_BSIC("SMOND_SC_BSIC", HeaderType.SCANNING),
        SMOND_SC_chann("SMOND_SC_chann", HeaderType.SCANNING),
        SMOND_SC_RxLev("SMOND_SC_RxLev", HeaderType.SCANNING, "dBm"),
        SMOND_SC_RxLev_Full("SMOND_SC_RxLev_Full", HeaderType.SCANNING, "dBm"),
        SMOND_SC_RxLev_Sub("SMOND_SC_RxLev_Sub", HeaderType.SCANNING, "dBm"),
        SMOND_SC_RxQual("SMOND_SC_RxQual", HeaderType.SCANNING),
        SMOND_SC_RxQual_Full("SMOND_SC_RxQual_Full", HeaderType.SCANNING),
        SMOND_SC_RxQual_Sub("SMOND_SC_RxQual_Sub", HeaderType.SCANNING),
        SMOND_SC_Timeslot("SMOND_SC_Timeslot", HeaderType.SCANNING),
        SMOND_NC_MCC("SMOND_NC_MCC", HeaderType.SCANNING),
        SMOND_NC_MNC("SMOND_NC_MNC", HeaderType.SCANNING),
        SMOND_NC_LAC("SMOND_NC_LAC", HeaderType.SCANNING),
        SMOND_NC_cell("SMOND_NC_cell", HeaderType.SCANNING),
        SMOND_NC_BSIC("SMOND_NC_BSIC", HeaderType.SCANNING),
        SMOND_NC_chann("SMOND_NC_chann", HeaderType.SCANNING),
        SMOND_NC_RxLev("SMOND_NC_RxLev", HeaderType.SCANNING, "dBm");

        private final String name;
        private final HeaderType type;
        private String units;

        private LogHeader(String name, HeaderType type, String units) {
            this.name = name;
            this.type = type;
            this.units = units;
        }

        private LogHeader(String name, HeaderType type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public HeaderType getType() {
            return type;
        }

        public String getUnits() {
            return units;
        }

        public static LogHeader getHeader(String txt) {
            for (LogHeader curLogHeader : values()) {
                if (txt.contains("MONP_") || txt.contains("SMONC_") || txt.contains("SMOND_NC")) {
                    if (txt.contains(curLogHeader.getName())) {
                        return curLogHeader;
                    }
                } else if (txt.equals(curLogHeader.getName())) {
                    return curLogHeader;
                }
            }
            return null;
        }
    }

    public enum EventLevel {
        CRITICAL(0), MAJOR(1), WARNING(2), INFO(3);

        private final int level;

        private EventLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public static EventLevel getEventLevel(int level) {
            for (EventLevel curEventLevel : values()) {
                if (curEventLevel.getLevel() == level) {
                    return curEventLevel;
                }
            }
            LOGGER.log(Level.SEVERE, "{0} does not match EventLevel enum", String.valueOf(level));
            return null;
        }
    }
}
