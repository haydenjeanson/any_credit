package com.any_credit;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.math.RoundingMode;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class StoreSave {
    private String storeName = "";
    private float credit = 0;
    private Context context;
    SharedPreferences store;
    SharedPreferences.Editor editor;

    public PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public StoreSave(final String storeName, final Context context) {
        this.storeName = storeName;
        this.context = context;

        this.store = this.context.getSharedPreferences(storeName, MODE_PRIVATE);
        this.editor = store.edit();
        this.credit = store.getFloat("credit", 0);
    }

    public void removeSave() {
        this.editor.clear();
        this.editor.commit();
    }

    public void addCredit(float amount) {
        this.credit += amount;
        storeCredit();
    }

    public void removeCredit(float amount) {
        this.credit -= amount;
        storeCredit();
    }

    private void storeCredit() {
        this.credit = ((int)(this.credit*100)) / 100f;
        editor.putFloat("credit", this.credit);
        editor.commit();
        this.changes.firePropertyChange("credit", null, this.credit);
    }

    public float getCredit() {
        this.credit = store.getFloat("credit", 0);
        return this.credit;
    }
}
