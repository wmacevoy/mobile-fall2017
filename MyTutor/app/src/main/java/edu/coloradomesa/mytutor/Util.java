package edu.coloradomesa.mytutor;

/**
 * Created by wmacevoy on 9/12/17.
 */

public class Util {
    public static boolean eq(Object a, Object b) {
        if (a == null || b == null) { return a == b; }
        if (a.getClass().isInstance(b)) return a.equals(b);
        if (b.getClass().isInstance(a)) return b.equals(a);
        return false;
    }

    public static boolean eq(CharSequence a, CharSequence b) {
        if (a == null || b == null) { return a == b; }
        return a.equals(b);
    }

}
