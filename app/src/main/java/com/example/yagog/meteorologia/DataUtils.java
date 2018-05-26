package com.example.yagog.meteorologia;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    public static String obtemDiaSemana (long dt){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date = new Date();
        date.setTime(dt * 1000);
        return sdf.format(date);
    }
}
