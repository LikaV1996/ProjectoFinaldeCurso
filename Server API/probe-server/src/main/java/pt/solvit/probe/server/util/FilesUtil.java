/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.logfiles.data.Coordinates;
import pt.solvit.probe.server.model.logfiles.EventData;
import pt.solvit.probe.server.model.logfiles.HeaderData;
import pt.solvit.probe.server.model.logfiles.FileHeader;
import pt.solvit.probe.server.model.logfiles.ScanData;
import pt.solvit.probe.server.model.logfiles.Event;
import pt.solvit.probe.server.model.logfiles.EventProperties;
import pt.solvit.probe.server.model.logfiles.SysLog;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.logfiles.data.Csq;
import pt.solvit.probe.server.model.logfiles.data.Moni;
import pt.solvit.probe.server.model.logfiles.data.Monp;
import pt.solvit.probe.server.model.logfiles.data.Smonc;
import pt.solvit.probe.server.model.logfiles.data.Smond;
import pt.solvit.probe.server.service.exception.impl.FileRelatedException;
import pt.solvit.probe.server.service.exception.impl.UnknownFileException;
import static pt.solvit.probe.server.util.DateUtil.FILE_DATE_FORMATTER;
import pt.solvit.probe.server.util.HeaderEnums.HeaderType;
import pt.solvit.probe.server.util.HeaderEnums.LogHeader;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class FilesUtil {

    private static final Logger LOGGER = Logger.getLogger(FilesUtil.class.getName());

    public static String zipString(String fileName, String str) {
        try {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(obj);

            zos.putNextEntry(new ZipEntry(fileName));

            zos.write(str.getBytes(StandardCharsets.UTF_8));
            zos.close();

            String outStr = new String(obj.toByteArray(), StandardCharsets.ISO_8859_1);

            return outStr;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File unzipBytes(byte[] content, String fileName) {
        try {
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
            zis.getNextEntry();

            File unzippedFile = new File(fileName);
            FileOutputStream fos = new FileOutputStream(unzippedFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            //Close File
            fos.close();
            //Close ZipEntry
            zis.closeEntry();
            zis.close();

            return unzippedFile;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new FileRelatedException("Unzip error.", e.getMessage());
        }
    }

    public static ObuFile parseFileName(long obuId, String fileName, File logFile) {
        LOGGER.log(Level.INFO, "Reading bytes from file");
        byte[] fileData;
        try {
            fileData = Files.readAllBytes(logFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.log(Level.INFO, "Parsing file name");

        Matcher sysLogMatcher = Pattern.compile("^systemLog_(.+).zip$").matcher(fileName);
        if (sysLogMatcher.find()) {
            String date = sysLogMatcher.group(1);
            LocalDateTime closeDate;
            try {
                closeDate = LocalDateTime.parse(date, FILE_DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new UnknownFileException(date + " is not on ISO format");
            }

            return new SysLog(null, obuId, fileName, closeDate, LocalDateTime.now(), fileData, null);
        }

        Matcher testLogMatcher = Pattern.compile("^testLog(\\d+)_(\\d+)_(.+).zip$").matcher(fileName);
        if (testLogMatcher.find()) {
            long testPlanId, setupId;
            try {
                testPlanId = Long.parseLong(testLogMatcher.group(1));
                setupId = Long.parseLong(testLogMatcher.group(2));
            } catch (NumberFormatException e) {
                throw new UnknownFileException("NumberFormatException {" + e.getMessage() + "}");
            }
            String date = testLogMatcher.group(3);
            LocalDateTime closeDate;
            try {
                closeDate = LocalDateTime.parse(date, FILE_DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new UnknownFileException(date + " is not on ISO format");
            }
            return new TestLog(null, obuId, fileName, closeDate, LocalDateTime.now(), fileData, testPlanId, setupId, null);
        }

        throw new UnknownFileException("File name does not match sysLog or testLog regex");
    }

    public static FileHeader parseTestLogHeader(String[] data) {
        List<HeaderData> headerDataList = new ArrayList();

        int eventSize = 0;
        int scanningSize = 0;

        int idx = 0;
        for (String curData : data) {
            LogHeader logHeader = LogHeader.getHeader(curData);
            if (logHeader == null) {
                throw new FileRelatedException("Invalid file", "Invalid header.");
            }

            if (logHeader.getType() != HeaderType.BASE) {
                eventSize++;
                scanningSize++;
            } else {
                scanningSize++;
            }

            boolean found = false;
            for (HeaderData curHeaderData : headerDataList) {
                if (curHeaderData.getHeaderLabel().equals(logHeader.getName())) {
                    curHeaderData.addIndex(idx);
                    found = true;
                }
            }
            if (!found) {
                HeaderData headerData = new HeaderData(logHeader.getName(), idx);
                headerDataList.add(headerData);
            }
            idx++;
        }

        if (headerDataList.isEmpty()) {
            throw new FileRelatedException("Invalid file", "Header: basicHeader not found.");
        }

        return new FileHeader(headerDataList, eventSize, scanningSize);
    }

    public static FileHeader createSysLogHeader() {
        List<HeaderData> headerDataList = new ArrayList();

        headerDataList.add(new HeaderData(LogHeader.DATETIME.getName(), 0));
        headerDataList.add(new HeaderData(LogHeader.LATITUDE.getName(), 1));
        headerDataList.add(new HeaderData(LogHeader.LONGITUDE.getName(), 2));
        headerDataList.add(new HeaderData(LogHeader.EVENT.getName(), 3));

        return new FileHeader(headerDataList, 4, 0);
    }

    public static ScanData parseScanData(FileHeader fileHeader, String[] data) {
        LOGGER.log(Level.FINE, "Parsing scan data...");

        LocalDateTime date = null;
        Coordinates coordinates = new Coordinates();
        Csq csq = new Csq();
        Moni moni = new Moni();
        List<Monp> monpList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            monpList.add(new Monp());
        }
        List<Smonc> smoncList = new ArrayList();
        for (int i = 0; i < 7; i++) {
            smoncList.add(new Smonc());
        }
        Smond smond = new Smond();

        if (fileHeader.getScanningSize() != data.length) {
            throw new FileRelatedException("Invalid file", "ScanData dimensions do not match header.");
        }

        //Parsing data
        for (HeaderData curLogData : fileHeader.getHeaderDataList()) {
            List<Integer> indexList = curLogData.getIndexList();

            int i = 0;
            for (Integer curIndex : indexList) {
                String curData = data[curIndex].trim();
                if (!curData.isEmpty()) {
                    LogHeader logHeader = (LogHeader) curLogData.getLogHeader();
                    String headerLabel = curLogData.getHeaderLabel();
                    switch (logHeader) {
                        case DATETIME:
                            LOGGER.log(Level.FINE, "Date parse: {0}", curData);
                            LocalDateTime datetime = DateUtil.getDateFromIsoString(curData);
                            if (datetime != null) {
                                date = datetime;
                            } else {
                                LOGGER.log(Level.SEVERE, "Error parsing date");
                            }
                            break;
                        case LATITUDE:
                        case LONGITUDE:
                            coordinates.parseCoordinates(logHeader, curData);
                            break;
                        case CSQ_BER:
                        case CSQ_RSSI:
                            csq.parseCsq(logHeader, curData);
                            break;
                        default:
                            if (headerLabel.contains("MONI_SC")) {
                                moni.parseMoniServingCell(logHeader, curData);
                            } else if (headerLabel.contains("MONI_DC")) {
                                moni.parseMoniDedicatedChannel(logHeader, curData);
                            } else if (headerLabel.contains("MONP")) {
                                int j = 0;
                                for (Monp curMonp : monpList) {
                                    if (i == j) {
                                        curMonp.parseMonp(logHeader, curData);
                                        break;
                                    }
                                    j++;
                                }
                            } else if (headerLabel.contains("SMONC")) {
                                int j = 0;
                                for (Smonc curSmonc : smoncList) {
                                    if (i == j) {
                                        curSmonc.parseSmonc(logHeader, curData);
                                    }
                                    j++;
                                }
                            } else if (headerLabel.contains("SMOND_SC")) {
                                smond.parseSmondSci(logHeader, curData);
                            } else if (headerLabel.contains("SMOND_NC")) {
                                smond.parseSmondNciList(logHeader, curData, i);
                            } else if (headerLabel.contains("SMOND")) {
                                smond.parseSmond(logHeader, curData);
                            }
                    }
                }
                i++;
            }
        }

        return new ScanData(date, coordinates, csq, moni, monpList, smoncList, smond);
    }

    public static EventData parseEventData(FileHeader headerInfo, String[] data) {
        LOGGER.log(Level.FINE, "Parsing event data...");

        LocalDateTime date = null;
        Coordinates coordinates = new Coordinates();
        String eventType = null;
        EventProperties properties = null;

        if (headerInfo.getEventSize() != data.length) {
            throw new FileRelatedException("Invalid file", "EventData dimensions do not match header.");
        }

        //Parsing data
        for (HeaderData curLogData : headerInfo.getHeaderDataList()) {
            Integer curIndex = curLogData.getIndexList().get(0);

            String curData = data[curIndex].trim();
            if (!curData.isEmpty()) {
                LogHeader logHeader = (LogHeader) curLogData.getLogHeader();
                switch (logHeader) {
                    case DATETIME:
                        LOGGER.log(Level.FINE, "Date parse: {0}", curData);
                        LocalDateTime datetime = DateUtil.getDateFromIsoString(curData);
                        if (datetime != null) {
                            date = datetime;
                        } else {
                            LOGGER.log(Level.SEVERE, "Error parsing date");
                        }
                        break;
                    case LATITUDE:
                    case LONGITUDE:
                        coordinates.parseCoordinates(logHeader, curData);
                        break;
                    case EVENT:
                        LOGGER.log(Level.FINE, "Event parse: {0}", curData);
                        Event event = GSON.fromJson(curData, Event.class);

                        eventType = event.getEventType();
                        properties = event.getProperties();
                        break;
                    default:
                    //Ignore
                }
            }
        }

        return new EventData(date, coordinates, eventType, properties);
    }
}
