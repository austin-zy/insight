package com.development.austin.insight.database;

import com.development.austin.insight.MainActivity;
import com.development.austin.insight.entity.Link;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ARL on 9/3/2016.
 */
public class DBHelper_Link implements LinkRepositoryInterface {


    @Override
    public void addLink(Link link, OnSaveLinkCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        realm.beginTransaction();

        Link link_created = realm.createObject(Link.class);

        link_created = link;

        realm.commitTransaction();

        if (callback != null){
            callback.onSuccess();
        }
    }

    @Override
    public void updateLinkByLinkId(Link link, String linkId, OnSaveLinkCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());
        Link toEdit = realm.where(Link.class)
                .equalTo("linkId", linkId).findFirst();
        realm.beginTransaction();
        toEdit = link;
        realm.commitTransaction();
    }

    @Override
    public void deleteLinkByLinkId(String linkId, OnDeleteLinkCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        realm.beginTransaction();

        Link result = realm.where(Link.class).equalTo("linkId", linkId).findFirst();

        result.removeFromRealm();

        realm.commitTransaction();

        if (callback != null)

            callback.onSuccess();
    }

    @Override
    public void getAllLinks(OnGetAllLinksCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        RealmResults results = realm.where(Link.class).findAll();

        if (callback != null)
            callback.onSuccess(results);
    }

    @Override
    public void getLinkByLinkId(String linkId, OnGetLinkCallback callback) {
        Realm realm = Realm.getInstance(MainActivity.getInstance());

        Link link = realm.where(Link.class).equalTo("linkId", linkId).findFirst();

        if (callback != null)
            callback.onSuccess(link);
    }
}
