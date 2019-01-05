package org.cg.common.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Exc {
    public static String getStackTrace(Exception e) {
       return Arrays.asList(e.getStackTrace()).stream()
               .map(i -> i.getLineNumber() + " " + i.getClassName() +" " + i.getMethodName())
               .collect(Collectors.joining("\r\n"));
    }
}
