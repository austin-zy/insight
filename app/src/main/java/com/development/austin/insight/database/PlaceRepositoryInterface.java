package com.development.austin.insight.database;

import com.development.austin.insight.entity.Place;

import java.util.Stack;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ARL on 8/31/2016.
 */
public interface PlaceRepositoryInterface {

    interface OnSavePlaceCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnDeletePlaceCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetPlaceCallback {
        void onSuccess(Place place);
        void onError(String message);
    }

    interface OnGetAllPlacesCallback {
        void onSuccess(RealmResults<Place> places);
        void onError(String message);
    }

    interface OnGetPlacesCallback{
        void onSuccess(RealmList<Place> places);
        void onError(String message);
    }

    interface OnGetPlaceStackCallback{
        void onSuccess(Stack<Place> places);
        void onError(String message);
    }


    void addPlace(Place place, OnSavePlaceCallback callback);

    void updatePlaceByPlaceId(Place place, String placeId, OnSavePlaceCallback callback);

    void deletePlaceByPlaceId(String placeId, OnDeletePlaceCallback callback);

    void getAllPlaces(OnGetAllPlacesCallback callback);

    void getAllPlacesByFloor(int floor, OnGetAllPlacesCallback callback);

    void getAllPlacesByBuilding(String building, OnGetAllPlacesCallback callback);

    void getAllPlacesByBlock(String block, OnGetAllPlacesCallback callback);

    void getAllToilet(OnGetAllPlacesCallback callback);

    void getAllExit(OnGetAllPlacesCallback callback);

    void getPlaceByTag(String tag, OnGetPlaceStackCallback callback);

    void getPlaceByName(String name, OnGetAllPlacesCallback callback);

    void getPlaceByPlaceId(String placeId, OnGetPlaceCallback callback);
}
