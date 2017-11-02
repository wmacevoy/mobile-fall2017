package edu.coloradomesa.fb;

import com.google.firebase.database.IgnoreExtraProperties;

import edu.coloradomesa.mytutor.Util;

/**
 * Created by wmacevoy on 9/28/17.
 */
@IgnoreExtraProperties
public class Message implements Comparable <Message> {
    public String id;
    public String subject;
    public String body;

    public Message(String _id, String _username, String _password) {
        id=_id;
        subject =_username;
        body =_password;
    }

    public Message() {}
    @Override public int compareTo(Message to) {
        int cmp;
        cmp = Util.cmp(subject,to.subject);
        if (cmp != 0) return cmp;
        cmp = Util.cmp(body, to.body);
        if (cmp != 0) return cmp;
        if (id != null && to.id != null) {
            cmp = Util.cmp(id,to.id);
            if (cmp != 0) return cmp;
        }
        return 0;
    }
    @Override public boolean equals(Object o) {
        return compareTo((Message) o) == 0;
    }

    @Override public String toString() {
        return "user(id=" + id + ",subject="+ subject +",body=" + body + ")";
    }
}
