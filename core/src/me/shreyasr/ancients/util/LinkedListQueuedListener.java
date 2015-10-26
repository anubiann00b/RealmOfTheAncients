package me.shreyasr.ancients.util;

import com.esotericsoftware.kryonet.Listener;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedListQueuedListener extends Listener.QueuedListener {

    protected final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

    public LinkedListQueuedListener() {
        this(null);
    }

    public LinkedListQueuedListener(Listener listener) {
        super(listener);
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
        queue.add(r);
    }
}
