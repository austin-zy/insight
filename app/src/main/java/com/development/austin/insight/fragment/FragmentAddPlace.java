package com.development.austin.insight.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.development.austin.insight.MyApplication;
import com.development.austin.insight.R;
import com.development.austin.insight.database.DBHelper_Place;
import com.development.austin.insight.database.PlaceRepositoryInterface;
import com.development.austin.insight.entity.Place;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class FragmentAddPlace extends Fragment implements FragmentGetLatLng.OnFragmentInteractionListener {

    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private AutoCompleteTextView editTextPlacename;
    private AutoCompleteTextView editTextPlaceid;
    private TextInputEditText editTextLatitude;
    private TextInputEditText editTextLongitude;
    private AutoCompleteTextView editTextBuilding;
    private AutoCompleteTextView editTextBlock;
    private AutoCompleteTextView editTextFloor;
    private EditText editTextDesc;
    private EditText editTextOwner;
    private ToggleButton toggleIsExit;
    private ToggleButton toggleIsToilet;
    private Button btnAddPlace;


    public FragmentAddPlace() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup Edit Text & Button


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);
        setupEditText(view);
        textViewLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentGetLatLng fragment = new FragmentGetLatLng();
                fragment.onAttach(getActivity());
                fragment.show(getFragmentManager(), "get_lat_lng");
            }
        });

        toggleIsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final List<String> placeIdList = new ArrayList<>();
        final List<Place> placeList = new ArrayList<>();
        final DBHelper_Place dbHelper_place = new DBHelper_Place();
        dbHelper_place.getAllPlaces(new PlaceRepositoryInterface.OnGetAllPlacesCallback() {
            @Override
            public void onSuccess(RealmResults<Place> places) {
                for (int i = 0; i < places.size(); i++) {
                    Place place = places.get(i);
                    placeIdList.add(place.getPlaceId());
                    placeList.add(place);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(MyApplication.applicationActivity, android.R.layout.simple_dropdown_item_1line, placeIdList);
        editTextPlaceid.setAdapter(arrayAdapter);
        editTextPlaceid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                int pos = placeIdList.indexOf(selected);
                Place place = placeList.get(pos);
                editTextPlacename.setText(place.getPlaceName());
                editTextBuilding.setText(place.getBuilding());
                editTextBlock.setText(place.getBlock());
                editTextFloor.setText(String.valueOf(place.getFloor()));
                editTextDesc.setText(place.getDesc());
                toggleIsExit.setChecked(place.getIsExit());
                toggleIsToilet.setChecked(place.getIsToilet());
            }
        });

        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeId = editTextPlaceid.getText().toString();
                String placeName = editTextPlacename.getText().toString();
                final double latitude = Double.parseDouble(editTextLatitude.getText().toString());
                final double longitude = Double.parseDouble(editTextLongitude.getText().toString());
                String building = editTextBuilding.getText().toString();
                String block = editTextBlock.getText().toString();
                int floor = Integer.parseInt(editTextFloor.getText().toString());
                String desc = editTextDesc.getText().toString();
                Boolean isExit = toggleIsExit.isChecked();
                Boolean isToilet = toggleIsExit.isChecked();
                String owner = editTextOwner.getText().toString();


                // Parse to object
                dbHelper_place.getPlaceByPlaceId(placeId, new PlaceRepositoryInterface.OnGetPlaceCallback() {
                    @Override
                    public void onSuccess(final Place place) {
                        place.setLatitude(latitude);
                        place.setLongitude(longitude);
                        dbHelper_place.updatePlaceByPlaceId(place, place.getPlaceId(), new PlaceRepositoryInterface.OnSavePlaceCallback() {
                            @Override
                            public void onSuccess() {
                                Snackbar.make(getView(), "Place is added successfully", Snackbar.LENGTH_SHORT).show();
                                // Firebase to save data to cloud
                                Firebase.setAndroidContext(getActivity());
                                Firebase ref = new Firebase("https://insight-16.firebaseio.com/");

                                Firebase placeRef = ref.child("places").child(place.getPlaceId());

                                placeRef.setValue(place, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {
                                            Toast.makeText(getActivity(), "Place addition contains error: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getActivity(), "Place is added successfully", Toast.LENGTH_SHORT).show();
                                            Fragment addPlaceFragment = new FragmentAddPlace();
                                            FragmentManager fm = getFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            ft.replace(R.id.fragment_container, addPlaceFragment);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });

                    }

                    @Override
                    public void onError(String message) {

                    }
                });

            }
        });


        return view;
    }

    private void setupEditText(View v) {
        textViewLatitude = (TextView) v.findViewById(R.id.textView_latitude);
        textViewLongitude = (TextView) v.findViewById(R.id.textView_longitude);
        editTextPlacename = (AutoCompleteTextView) v.findViewById(R.id.editText_placename);
        editTextPlaceid = (AutoCompleteTextView) v.findViewById(R.id.editText_placeid);
        editTextLatitude = (TextInputEditText) v.findViewById(R.id.editText_latitude);
        editTextLongitude = (TextInputEditText) v.findViewById(R.id.editText_longitude);
        editTextBuilding = (AutoCompleteTextView) v.findViewById(R.id.editText_building);
        editTextBlock = (AutoCompleteTextView) v.findViewById(R.id.editText_block);
        editTextFloor = (AutoCompleteTextView) v.findViewById(R.id.editText_floor);
        editTextDesc = (EditText) v.findViewById(R.id.editText_desc);
        editTextOwner = (EditText) v.findViewById(R.id.editText_owner);
        toggleIsExit = (ToggleButton) v.findViewById(R.id.toggle_isExit);
        toggleIsToilet = (ToggleButton) v.findViewById(R.id.toggle_isToilet);
        btnAddPlace = (Button) v.findViewById(R.id.btn_addPlace);
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-08-31 15:55:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

    }

    @Override
    public void updateLocation(LatLng latlng) {
        double lat = latlng.getLatitude();
        double lng = latlng.getLongitude();
        Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();
        editTextLatitude.setText(String.valueOf(lat));
        editTextLatitude.setText(String.valueOf(lng));
    }


}
