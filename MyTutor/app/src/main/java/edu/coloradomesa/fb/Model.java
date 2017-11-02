package edu.coloradomesa.fb;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.coloradomesa.mytutor.CoreActivity;

/**
 * Created by wmacevoy on 10/3/17.
 */

public class Model {
    public static class Lazy extends edu.coloradomesa.mytutor.Lazy < Model > {
        public Lazy() { super(Model.class); }
    }

    private edu.coloradomesa.mytutor.Lazy< FirebaseAuth > mLazyAuth =
            new edu.coloradomesa.mytutor.Lazy < FirebaseAuth > () {
      @Override public FirebaseAuth create() {
            return FirebaseAuth.getInstance();
        }
    };
    public FirebaseAuth auth() { return mLazyAuth.self(); }

    private edu.coloradomesa.mytutor.Lazy < FirebaseDatabase > mLazyDatabase =
            new edu.coloradomesa.mytutor.Lazy <FirebaseDatabase> () {
        @Override public FirebaseDatabase create() {
            return FirebaseDatabase.getInstance();
        }
    };
    public FirebaseDatabase database() { return mLazyDatabase.self(); }
    public DatabaseReference reference() { return database().getReference(); }

    private edu.coloradomesa.mytutor.Lazy <Messages> mLazyMessages =
            new edu.coloradomesa.mytutor.Lazy<Messages>(Messages.class);

    public Messages messages() { return mLazyMessages.self(); }

    interface Act { void action(); }
    DataSnapshot complete(DatabaseReference ref, Act act, int msTimeout) {
        final DataSnapshot [] snap = new DataSnapshot[1];
        final Object barrier = new Object();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snap[0]=dataSnapshot;
                synchronized (barrier) { barrier.notify(); }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                synchronized (barrier) { barrier.notify(); }
            }
        });
        act.action();
        synchronized (barrier) {
            try {
                barrier.wait(msTimeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return snap[0];
    }

    public static final int DEFAULT_TIMEOUT = 4000;

    DataSnapshot complete(DatabaseReference ref, Act act) {
        return complete(ref,act,DEFAULT_TIMEOUT);
    }

    public class Messages {
        DatabaseReference ref() { return reference().child("messages"); }
        public DataSnapshot complete(Act act, boolean blocking) {
            if (blocking) {
                return Model.this.complete(ref(),act);
            } else {
                act.action();
                return null;
            }
        }

        public void create(boolean blocking) {
            complete(new Act() {
                @Override
                public void action() {
                    ref().setValue(new Object());
                }
            }, blocking);
        }

        public void drop(boolean blocking) {
            complete(new Act() {
                @Override public void action() {  ref().removeValue(); }
            }, blocking);
        }

        public void reset(boolean blocking) {
            drop(blocking);
            create(blocking);
        }

        public void insert(Message message) {
            DatabaseReference pushed = ref().push();
            message.id = pushed.getKey();
            pushed.setValue(message);
        }

        public void delete(Message message)  {
            if (message.id == null) {
                if (message.subject == null) {
                    throw new IllegalStateException();
                }
                Query query = ref().orderByChild("subject").equalTo(message.subject);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot items) {
                        for (DataSnapshot item : items.getChildren()) {
                            item.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(CoreActivity.TAG, "onCancelled", databaseError.toException());
                    }
                });
            } else {
                delete(message.id);
            }
        }

        public void delete(String id) {
            ref().child(id).removeValue();
        }

        public ArrayList<Message> all() {
            final ArrayList<Message> ans = new ArrayList<Message>();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot items) {
                    for (DataSnapshot item : items.getChildren()) {
                        Message message = item.getValue(Message.class);
                        ans.add(message);
                    }
                    synchronized (ans) {
                        ans.notify();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(CoreActivity.TAG, "loadPost:onCancelled", databaseError.toException());
                    ans.notify();
                }
            };
            ref().addValueEventListener(listener);
            synchronized (ans) {
                try {
                    ans.wait();
                } catch (InterruptedException e) {
                    Log.w(CoreActivity.TAG, "interrupted");
                }
            }
            return ans;
        }
    }
}
