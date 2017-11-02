package edu.coloradomesa.mytutor;

import android.os.Bundle;

/**
 * Created by wmacevoy on 10/19/17.
 */

public interface Activated {
    void onCreate(Bundle state);
    void onStart();
    void onResart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    class Default implements Activated {
        @Override
        public void onCreate(Bundle state) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onResart() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }
    }
}
