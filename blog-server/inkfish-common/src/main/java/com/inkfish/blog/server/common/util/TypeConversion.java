package com.inkfish.blog.server.common.util;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @author HALOXIAO
 **/
public class TypeConversion {

    public static String timeFormatTo(Timestamp timestamp, String pattern) {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

}
