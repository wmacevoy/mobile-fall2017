package edu.coloradomesa.mytutor;

import android.util.Log;

/**
 * Created by wmacevoy on 9/17/17.
 */

public abstract class Lazy < T > implements AutoCloseable {
    private T mSelf;
    T self() {
        if (mSelf == null) {
            synchronized (this) {
                if (mSelf == null) {
                    mSelf = create();
                }
            }
        }
        return mSelf;
    }
    public void close() {
        synchronized (this) {
            if (mSelf != null) {
                T self = mSelf;
                mSelf = null;
                close(self);

            }
        }
    }
    abstract T create();
    void close(T self) {
        if (self instanceof AutoCloseable) {
            try {
                ((AutoCloseable) self).close();
            } catch (Exception ex) {
                Log.e(MainActivity.APP, "Exception " + ex + " closing " + self);
            }
        }
    }
}
