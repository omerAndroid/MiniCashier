package com.aoa.mini_cashier.item_classes;

public class purchases_item_class {
    public String barcode;
    public String name;
    public String buy_price;
    public String quintity;
    public String total;
    public String free_quintity;
    public String date_purchase;
    public String date_expare;

    public purchases_item_class(String barcode, String name, String buy_price, String quintity, String total, String free_quintity, String date_purchase, String date_expare) {
        this.barcode = barcode;
        this.name = name;
        this.buy_price = buy_price;
        this.quintity = quintity;
        this.total = total;
        this.free_quintity = free_quintity;
        this.date_purchase = date_purchase;
        this.date_expare = date_expare;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public String getQuintity() {
        return quintity;
    }

    public String getTotal() {
        return total;
    }

    public String getFree_quintity() {
        return free_quintity;
    }

    public String getDate_purchase() {
        return date_purchase;
    }

    public String getDate_expare() {
        return date_expare;
    }
}
