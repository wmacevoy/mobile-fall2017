package edu.coloradomesa.fb;

import java.util.ArrayList;

import edu.coloradomesa.mytutor.Course;
import edu.coloradomesa.mytutor.Courses;
import edu.coloradomesa.mytutor.LiteDB;

/**
 * Created by wmacevoy on 10/3/17.
 */

public class Users {
    public static class Lazy extends edu.coloradomesa.mytutor.Lazy <Users> {
        Lazy(Model.Lazy model) {
            super(Users.class, model);
        }
    }
    private Model.Lazy mModel;
    public Model model() { return mModel.self(); }

    public Users(Model.Lazy model) {
        mModel = model;
    }


    public static final int CACHE_TIMEOUT_MILLISECONDS = 1000;
    private long mAllTimeout = Long.MIN_VALUE;
    private ArrayList<User> mAll = null;

    public ArrayList<User> all() {
        if (System.currentTimeMillis() > mAllTimeout) {
            synchronized (this) {
                mAll = model().users().all();
                mAllTimeout = System.currentTimeMillis() + CACHE_TIMEOUT_MILLISECONDS;
            }
        }
        return mAll;
    }

    public void clear() {
        synchronized (this) {
            model().users().reset();
            if (mAll != null) mAll.clear();
        }
    }

    public void add(User user) {
        if (user == null || user.username == null || user.password == null) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
            User anyIdUser = new User(null, user.username, user.password);
            if (!all().contains(anyIdUser)) {
                model().users().insert(anyIdUser);
                if (mAll != null) { mAll.add(anyIdUser); }
            }
        }
    }

    public void add(String username, String password) {
        add(new User(null, username, password));
    }

    public void remove(User user) {
        synchronized (this) {
            model().users().delete(user);
            if (mAll != null) { mAll.remove(user); }
        }
    }

    public void remove(String id) {
        synchronized (this) {
            model().users().delete(id);
            if (mAll != null) {
                for (int i = 0; i < mAll.size(); ++i) {
                    if (mAll.get(i).id == id) {
                        mAll.remove(i);
                        return;
                    }
                }
            }
        }
    }

    public void remove(String username, String password) {
        remove(new User(null, username, password));
    }

}
