package com.easy.code.app.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tanieska on 23/3/2017.
 */

public class Validation {

    public boolean isPhone(String value) {
        Pattern path = Pattern.compile("^2||8\\d{7}");
        Matcher match = path.matcher(value);

        return match.matches();
    }

    public boolean isEmail(String value) {
        Pattern path = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher match = path.matcher(value);
        return match.matches();
    }

}
