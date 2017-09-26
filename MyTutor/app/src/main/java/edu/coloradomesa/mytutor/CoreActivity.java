package edu.coloradomesa.mytutor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by wmacevoy on 9/14/17.
 */

public class CoreActivity extends AppCompatActivity implements AutoCloseable {
    public static final String TAG = "MyTutor";
    Model.Lazy mModel = new Model.Lazy(this);
    @Override public void close() { mModel.close(); }
    @Override public void onDestroy() {
        close();
        super.onDestroy();
    }

    Prefs prefs() { return mModel.self().prefs(); }
    LiteDB liteDB() { return mModel.self().liteDB(); }
    User user() { return mModel.self().user(); }
    Courses courses() { return mModel.self().courses(); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignIn();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    onSignOut();
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    void onSignIn() {

    }

    void onSignOut() {

    }
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

}
