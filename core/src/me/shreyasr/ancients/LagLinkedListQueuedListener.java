package me.shreyasr.ancients;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LagLinkedListQueuedListener extends LinkedListQueuedListener {

    private final ScheduledExecutorService threadPool;
    private int lag;
    private int loss;

    public LagLinkedListQueuedListener(int lag, int loss) {
        super();
        this.lag = lag;
        this.loss = loss;
        threadPool = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void queue(final Runnable r) {
        if (Math.random() >= loss) {
            threadPool.schedule(new Runnable() {
                public void run() {
                    queue.add(r);
                }
            }, (long) (lag + lag * Math.random()), TimeUnit.MILLISECONDS);
        }
    }
}
