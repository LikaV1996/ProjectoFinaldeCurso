/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles.data;

/**
 *
 * @author AnaRita
 */
public class MoniDedicatedChannel {

    private int chann, ts, timAdv, pwr, dBm, q;
    private String chMod;

    private String dedicatedChannelInfo;

    public MoniDedicatedChannel() {
    }

    public MoniDedicatedChannel(String dedicatedChannelInfo) {
        this.dedicatedChannelInfo = dedicatedChannelInfo;
    }

    public MoniDedicatedChannel(int chann, int ts, int timAdv, int pwr, int dBm, int q, String chMod) {
        this.chann = chann;
        this.ts = ts;
        this.timAdv = timAdv;
        this.pwr = pwr;
        this.dBm = dBm;
        this.q = q;
        this.chMod = chMod;
    }

    public int getChann() {
        return chann;
    }

    public int getTs() {
        return ts;
    }

    public int getTimAdv() {
        return timAdv;
    }

    public int getPwr() {
        return pwr;
    }

    public int getdBm() {
        return dBm;
    }

    public int getQ() {
        return q;
    }

    public String getDedicatedChannelInfo() {
        return dedicatedChannelInfo;
    }

    public String getChMod() {
        return chMod;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public void setTimAdv(int timAdv) {
        this.timAdv = timAdv;
    }

    public void setPwr(int pwr) {
        this.pwr = pwr;
    }

    public void setdBm(int dBm) {
        this.dBm = dBm;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public void setChann(int chann) {
        this.chann = chann;
    }

    public void setChMod(String chMod) {
        this.chMod = chMod;
    }

    public void setDedicatedChannelInfo(String dedicatedChannelInfo) {
        this.dedicatedChannelInfo = dedicatedChannelInfo;
    }
}
