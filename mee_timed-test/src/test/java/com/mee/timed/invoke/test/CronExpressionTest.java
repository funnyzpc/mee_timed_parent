/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package com.mee.timed.invoke.test;

import com.mee.timed.config.CronExpression;
import org.junit.jupiter.api.Test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class CronExpressionTest {
    private static final String[] VERSIONS = new String[] {"1.5.2"};

    private static final TimeZone EST_TIME_ZONE = TimeZone.getTimeZone("US/Eastern"); 

    /**
     * Get the object to serialize when generating serialized file for future
     * tests, and against which to validate deserialized object.
     */
    protected Object getTargetObject() throws ParseException {
        CronExpression cronExpression = new CronExpression("0 15 10 * * ? 2005");
        cronExpression.setTimeZone(EST_TIME_ZONE);
        return cronExpression;
    }
    
    /**
     * Get the Quartz versions for which we should verify
     * serialization backwards compatibility.
     */
    protected String[] getVersions() {
        return VERSIONS;
    }
    
//    /**
//     * Verify that the target object and the object we just deserialized
//     * match.
//     */
//    protected void verifyMatch(Object target, Object deserialized) {
//        CronExpression targetCronExpression = (CronExpression)target;
//        CronExpression deserializedCronExpression = (CronExpression)deserialized;
//
//        assertNotNull(deserializedCronExpression);
//        assertEquals(targetCronExpression.getCronExpression(), deserializedCronExpression.getCronExpression());
//        assertEquals(targetCronExpression.getTimeZone(), deserializedCronExpression.getTimeZone());
//    }
//
//    /*
//     * Test method for 'org.quartz.CronExpression.isSatisfiedBy(Date)'.
//     */
//    public void testIsSatisfiedBy() throws Exception {
//        CronExpression cronExpression = new CronExpression("0 15 10 * * ? 2005");
//
//        Calendar cal = Calendar.getInstance();
//
//        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
//        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));
//
//        cal.set(Calendar.YEAR, 2006);
//        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));
//
//        cal = Calendar.getInstance();
//        cal.set(2005, Calendar.JUNE, 1, 10, 16, 0);
//        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));
//
//        cal = Calendar.getInstance();
//        cal.set(2005, Calendar.JUNE, 1, 10, 14, 0);
//        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));
//    }
//
//    public void testLastDayOffset() throws Exception {
//        CronExpression cronExpression = new CronExpression("0 15 10 L-2 * ? 2010");
//        Calendar cal = Calendar.getInstance();
//        cal.set(2010, Calendar.OCTOBER, 29, 10, 15, 0); // last day - 2
//        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));
//        cal.set(2010, Calendar.OCTOBER, 28, 10, 15, 0);
//        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));
//        cronExpression = new CronExpression("0 15 10 L-5W * ? 2010");
//        cal.set(2010, Calendar.OCTOBER, 26, 10, 15, 0); // last day - 5
//        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));
//        cronExpression = new CronExpression("0 15 10 L-1 * ? 2010");
//        cal.set(2010, Calendar.OCTOBER, 30, 10, 15, 0); // last day - 1
//        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));
//        cronExpression = new CronExpression("0 15 10 L-1W * ? 2010");
//        cal.set(2010, Calendar.OCTOBER, 29, 10, 15, 0); // nearest weekday to last day - 1 (29th is a friday in 2010)
//        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));
//    }

    @Test
    public void test02() throws ParseException {
        Instant instant = Instant.now(); // 获取当前时间（UTC）
        ZonedDateTime newYorkZone = instant.atZone(ZoneId.of("America/New_York"));

        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d = new Date(newYorkZone.toInstant().toEpochMilli());
        Date d = new Date();
//        CronExpression cronExpression = new CronExpression("11 11 12 ? * MON-FRI");
        CronExpression cronExpression = new CronExpression("0 30 10 L * ?");
//        cronExpression.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        cronExpression.setTimeZone(TimeZone.getDefault());
        cronExpression.getTimeAfter(d);
        for(int i=0;i<10;i++ ){
            Date nd = cronExpression.getNextValidTimeAfter(d);
            System.out.println("fireTime: " + fmt.format(nd));
            d=nd;
        }
    }

    @Test
    public void test03(){
        final DateTimeFormatter FORMAT_DAY_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date();
        ZoneId zoneId = ZoneId.of("America/New_York");
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
        System.out.println(dateTime);
        Date date1 = new Date(dateTime.toInstant().toEpochMilli());
        System.out.println("date:"+fmt.format(date));
        System.out.println("date1:"+fmt.format(date1));
        System.out.println(dateTime.format(FORMAT_DAY_TIME));
    }

//    /**
//     * QTZ-259 : last day offset causes repeating fire time
//     *
//     */
//    @Test
// 	public void testQtz259() throws Exception {
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0 0 0 L-2 * ? *");
////        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0 * * * * ? *");
//        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0 0/2 * ? * MON-FRI");
// 		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("test").withSchedule(schedBuilder).build();
//
// 		int i = 0;
// 		Date pdate = trigger.getFireTimeAfter(new Date());
// 		while (++i < 26) {
// 			Date date = trigger.getFireTimeAfter(pdate);
// 			System.out.println("fireTime: " + fmt.format(date) + ", previousFireTime: " + fmt.format(pdate));
// 			pdate = date;
// 		}
// 	}
    
//    /**
//     * QTZ-259 : last day offset causes repeating fire time
//     *
//     */
// 	public void testQtz259LW() throws Exception {
// 		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0 0 0 LW * ? *");
// 		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("test").withSchedule(schedBuilder).build();
//
// 		int i = 0;
// 		Date pdate = trigger.getFireTimeAfter(new Date());
// 		while (++i < 26) {
// 			Date date = trigger.getFireTimeAfter(pdate);
// 			System.out.println("fireTime: " + date + ", previousFireTime: " + pdate);
// 			assertFalse("Next fire time is the same as previous fire time!", pdate.equals(date));
// 			pdate = date;
// 		}
// 	}
//
//    /*
//     * QUARTZ-574: Showing that storeExpressionVals correctly calculates the month number
//     */
//    public void testQuartz574() {
//        try {
//            new CronExpression("* * * * Foo ? ");
//            fail("Expected ParseException did not fire for non-existent month");
//        } catch(ParseException pe) {
//            assertTrue("Incorrect ParseException thrown",
//                pe.getMessage().startsWith("Invalid Month value:"));
//        }
//
//        try {
//            new CronExpression("* * * * Jan-Foo ? ");
//            fail("Expected ParseException did not fire for non-existent month");
//        } catch(ParseException pe) {
//            assertTrue("Incorrect ParseException thrown",
//                pe.getMessage().startsWith("Invalid Month value:"));
//        }
//    }

    


}
