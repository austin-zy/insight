package com.development.austin.insight.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.austin.insight.MyApplication;
import com.development.austin.insight.R;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


public class FragmentGetLatLng extends DialogFragment {
    private MapView mapView;
    private OnFragmentInteractionListener mListener;

    public FragmentGetLatLng() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_get_lat_lng, container, false);
        MapboxAccountManager.start(getActivity(), "pk.eyJ1IjoiZ3p5NTk2NSIsImEiOiJjaW13eHYwa2kwM2QxdXJrazQzdTBjMzgzIn0.-CJwMxJHPCcwHNPZxZEmTQ");

        mapView = (MapView)v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl("mapbox://styles/gzy5965/ciomke2s5000pdgm4bc71to9m");
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull final LatLng point) {
                        if(mListener!=null){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    MyApplication.applicationActivity);

                            // set title
                            alertDialogBuilder.setTitle("Insight Marker");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("Are you sure with the location?\nLat:"+point.getLatitude()+"\nLng:"+point.getLongitude())
                                    .setCancelable(false)
                                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            mListener.updateLocation(point);
                                            dismiss();
                                        }
                                    })
                                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                    }
                });
            }
        });
        return v;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void updateLocation(LatLng latLng);
    }
}
