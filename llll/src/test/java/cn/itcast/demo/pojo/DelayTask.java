package cn.itcast.demo.pojo;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时执行的任务， D是任务相关数据
 * @author 虎哥
 */
@Data
public class DelayTask<D> implements Delayed {
    /**
     * 任务执行时间，标准时间1970开始的毫秒值
     */
    private long executeTime;
    /**
     * 任务中需要的数据
     */
    private D data;

    public DelayTask(long executeTime, D data) {
        this.executeTime = executeTime;
        this.data = data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // 用执行时间减去当前时间，得到剩余时间  比方说 我要 30 执行 那么现在是20 剩余了10
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        // 比较剩余时间的大小
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));  //现在是 当前是2 0 2 - o 1 是降序
    }
}