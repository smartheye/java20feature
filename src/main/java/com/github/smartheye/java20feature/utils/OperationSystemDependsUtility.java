package com.github.smartheye.java20feature.utils;

import java.util.Locale;

public class OperationSystemDependsUtility {

    public enum OperationSystemType {
        Windows, MacOS, Linux, Other
    };

    /**
     * detect the operating system from the os.name System property and cache
     * the result
     * 
     * @returns - the operating system detected
     */
    public static OperationSystemType getOperatingSystemType() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            return OperationSystemType.MacOS;
        } else if (OS.indexOf("win") >= 0) {
            return OperationSystemType.Windows;
        } else if (OS.indexOf("nux") >= 0) {
            return OperationSystemType.Linux;
        }
        return OperationSystemType.Other;
    }

}
