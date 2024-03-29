package com.chensoul.date;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期区间格式化类，用于格式化输出两个日期相差的时间长度
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
public final class BetweenFormatter implements Serializable {

    private static final long serialVersionUID = 1446611117930620343L;
    private final int levelMaxCount;

    /**
     * 时长毫秒数
     */
    private long betweenMs;

    /**
     * 格式化级别
     */
    private Level level;

    /**
     * 构造
     *
     * @param betweenMs 日期间隔
     * @param level     级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
     */
    public BetweenFormatter(final long betweenMs, final Level level) {
        this(betweenMs, level, 0);
    }

    /**
     * 构造
     *
     * @param betweenMs     日期间隔
     * @param level         级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
     * @param levelMaxCount 格式化级别的最大个数，假如级别个数为1，但是级别到秒，那只显示一个级别
     */
    public BetweenFormatter(final long betweenMs, final Level level, final int levelMaxCount) {
        this.betweenMs = betweenMs;
        this.level = level;
        this.levelMaxCount = levelMaxCount;
    }

    /**
     * 格式化日期间隔输出<br>
     * 根据时间间隔计算天、小时、分钟、秒和毫秒，并将计算结果以字符串形式返回<br>
     *
     * @return 格式化后的字符串
     */
    public String format() {
        final StringBuilder sb = new StringBuilder();
        if (betweenMs > 0) {
            final long day = betweenMs / DateUnit.DAY.getMillis();
            final long hour = betweenMs / DateUnit.HOUR.getMillis() - day * 24;
            final long minute = betweenMs / DateUnit.MINUTE.getMillis() - day * 24 * 60 - hour * 60;


            final int level = this.level.ordinal();
            int levelCount = 0;

            if (isLevelCountValid(levelCount) && 0 != day && level >= Level.DAY.ordinal()) {
                sb.append(day).append(Level.DAY.name);
                levelCount++;
            }
            if (isLevelCountValid(levelCount) && 0 != hour && level >= Level.HOUR.ordinal()) {
                sb.append(hour).append(Level.HOUR.name);
                levelCount++;
            }
            if (isLevelCountValid(levelCount) && 0 != minute && level >= Level.MINUTE.ordinal()) {
                sb.append(minute).append(Level.MINUTE.name);
                levelCount++;
            }

            final long betweenOfSecond = ((day * 24 + hour) * 60 + minute) * 60;
            final long second = betweenMs / DateUnit.SECOND.getMillis() - betweenOfSecond;
            if (isLevelCountValid(levelCount) && 0 != second && level >= Level.SECOND.ordinal()) {
                sb.append(second).append(Level.SECOND.name);
                levelCount++;
            }

            final long millisecond = betweenMs - (betweenOfSecond + second) * 1000;
            if (isLevelCountValid(levelCount) && 0 != millisecond && level >= Level.MILLISECOND.ordinal()) {
                sb.append(millisecond).append(Level.MILLISECOND.name);
                // levelCount++;
            }
        }

        if (StringUtils.isEmpty(sb)) {
            sb.append(0).append(level.name);
        }

        return sb.toString();
    }


    /**
     * 获得 时长毫秒数
     *
     * @return 时长毫秒数
     */
    public long getBetweenMs() {
        return betweenMs;
    }

    /**
     * 设置 时长毫秒数
     *
     * @param betweenMs 时长毫秒数
     */
    public void setBetweenMs(final long betweenMs) {
        this.betweenMs = betweenMs;
    }

    /**
     * 获得 格式化级别
     *
     * @return 格式化级别
     */
    public Level getLevel() {
        return level;
    }

    /**
     * 设置格式化级别
     *
     * @param level 格式化级别
     */
    public void setLevel(final Level level) {
        this.level = level;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return format();
    }

    /**
     * 判断等级数量是否有效<br>
     * 有效的定义是：levelMaxCount大于0（被设置），当前等级数量没有超过这个最大值
     *
     * @param levelCount 等级数量
     * @return 是否有效
     */
    private boolean isLevelCountValid(final int levelCount) {
        return levelMaxCount <= 0 || levelCount < levelMaxCount;
    }

    /**
     * 级别枚举
     *
     * @author Looly
     */
    public enum Level {

        /**
         * 天
         */
        DAY("天"),
        /**
         * 小时
         */
        HOUR("小时"),
        /**
         * 分钟
         */
        MINUTE("分"),
        /**
         * 秒
         */
        SECOND("秒"),
        /**
         * 毫秒
         */
        MILLISECOND("毫秒");

        /**
         * 级别名称
         */
        private final String name;

        /**
         * 构造
         *
         * @param name 级别名称
         */
        Level(final String name) {
            this.name = name;
        }

        /**
         * 获取级别名称
         *
         * @return 级别名称
         */
        public String getName() {
            return name;
        }

    }

}

