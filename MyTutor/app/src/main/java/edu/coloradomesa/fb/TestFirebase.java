package edu.coloradomesa.fb;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.Collection;
import java.util.Date;

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
        // valid messages must be readable as an unathenticated user for this to work (firebase->db console->database [rules tab]
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
        model().messages().reset(false);
        Collection<Message> messages = model().messages().all();
        assertEquals(true, messages.isEmpty());
    }

    @Test
    public void testUsersInsert() {
        i("testUserInsert");
        model().messages().reset(false);
        model().messages().insert(new Message(null,"alice","a"));
        model().messages().insert(new Message(null,"bob","b"));
        model().messages().insert(new Message(null,"cindy","c"));
        Collection<Message> messages = model().messages().all();
        assertEquals(true, messages.contains(new Message(null, "alice","a")));
        assertEquals(true, messages.contains(new Message(null, "bob","b")));
        assertEquals(true, messages.contains(new Message(null, "cindy","c")));
    }


    @Test
    public void testUsersDeleteByUsername() {
        i("testUserDeleteByUsername");
        model().messages().reset(false);
        model().messages().insert(new Message(null,"alice","a"));
        model().messages().insert(new Message(null,"bob","b"));
        model().messages().insert(new Message(null,"cindy","c"));
        model().messages().delete(new Message(null,"bob",null));
        Collection<Message> messages = model().messages().all();
        assertEquals(true, messages.contains(new Message(null, "alice","a")));
        assertEquals(false, messages.contains(new Message(null, "bob","b")));
        assertEquals(true, messages.contains(new Message(null, "cindy","c")));
    }

    @Test
    public void testCoursesDeleteById() {
        i("testCourseDeleteById");
        model().messages().reset(false);
        model().messages().insert(new Message(null,"alice","a"));
        model().messages().insert(new Message(null,"bob","b"));
        model().messages().insert(new Message(null,"cindy","c"));

        for (Message message : model().messages().all()) {
            if (message.subject.equals("bob") == false) {
                model().messages().delete(message.id);
            }
        }

        Collection<Message> messages = model().messages().all();
        assertEquals(false, messages.contains(new Message(null, "alice","a")));
        assertEquals(true, messages.contains(new Message(null, "bob","b")));
        assertEquals(false, messages.contains(new Message(null, "cindy","c")));
    }

    public void testStorage() {
        i("testStorage");
        loginAsTestUser();
        File dir = new File(activity.getApplicationInfo().dataDir);
        File beepFile = new File(dir, "beep.m4a");
        Uri file = Uri.fromFile(beepFile);
        //StorageReference beepRef = activity.mStorageRef.child("sounds/beep.m4a");

        //beepRef.putFile(file)
        //        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        //            @Override
        //            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        //                // Get a URL to the uploaded content
        //                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
        //            }
        //        })
        //        .addOnFailureListener(new OnFailureListener() {
        //            @Override
        //            public void onFailure(@NonNull Exception exception) {
        //                // Handle unsuccessful uploads
        //                // ...
        //            }
        //        });
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
