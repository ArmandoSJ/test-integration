package com.developer.unittest.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String name;
    private int age;
    private String lastname;
    private String firstname;
}
