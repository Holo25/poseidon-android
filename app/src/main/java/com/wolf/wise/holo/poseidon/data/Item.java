package com.wolf.wise.holo.poseidon.data;

public class Item {
    private String uid;
    private String name;
    private Integer credit;
    private Integer price;

    public Item() {
    }

    public Item(String uid, String name, Integer credit, Integer price) {
        this.uid = uid;
        this.name = name;
        this.credit = credit;
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}