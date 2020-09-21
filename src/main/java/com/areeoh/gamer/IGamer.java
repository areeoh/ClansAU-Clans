package com.areeoh.gamer;

public interface IGamer {

    int getCoins();

    void setCoins(int coins);

    void addCoins(int coins);

    void removeCoins(int coins);

    boolean hasCoins(int coins);

    long getTimePlayed();

    void setTimePlayed(long timePlayed);

    long getLastLogin();

    void setLastLogin(long lastLogin);

    long getLastOnline();

    void setLastOnline(long lastOnline);
}