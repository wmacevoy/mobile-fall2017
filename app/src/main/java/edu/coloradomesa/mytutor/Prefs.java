package edu.coloradomesa.mytutor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/7/17.
 */

public class Prefs implements AutoCloseable {
    public static final String PREFERENCES = "preferences";

    private Context mContext;
    Prefs(Context context) { mContext = context; }

    private SharedPreferences mPreferences = null;
    private SharedPreferences.Editor mEditor = null;

    private SharedPreferences preferences() {
        if (mPreferences == null) {
            synchronized(this) {
                if (mPreferences == null) {
                    mPreferences = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                }
            }
        }
        return mPreferences;
    }

    private SharedPreferences.Editor editor() {
        if (mEditor == null) {
            synchronized (this) {
                if (mEditor == null) {
                    mEditor = preferences().edit();
                }
            }
        }
        return mEditor;
    }

    public static final String AUTHENTICATED = "authenticated";
    public static final boolean AUTHENTICAED_DEFAULT = false;

    boolean authenticated() {
        return preferences().getBoolean(AUTHENTICATED,AUTHENTICAED_DEFAULT);
    }

    void authenticated(boolean value) {
        if (authenticated() != value) {
            editor().putBoolean(AUTHENTICATED, value);
        }
    }

    public static final String USER = "user";
    public static final String USER_DEFAULT = null;
    String user() {
        return preferences().getString(USER, USER_DEFAULT);
    }


    void user(String value) {
        if (!eq(user(),value)) {
            editor().putString(USER, value);
        }
    }

    public static final String GROUPS = "groups";
    public static final Set<String> GROUPS_DEFAULT = new HashSet<String>();
    Set<String> groups() {
        return preferences().getStringSet(GROUPS,GROUPS_DEFAULT);
    }

    void groups(Set<String> value) {
        if (!groups().equals(value)) {
            editor().putStringSet(GROUPS, value);
        }
    }

    void groups(String... values) {
        HashSet<String> valueAsSet = new HashSet<String>();
        for (String group : values) { valueAsSet.add(group); }
        groups(valueAsSet);
    }

    void cancel() {
        if (mEditor != null) {
            synchronized (this) {
                if (mEditor != null) {
                    mEditor = null;
                }
            }
        }

    }

    void save() {
        if (mEditor != null) {
            synchronized (this) {
                if (mEditor != null) {
                    mEditor.commit();
                    mEditor = null;
                }
            }
        }
    }

    @Override
    public void close() {
        save();
    }
}
