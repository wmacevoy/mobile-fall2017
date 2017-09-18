package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wmacevoy on 9/14/17.
 */

public class CoreActivity extends AppCompatActivity implements AutoCloseable {
    Model mModel = new Model(this);
    @Override public void close() { mModel.close(); }
    @Override public void onDestroy() {
        close();
        super.onDestroy();
    }

    Prefs prefs() { return mModel.mPrefs.self(); }
    LiteDB liteDB() { return mModel.mLiteDB.self(); }
    User user() { return mModel.mUser.self(); }
    Courses courses() { return mModel.mCourses.self(); }
}
