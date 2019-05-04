/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import java.util.ArrayList;
import java.util.List;
import pt.solvit.probe.server.util.HeaderEnums.LogHeader;

/**
 *
 * @author AnaRita
 */
public class HeaderData {

    private String headerLabel;
    private List<Integer> indexList = new ArrayList();

    public HeaderData(String headerLabel, int idx) {
        this.headerLabel = headerLabel;
        indexList.add(idx);
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public LogHeader getLogHeader() {
        return LogHeader.getHeader(headerLabel);
    }

    public List<Integer> getIndexList() {
        return indexList;
    }

    public void addIndex(int idx) {
        indexList.add(idx);
    }
}
