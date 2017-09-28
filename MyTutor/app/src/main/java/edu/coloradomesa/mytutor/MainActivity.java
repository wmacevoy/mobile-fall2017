package edu.coloradomesa.mytutor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends CoreActivity {


    public static final int LOGIN_REQUEST = 1;

    private TextView mTextMessage;
    private MenuView.ItemView mScheduleItemView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i("MyTutor","bottom nav item " + item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_schedule:
                    mTextMessage.setText(R.string.title_schedule);
                    Log.i("MyTutor","schedule tab");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.i(TAG,"request = " + requestCode + " result = " + resultCode + " (ok=" + RESULT_OK + ")");
        switch(requestCode) {
            case LOGIN_REQUEST:
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "user is " + user().user());

                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            } else {
                login();
            }
            break;
            default:
                Log.i(TAG, "no result handler");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mScheduleItemView = (MenuView.ItemView) findViewById(R.id.navigation_schedule);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tests();
    }
    void testReadUsers() {
        DatabaseReference mDatabase;
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference users = mDatabase.child("users");

        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                edu.coloradomesa.db.User user = dataSnapshot.getValue(edu.coloradomesa.db.User.class);
                Log.i(TAG,"username = " + user.username);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        users.addValueEventListener(usersListener);

    }
    void tests() {
        testReadUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!user().authenticated()) { login(); }
    }

    void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    void logout() {
        user().logout();
        recreate();
    }

}
