package com.wolf.wise.holo.poseidon.data;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String username;
    private Integer balance;
    private List<String> items;

    public User(){
        items=new ArrayList<>();
    }

    public User(String uid, String username, Integer balance) {
        this.uid = uid;
        this.username = username;
        this.balance = balance;
        items=new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void addItem(String s){
        items.add(s);
    }

    public boolean contains(String s){
        return items.contains(s);
    }
}
