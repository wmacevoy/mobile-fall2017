package edu.coloradomesa.fb;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.coloradomesa.mytutor.*;

import static org.junit.Assert.*;
import static edu.coloradomesa.mytutor.Util.*;

/**
 * Created by wmacevoy on 9/28/17.
 */

// @RunWith(AndroidJUnit4.class)
public class TestFirebase  {
    public static TestFirebaseActivity activity;

    Model.Lazy mLazyModel = new Model.Lazy();
    Model model() {
        return mLazyModel.self();
    }

    @Test public  void testHello() {
        i("testHello");
        // valid users must be readable as an unathenticated user for this to work (firebase->db console->database [rules tab]
        DatabaseReference mDatabase;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World! date=" + new Date());
    }

    public void logout() {
        activity.mAuth.signOut();
    }
    public void loginAsTestUser() {
        FirebaseUser currentUser = activity.mAuth.getCurrentUser();
        final Object barrier = new Object();

        if (currentUser == null || !currentUser.getEmail().equals(activity.getString(R.string.test_firebase_user))) {
            if (currentUser != null) logout();
            i("here");
            activity.mAuth.signInWithEmailAndPassword(activity.getString(R.string.test_firebase_user), activity.getString(R.string.test_firebase_password))
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                i("signInWithEmail:success");
                            } else {
                                // If sign in fails, display a message to the user.
                                i("signInWithEmail:failure " + task.getException());
                            }
                            synchronized (barrier) { barrier.notifyAll(); }
                        }

                    });

        }

        synchronized (barrier) {
            try {
                barrier.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        currentUser = activity.mAuth.getCurrentUser();
        i("logged in as " + currentUser.getEmail());
        assertEquals(activity.getString(R.string.test_firebase_user), currentUser.getEmail());

    }

    @Test
    public void testUsersEmpty() {
        i("testUserEmpty");
        model().users().reset();
        Collection<User> users = model().users().all();
        assertEquals(true, users.isEmpty());
    }

    @Test
    public void testUsersInsert() {
        i("testUserInsert");
        model().users().reset();
        model().users().insert(new User(null,"alice","a"));
        model().users().insert(new User(null,"bob","b"));
        model().users().insert(new User(null,"cindy","c"));
        Collection<User> users = model().users().all();
        assertEquals(true, users.contains(new User(null, "alice","a")));
        assertEquals(true, users.contains(new User(null, "bob","b")));
        assertEquals(true, users.contains(new User(null, "cindy","c")));
    }


    @Test
    public void testUsersDeleteByUsername() {
        i("testUserDeleteByUsername");
        model().users().reset();
        model().users().insert(new User(null,"alice","a"));
        model().users().insert(new User(null,"bob","b"));
        model().users().insert(new User(null,"cindy","c"));
        model().users().delete(new User(null,"bob",null));
        Collection<User> users = model().users().all();
        assertEquals(true, users.contains(new User(null, "alice","a")));
        assertEquals(false, users.contains(new User(null, "bob","b")));
        assertEquals(true, users.contains(new User(null, "cindy","c")));
    }

    @Test
    public void testCoursesDeleteById() {
        i("testCourseDeleteById");
        model().users().reset();
        model().users().insert(new User(null,"alice","a"));
        model().users().insert(new User(null,"bob","b"));
        model().users().insert(new User(null,"cindy","c"));

        for (User user : model().users().all()) {
            if (user.username.equals("bob") == false) {
                model().users().delete(user.id);
            }
        }

        Collection<User> users = model().users().all();
        assertEquals(false, users.contains(new User(null, "alice","a")));
        assertEquals(true, users.contains(new User(null, "bob","b")));
        assertEquals(false, users.contains(new User(null, "cindy","c")));
    }

    @Test public void testStorage() {
        i("testStorage");
        loginAsTestUser();
        File dir = new File(activity.getApplicationInfo().dataDir);
        File beepFile = new File(dir, "beep.m4a");
        Uri file = Uri.fromFile(beepFile);
        StorageReference beepRef = activity.mStorageRef.child("sounds/beep.m4a");

        beepRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    @Rule
    public TestRule watchman = new TestWatcher() {

        @Override public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override protected void succeeded(Description description) {
            i(description.getDisplayName() + " " + "passed");
        }

        @Override protected void failed(Throwable e, Description description) {
            w(description.getDisplayName() + " " + "failed");
        }
    };

}
