package com.aoa.mini_cashier.item_classes;

public class report_item_class {
    public String barcode;
    public String name;
    public String data_1;
    public String data_2;
    public String data_3;
    public String data_4;
    public String data_5;

    public report_item_class(String barcode, String name, String data_1, String data_2, String data_3, String data_4, String data_5) {
        this.barcode = barcode;
        this.name = name;
        this.data_1 = data_1;
        this.data_2 = data_2;
        this.data_3 = data_3;
        this.data_4 = data_4;
        this.data_5 = data_5;
    }

    public String getParcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getData_1() {
        return data_1;
    }

    public String getData_2() {
        return data_2;
    }

    public String getData_3() {
        return data_3;
    }

    public String getData_4() {
        return data_4;
    }

    public String getData_5() {
        return data_5;
    }
}
