package com.clhost.weatherbot.utils;

public class StringUtils {
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static String getHourStringByHour(int hour) {
        switch (hour) {
            case 1: return "час";
            case 2: return "часа";
            case 3: return "часа";
            case 4: return "часа";
            case 5: return "часов";
            case 6: return "часов";
            case 7: return "часов";
            case 8: return "часов";
            case 9: return "часов";
            case 10: return "часов";
            case 11: return "часов";
            case 12: return "часов";
            case 13: return "часов";
            case 14: return "часов";
            case 15: return "часов";
            case 16: return "часов";
            case 17: return "часов";
            case 18: return "часов";
            case 19: return "часов";
            case 20: return "часов";
            case 21: return "час";
            case 22: return "часа";
            case 23: return "часа";
            case 24: return "часа";
        }
        return null;
    }
}
