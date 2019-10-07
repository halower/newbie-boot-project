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
/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
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
