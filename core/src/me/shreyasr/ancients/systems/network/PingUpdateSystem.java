package me.shreyasr.ancients.systems.network;

import com.badlogic.ashley.systems.IntervalSystem;
import com.esotericsoftware.kryonet.Client;

public class PingUpdateSystem extends IntervalSystem {

    private Client client;

    public PingUpdateSystem(int priority, Client client) {
        super(500, priority);
        this.client = client;
        updateInterval();
    }

    @Override
    protected void updateInterval() {
        client.updateReturnTripTime();
    }
}
