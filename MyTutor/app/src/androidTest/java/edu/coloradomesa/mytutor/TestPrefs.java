package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestPrefs {
    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testPrefsAuthenticated() {
        try (Prefs prefs = new Prefs(context)) {
            prefs.authenticated(false);
        }

        try (Prefs prefs = new Prefs(context)) {
            assertEquals(false,prefs.authenticated());
        }

        try (Prefs prefs = new Prefs(context)) {
            prefs.authenticated(true);
        }

        try (Prefs prefs = new Prefs(context)) {
            assertEquals(true,prefs.authenticated());
        }

        try (Prefs prefs = new Prefs(context)) {
            prefs.authenticated(false);
        }
    }

    @Test
    public void testPrefsUser() {
        try (Prefs prefs = new Prefs(context)) {
            prefs.user(null);
        }

        try (Prefs prefs = new Prefs(context)) {
            assertEquals(null,prefs.user());
        }


        try (Prefs prefs = new Prefs(context)) {
            prefs.user("authenticated-user");
        }

        try (Prefs prefs = new Prefs(context)) {
            assertEquals("authenticated-user",prefs.user());
        }

        try (Prefs prefs = new Prefs(context)) {
            prefs.user(null);
        }
    }

    @Test public void testPrefsSimulEdit() {
        try (Prefs p1 = new Prefs(context)) {
            p1.authenticated(true);
            p1.save();
            try (Prefs p2 = new Prefs(context)) {
                p2.authenticated(false);
                p2.save();
            }
            assertEquals(false,p1.authenticated());
        }
    }

}
