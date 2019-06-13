package com.any_credit;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SpinnerClass {
    private Spinner spinner = null;
    private ArrayList<String> dropdownList = new ArrayList<String>();
    private ArrayAdapter<String> adapter = null;
    private Context context = null;

    public SpinnerClass(final Context context, final Spinner spinner) {
        this.context = context;
        this.spinner = spinner;
        this.adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, dropdownList);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapter);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addDropdownItem(String item) {
        this.dropdownList.add(item);
        resetAdapter();
    }

    public void removeDropdownItem(String item) {
        this.dropdownList.remove(item);
        resetAdapter();
    }

    private void resetAdapter() {
        this.adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, dropdownList);
    }

    public class DropdownListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
