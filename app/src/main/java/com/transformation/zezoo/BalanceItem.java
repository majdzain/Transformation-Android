package com.transformation.zezoo;

public class BalanceItem {
    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getProfits() {
        return Profits;
    }

    public void setProfits(String profits) {
        Profits = profits;
    }

    public String getDebt() {
        return Debt;
    }

    public void setDebt(String debt) {
        Debt = debt;
    }

    int Column;
    String Balance;
    String Profits;
    String Debt;

    public String getSyrBalance() {
        return syrBalance;
    }

    public void setSyrBalance(String syrBalance) {
        this.syrBalance = syrBalance;
    }

    public String getSyrBalance_() {
        return syrBalance_;
    }

    public void setSyrBalance_(String syrBalance_) {
        this.syrBalance_ = syrBalance_;
    }

    public String getMtnBalance() {
        return mtnBalance;
    }

    public void setMtnBalance(String mtnBalance) {
        this.mtnBalance = mtnBalance;
    }

    public String getMtnBalance_() {
        return mtnBalance_;
    }

    public void setMtnBalance_(String mtnBalance_) {
        this.mtnBalance_ = mtnBalance_;
    }

    String syrBalance;
    String syrBalance_;
    String mtnBalance;
    String mtnBalance_;

    BalanceItem() {

    }

    public BalanceItem(int column, String balance, String profits, String debt,String syrBalance, String syrBalance_, String mtnBalance, String mtnBalance_) {
        Column = column;
        Balance = balance;
        Profits = profits;
        Debt = debt;
        this.syrBalance = syrBalance;
        this.syrBalance_ = syrBalance_;
        this.mtnBalance = mtnBalance;
        this.mtnBalance_ = mtnBalance_;
    }
}
