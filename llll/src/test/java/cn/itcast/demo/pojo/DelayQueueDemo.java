package cn.itcast.demo.pojo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 虎哥
 */
@Slf4j
public class DelayQueueDemo {

    private ExecutorService es = Executors.newFixedThreadPool(3);
    @Test
    public void testDelayQueue() throws InterruptedException {
        // 创建一个延迟队列
        DelayQueue<DelayTask<String>> queue = new DelayQueue<>();

        // 开始执行任务
        es.submit(() ->{
            while (true){
                try {
                    log.info("尝试获取任务。。。");
                    DelayTask<String> task = queue.take();
                    log.warn("获取到任务{}", task.getData());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        // 开始向队列中添加任务
        es.submit(() -> {
            log.error("开始添加任务。。。");
            for (int i = 1; i <= 1000; i++) {
                queue.add(new DelayTask<String>(System.currentTimeMillis() + 1000 * i, "task_" + i ));
            }
            log.error("任务添加结束。。。");
        });

        // 等待任务执行完毕
        log.info("主函数任务结束。。。");
        Thread.sleep(1000000);
    }
}