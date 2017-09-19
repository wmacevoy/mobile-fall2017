package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestUser {
    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testUserAuthenticate() {
        try (Prefs.Lazy prefs = new Prefs.Lazy(context)) {
            User user = new User(prefs);
            assertEquals(true, user.authenticate("foo@example.com", "hello"));
            assertEquals(true, user.authenticate("bar@example.com", "world"));
            assertEquals(true, user.authenticate("admin@localhost", "secret"));
            assertEquals(false, user.authenticate("baz@example.com", "boo"));
        }
    }

    @Test
    public void testUserLogin() {
        try (Prefs.Lazy prefs = new Prefs.Lazy(context)) {
            User user = new User(prefs);
            assertEquals(true, user.login("foo@example.com"));
            assertEquals("foo@example.com",user.user());
            assertEquals(true, user.authenticated());
            assertEquals(true, user.isUser());
            assertEquals(false, user.isAdmin());

            assertEquals(true, user.login("bar@example.com"));
            assertEquals("bar@example.com",user.user());
            assertEquals(true, user.authenticated());
            assertEquals(true, user.isUser());
            assertEquals(false, user.isAdmin());

            assertEquals(true, user.login("admin@localhost"));
            assertEquals("admin@localhost",user.user());
            assertEquals(true, user.authenticated());
            assertEquals(true, user.isUser());
            assertEquals(true, user.isAdmin());

            assertEquals(false, user.login("baz@example.com"));
            assertEquals(null, user.user());
            assertEquals(false, user.authenticated());
            assertEquals(false, user.isUser());
            assertEquals(false, user.isAdmin());
        }
    }

    @Test
    public void testUserLogout() {
        try (Prefs.Lazy prefs = new Prefs.Lazy(context)) {
            User user = new User(prefs);
            user.login("admin");
            user.logout();

            assertEquals(false, user.authenticated());
            assertEquals(false, user.isUser());
            assertEquals(false, user.isAdmin());
        }
    }

}
