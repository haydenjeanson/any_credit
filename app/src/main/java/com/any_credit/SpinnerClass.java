package com.any_credit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpinnerClass implements AdapterView.OnItemSelectedListener {
    private Spinner spinner = null;
    private ArrayList<String> dropdownList = new ArrayList<String>();
    private ArrayAdapter<String> adapter = null;
    private Context context = null;
    private SelectStoreInterface storeInterface = null;
    private Boolean isHomeSpinner;
    private boolean firstSelection = true;

    SpinnerClass(SelectStoreInterface storeInterface, final Context context, final Spinner spinner, final SharedPreferences stores, final boolean isHomeSpinner) {
        populateExisting(stores);

        this.context = context;
        this.storeInterface = storeInterface;
        this.isHomeSpinner = isHomeSpinner;
        this.spinner = spinner;
        this.adapter = new ArrayAdapter<>(this.context, R.layout.store_spinner, dropdownList);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapter);
        this.spinner.setOnItemSelectedListener(this);
    }

    void addDropdownItem(String item) {
        this.dropdownList.add(item);
    }

    void removeDropdownItem(String item) {
        this.dropdownList.remove(item);
    }

    void setSpinner(String item) {
        spinner.setSelection(adapter.getPosition(item));
    }

    private void populateExisting(SharedPreferences storeList) {
        Set<String> storeSet = storeList.getStringSet("stores", new HashSet<String>());
        dropdownList.addAll(storeSet);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (this.isHomeSpinner) {
            if (!this.firstSelection) {
                this.storeInterface.onSelectFromHome();
                this.storeInterface.selectStore(parent.getItemAtPosition(pos).toString());
            } else {
                this.firstSelection = false;
            }
        } else {
            this.storeInterface.selectStore(parent.getItemAtPosition(pos).toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
