package me.shreyasr.ancients;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LagLinkedListQueuedListener extends LinkedListQueuedListener {

    private final ScheduledExecutorService threadPool;
    private int lag;

    public LagLinkedListQueuedListener(int lag) {
        super();
        this.lag = lag;
        threadPool = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void queue(final Runnable r) {
        if (Math.random() < 0.5) {
            threadPool.schedule(new Runnable() {
                public void run() {
                    queue.add(r);
                }
            }, (long) (lag + lag * Math.random()), TimeUnit.MILLISECONDS);
        }
    }
}
