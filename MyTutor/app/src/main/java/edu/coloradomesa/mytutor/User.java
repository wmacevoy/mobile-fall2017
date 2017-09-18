package edu.coloradomesa.mytutor;

import android.content.Context;
import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/12/17.
 */

public class User {
    Prefs.Lazy mPrefs;
    Prefs prefs() { return mPrefs.self(); }

    User(Prefs.Lazy prefs) {
        mPrefs=prefs;
    }

    public static class Lazy extends edu.coloradomesa.mytutor.Lazy < User > {
        Prefs.Lazy mPrefs;
        Lazy(Prefs.Lazy prefs) {
            mPrefs = prefs;
        }
        User create() { return new User(mPrefs); }
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
                prefs().authenticated(true);
                prefs().user(user);
                prefs().groups("users");
                prefs().save();
                break;
            case "admin@localhost":
                prefs().authenticated(true);
                prefs().user("admin@localhost");
                prefs().groups("admins","users");
                prefs().save();
                break;
            default:
                logout();
        }
        return prefs().authenticated();
    }

    void logout() {
        prefs().authenticated(false);
        prefs().user(null);
        prefs().groups(new String[] {});
        prefs().save();
    }

    boolean authenticated() { return mPrefs.self().authenticated(); }
    String user() { return mPrefs.self().user(); }
    boolean isUser() { return mPrefs.self().groups().contains("users"); }
    boolean isAdmin() { return mPrefs.self().groups().contains("admins"); }
}

