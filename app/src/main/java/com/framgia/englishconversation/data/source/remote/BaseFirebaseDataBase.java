package com.framgia.englishconversation.data.source.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by toand on 5/13/2017.
 */

public class BaseFirebaseDataBase {
    protected FirebaseDatabase mDatabase;
    protected DatabaseReference mReference;

    public BaseFirebaseDataBase(String reference) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference(reference);
    }
}
