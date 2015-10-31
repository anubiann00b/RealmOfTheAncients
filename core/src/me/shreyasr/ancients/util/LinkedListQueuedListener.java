package me.shreyasr.ancients.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class LinkedListQueuedListener extends Listener {

    protected final Queue<QueueElementWrapper> queue = new PriorityBlockingQueue<QueueElementWrapper>();
    protected final Queue<QueueElementWrapper> temp = new ConcurrentLinkedQueue<QueueElementWrapper>();
    private Listener listener;

    public LinkedListQueuedListener(Listener listener) {
        this.listener = listener;
    }

    int iter = 0;

    public void updateQueue() {
        iter++;
        synchronized (temp) {
            queue.addAll(temp);
            temp.clear();
        }
    }

    public void runOne() {
        QueueElementWrapper r = queue.poll();
        r.run();
    }

    public int getQueueSize() {
        return queue.size();
    }

    protected void queue(final QueueElementWrapper r) {
        synchronized (temp) {
            temp.add(r);
        }
    }

    public abstract static class QueueElementWrapper implements Runnable, Comparable<QueueElementWrapper> {

        private static HashMap<Class, Integer> processOrder = new HashMap<Class, Integer>();

        public static void putClassPriority(Class cls, int priority) {
            processOrder.put(cls, priority);
        }

        private Object obj;

        public QueueElementWrapper(Object obj) {
            this.obj = obj;
        }

        @Override
        public int compareTo(QueueElementWrapper o) {
            if (o == null) return 1;
            if (obj == null && o.obj == null) return 0;
            if (o.obj == null) return 1;
            if (obj == null) return -1;

            Integer myPriority = processOrder.get(obj.getClass());
            Integer otherPriority = processOrder.get(o.obj.getClass());

            if (myPriority == null && otherPriority == null) return 0;
            if (otherPriority == null) return 1;
            if (myPriority == null) return -1;

            return Integer.compare(myPriority, otherPriority);
        }
    }

    @Override
    public void connected(final Connection connection) {
        queue(new QueueElementWrapper(null) {
            public void run() {
                listener.connected(connection);
            }
        });
    }

    @Override
    public void disconnected(final Connection connection) {
        queue(new QueueElementWrapper(null) {
            public void run() {
                listener.disconnected(connection);
            }
        });
    }

    @Override
    public void received(final Connection connection, final Object object) {
        queue(new QueueElementWrapper(object) {
            public void run() {
                listener.received(connection, object);
            }
        });
    }

    @Override
    public void idle(final Connection connection) {
        queue(new QueueElementWrapper(null) {
            public void run() {
                listener.idle(connection);
            }
        });
    }
}
