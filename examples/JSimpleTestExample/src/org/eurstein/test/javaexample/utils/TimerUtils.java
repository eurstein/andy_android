
package org.eurstein.test.javaexample.utils;

import java.util.LinkedHashMap;

/**
 * 用于统计时间
 * 
 * @author andygzyu
 */
public class TimerUtils {

    public static class TagTimer {
        private long startTime = 0;
        private LinkedHashMap<String, Long> times = new LinkedHashMap<String, Long>();

        /**
         * 设置初试时间
         */
        public void setStartTimeToNow() {
            startTime = System.currentTimeMillis();
        }

        /**
         * 标记并记录调用间隔 (此次调用时间 - 上次setStartTimeToNow/tapAndSetStartTimeToNow调用时间)
         * 
         * @param eventTag 时间间隔标记
         */
        public void tapAndSetStartTimeToNow(String eventTag) {
            long nowTime = System.currentTimeMillis();
            times.put(eventTag, nowTime - startTime);
            startTime = nowTime;
        }
        
        public void reset() {
            startTime = 0;
            times.clear();
        }
        
        @Override
        public String toString() {
            return times.toString();
        }
    }

    public static class TagStatisticsTimer {
        private LinkedHashMap<String, Long> times = new LinkedHashMap<String, Long>();
        private long startTime = 0;

        /**
         * 设置初试时间
         */
        public void setStartTimeToNow() {
            startTime = System.currentTimeMillis();
        }

        /**
         * 标记并累积同一事件调用间隔 (此次调用时间 - 上次setStartTimeToNow/tapAndSetStartTimeToNow调用时间)
         * 
         * @param eventTag
         */
        public void tapAndSetStartTimeToNow(String eventTag) {
            long nowTime = System.currentTimeMillis();
            Long lastTime = times.get(eventTag);
            if (lastTime == null) {
                lastTime = 0L;
            }
            times.put(eventTag, (nowTime - startTime) + lastTime);
            startTime = nowTime;
        }

        public void reset() {
            startTime = 0;
            times.clear();
        }
        
        @Override
        public String toString() {
            return times.toString();
        }
    }
}
