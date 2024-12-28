package com.example.bdcsamsungdevelopertest.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerExtension {

    public static <T> Logger getLogger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
