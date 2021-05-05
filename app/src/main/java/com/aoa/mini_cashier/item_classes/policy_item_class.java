package com.aoa.mini_cashier.item_classes;

public class policy_item_class {
    public String sum;
    public String date;
    public String note;
    public String type;

    public policy_item_class(String sum, String date, String note, String type) {
        this.sum = sum;
        this.date = date;
        this.note = note;
        this.type = type;
    }

    public String getSum() {
        return sum;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getType() {
        return type;
    }
}
