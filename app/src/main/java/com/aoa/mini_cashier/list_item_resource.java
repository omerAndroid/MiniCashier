package com.aoa.mini_cashier;

public class list_item_resource {
    public String name;
    public String phone;
    public String mobile;
    public String address;

    public list_item_resource(String name, String phone,String mobile, String address) {
        this.name = name;
        this.phone = phone;
        this.mobile = mobile;
        this.address = address;
    }
    public String getName() {
        return name;
    }

    public String getPhone() { return phone; }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }
}
