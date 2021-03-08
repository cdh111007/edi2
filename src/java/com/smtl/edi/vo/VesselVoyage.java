package com.smtl.edi.vo;

/**
 *
 * @author Administrator
 */
public class VesselVoyage {

    private String vessel;
    private String vesselCode;
    private String voyage;
    private String vesselIE;
    private String vesselReference;

    public VesselVoyage(String vessel, String voyage) {
        this.vessel = vessel;
        this.voyage = voyage;
    }

    public VesselVoyage(String vessel, String voyage, String vesselIE) {
        this.vessel = vessel;
        this.voyage = voyage;
        this.vesselIE = vesselIE;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVesselCode() {
        return vesselCode;
    }

    public void setVesselCode(String vesselCode) {
        this.vesselCode = vesselCode;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getVesselIE() {
        return vesselIE;
    }

    public void setVesselIE(String vesselIE) {
        this.vesselIE = vesselIE;
    }

    public String getVesselReference() {
        return vesselReference;
    }

    public void setVesselReference(String vesselReference) {
        this.vesselReference = vesselReference;
    }

}
