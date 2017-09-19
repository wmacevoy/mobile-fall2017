package edu.coloradomesa.mytutor;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by wmacevoy on 9/13/17.
 */

public class Courses {
    private LiteDB.Lazy mLiteDB;
    public LiteDB liteDB() { return mLiteDB.self(); }

    public Courses(LiteDB.Lazy liteDB) {
        mLiteDB = liteDB;
    }

    public static class Lazy extends edu.coloradomesa.mytutor.Lazy < Courses > {
        Lazy(LiteDB.Lazy liteDB) {
            super(Courses.class, liteDB);
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
            if (mAll != null) mAll.clear();
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
                if (mAll != null) { mAll.add(anyIdCourse); }
            }
        }
    }

    public void add(String department, long number) {
        add(new Course(-1, department, number));
    }

    public void remove(Course course) {
        synchronized (this) {
            liteDB().courses().delete(course);
            if (mAll != null) { mAll.remove(course); }
        }
    }

    public void remove(long id) {
        synchronized (this) {
            liteDB().courses().delete(id);
            if (mAll != null) {
                for (int i = 0; i < mAll.size(); ++i) {
                    if (mAll.get(i).mId == id) {
                        mAll.remove(i);
                        return;
                    }
                }
            }
        }
    }

    public void remove(String department, long number) {
        remove(new Course(department,number));
    }
}

