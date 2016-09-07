package com.development.austin.insight.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.development.austin.insight.MyApplication;
import com.development.austin.insight.R;
import com.development.austin.insight.database.DBHelper_Link;
import com.development.austin.insight.database.DBHelper_Place;
import com.development.austin.insight.database.LinkRepositoryInterface;
import com.development.austin.insight.database.PlaceRepositoryInterface;
import com.development.austin.insight.entity.Link;
import com.development.austin.insight.entity.Place;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateLink extends Fragment {
    private TextView textviewRef1;
    private AutoCompleteTextView editTextRef1;
    private TextView textViewP1;
    private AutoCompleteTextView editTextP1;
    private TextView textViewDistance;
    private Button btnCreate;

    public FragmentCreateLink() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_create_link, container, false);
        findViews(v);
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
                Toast.makeText(MyApplication.applicationActivity,message,Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(MyApplication.applicationActivity, android.R.layout.simple_dropdown_item_1line, placeIdList);
        editTextRef1.setAdapter(arrayAdapter);
        editTextP1.setAdapter(arrayAdapter);

        editTextP1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(editTextRef1.getText().toString().equals("")){
                    Toast.makeText(MyApplication.applicationActivity,"Please select your reference point first",Toast.LENGTH_SHORT).show();
                    editTextP1.setText("");
                    textViewDistance.setVisibility(View.GONE);
                } else {
                    // get reference place
                    String placeId_ref = editTextRef1.getText().toString();
                    int pos_ref = placeIdList.indexOf(placeId_ref);
                    Place placeref = placeList.get(pos_ref);
                    // Get p1 object
                    String placeId_p1 = (String) parent.getItemAtPosition(position);
                    int pos_p1 = placeIdList.indexOf(placeId_p1);
                    Place placep1 = placeList.get(pos_p1);

                    float distance = distance(placeref.getLatitude(),placeref.getLongitude(),placep1.getLatitude(),placep1.getLongitude());

                    textViewDistance.setText(String.valueOf(distance)+" m");
                    textViewDistance.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeId_ref = editTextRef1.getText().toString();
                String placeId_p1 = editTextP1.getText().toString();

                if(!placeId_p1.equals("")&&!placeId_ref.equals("")){
                    int pos_ref = placeIdList.indexOf(placeId_ref);
                    int pos_p1 = placeIdList.indexOf(placeId_p1);
                    Place placep1 = placeList.get(pos_p1);
                    Place placeref = placeList.get(pos_ref);
                    float distance = distance(placeref.getLatitude(),placeref.getLongitude(),placep1.getLatitude(),placep1.getLongitude());

                    Link link = new Link(placeId_ref,placeId_p1,distance);
                    DBHelper_Link dbHelper_link = new DBHelper_Link();
                    dbHelper_link.addLink(link, new LinkRepositoryInterface.OnSaveLinkCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MyApplication.applicationActivity,"Link Added Successfully",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(MyApplication.applicationActivity,"Error: "+message,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return v;
    }



    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-09-03 17:20:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View v) {
        textviewRef1 = (TextView)v.findViewById( R.id.textview_ref1 );
        editTextRef1 = (AutoCompleteTextView)v.findViewById( R.id.editText_ref1 );
        textViewP1 = (TextView)v.findViewById( R.id.textView_p1 );
        editTextP1 = (AutoCompleteTextView)v.findViewById( R.id.editText_p1 );
        textViewDistance = (TextView)v.findViewById( R.id.textView_distance );
        btnCreate = (Button)v.findViewById( R.id.btn_create );

    }

    public float distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }


}
