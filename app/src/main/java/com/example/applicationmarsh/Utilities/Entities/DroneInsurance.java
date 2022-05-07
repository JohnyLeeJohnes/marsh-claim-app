package com.example.applicationmarsh.Utilities.Entities;

import com.example.applicationmarsh.Utilities.Global;

import java.text.ParseException;
import java.util.Date;

public class DroneInsurance {

    private int ID;
    private int PolicyNumber;
    private Date datumOd;
    private Date datumDo;

    public DroneInsurance(int ID, int policyNumber, String datumOd, String datumDo) throws ParseException {
        this.setID(ID);
        this.setPolicyNumber(policyNumber);
        this.setDatumOd(datumOd);
        this.setDatumDo(datumDo);
    }

    //GET & SET
    public int getID() { return ID; }
    public int getPolicyNumber() { return PolicyNumber; }
    public String getDatumOd() { return Global.dateFormatOut.format(this.datumOd); }
    public String getDatumDo() { return Global.dateFormatOut.format(this.datumDo); }

    private void setID(int ID) { this.ID = ID; }
    private void setPolicyNumber(int policyNumber) { PolicyNumber = policyNumber; }
    private void setDatumOd(String datumOd) throws ParseException { this.datumOd = Global.dateFormatIn.parse(datumOd); }
    private void setDatumDo(String datumDo) throws ParseException { this.datumDo = Global.dateFormatIn.parse(datumDo); }

}