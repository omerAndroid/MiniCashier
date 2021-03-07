package com.aoa.mini_cashier;

public class list_item_update {
    public String name;
    public int quantity;
    public String quantity_type;
    public float buy_price;
    public float sale_price;
    public String date_ex;

    public list_item_update(String name, int quantity, String quantity_type, float buy_price, float sale_price, String date_ex) {
        this.name = name;
        this.quantity = quantity;
        this.quantity_type = quantity_type;
        this.buy_price = buy_price;
        this.sale_price = sale_price;
        this.date_ex = date_ex;
    }
}
