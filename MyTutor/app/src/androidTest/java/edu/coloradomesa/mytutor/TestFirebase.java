package edu.coloradomesa.mytutor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

/**
 * Created by wmacevoy on 9/28/17.
 */

public class TestFirebase {

    @Test public  void testHello() {
        // valid users must be readable as an unathenticated user for this to work (firebase->db console->database [rules tab]
        DatabaseReference mDatabase;
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }
}
