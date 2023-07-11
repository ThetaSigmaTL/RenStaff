package com.example.renstaff;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Credit {
    private BigDecimal creditSum;
    private int earned;
    private Date date;
    private String name;
    private String contractNumber;
    private CreditType creditType;
    List <BankService> bankServicesList;

    public Credit(BigDecimal creditSum, int earned, Date date, String name, String contractNumber, CreditType creditType, List<BankService> bankServicesList){
        this.creditSum = creditSum;
        this.earned = earned;
        this.date = date;
        this.name = name;
        this.contractNumber = contractNumber;
        this.creditType = creditType;
        this.bankServicesList = bankServicesList;
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

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public int getEarned() {
        return earned;
    }

    public void setEarned(int earned) {
        this.earned = earned;
    }
}
