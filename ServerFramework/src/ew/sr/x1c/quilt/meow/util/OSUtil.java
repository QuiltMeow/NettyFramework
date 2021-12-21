package ew.sr.x1c.quilt.meow.util;

import java.util.Locale;

public class OSUtil {

    public enum OSType {
        Windows, MacOS, Linux, Other
    }

    protected static OSType detectOS;

    public static OSType getOperatingSystemType() {
        if (detectOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (OS.contains("mac") || OS.contains("darwin")) {
                detectOS = OSType.MacOS;
            } else if (OS.contains("win")) {
                detectOS = OSType.Windows;
            } else if (OS.contains("nux")) {
                detectOS = OSType.Linux;
            } else {
                detectOS = OSType.Other;
            }
        }
        return detectOS;
    }
}
