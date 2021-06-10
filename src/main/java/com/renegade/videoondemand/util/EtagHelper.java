package com.renegade.videoondemand.util;

import com.renegade.videoondemand.exception.ObjectOutOfDateException;

public class EtagHelper {
    public static void checkEtagCorrectness(Integer version, String etag) {
        if (!version.toString().equals(etag.replaceAll("\"", ""))) {
            throw new ObjectOutOfDateException();
        }
    }
}
