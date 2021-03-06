package com.any_credit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;

import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class MainActivity extends AppCompatActivity implements SelectStoreInterface {
    TextView lbl_credit;
    Button btn_add;
    Button btn_remove;
    private SpinnerClass spinner;
    private SpinnerClass homeSelectStore;
    SharedPreferences allStores;
    SharedPreferences.Editor allStoresEditor;
    Set<String> storeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        allStores = getSharedPreferences("stores", MODE_PRIVATE);
        storeSet = allStores.getStringSet("stores", new HashSet<String>());

        if (storeSet.isEmpty()) {
            storeSet.add("Home");
        } else {
            storeSet.remove("Home");
        }
        storeSet.add("");
        allStoresEditor = allStores.edit();
        allStoresEditor.putStringSet("stores", storeSet);
        allStoresEditor.apply();

        homeSelectStore = new SpinnerClass(this, this, (Spinner) findViewById(R.id.homeStoreDropdown), allStores, true);

        storeSet.remove("");
        allStoresEditor.remove("stores");
        allStoresEditor.commit();
        allStoresEditor.putStringSet("stores", storeSet);
        allStoresEditor.apply();

    }

    @Override
    public void onSelectFromHome() {
        setContentView(R.layout.activity_main);

        lbl_credit = findViewById(R.id.lbl_credit);
        btn_add = findViewById(R.id.btn_add);
        btn_remove = findViewById(R.id.btn_remove);

        spinner = new SpinnerClass(this, this, (Spinner) findViewById(R.id.storeDropdown), allStores, false);
    }

    protected void goHome() {
        setContentView(R.layout.home);

        storeSet.add("");
        allStoresEditor.remove("stores");
        allStoresEditor.commit();
        allStoresEditor.putStringSet("stores", storeSet);
        allStoresEditor.apply();

        homeSelectStore = new SpinnerClass(MainActivity.this, MainActivity.this, (Spinner) findViewById(R.id.homeStoreDropdown), allStores, true);
    }

    protected void addStore() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter Store Name:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String storeName = input.getText().toString();
                MainActivity.this.spinner.addDropdownItem(storeName);
                storeSet.add(storeName);
                allStoresEditor.remove("stores");
                allStoresEditor.commit();
                allStoresEditor.putStringSet("stores", storeSet);
                allStoresEditor.commit();
                selectStore(storeName);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    @Override
    public void selectStore(final String storeName) {

        final StoreSave store = new StoreSave(storeName, this.getApplicationContext());
        final Button btn_addStore = findViewById(R.id.btn_addStore);
        final Button btn_del = findViewById(R.id.btn_delStore);
        final Button btn_home = findViewById(R.id.btn_home);

        spinner.setSpinner(storeName);

        lbl_credit.setText(Float.toString(store.getCredit()));

        PropertyChangeListener creditListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                lbl_credit.setText(Float.toString(store.getCredit()));
            }
        };
        store.changes.addPropertyChangeListener(creditListener);

        btn_add.setVisibility(View.VISIBLE);
        btn_remove.setVisibility(View.VISIBLE);
        btn_add.setEnabled(true);
        btn_remove.setEnabled(true);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("Amount to add:");

                // Set an EditText view to get user input
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!input.getText().toString().isEmpty()) {
                            store.addCredit(Float.parseFloat(input.getText().toString()));
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("Amount to remove:");

                // Set an EditText view to get user input
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!input.getText().toString().isEmpty()) {
                            store.removeCredit(Float.parseFloat(input.getText().toString()));
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        btn_addStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStore();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Are you sure you want to delete " + storeName + "?");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        storeSet.remove(storeName);
                        allStoresEditor.remove("stores");
                        allStoresEditor.commit();
                        allStoresEditor.putStringSet("stores", storeSet);
                        allStoresEditor.commit();
                        store.removeSave();
                        spinner.removeDropdownItem(storeName);
                        selectStore(storeSet.toArray()[0].toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
    }
}
