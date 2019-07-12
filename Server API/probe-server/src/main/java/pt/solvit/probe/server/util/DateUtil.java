/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaRita
 */
public class DateUtil {

    private static final Logger LOGGER = Logger.getLogger(DateUtil.class.getName());

    public static final DateTimeFormatter ISO8601_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter PRETTY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static LocalDateTime getDateFromIsoString(String date) {
        try {
            return LocalDateTime.parse(date, ISO8601_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.SEVERE, "{0} is not on ISO format", date);
            return null;
        }
    }
}
