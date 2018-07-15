package com.clhost.weatherbot.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testIsNumeric() throws Exception {
        // true
        Assert.assertTrue(StringUtils.isNumeric("1"));
        Assert.assertTrue(StringUtils.isNumeric("0.5"));
        Assert.assertTrue(StringUtils.isNumeric("-0.5"));
        Assert.assertTrue(StringUtils.isNumeric("-1"));

        // false
        Assert.assertFalse(StringUtils.isNumeric("a"));
        Assert.assertFalse(StringUtils.isNumeric("--1"));
        Assert.assertFalse(StringUtils.isNumeric("0,5"));
        Assert.assertFalse(StringUtils.isNumeric(" "));
        Assert.assertFalse(StringUtils.isNumeric("."));
        Assert.assertFalse(StringUtils.isNumeric(".5"));
        Assert.assertFalse(StringUtils.isNumeric("-.5"));
        Assert.assertFalse(StringUtils.isNumeric("1.1.1"));
    }
}
