package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestCourses {
    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testCourses() {
        try (Courses courses = new Courses(context)) {
            courses.clear();
            assertEquals(true, courses.all().isEmpty());
        }

        try (Courses courses = new Courses(context)) {
            courses.add("CSCI",405);
            courses.add("ENGL",111);
            assertEquals(true, courses.all().contains(new Course("CSCI",405)));
            assertEquals(true, courses.all().contains(new Course("ENGL",111)));
        }

        try (Courses courses = new Courses(context)) {
            courses.add("CSCI",405);
            courses.add("ENGL",111);
            courses.remove("CSCI",405);
            assertEquals(false, courses.all().contains(new Course("CSCI",405)));
            assertEquals(true, courses.all().contains(new Course("ENGL",111)));
        }
    }
}
