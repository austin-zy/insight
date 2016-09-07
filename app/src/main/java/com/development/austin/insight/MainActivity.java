package com.development.austin.insight;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.development.austin.insight.database.DBHelper_Place;
import com.development.austin.insight.database.RealmModule;
import com.development.austin.insight.fragment.FragmentAddPlace;
import com.development.austin.insight.fragment.FragmentCreateLink;
import com.development.austin.insight.fragment.FragmentGetLatLng;
import com.development.austin.insight.fragment.MainFragment;
import com.development.austin.insight.session.SessionManager;
import com.mapbox.mapboxsdk.geometry.LatLng;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener,FragmentGetLatLng.OnFragmentInteractionListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private static MainActivity instance;

    private boolean viewIsAtHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationView();

        /**
         * Set context, instance and activity
         */
        MyApplication.applicationActivity = this;
        instance = this;
        // Realm setup
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).setModules(new RealmModule())
                .build();
        Realm.setDefaultConfiguration(config);
        DBHelper_Place dbHelper_place = new DBHelper_Place();
        dbHelper_place.insertPlaceForBeginning();
        // Setup Compass

        displayView("main");

    }

    private void setupNavigationView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        FragmentManager fm = getFragmentManager();
        // Transaction start
        //final FragmentTransaction ft = fm.beginTransaction();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.menu_navigation:
                        displayView("main");
                    case R.id.menu_log_out:
                        SessionManager session = new SessionManager(getApplication());
                        session.logoutUser();
                        return true;
                    case R.id.menu_add_location:
                        displayView("add_place");
                        return true;
                    case R.id.menu_add_link:
                        displayView("create_link");
                        return true;
                    default:
                        return true;

                }
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView("main"); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
    }

    public void displayView(String tag) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (tag) {
            case "main":
                fragment = new MainFragment();
                viewIsAtHome = true;

                break;
            case "add_place":
                fragment = new FragmentAddPlace();
                title = "Add Place";
                viewIsAtHome = false;
                break;
            case "create_link":
                fragment = new FragmentCreateLink();
                title = "Create Link";
                viewIsAtHome = false;
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public static MainActivity getInstance() {

        return instance;

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateLocation(LatLng latlng) {
        double lat = latlng.getLatitude();
        double lng = latlng.getLongitude();

        Toast.makeText(this, "Location updated", Toast.LENGTH_SHORT).show();
        TextInputEditText editTextLatitude = (TextInputEditText) findViewById(R.id.editText_latitude);
        TextInputEditText editTextLongitude = (TextInputEditText) findViewById(R.id.editText_longitude);
        editTextLatitude.setText(String.valueOf(lat));
        editTextLongitude.setText(String.valueOf(lng));
    }





}
