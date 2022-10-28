package com.transformation.zezoo;

public class OpenItem {
    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }


    public OpenItem(int column, String open) {

        Column = column;
        Open = open;
    }
    public OpenItem(){

    }
    int Column;
    private String Open;

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }
}
