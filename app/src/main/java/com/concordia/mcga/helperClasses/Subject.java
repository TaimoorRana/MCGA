package com.concordia.mcga.helperClasses;

/**
 * Created by taimoorrana on 2017-02-16.
 */

public interface Subject {

    void register(Observer o);

    void unRegister(Observer o);

    void notifyObservers();

}
