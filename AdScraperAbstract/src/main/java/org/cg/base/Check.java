package org.cg.base;

public class Check {

    public static void notNull(Object o) {
        if (o == null) fail();
    }

    public static void notEmpty(String s) {
        notNull(s);
        if (s.length() == 0) fail();
    }

    public static void isTrue(boolean b) {
        if (!b) fail();
    }

    public static void isFalse(boolean b) {
        isTrue(!b);
    }

    private static void fail() {
        throw new IllegalStateException();
    }
}
