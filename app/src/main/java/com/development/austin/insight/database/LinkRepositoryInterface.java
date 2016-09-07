package com.development.austin.insight.database;

import com.development.austin.insight.entity.Link;

import java.util.Stack;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ARL on 9/3/2016.
 */
public interface LinkRepositoryInterface {
    interface OnSaveLinkCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnDeleteLinkCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetLinkCallback {
        void onSuccess(Link link);
        void onError(String message);
    }

    interface OnGetAllLinksCallback {
        void onSuccess(RealmResults<Link> links);
        void onError(String message);
    }

    interface OnGetLinksCallback{
        void onSuccess(RealmList<Link> links);
        void onError(String message);
    }

    interface OnGetLinkStackCallback{
        void onSuccess(Stack<Link> links);
        void onError(String message);
    }


    void addLink(Link link, OnSaveLinkCallback callback);

    void updateLinkByLinkId(Link link, String linkId, OnSaveLinkCallback callback);

    void deleteLinkByLinkId(String linkId, OnDeleteLinkCallback callback);

    void getAllLinks(OnGetAllLinksCallback callback);

    void getLinkByLinkId(String linkId, OnGetLinkCallback callback);
}
