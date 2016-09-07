package com.development.austin.insight.database;

import android.content.res.AssetManager;
import android.widget.Toast;

import com.development.austin.insight.MainActivity;
import com.development.austin.insight.MyApplication;
import com.development.austin.insight.entity.Place;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ARL on 8/31/2016.
 */
public class DBHelper_Place implements PlaceRepositoryInterface {
    /**
     * Import to local database (Realm)
     * added on 31/8/2016
     * by Austin
     * from CSV
     */
    public void insertPlaceForBeginning(){
        getAllPlaces(new OnGetAllPlacesCallback() {
            @Override
            public void onSuccess(RealmResults<Place> places) {

            }

            @Override
            public void onError(String message) {
                List<Place> medicineList = new ArrayList<>();
                AssetManager assetManager = MyApplication.applicationActivity.getAssets();
                Realm realm = Realm.getInstance(MyApplication.applicationActivity);
                try {
                    InputStream csvStream = assetManager.open("place.csv");
                    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                    CSVReader csvReader = new CSVReader(csvStreamReader);
                    String[] line;

                    // throw away the header
                    csvReader.readNext();

                    while ((line = csvReader.readNext()) != null) {
                        Place place = new Place(Integer.parseInt(line[0]),line[1],line[2],0,0,line[3],line[4],Integer.parseInt(line[5]),"",parseIntegerToBoolean(Integer.parseInt(line[7])),line[8],parseIntegerToBoolean(Integer.parseInt(line[9])),line[6]);
                        medicineList.add(place);
                        // Create the object

                        realm.beginTransaction();
                        Place place_2 = realm.copyToRealm(place);
                        realm.commitTransaction();
                        // Create Object in REALM

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Helper method
     * @param input(int)
     * @return boolean
     */

    private Boolean parseIntegerToBoolean(int input){
        Boolean output = false;
        if(input == 0){
            output = false;
        } else if (input == 1) {
            output = true;

        } else {
            output = false;
        }
        return output;
    }

    @Override
    public void addPlace(final Place place, final OnSavePlaceCallback callback) {
        getPlaceByPlaceId(place.getPlaceId(), new OnGetPlaceCallback() {
            @Override
            public void onSuccess(Place place) {
                Toast.makeText(MyApplication.applicationActivity,"Place with same id exists",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String message) {
                Realm realm = Realm.getInstance(MainActivity.getInstance());

                realm.beginTransaction();

                Place place_created = realm.createObject(Place.class);

                place_created = place;

                realm.commitTransaction();

                if (callback != null){
                    callback.onSuccess();
                }
            }
        });


    }

    @Override
    public void updatePlaceByPlaceId(Place place, final String placeId, OnSavePlaceCallback callback) {
        getPlaceByPlaceId(placeId, new OnGetPlaceCallback() {
            @Override
            public void onSuccess(Place place) {
                Realm realm = Realm.getInstance(MainActivity.getInstance());
                Place toEdit = realm.where(Place.class)
                        .equalTo("placeId", placeId).findFirst();
                realm.beginTransaction();
                toEdit = place;
                realm.commitTransaction();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MyApplication.applicationActivity,"Place with the place Id not exists",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void deletePlaceByPlaceId(String placeId, OnDeletePlaceCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        realm.beginTransaction();

        Place result = realm.where(Place.class).equalTo("placeId", placeId).findFirst();

        result.removeFromRealm();

        realm.commitTransaction();

        if (callback != null)

            callback.onSuccess();
    }


    @Override
    public void getAllPlaces(OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).findAll();

        if (callback != null)
            callback.onSuccess(results);
    }

    @Override
    public void getAllPlacesByFloor(int floor, OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).equalTo("floor", floor).findAll();


        if (callback != null)

            callback.onSuccess(results);
    }

    @Override
    public void getAllPlacesByBuilding(String building, OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).equalTo("building", building).findAll();

        if (callback != null)

            callback.onSuccess(results);
    }


    @Override
    public void getAllPlacesByBlock(String block, OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).equalTo("block", block).findAll();

        if (callback != null)

            callback.onSuccess(results);
    }

    @Override
    public void getAllToilet(OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).equalTo("isToilet", true).findAll();

        if (callback != null)

            callback.onSuccess(results);


    }

    @Override
    public void getAllExit(OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Place.class).equalTo("isExit", true).findAll();

        if (callback != null)

            callback.onSuccess(results);
    }

    @Override
    public void getPlaceByTag(String tag, OnGetPlaceStackCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());
        String [] taghint = tag.split(" ");
        RealmResults<Place> results = realm.where(Place.class).findAll();
        final Stack<Place> result = new Stack<>();
        Map<Integer,String> placeHashMap = new HashMap<>();
        int resSize = results.size();
        for(int i=0;i<taghint.length;i++){
            for(int j=0;j<resSize;j++){
                Place place = results.get(j);
                String tagging = place.getTag();
                String [] taggingList = tagging.split(";");
                int taggingSize = taggingList.length;

                int point = 0;
                for (int k=0;k<taggingSize;k++){
                    if(taggingList[k].equals(taghint[i])){
                        point = point+1;
                    }
                }
                placeHashMap.put(point,place.getPlaceId());
            }
        }
        Map<Integer, String> map = new TreeMap<>(placeHashMap);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            getPlaceByPlaceId(me2.getValue().toString(), new OnGetPlaceCallback() {
                @Override
                public void onSuccess(Place place) {
                    result.push(place);

                }

                @Override
                public void onError(String message) {

                }
            });
        }

        if(callback !=null){
            callback.onSuccess(result);
        }


    }

    @Override
    public void getPlaceByName(String name, OnGetAllPlacesCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults<Place> results = realm.where(Place.class).equalTo("placeName", name).findAll();

        if (callback != null)
            callback.onSuccess(results);
    }

    @Override
    public void getPlaceByPlaceId(String placeId, OnGetPlaceCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        Place place = realm.where(Place.class).equalTo("placeId", placeId).findFirst();

        if (callback != null)
            callback.onSuccess(place);
    }
}
