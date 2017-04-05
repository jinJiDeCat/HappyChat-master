package com.zz.zy.happychat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {
    public static String TimeToString(Long time){
        int m,s;
        m= (int) (time/60);
        s= (int) (time%60);
        return m+":"+String.format("%02d%n",s);
    }
    public static String UnixToTime(String dataFormat,long timeStamp){
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat, Locale.CHINA);
        result = format.format(new Date(timeStamp));
        return result;
    }
    public static String dateToStar(Date date){
        String star = null;
        switch (date.getMonth()){
            case 0:
                if(date.getDate()<=19)
                    star= "摩羯座";
                else
                    star= "水瓶座";
                break;
            case 1:
                if(date.getDate()<=18)
                    star= "水瓶座";
                else
                    star= "双鱼座";
                break;
            case 2:
                if(date.getDate()<=20)
                    star= "双鱼座";
                else
                    star= "白羊座";
                break;
            case 3:
                if(date.getDate()<=19)
                    star= "白羊座";
                else
                    star= "金牛座";
                break;
            case 4:
                if(date.getDate()<=20)
                    star= "金牛座";
                else
                    star= "双子座";
                break;
            case 5:
                if(date.getDate()<=21)
                    star= "双子座";
                else
                    star= "巨蟹座";
                break;
            case 6:
                if(date.getDate()<=22)
                    star= "巨蟹座";
                else
                    star= "狮子座";
                break;
            case 7:
                if(date.getDate()<=22)
                    star= "狮子座";
                else
                    star= "处女座";
                break;
            case 8:
                if(date.getDate()<=22)
                    star= "处女座";
                else
                    star= "天枰座";
                break;
            case 9:
                if(date.getDate()<=23)
                    star= "天枰座";
                else
                    star= "天蝎座";
                break;
            case 10:
                if(date.getDate()<=22)
                    star= "天蝎座";
                else
                    star= "射手座";
                break;
            case 11:
                if(date.getDate()<=21)
                    star= "射手座";
                else
                    star= "摩羯座";
                break;
        }
        return star;
    }
}
