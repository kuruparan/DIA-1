package org.yarlithub.dia.util;

import java.util.Calendar;

/**
 * Created by nirajh on 6/12/14.
 */
public class DiaCommonUtil {
    public static int temperatureValue(String sensorData) {
        sensorData=sensorData.toLowerCase();
        String[] tem = sensorData.split(";");
        return Integer.parseInt(tem[0].replace("t:", ""));
    }

    public static int moistureValue(String sensorData) {
        sensorData=sensorData.toLowerCase();
        String[] tem = sensorData.split(";");
        return 100-Integer.parseInt(tem[1].replace("m:", ""));
    }

    /**
     * Monday =0 , Tuesday=1  ....Sunday=6
     *
     * @return
     */
    public static int getCurrentDay() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            //java calender convention:  Sunday =1,Monday= 2.....
            day = 6;
        } else {
            day = day - 2;
        }
        return day;
    }
}
