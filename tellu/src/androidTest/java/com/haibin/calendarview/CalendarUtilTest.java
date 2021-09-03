package com.haibin.calendarview;

import jzy.spark.tellu.convert.CalendarUtil;
import jzy.spark.tellu.data.Calendar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 新版月视图算法测试
 * Created by huanghaibin on 2018/2/8.
 */
@SuppressWarnings("all")
public class CalendarUtilTest {

    /**
     * 根据星期数和最小日期推算出该星期的第一天
     */
    @Test
    public void getFirstCalendarStartWithMinCalendar() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(13);

        Calendar firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                2);
        assertEquals(calendar, firstCalendar);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(20);

        firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                2,
                2);
        assertEquals(calendar, firstCalendar);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(12);

        firstCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                1);
        assertEquals(calendar, firstCalendar);
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     *
     * @return 周数用于WeekViewPager itemCount
     */
    @Test
    public void getWeekCountBetweenBothCalendar() throws Exception {
        int count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 5,
                2018, 8, 12, 1);
        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 1,
                2018, 8, 12, 2);

        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 7, 29,
                2018, 8, 12, 2);

        assertEquals(3, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2017, 12, 1,
                2017, 12, 3, 2);

        assertEquals(1, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 13,
                2018, 12, 12, 2);

        assertEquals(18, count);
    }


    /**
     * 根据日期获取距离最小年份是第几周
     */
    @Test
    public void getWeekFromCalendarStartWithMinCalendar() throws Exception {
        Calendar calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(8);
        calendar.setDay(1);

        int week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendar,
                2018, 8, 1, 1);

        assertEquals(1, week);

        calendar = new Calendar();
        calendar.setYear(2018);
        calendar.setMonth(9);
        calendar.setDay(1);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendar,
                2018, 8, 13, 2);

        assertEquals(3, week);

    }

    @Test
    public void getWeekCountDiff() throws Exception {
        int diff = CalendarUtil.getWeekViewEndDiff(2020,4,2,1);
        assertEquals(2,diff);
    }


    @Test
    public void getWeekViewStartDiff() throws Exception {

    }


    @Test
    public void getWeekViewEndDiff() throws Exception {

    }


    /**
     * 根据日期获取两个年份中第几周,用来设置 WeekView currentItem
     *
     * @throws Exception Exception
     */
    @Test
    public void differ() throws Exception {
        Calendar calendar1 = new Calendar();
        calendar1.setYear(2018);
        calendar1.setMonth(4);
        calendar1.setDay(1);

        Calendar calendar2 = new Calendar();
        calendar2.setYear(2018);
        calendar2.setMonth(4);
        calendar2.setDay(3);

        assertEquals(-2,CalendarUtil.differ(calendar1,calendar2));

        calendar1.setYear(2018);
        calendar1.setMonth(9);
        calendar1.setDay(30);

        calendar2.setYear(2018);
        calendar2.setMonth(9);
        calendar2.setDay(1);

        assertEquals(29,CalendarUtil.differ(calendar1,calendar2));

        calendar1.setYear(2018);
        calendar1.setMonth(9);
        calendar1.setDay(12);

        calendar2.setYear(2018);
        calendar2.setMonth(9);
        calendar2.setDay(5);

        assertEquals(7,CalendarUtil.differ(calendar1,calendar2));
    }
}