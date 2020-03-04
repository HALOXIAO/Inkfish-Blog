package com.inkfish.blog.server.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HALOXIAO
 **/
public class FieldJudgement {
    static Pattern PHONE_PATTERN = Pattern
            .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");


    public static boolean isEmail(String str) {
        String expr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";
        if (str.matches(expr)) {
            return true;
        }
        return false;
    }

    public static boolean isPhone(String str) {
        Matcher matcher = PHONE_PATTERN.matcher(str);
        return matcher.matches();
    }

}
