package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wmacevoy on 9/14/17.
 */

public class CoreActivity extends AppCompatActivity implements AutoCloseable {
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
}
