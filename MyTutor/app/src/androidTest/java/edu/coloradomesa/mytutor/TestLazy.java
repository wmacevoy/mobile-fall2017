package edu.coloradomesa.mytutor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLazy {
    static class Alpha {
        static class Lazy extends edu.coloradomesa.mytutor.Lazy < Alpha > {
            Lazy(String _x) { super(Alpha.class, _x); }
        }
        String x;
        Alpha(String _x) {
            x = _x;
        }
    }
    @Test
    public void testLazyAlpha() {

        String arg = "a";
        Alpha.Lazy zAlpha = new Alpha.Lazy(arg);
        assertEquals(zAlpha.self(),zAlpha.self());
        assertEquals(arg, zAlpha.self().x);
    }


    static class Beta {
        static class Lazy extends edu.coloradomesa.mytutor.Lazy < Beta > {
            Lazy(CharSequence _x) { super(Beta.class, _x); }
        }
        CharSequence x;
        Beta(CharSequence _x) { x = _x; }
    }

    @Test
    public void testLazyBeta() {

        String arg = "a";
        Beta.Lazy zBeta = new Beta.Lazy(arg);
        assertEquals(zBeta.self(),zBeta.self());
        assertEquals(arg, zBeta.self().x);
    }

    static class Gamma {
        static class Lazy extends edu.coloradomesa.mytutor.Lazy < Gamma > {
            Lazy(int _x) { super(Gamma.class, _x); }
        }
        int x;
        Gamma(int _x) { x = _x; }
    }

    @Test
    public void testLazyGamma() {

        int arg = 3;
        Gamma.Lazy zGamma = new Gamma.Lazy(arg);
        assertEquals(zGamma.self(),zGamma.self());
        assertEquals(arg, zGamma.self().x);
    }
}
