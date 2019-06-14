package com.any_credit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SelectStoreInterface {
    TextView lbl_credit;
    Toolbar titlebar;
    Button btn_add;
    Button btn_remove;
    private SpinnerClass spinner;
    SharedPreferences allStores;
    SharedPreferences.Editor allStoresEditor;
    Set<String> storeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        lbl_credit = (TextView) findViewById(R.id.lbl_credit);
        titlebar = (Toolbar) findViewById(R.id.toolbar);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);

        allStores = getSharedPreferences("stores", MODE_PRIVATE);
        storeSet = allStores.getStringSet("stores", new HashSet<String>());

        if (storeSet.isEmpty()) {
            storeSet.add("Home");
        }
        allStoresEditor = allStores.edit();
        allStoresEditor.putStringSet("stores", storeSet);
        allStoresEditor.apply();

        spinner = new SpinnerClass(this, this, (Spinner) findViewById(R.id.storeDropdown), allStores);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_add) {
            addStore();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    PopupWindow popupWindow;
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
                allStoresEditor.putStringSet("stores", storeSet);
                allStoresEditor.apply();
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
    public void selectStore(String storeName) {
        this.titlebar.setTitle(storeName);

        final StoreSave store = new StoreSave(storeName, this.getApplicationContext());

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
                input.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
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
                input.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
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
    }
}
