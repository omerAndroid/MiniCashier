package com.aoa.mini_cashier;

public class list_item_update {
    public String barcode;
    public String name;
    public String quantity;
    public String date_buy ;
    public String date_ex;

    public list_item_update(String barcode,String name, String quantity, String date_buy, String date_ex) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.date_buy = date_buy;
        this.date_ex = date_ex;
    }
}
