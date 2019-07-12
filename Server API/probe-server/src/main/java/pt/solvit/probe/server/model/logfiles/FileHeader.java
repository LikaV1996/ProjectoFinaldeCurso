/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import java.util.List;

/**
 *
 * @author AnaRita
 */
public class FileHeader {

    private List<HeaderData> headerDataList;
    private int eventSize;
    private int scanningSize;

    public FileHeader(List<HeaderData> headerDataList, int eventSize, int scanningSize) {
        this.headerDataList = headerDataList;
        this.eventSize = eventSize;
        this.scanningSize = scanningSize;
    }

    public List<HeaderData> getHeaderDataList() {
        return headerDataList;
    }

    public int getEventSize() {
        return eventSize;
    }

    public int getScanningSize() {
        return scanningSize;
    }
}
