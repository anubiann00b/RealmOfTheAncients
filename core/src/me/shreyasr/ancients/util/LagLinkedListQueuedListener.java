package me.shreyasr.ancients.util;

import com.esotericsoftware.kryonet.Listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LagLinkedListQueuedListener extends LinkedListQueuedListener {

    private final ScheduledExecutorService threadPool;
    private int lag;
    private int error;
    private double loss;

    public LagLinkedListQueuedListener(Listener listener, int lag, int error, double loss) {
        super(listener);
        this.lag = lag;
        this.error = error;
        this.loss = loss;
        threadPool = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void queue(final QueueElementWrapper r) {
        if (Math.random() >= loss) {
            threadPool.schedule(new Runnable() {
                public void run() {
                    synchronized (temp) {
                        temp.add(r);
                    }
                }
            }, (long) (lag + error * Math.random()), TimeUnit.MILLISECONDS);
        }
    }
}
