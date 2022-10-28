package com.transformation.zezoo;

public class KeyItem {
    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public String getImeiCode() {
        return ImeiCode;
    }

    public void setImeiCode(String imeiCode) {
        ImeiCode = imeiCode;
    }

    public String getSimCode() {
        return SimCode;
    }

    public void setSimCode(String simCode) {
        SimCode = simCode;
    }

    public KeyItem(int column, String imeiCode, String simCode) {

        Column = column;
        ImeiCode = imeiCode;
        SimCode = simCode;
    }
    public KeyItem(){

    }
    int Column;
    String ImeiCode,SimCode;
}
