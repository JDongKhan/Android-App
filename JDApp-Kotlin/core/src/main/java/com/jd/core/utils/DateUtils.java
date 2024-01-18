package com.jd.core.utils;

import android.os.SystemClock;
import android.util.Log;

import com.jd.core.log.LogUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author jd
 */
public class DateUtils {

    private static SimpleDateFormat simpleDateFormat = null;

    static {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        }
    }

    private DateUtils() {
        throw new UnsupportedOperationException("Can not be created！");
    }

    /**
     * 获取当前日期时间并格式化
     *
     * @param format
     * @return
     */
    public static String getCurrentDateTime(String format) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(DateManager.getInstance().getCurrentDate());
    }

    /**
     * 按照指定格式格式化日期
     *
     * @param time
     * @param oldFormat 旧格式
     * @param format    新格式
     * @return
     */
    public static String getFormatDateString(String time, String oldFormat, String format) {
        Date date = getFormatDate(time, oldFormat);
        if (date != null) {
            return getFormatDateString(date, format);
        }
        return "";
    }

    /**
     * 格式化指定日期
     *
     * @param date
     * @param format
     * @return 日期字符串
     */
    public static String getFormatDateString(Date date, String format) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化指定日期
     *
     * @param time
     * @param format
     * @return 日期类型
     */
    public static Date getFormatDate(String time, String format) {
        simpleDateFormat.applyPattern(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (Exception e) {
            Log.w("Sonar Catch Exception", e);
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期字符串转时间戳
     *
     * @param time
     * @param format
     * @return
     */
    public static long getLongTimeFromString(String time, String format) {
        Date date = getFormatDate(time, format);
        if (date != null) {
            return date.getTime();
        }
        return 0;
    }

    /**
     * 将时间戳转换为指定格式的日期字符串
     *
     * @param time
     * @param formatString
     * @return
     */
    public static String getTimeFromLong(long time, String formatString) {
        simpleDateFormat.applyPattern(formatString);
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期转换为指定格式
     *
     * @param time
     * @return
     */
    public static String getTimeFromString(String time) {
        return getTimeFromLong(Long.parseLong(time), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 判断当前日期是否在指定日期区间内
     *
     * @param dayBefore
     * @param dayAfter
     * @return
     */
    public static boolean compareDate(String dayBefore, String dayAfter) {
        String format = "yyyy-MM-dd HH:mm:ss";
        String curTime = getCurrentDateTime(format);
        Date cur = getFormatDate(curTime, format);

        Date parBefore = getFormatDate(dayBefore, format);
        Date parEnd = getFormatDate(dayAfter, format);

        return cur.after(parBefore) || cur.before(parEnd);
    }

    /**
     * 判断当前时间是否在指定时间区间内
     *
     * @param timeBefore
     * @param timeAfter
     * @return
     */
    public static boolean compareTime(String timeBefore, String timeAfter) {
        String format = "yyyy-MM-dd HH:mm:ss";
        String curTime = getCurrentDateTime(format);
        String cur = getTimeFromString(curTime);
        String parBefore = getTimeFromString(timeBefore);
        String parEnd = getTimeFromString(timeAfter);

        Date curDate = getFormatDate(cur, format);
        Date parBeforeDate = getFormatDate(parBefore, format);
        Date parEndDate = getFormatDate(parEnd, format);

        return curDate.after(parBeforeDate) || curDate.before(parEndDate);
    }

    /**
     * 获取指定日期的星期
     *
     * @param date
     * @return
     */
    public static int getWeek(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (Exception e) {
            Log.w("Sonar Catch Exception", e);
            e.printStackTrace();
        }
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 ? 7 : dayOfWeek - 1;// 1:星期天 2:星期一
    }

    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(date);
        } catch (Exception e) {
            Log.w("Sonar Catch Exception", e);
            e.printStackTrace();
        }
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 ? 7 : dayOfWeek - 1;// 1:星期天 2:星期一
    }

    public static String getStringFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 获取与某个日期相隔指定天数的日期
     *
     * @param date
     * @param day
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public static String getSomeDay(String date, int day, String inputFormat, String outputFormat) {
        Date date1 = getFormatDate(date, inputFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DATE, day);
        return getFormatDateString(calendar.getTime(), outputFormat);
    }

    /**
     * 格式"yyyy-MM-dd" 转yyyy年mm月dd日
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     */
    public static String strToDateFormat(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
            formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        } catch (ParseException e) {
            Log.w("Sonar Catch Exception", e);
            e.printStackTrace();
        }
        return formatter.format(newDate);
    }


    /**
     * string to date
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * @param date
     * @param sformat
     * @return
     */
    public static String getUserDate(Date date, String sformat) {
        SimpleDateFormat formatter = new SimpleDateFormat(sformat, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(DateManager.getInstance().getCurrentDate());
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(DateManager.getInstance().getCurrentDate());
        return cd.get(Calendar.MONTH);
    }

    /**
     * 判断是否当天
     *
     * @param date
     * @return 是否当天
     */
    public static boolean isToday(String date) {
        String today = getCurrentDateTime("yyyyMMdd");
        return today.equals(date);
    }




    public static class DateManager {

        /**
         * 时间误差
         */
        private static final long TIME_ERROR = 30 * 1000;
        private static SimpleDateFormat simpleDateFormat = null;

        static {
            if (simpleDateFormat == null) {
                simpleDateFormat = new SimpleDateFormat();
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            }
        }

        /**
         * 服务器时间戳
         */
        private long mServerStamp = 0L;
        /**
         * 获取服务器时的系统已启动时间
         */
        private long mServerElapsedRealtime;
        /**
         * 时间是否差异
         */
        private boolean isDateDifferent;

        /**
         * 构造方法
         */
        private DateManager() {
            // do something
        }

        /**
         * 获取单例方法
         *
         * @return DateManager
         */
        public static DateManager getInstance() {
            return DateManagerHolder.sDateManager;
        }

        /**
         * 设置
         *
         * @param serverStampStr
         */
        public void setServerStamp(String serverStampStr) {
            // 判断时间戳是否为空
            if (StringUtils.isEmptyWithBlank(serverStampStr)) {
                return;
            }
            long serverStamp = 0L;
            // 转换服务器时间戳
            try {
                serverStamp = Long.parseLong(serverStampStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                LogUtils.e("date", "服务器时间戳转换异常:" + e.toString());
                isDateDifferent = false;
                mServerStamp = 0L;
                return;
            }
            // 判断服务器时间与本地时间误差是否在允许范围内，如果在范围内则使用本地时间
            Date currentDate = new Date();
            long currentLocalStamp = currentDate.getTime();
            if (Math.abs(serverStamp - currentLocalStamp) >= TIME_ERROR) {
                // 时间超出范围，有差异
                isDateDifferent = true;
                mServerStamp = serverStamp;
                // 记录当前系统启动时间
                mServerElapsedRealtime = SystemClock.elapsedRealtime();
                LogUtils.i("date", "机器本地时间异常，使用服务端时间用来请求");
            } else {
                isDateDifferent = false;
                mServerStamp = 0L;
            }
            // 如果时区不正确则打印一下
            TimeZone tz = TimeZone.getDefault();
            if (!"GMT+08:00".equals(tz.getDisplayName(false, TimeZone.SHORT))) {
                // 记录当前时区
                LogUtils.e("date", "当前设备的时区不是GMT+08:00！(时区:" + tz.getDisplayName(false, TimeZone.SHORT) + " 编号:" + tz.getID() + ")");
            }
        }

        /**
         * 获取当前日期
         *
         * @return Date
         */
        public Date getCurrentDate() {
            // 如果本地时间无差异或者服务器时间戳为0则使用本地时间
            if (!isDateDifferent || mServerStamp == 0L) {
                return new Date();
            }
            // 计算时间差
            long dx = SystemClock.elapsedRealtime() - mServerElapsedRealtime;
            // 创建Date类
            return new Date(mServerStamp + dx);
        }

        /**
         * 获取当前时间戳
         *
         * @return currentStamp
         */
        public long getCurrentDateStamp() {
            long currentStamp = System.currentTimeMillis();
            Date date = getCurrentDate();
            if (date != null) {
                currentStamp = date.getTime();
            }
            return currentStamp;
        }

        /**
         * 获取当前日期时间并格式化
         *
         * @param format 格式化样式
         * @return String
         */
        public String getCurrentDateTime(String format) {
            simpleDateFormat.applyPattern(format);
            return simpleDateFormat.format(getCurrentDate());
        }

        /**
         * 静态内部类
         *
         * @author 17093029
         * @name DateManagerHolder
         * @date 2019/9/24
         * @package com.cnsuning.mobile.apos.module.date
         */
        private static class DateManagerHolder {
            /**
             * 单例对象
             */
            private static final DateManager sDateManager = new DateManager();
        }

    }

}
