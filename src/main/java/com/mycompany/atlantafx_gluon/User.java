package com.mycompany.atlantafx_gluon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class User {
    private IntegerProperty default_theme = new SimpleIntegerProperty(-1);
    public Integer getThemeNumber() {return default_theme.get();}
    public IntegerProperty nameProperty() {
        return default_theme;
    }
    public void setThemeNumber(int name) {
        default_theme.set(name);}
}
