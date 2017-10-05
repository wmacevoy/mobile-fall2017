package edu.coloradomesa.fb;

import android.content.Context;
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
import edu.coloradomesa.mytutor.Lazy;

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

    private edu.coloradomesa.mytutor.Lazy < Users > mLazyUsers =
            new edu.coloradomesa.mytutor.Lazy< Users >(Users.class);

    public Users users() { return mLazyUsers.self(); }

    public class Users {
        DatabaseReference ref() { return reference().child("users"); }

        public void create() {
            ref().setValue(new Object());
        }

        public void drop() {
            ref().removeValue();
        }

        public void reset() {
            drop();
            create();
        }

        public void insert(User user) {
            DatabaseReference pushed = ref().push();
            user.id = pushed.getKey();
            pushed.setValue(user);
        }

        public void delete(User user)  {
            if (user.id == null) {
                if (user.username == null) {
                    throw new IllegalStateException();
                }
                Query query = ref().orderByChild("username").equalTo(user.username);

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
                delete(user.id);
            }
        }

        public void delete(String id) {
            ref().child(id).removeValue();
        }

        public ArrayList<User> all() {
            final ArrayList<User> ans = new ArrayList<User>();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot items) {
                    for (DataSnapshot item : items.getChildren()) {
                        User user = item.getValue(User.class);
                        ans.add(user);
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
