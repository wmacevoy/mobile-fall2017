package edu.coloradomesa.mytutor;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by wmacevoy on 9/13/17.
 */

public class Courses implements AutoCloseable {
    private Context mContext;
    private LiteDB mLiteDB = null;

    Courses(Context context) {
        mContext = context;
    }

    public LiteDB liteDB() {
        if (mLiteDB == null) {
            synchronized (this) {
                if (mLiteDB == null) {
                    mLiteDB = new LiteDB(mContext);
                }
            }
        }
        return mLiteDB;
    }

    @Override
    public void close() {
        synchronized (this) {
            if (mLiteDB != null) {
                mLiteDB.close();
            }
            mLiteDB = null;
        }
    }

    public static final int CACHE_TIMEOUT_MILLISECONDS = 1000;
    private long mAllTimeout = Long.MIN_VALUE;
    private ArrayList<Course> mAll = null;

    public ArrayList<Course> all() {
        if (System.currentTimeMillis() > mAllTimeout) {
            synchronized (this) {
                mAll = liteDB().courses().all();
                mAllTimeout = System.currentTimeMillis() + CACHE_TIMEOUT_MILLISECONDS;
            }
        }
        return mAll;
    }

    public void clear() {
        synchronized (this) {
            liteDB().courses().reset();
            mAllTimeout = Long.MIN_VALUE;
        }
    }

    public void add(Course course) {
        if (course == null || course.mDepartment == null || course.mNumber <= 0) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
            Course anyIdCourse = new Course(-1, course.mDepartment, course.mNumber);
            if (!all().contains(anyIdCourse)) {
                liteDB().courses().insert(anyIdCourse);
                mAllTimeout = Long.MIN_VALUE;
            }
        }
    }

    public void add(String department, long number) {
        add(new Course(-1, department, number));
    }

    public void remove(Course course) {
        synchronized (this) {
            liteDB().courses().delete(course);
            mAllTimeout = Long.MIN_VALUE;
        }
    }

    public void remove(long id) {
        synchronized (this) {
            liteDB().courses().delete(id);
            mAllTimeout = Long.MIN_VALUE;
        }
    }

    public void remove(String department, long number) {
        remove(new Course(department,number));
    }
}

