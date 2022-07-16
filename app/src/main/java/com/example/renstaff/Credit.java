package com.example.renstaff;

import java.math.BigDecimal;
import java.util.Date;

public class Credit {
    private BigDecimal creditSum;
    private Date date;
    private String name;
    private CreditType creditType;
    private boolean insurance;
    private boolean sms;
    private boolean lawyer;
    private boolean dateChanger;
    private boolean creditHistory;

    public Credit(BigDecimal creditSum, Date date, String name, CreditType creditType, boolean insurance, boolean sms,
                  boolean lawyer, boolean dateChanger, boolean creditHistory){
        this.creditSum = creditSum;
        this.date = date;
        this.name = name;
        this.creditType = creditType;
        this.insurance = insurance;
        this.sms = sms;
        this.lawyer = lawyer;
        this.dateChanger = dateChanger;
        this.creditHistory = creditHistory;
    }

    public BigDecimal getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(BigDecimal creditSum) {
        this.creditSum = creditSum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isLawyer() {
        return lawyer;
    }

    public void setLawyer(boolean lawyer) {
        this.lawyer = lawyer;
    }

    public boolean isDateChanger() {
        return dateChanger;
    }

    public void setDateChanger(boolean dateChanger) {
        this.dateChanger = dateChanger;
    }

    public boolean isCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(boolean creditHistory) {
        this.creditHistory = creditHistory;
    }
}
