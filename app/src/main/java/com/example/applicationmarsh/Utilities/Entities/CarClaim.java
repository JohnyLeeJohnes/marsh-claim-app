package com.example.applicationmarsh.Utilities.Entities;

import android.graphics.Bitmap;

import com.example.applicationmarsh.Utilities.Global;

import java.text.ParseException;
import java.util.Date;

public class CarClaim {
    private int ID;
    private Date datum;
    private String place;
    private String SPZ;
    private String carType;
    private String contactFirstname;
    private String contactLastname;
    private String contactEmail;
    private String contactPhone;
    private int policyNumber;
    private boolean damageCar;
    private boolean damageOtherCar;
    private boolean damageOther;
    private Bitmap photo1;
    private Bitmap photo2;
    private Bitmap photo3;
    private Bitmap photo4;


    public CarClaim(int ID, String datum, String place, String SPZ, String carType, String contactFirstname, String contactLastname, String contactEmail, String contactPhone, int policyNumber, boolean damageCar, boolean damageOtherCar, boolean damageOther, Bitmap photo1, Bitmap photo2, Bitmap photo3, Bitmap photo4) throws ParseException {
        this.setID(ID);
        this.setDatum(datum);
        this.setPlace(place);
        this.setSPZ(SPZ);
        this.setCarType(carType);
        this.setContactFirstname(contactFirstname);
        this.setContactLastname(contactLastname);
        this.setContactEmail(contactEmail);
        this.setContactPhone(contactPhone);
        this.setPolicyNumber(policyNumber);
        this.setDamageCar(damageCar);
        this.setDamageOtherCar(damageOtherCar);
        this.setDamageOther(damageOther);
        this.setPhoto1(photo1);
        this.setPhoto2(photo2);
        this.setPhoto3(photo3);
        this.setPhoto4(photo4);
    }

    //GET & SET
    public int getID() { return ID; }
    public String getDatum() { return Global.dateTimeFormatOut.format(this.datum); }
    public String getPlace() { return place; }
    public String getSPZ() { return SPZ; }
    public String getCarType() { return carType; }
    public String getContactFirstname() { return contactFirstname; }
    public String getContactLastname() {return contactLastname; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public int getPolicyNumber() { return policyNumber; }
    public boolean isDamageCar() { return damageCar; }
    public boolean isDamageOtherCar() { return damageOtherCar; }
    public boolean isDamageOther() { return damageOther; }
    public Bitmap getPhoto1() { return photo1; }
    public Bitmap getPhoto2() { return photo2; }
    public Bitmap getPhoto3() { return photo3; }
    public Bitmap getPhoto4() { return photo4; }

    private void setID(int ID) { this.ID = ID;}
    private void setDatum(String datum) throws ParseException { this.datum = Global.dateTimeFormatIn.parse(datum); }
    private void setPlace(String place) { this.place = place; }
    private void setSPZ(String SPZ) { this.SPZ = SPZ; }
    private void setCarType(String carType) { this.carType = carType; }
    private void setContactFirstname(String dontactFirstname) { this.contactFirstname = dontactFirstname; }
    private void setContactLastname(String dontactLastname) { this.contactLastname = dontactLastname; }
    private void setContactEmail(String dontactEmail) { this.contactEmail = dontactEmail; }
    private void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    private void setPolicyNumber(int policyNumber) { this.policyNumber = policyNumber; }
    private void setDamageCar(boolean damageCar) { this.damageCar = damageCar; }
    private void setDamageOtherCar(boolean damageOtherCar) { this.damageOtherCar = damageOtherCar; }
    private void setDamageOther(boolean damageOther) { this.damageOther = damageOther; }
    private void setPhoto1(Bitmap photo1) { this.photo1 = photo1; }
    private void setPhoto2(Bitmap photo2) { this.photo2 = photo2; }
    private void setPhoto3(Bitmap photo3) { this.photo3 = photo3; }
    private void setPhoto4(Bitmap photo4) { this.photo4 = photo4; }


}
