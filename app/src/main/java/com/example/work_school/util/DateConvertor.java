package com.example.work_school.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;

public class DateConvertor {


        public static Date convertToDate(String dateString, String timeString) {
            String combined = dateString + " " + timeString; // e.g., "2025-05-21 14:30"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                return sdf.parse(combined);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }


}
