package edu.coloradomesa.mytutor;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestReflect {

    static class Alpha {
        String x;
        Alpha(String _x) {
            x = _x;
        }
    }

    @Test
    public void testCtorAlpha() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        String arg = "a";
        Constructor<Alpha> ctor = (Constructor<Alpha>) Reflect.getConstructor(Alpha.class,arg);
        Alpha alpha = ctor.newInstance(arg);
        assertEquals(arg,alpha.x);
    }

    static class Beta {
        int x;
        Beta(int _x) { x = _x; }
    }

    @Test
    public void testCtorBeta() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int arg = 3;
        Constructor<Beta> ctor = (Constructor<Beta>) Reflect.getConstructor(Beta.class,arg);
        Beta beta = ctor.newInstance(arg);
        assertEquals(arg,beta.x);
    }

    static class Gamma {
        CharSequence x;
        Gamma(CharSequence _x) { x = _x; }
    }

    @Test
    public void testCtorGamma() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        String arg = "g";
        Constructor<Gamma> ctor = (Constructor<Gamma>) Reflect.getConstructor(Gamma.class,arg);
        Gamma gamma = ctor.newInstance(arg);
        assertEquals(arg,gamma.x);
    }

}
