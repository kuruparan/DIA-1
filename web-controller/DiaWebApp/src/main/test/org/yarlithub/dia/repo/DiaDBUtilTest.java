package org.yarlithub.dia.repo;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/25/14.
 */
public class DiaDBUtilTest {

    @Test
    public void isDeviceTest() {
       // assertTrue(DiaDBUtil.isDevice());
        System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }
}
