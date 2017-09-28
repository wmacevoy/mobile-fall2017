package edu.coloradomesa.db;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by wmacevoy on 9/28/17.
 */
@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    public User(String _username, String _password) {
        username=_username;
        password=_password;
    }
    public User() {}
}
