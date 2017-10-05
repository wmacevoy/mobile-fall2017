package edu.coloradomesa.fb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import edu.coloradomesa.mytutor.R;

import static edu.coloradomesa.mytutor.Util.i;

public class TestFirebaseActivity extends edu.coloradomesa.mytutor.CoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TestFirebase.activity = this;
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(TestFirebase.class);
        i("TestFirebase: ran " + result.getRunCount()
                + " tests with " + result.getFailureCount() + " failed "
                + " and " + result.getIgnoreCount() + " ignored");
    }
}
