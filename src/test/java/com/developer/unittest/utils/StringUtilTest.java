package com.developer.unittest.utils;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilTest {

    @Test
    public void repeat_string_once() {
        assertEquals("hello", StringUtil.repeat("hello", 1));
    }

    @Test
    public void repeat_string_multiple_times() {
        assertEquals("hello-hello-hello-", StringUtil.repeat("hello-", 3));
    }

    @Test
    public void repeat_string_zero_times() {
        assertEquals("", StringUtil.repeat("hello", 0));
    }

    @Test
    public void repeat_string_negative_times() {
        assertThrows(IllegalArgumentException.class, ()->{
            StringUtil.repeat("hello", -1);
        });
    }
}