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

    SpinnerClass(SelectStoreInterface storeInterface, final Context context, final Spinner spinner, final SharedPreferences stores) {
        //this.dropdownList.add("");
        populateExisting(stores);

        this.context = context;
        this.storeInterface = storeInterface;
        this.spinner = spinner;
        this.adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, dropdownList);
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

    private void populateExisting(SharedPreferences storeList) {
        Set<String> storeSet = storeList.getStringSet("stores", new HashSet<String>());
        for (String store : storeSet) {
            dropdownList.add(store);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        storeInterface.selectStore(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
