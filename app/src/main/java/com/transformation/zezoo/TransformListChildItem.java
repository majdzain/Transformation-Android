package com.transformation.zezoo;

public class TransformListChildItem {
    public TransformListChildItem(int column, String name, String number, String amount, String date, String code, String debt) {
        Column = column;
        Name = name;
        Number = number;
        Amount = amount;
        Date = date;
        Code = code;
        Debt = debt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    private String Name,Number,Amount,Date,Code,Debt;
    private int Column;

    TransformListChildItem() {

    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDebt() {
        return Debt;
    }

    public void setDebt(String debt) {
        Debt = debt;
    }
}
