package com.company;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Date_today {
    public String date_filter(){
        String dateFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar cal1 = Calendar.getInstance();
        cal1.add(5, -1);
        return sdf.format(cal1.getTime());
    }


}
