package com.aoa.mini_cashier;

public class list_buy_restore {
    public String name;
    public String quantity_type;
    public String goods_quanitity;
    public String buy_price;
    public String sale_price;

    public list_buy_restore(String name, String quantity_type,String goods_quanitity, String buy_price, String sale_price) {
        this.name = name;
        this.goods_quanitity = goods_quanitity;
        this.quantity_type = quantity_type;
        this.buy_price = buy_price;
        this.sale_price = sale_price;
    }

    public String getName() {
        return name;
    }

    public String getQuantity_type() {
        return quantity_type;
    }

    public String getGoods_quanitity() {
        return goods_quanitity;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public String getSale_price() {
        return sale_price;
    }
}
