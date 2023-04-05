package com.developer.unittest.utils;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.developer.unittest.utils.PasswordUtil.SecurityLevel.*;

class PasswordUtilTest {

    @Test
    public void weak_when_has_less_than_8_letters() {
        assertEquals(WEAK, PasswordUtil.validatePassword("123aa!"));
    }

    @Test
    public void weak_when_has_only_letters() {
        assertEquals(WEAK, PasswordUtil.validatePassword("abcdefghjk"));
    }

    @Test
    public void medium_when_has_letters_and_numbers() {
        assertEquals(MEDIUM, PasswordUtil.validatePassword("abcde12345"));
    }

    @Test
    public void strong_when_has_letters_numbers_and_symbols() {
        assertEquals(STRONG, PasswordUtil.validatePassword("abcd123!"));
    }

}