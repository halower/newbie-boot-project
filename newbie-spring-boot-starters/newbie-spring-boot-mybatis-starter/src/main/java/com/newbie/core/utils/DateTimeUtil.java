/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newbie.core.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * 时间操作
 */
public class DateTimeUtil {
        public  Date str2date(String strTime, Pattern pattern) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern.getPattern());
            DateTime dateTime = fmt.parseDateTime(strTime);
            return dateTime.toDate();
        }


        public  String date2str(Date date, Pattern pattern) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern.getPattern());
            DateTime dateTime = new DateTime(date);
            return fmt.print(dateTime);
        }

        /**
         * 字符串在两种格式之间转换
         */
        public  String strToStr(String fromStr, Pattern fromPattern, Pattern toPattern) {
            Date d = str2date(fromStr, fromPattern);
            return date2str(d, toPattern);
        }


        public  Date addYear(Date d, int years) {
            DateTime dateTime = new DateTime(d);
            dateTime = dateTime.plusYears(years);
            return dateTime.toDate();
        }

        public  Date addSeconds(Date d, int sec) {
            DateTime dateTime = new DateTime(d);
            dateTime = dateTime.plusSeconds(sec);
            return dateTime.toDate();
        }

        public  Date addMinutes(Date d, int min) {
            DateTime dateTime = new DateTime(d);
            dateTime = dateTime.plusMinutes(min);
            return dateTime.toDate();
        }

        public  Date addHours(Date d, int hours) {
            DateTime dateTime = new DateTime(d);
            dateTime = dateTime.plusHours(hours);
            return dateTime.toDate();
        }

        /**
         * 获取Date中的分钟
         *
         * @param d
         * @return
         */
        public  int getMinute(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getMinuteOfHour();
        }

        /**
         * 获取Date中的小时(24小时)
         *
         * @param d
         * @return
         */
        public  int getHour(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getHourOfDay();
        }

        /**
         * 获取Date中的秒
         *
         * @param d
         * @return
         */
        public  int getSecond(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getSecondOfMinute();
        }

        /**
         * 获取Date中的毫秒
         *
         * @param d
         * @return
         */
        public  int getMilliSecond(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getMillisOfSecond();
        }

        /**
         * 获取xxxx-xx-xx的日
         *
         * @param d
         * @return
         */
        public  int getDay(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getDayOfMonth();
        }

        /**
         * 获取月份，1-12月
         *
         * @param d
         * @return
         */
        public  int getMonth(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getMonthOfYear();
        }

        /**
         * 获取19xx,20xx形式的年
         *
         * @param d
         * @return
         */
        public  int getYear(Date d) {
            DateTime dateTime = new DateTime(d);
            return dateTime.getYear();
        }

        /**
         * 得到d 的年份+月份,如200505
         *
         * @return
         */
        public  String getYearMonthOfDate(Date d) {
            return date2str(d, Pattern.yyyyMM);
        }

        /**
         * 得到上个月的年份+月份,如200505
         *
         * @return
         */
        public  String getYearMonthOfPreviousMonth(Date date) {

            return date2str(addMonth(date, -1), Pattern.yyyyMM);
        }

        /**
         * 得到当前日期的年和月如200509
         *
         * @return String
         */
        public  String getCurYearMonth() {
            return date2str(DateTime.now().toDate(), Pattern.yyyyMM);
        }

        /**
         * 获得系统当前月份的天数
         *
         * @return
         */
        public  int getCurrentMonthDays() {
            return getMonthDays(DateTime.now().toDate());
        }

        /**
         * 获得指定日期月份的天数
         *
         * @return
         */
        public  int getMonthDays(Date date) {
            return new DateTime(date).dayOfMonth().withMaximumValue().getDayOfMonth();
        }

        /**
         * 在传入时间基础上加一定月份数
         *
         * @param oldTime Date
         * @param months  int
         * @return long
         */
        public  Date addMonth(Date oldTime, final int months) {
            DateTime dateTime = new DateTime(oldTime);
            dateTime = dateTime.plusMonths(months);
            return dateTime.toDate();
        }

        public  long addMonth(long oldTime, final int months) {
            DateTime dateTime = new DateTime(oldTime);
            dateTime = dateTime.plusMonths(months);
            return dateTime.getMillis();
        }

        /**
         * 在传入时间基础上加一定天数
         *
         * @param oldTime long
         * @param day     int
         * @return long
         */
        public  long addDay(final long oldTime, final int day) {
            DateTime dateTime = new DateTime(oldTime);
            dateTime = dateTime.plusDays(day);
            return dateTime.getMillis();
        }

        /**
         * 在传入时间基础上加一定天数
         *
         * @param oldTime Calendar
         * @param day     int
         * @return long
         */
        public  Date addDay(final Date oldTime, final int day) {
            DateTime dateTime = new DateTime(oldTime);
            dateTime = dateTime.plusDays(day);
            return dateTime.toDate();
        }

        /**
         * 获得周一的日期
         *
         * @param date
         * @return
         */
        public  Date getMonday(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.withDayOfWeek(DateTimeConstants.MONDAY);
            return dateTime.toDate();
        }

        /**
         * 获得周五的日期
         *
         * @param date
         * @return
         */
        public  Date getFriday(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.withDayOfWeek(DateTimeConstants.FRIDAY);
            return dateTime.toDate();

        }

        /**
         * 得到月的第一天
         */
        public  Date getMonthFirstDay(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.dayOfMonth().withMinimumValue();
            return dateTime.toDate();

        }

        /**
         * 得到月的最后一天
         */
        public  Date getMonthLastDay(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.dayOfMonth().withMaximumValue();
            return dateTime.toDate();

        }

        /**
         * 得到季度的第一天
         */
        public  Date getSeasonFirstDay(Date date) {
            DateTime dateTime = new DateTime(date);
            int curMonth = dateTime.getMonthOfYear();
            if (curMonth >= DateTimeConstants.JANUARY && curMonth <= DateTimeConstants.MARCH) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.JANUARY);
            } else if (curMonth >= DateTimeConstants.APRIL && curMonth <= DateTimeConstants.JUNE) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.APRIL);
            } else if (curMonth >= DateTimeConstants.JULY && curMonth <= DateTimeConstants.SEPTEMBER) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.JULY);
            } else {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.OCTOBER);
            }
            dateTime = dateTime.dayOfMonth().withMinimumValue();
            return dateTime.toDate();
        }

        /**
         * 得到季度的最后一天
         */
        public  Date getSeasonLastDay(Date date) {
            DateTime dateTime = new DateTime(date);
            int curMonth = dateTime.getMonthOfYear();
            if (curMonth >= DateTimeConstants.JANUARY && curMonth <= DateTimeConstants.MARCH) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.MARCH);
            } else if (curMonth >= DateTimeConstants.APRIL && curMonth <= DateTimeConstants.JUNE) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.JUNE);
            } else if (curMonth >= DateTimeConstants.JULY && curMonth <= DateTimeConstants.SEPTEMBER) {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.SEPTEMBER);
            } else {
                dateTime = dateTime.withMonthOfYear(DateTimeConstants.DECEMBER);
            }
            dateTime = dateTime.dayOfMonth().withMaximumValue();
            return dateTime.toDate();
        }

        /**
         * 获取年第一天日期
         *
         * @return Date
         */
        public  Date getYearFirstDay(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.dayOfYear().withMinimumValue();
            return dateTime.toDate();
        }

        /**
         * 获取年最后一天日期
         *
         * @return Date
         */
        public  Date getYearLastDay(Date date) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.dayOfYear().withMaximumValue();
            return dateTime.toDate();
        }

        public enum Pattern {
            //如果不够用可以自己添加
            yyyy_MM("yyyy-MM"),
            yyyyMM("yyyyMM"),
            yyyy_MM_dd("yyyy-MM-dd"),
            yyyyMMdd("yyyyMMdd"),
            yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
            yyyyMMddHHmmss("yyyyMMddHHmmss"),
            yyyy_MM_dd_HH_mm_ss_zh("yyyy年MM月dd日HH时mm分ss秒"),
            yyyy_MM_dd_HH_mm_ss_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
            yyyy_MM_dd_zh("yyyy年MM月dd日"),
            yyyy_MM_dd_HH_mm_zh("yyyy年MM月dd日HH时mm分");
            private String pattern;

            Pattern(String pattern) {
                this.pattern = pattern;
            }

            public String getPattern() {
                return pattern;
            }
        }
}
