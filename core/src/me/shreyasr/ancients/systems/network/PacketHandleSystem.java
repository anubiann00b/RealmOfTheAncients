package me.shreyasr.ancients.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import me.shreyasr.ancients.util.LinkedListQueuedListener;

public class PacketHandleSystem extends EntitySystem implements EntityListener {

    private final LinkedListQueuedListener queuedListener;

    public PacketHandleSystem(int priority, LinkedListQueuedListener queuedListener) {
        super(priority);
        this.queuedListener = queuedListener;
    }

    private boolean breakingModification;

    @Override
    public void update(float deltaTime) {
        breakingModification = false;
        while (queuedListener.getQueueSize() > 0) {
            queuedListener.runOne();
            if (breakingModification) {
                System.out.println("Breaking mod");
                break;
            }
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        breakingModification = true;
    }

    @Override
    public void entityRemoved(Entity entity) {
        breakingModification = true;
    }
}
