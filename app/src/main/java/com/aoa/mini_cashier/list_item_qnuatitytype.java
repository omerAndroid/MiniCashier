package com.aoa.mini_cashier;

public class list_item_qnuatitytype {
    public String name;
    public String quantity;
    public String buy_price;
    public String sale_price;

    public list_item_qnuatitytype(String name, String guatity, String buy_price, String sale_price) {
        this.name = name;
        this.quantity = guatity;
        this.buy_price = buy_price;
        this.sale_price = sale_price;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public String getSale_price() {
        return sale_price;
    }
}
