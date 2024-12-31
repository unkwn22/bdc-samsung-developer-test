package com.example.bdcsamsungdevelopertest.common.util;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

public class RequestValue {

    public static Map<String, Map<String, Boolean>> RequestParam = new HashMap<>();
    public static Map<String, Map<String, Boolean>> RequestBody = new HashMap<>();
    public static Map<String, Map<String, Boolean>> PathParam = new HashMap<>();

    private final String
    private final String ROOT_PACKAGE_NAME = "com.example.bdcsamsungdevelopertest";

    @PostConstruct

}
