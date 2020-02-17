/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuktuk.config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Teran Weerasinghe
 */
public class Helper {
    public static String getTimeOrDate(String type) {
        Date today = new Date();

        if (type.equalsIgnoreCase("time")) {
            SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
            return time_format.format(today);
        } else {
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            return date_format.format(today);
        }

    }
}
