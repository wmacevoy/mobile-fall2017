package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLiteDB {
    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testCoursesEmpty() {
        try (LiteDB liteDB = new LiteDB(context)) {
            liteDB.courses().reset();
        }
        try (LiteDB liteDB = new LiteDB(context)) {
            Collection<Course> courses = liteDB.courses().all();
            assertEquals(true, courses.isEmpty());
        }
    }

    @Test
    public void testCoursesInsert() {
        try (LiteDB liteDB = new LiteDB(context)) {
            liteDB.courses().reset();
            liteDB.courses().insert(new Course(-1, "CSCI", 405));
            liteDB.courses().insert(new Course(-1, "MATH", 151));
            liteDB.courses().insert(new Course(-1, "ENGL", 111));
        }
        try (LiteDB liteDB = new LiteDB(context)) {
            Collection<Course> courses = liteDB.courses().all();
            assertEquals(true, courses.contains(new Course(-1, "CSCI", 405)));
            assertEquals(true, courses.contains(new Course(-1, "MATH", 151)));
            assertEquals(true, courses.contains(new Course(-1, "ENGL", 111)));
        }
    }


    @Test
    public void testCoursesDeleteByDepartmentAndNumber() {

        try (LiteDB liteDB = new LiteDB(context)) {
            liteDB.courses().reset();
            liteDB.courses().insert(new Course(-1, "CSCI", 405));
            liteDB.courses().insert(new Course(-1, "MATH", 151));
            liteDB.courses().insert(new Course(-1, "ENGL", 111));
            liteDB.courses().delete(new Course(-1, "MATH", 151));
        }
        try (LiteDB liteDB = new LiteDB(context)) {
            Collection<Course> courses = liteDB.courses().all();
            assertEquals(true, courses.contains(new Course(-1, "CSCI", 405)));
            assertEquals(false, courses.contains(new Course(-1, "MATH", 151)));
            assertEquals(true, courses.contains(new Course(-1, "ENGL", 111)));
        }
    }

    @Test
    public void testCoursesDeleteById() {

        try (LiteDB liteDB = new LiteDB(context)) {
            liteDB.courses().reset();
            liteDB.courses().insert(new Course(-1, "CSCI", 405));
            liteDB.courses().insert(new Course(-1, "MATH", 151));
            liteDB.courses().insert(new Course(-1, "ENGL", 111));
        }

        try (LiteDB liteDB = new LiteDB(context)) {
            for (Course course : liteDB.courses().all()) {
                if (course.mNumber < 200) {
                    liteDB.courses().delete(course.mId);
                }
            }
        }
        try (LiteDB liteDB = new LiteDB(context)) {
            Collection<Course> courses = liteDB.courses().all();
            assertEquals(true, courses.contains(new Course(-1, "CSCI", 405)));
            assertEquals(false, courses.contains(new Course(-1, "MATH", 151)));
            assertEquals(false, courses.contains(new Course(-1, "ENGL", 111)));
        }
    }
}
