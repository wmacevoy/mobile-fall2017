package edu.coloradomesa.mytutor;

import android.content.Context;
import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/12/17.
 */

public class User {
    public static class Lazy extends edu.coloradomesa.mytutor.Lazy < User > {
        Lazy(Prefs.Lazy prefs) {
            super(User.class, prefs);
        }
    }

    Prefs.Lazy mPrefs;
    User(Prefs.Lazy prefs) {
        mPrefs=prefs;
    }

    Prefs prefs() { return mPrefs.self(); }

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

    boolean authenticated() { return prefs().authenticated(); }
    String user() { return prefs().user(); }
    boolean isUser() { return prefs().groups().contains("users"); }
    boolean isAdmin() { return prefs().groups().contains("admins"); }
}

