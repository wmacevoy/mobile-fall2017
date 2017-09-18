package edu.coloradomesa.mytutor;

import android.content.Context;

/**
 * Created by wmacevoy on 9/17/17.
 */

public class Model implements AutoCloseable {
    Context mContext;
    Context context() { return mContext; }
    Prefs.Lazy mPrefs;
    Prefs prefs() { return mPrefs.self(); }
    LiteDB.Lazy mLiteDB;
    LiteDB liteDB() { return mLiteDB.self(); }
    User.Lazy mUser;
    User user() { return mUser.self(); }
    Courses.Lazy mCourses;
    Courses courses() { return mCourses.self(); }

    Model(Context context) {
        mContext = context;
        mPrefs = new Prefs.Lazy(mContext);
        mLiteDB = new LiteDB.Lazy(mContext);
        mUser = new User.Lazy(mPrefs);
        mCourses = new Courses.Lazy(mLiteDB);
    }

    @Override
    public void close() {
        mLiteDB.close();
        mPrefs.close();
    }
}
