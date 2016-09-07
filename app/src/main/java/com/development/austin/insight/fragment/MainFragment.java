package com.development.austin.insight.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.development.austin.insight.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


public class MainFragment extends Fragment {

    FloatingActionButton fab;
    SearchView searchView;
    TextView resultText;
    MapboxMap mapboxMap;
    private MapView mapView;
    // Sensor
    private int mAzimuth = 0; // degree
    private SensorManager mSensorManager = null;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        final FloatingActionButton fab_scene = (FloatingActionButton) view.findViewById(R.id.action_a);
        final FloatingActionButton fab_qr_code = (FloatingActionButton) view.findViewById(R.id.action_b);
        final FloatingActionButton fab_wifi = (FloatingActionButton) view.findViewById(R.id.action_c);

        setupSearchView(view);
        setupTextView(view);


        Firebase.setAndroidContext(getActivity());
        final Firebase ref = new Firebase("https://insight-16.firebaseio.com/places");

        MapboxAccountManager.start(getActivity(), "pk.eyJ1IjoiZ3p5NTk2NSIsImEiOiJjaW13eHYwa2kwM2QxdXJrazQzdTBjMzgzIn0.-CJwMxJHPCcwHNPZxZEmTQ");

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl("mapbox://styles/gzy5965/ciomke2s5000pdgm4bc71to9m");
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                fab_qr_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(getActivity());
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Locate Nearest QR Code");
                        integrator.setCameraId(0);  // Use a specific camera of the device
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(true);
                        integrator.initiateScan();
                        //mapboxMap.addMarker()
                        Query queryRef = ref.orderByChild("placeId").equalTo(resultText.getText().toString());
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                System.out.println(snapshot.child("latitude").getValue().toString());
                                System.out.println(snapshot.child("longitude").getValue().toString());

                                // Create an Icon object for the marker to use
                                IconFactory iconFactory = IconFactory.getInstance(getActivity());
                                Drawable iconDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.avatar);
                                Icon icon = iconFactory.fromDrawable(iconDrawable);

                                if (mapboxMap.getMarkers().isEmpty()) {
                                    mapboxMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(snapshot.child("latitude").getValue().toString()), Double.parseDouble(snapshot.child("longitude").getValue().toString())))
                                            .icon(icon));
                                } else {
                                    mapboxMap.removeAnnotations();
                                    mapboxMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(snapshot.child("latitude").getValue().toString()), Double.parseDouble(snapshot.child("longitude").getValue().toString())))
                                            .icon(icon));
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Choose View");
                builder.setMessage("Do you want to view it on map or augmented reality?");
                // Back to the same activity
                builder.setPositiveButton("Map", null);
                // Go to AR Activity
                builder.setNegativeButton("Augmented Reality", null);
                builder.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupSearchView(View view) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.search_bar);

    }

    private void setupTextView(View view) {
        resultText = (TextView) view.findViewById(R.id.resultText);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            resultText.setText(scanContent);
            Toast.makeText(getActivity(), resultText.getText().toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float[] orientation = new float[3];
        float[] rMat = new float[9];

        public void onAccuracyChanged(Sensor sensor, int accuracy ) {}

        @Override
        public void onSensorChanged( SensorEvent event ) {
            if( event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR ){
                // calculate th rotation matrix
                SensorManager.getRotationMatrixFromVector( rMat, event.values );
                // get the azimuth value (orientation[0]) in degree
                mAzimuth = (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
                float rotation = mAzimuth*-1;
                mapView.setRotation(rotation);
            }
        }
    };
}
