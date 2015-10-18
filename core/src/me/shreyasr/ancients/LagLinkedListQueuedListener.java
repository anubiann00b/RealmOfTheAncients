package me.shreyasr.ancients;

import com.esotericsoftware.kryonet.Listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LagLinkedListQueuedListener extends LinkedListQueuedListener {

    private final ScheduledExecutorService threadPool;

    public LagLinkedListQueuedListener(Listener listener) {
        super(listener);
        threadPool = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void queue(final Runnable r) {
        threadPool.schedule(new Runnable() {
            public void run() {
                queue.add(r);
            }
        }, 100, TimeUnit.MILLISECONDS);
    }
}
