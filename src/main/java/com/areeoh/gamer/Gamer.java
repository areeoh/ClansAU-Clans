package com.areeoh.gamer;

public class Gamer implements IGamer {

    private int coins;
    private long timePlayed;
    private long lastLogin;
    private long lastOnline;

    public Gamer() {
        this.coins = 5000;
        this.timePlayed = 0L;
        this.lastLogin = System.currentTimeMillis();
        this.lastOnline = 0L;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public void addCoins(int coins) {
        //TODO MAYBE ADD COINUPDATEEVENT?
        this.coins += coins;
    }

    @Override
    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    @Override
    public boolean hasCoins(int coins) {
        return this.coins >= coins;
    }

    @Override
    public long getTimePlayed() {
        return timePlayed;
    }

    @Override
    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    @Override
    public long getLastLogin() {
        return lastLogin;
    }

    @Override
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public long getLastOnline() {
        return lastOnline;
    }

    @Override
    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }
}
