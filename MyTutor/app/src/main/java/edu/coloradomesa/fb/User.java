package edu.coloradomesa.fb;

import com.google.firebase.database.IgnoreExtraProperties;

import edu.coloradomesa.mytutor.Util;

/**
 * Created by wmacevoy on 9/28/17.
 */
@IgnoreExtraProperties
public class User implements Comparable <User> {
    public String id;
    public String username;
    public String password;

    public User(String _id, String _username, String _password) {
        id=_id;
        username=_username;
        password=_password;
    }

    public User() {}
    @Override public int compareTo(User to) {
        int cmp;
        cmp = Util.cmp(username,to.username);
        if (cmp != 0) return cmp;
        cmp = Util.cmp(password, to.password);
        if (cmp != 0) return cmp;
        if (id != null && to.id != null) {
            cmp = Util.cmp(id,to.id);
            if (cmp != 0) return cmp;
        }
        return 0;
    }
    @Override public boolean equals(Object o) {
        return compareTo((User) o) == 0;
    }

    @Override public String toString() {
        return "user(id=" + id + ",username="+username+",password=" + password + ")";
    }
}
