package com.transformation.zezoo;

public class CustomerListChildItem {
    private String Name, Number, Amount, Balance, Code, Mode;
    private int Column;

    public CustomerListChildItem(int column, String name, String number, String amount, String balance, String code,String mode) {
        Column = column;
        Name = name;
        Number = number;
        Amount = amount;
        Balance = balance;
        Code = code;
        Mode = mode;
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

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String date) {
        Balance = date;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    CustomerListChildItem() {

    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerListChildItem custom = (CustomerListChildItem) o;

        if (!Name.equals(custom.getName())) {
            return false;
        }
        if (!Number.equals(custom.getNumber())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Name.hashCode();
        result = 31 * result + Number.hashCode();
        return result;
    }
}
