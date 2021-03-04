package com.smtl.edi.vo;

/**
 *
 * @author nm
 */
public class DateRange {

    private String begin;
    private String end;

    public DateRange(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public DateRange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
