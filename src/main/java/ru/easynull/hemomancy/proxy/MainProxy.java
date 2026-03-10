package ru.easynull.hemomancy.proxy;

public final class MainProxy implements Proxy {
    public static Proxy PROXY = null;

    public static void setProxy(Proxy proxy){
        PROXY = proxy;
    }
}
