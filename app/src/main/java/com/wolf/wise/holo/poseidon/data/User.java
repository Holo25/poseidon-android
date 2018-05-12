package com.wolf.wise.holo.poseidon.data;

public class User {
    private String uid;
    private String username;
    private Integer balance;

    public User(){

    }

    public User(String uid, String username, Integer balance) {
        this.uid = uid;
        this.username = username;
        this.balance = balance;
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
}
