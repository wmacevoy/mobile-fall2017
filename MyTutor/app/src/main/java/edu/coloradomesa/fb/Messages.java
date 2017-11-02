package edu.coloradomesa.fb;

import java.util.ArrayList;

/**
 * Created by wmacevoy on 10/3/17.
 */

public class Messages {
    public static class Lazy extends edu.coloradomesa.mytutor.Lazy <Messages> {
        Lazy(Model.Lazy model) {
            super(Messages.class, model);
        }
    }
    private Model.Lazy mModel;
    public Model model() { return mModel.self(); }

    public Messages(Model.Lazy model) {
        mModel = model;
    }


    public static final int CACHE_TIMEOUT_MILLISECONDS = 1000;
    private long mAllTimeout = Long.MIN_VALUE;
    private ArrayList<Message> mAll = null;

    public ArrayList<Message> all() {
        if (System.currentTimeMillis() > mAllTimeout) {
            synchronized (this) {
                mAll = model().messages().all();
                mAllTimeout = System.currentTimeMillis() + CACHE_TIMEOUT_MILLISECONDS;
            }
        }
        return mAll;
    }

    public void clear() {
        synchronized (this) {
            model().messages().reset(true);
            if (mAll != null) mAll.clear();
        }
    }

    public void add(Message message) {
        if (message == null || message.subject == null || message.body == null) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
            Message anyIdMessage = new Message(null, message.subject, message.body);
            if (!all().contains(anyIdMessage)) {
                model().messages().insert(anyIdMessage);
                if (mAll != null) { mAll.add(anyIdMessage); }
            }
        }
    }

    public void add(String username, String password) {
        add(new Message(null, username, password));
    }

    public void remove(Message message) {
        synchronized (this) {
            model().messages().delete(message);
            if (mAll != null) { mAll.remove(message); }
        }
    }

    public void remove(String id) {
        synchronized (this) {
            model().messages().delete(id);
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
        remove(new Message(null, username, password));
    }

}
