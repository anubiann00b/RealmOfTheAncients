package me.shreyasr.ancients;

import com.esotericsoftware.kryonet.Listener;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LinkedListQueuedListener extends Listener.QueuedListener {

    private final ScheduledExecutorService threadPool;
    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

    public LinkedListQueuedListener() {
        this(null);
    }

    public LinkedListQueuedListener(Listener listener) {
        super(listener);
        threadPool = Executors.newScheduledThreadPool(1);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void runAll() {
        while (!queue.isEmpty()) {
            runOne();
        }
    }

    public void runOne() {
        Runnable r = queue.poll();
        if (r != null) {
            r.run();
        }
    }

    public int getQueueSize() {
        return queue.size();
    }

    @Override
    protected void queue(final Runnable r) {
        if (Math.random() < 0.1) {
            threadPool.schedule(new Runnable() {
                public void run() {
                    queue.add(r);
                }
            }, 500, TimeUnit.MILLISECONDS);
        }
    }
}
