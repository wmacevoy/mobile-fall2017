package edu.coloradomesa.mytutor;

import android.content.Context;
import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/12/17.
 */

public class User implements AutoCloseable {
    Prefs mPrefs;

    @Override public void close() {
        mPrefs.close();
    }

    User(Context context) {
        mPrefs=new Prefs(context);
    }

    boolean exists(String user) {
        return eq(user, "foo@example.com") || eq(user,"bar@example.com") || eq(user,"admin@localhost");
    }

    boolean authenticate(String user, String password) {
        switch(user) {
            case "foo@example.com": return eq("hello",password);
            case "bar@example.com": return eq("world",password);
            case "admin@localhost": return eq("secret", password);
        }
        return false;
    }

    boolean login(String user) {
        switch(user) {
            case "foo@example.com":
            case "bar@example.com":
                mPrefs.authenticated(true);
                mPrefs.user(user);
                mPrefs.groups("users");
                mPrefs.save();
                break;
            case "admin@localhost":
                mPrefs.authenticated(true);
                mPrefs.user("admin@localhost");
                mPrefs.groups("admins","users");
                mPrefs.save();
                break;
            default:
                logout();
        }
        return mPrefs.authenticated();
    }

    void logout() {
        mPrefs.authenticated(false);
        mPrefs.user(null);
        mPrefs.groups(new String[] {});
        mPrefs.save();
    }

    boolean authenticated() { return mPrefs.authenticated(); }
    String user() { return mPrefs.user(); }
    boolean isUser() { return mPrefs.groups().contains("users"); }
    boolean isAdmin() { return mPrefs.groups().contains("admins"); }
}

