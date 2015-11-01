package me.shreyasr.ancients.components.player.dash;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.player.attack.Attack;

public class DashComponent implements Component, Pool.Poolable {

    public static ComponentMapper<DashComponent> MAPPER
            = ComponentMapper.getFor(DashComponent.class);
    public static DashComponent create(DashBehavior behavior, Attack attack, int cooldown, int duration, float distance, int stunTime, boolean square) {
        DashComponent c = new DashComponent();
        c.behavior = behavior;
        c.attack = attack;
        c.cooldown = cooldown;
        c.duration = duration;
        c.distance = distance;
        c.stunTime = stunTime;
        c.square = square;
        return c;
    }

    public DashBehavior behavior;

    public Attack attack;
    public long startTime;

    public int duration;
    public float distance;
    public int cooldown;
    public int stunTime;

    public boolean square;
    public float dx;
    public float dy;
    public boolean firstFrame;

    public void start(float dx, float dy, long time) {
        firstFrame = true;
        startTime = time;
        if (square) {
            if (Math.abs(dx) >= Math.abs(dy)) {
                this.dx = dx;
                this.dy = 0;
            } else {
                this.dx = 0;
                this.dy = dy;
            }
        } else {
            this.dx = dx;
            this.dy = dy;
        }
        normalize();
    }

    private void normalize() {
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        dx = dx / len;
        dy = dy / len;
    }

    public boolean inFuture() {
        return startTime > Time.getServerMillis();
    }

    public boolean isActive() {
        return startTime <= Time.getServerMillis()
                && Time.getServerMillis() <= startTime + duration;
    }

    public boolean isStunned() {
        return startTime <= Time.getServerMillis()
                && Time.getServerMillis() <= startTime + duration + stunTime;
    }

    public boolean isReady() {
        return startTime + duration + cooldown <= Time.getServerMillis();
    }

    public DashComponent() {
        reset();
    }

    @Override
    public void reset() {
        behavior = null;
        attack = null;
        startTime = -1;
        cooldown = 0;
        duration = 0;
        distance = 0;
        stunTime = 0;
        dx = 0;
        dy = 0;
        firstFrame = true;
    }
}
