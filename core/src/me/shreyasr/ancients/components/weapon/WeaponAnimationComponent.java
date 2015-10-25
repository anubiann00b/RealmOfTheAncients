package me.shreyasr.ancients.components.weapon;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class WeaponAnimationComponent implements Component, Pool.Poolable {

    public static ComponentMapper<WeaponAnimationComponent> MAPPER
            = ComponentMapper.getFor(WeaponAnimationComponent.class);

    public static WeaponAnimationComponent create(PooledEngine engine, int length,
                                                  int frameSize, int startFrame, int numFrames,
                                                  int swingTime, int lastFrameHoldTime) {
        WeaponAnimationComponent anim = engine.createComponent(WeaponAnimationComponent.class);
        anim.length = length;
        anim.frameSize = frameSize;
        anim.startFrame = startFrame;
        anim.numFrames = numFrames;
        anim.swingFrameTime = swingTime;
        anim.holdFrameTime = lastFrameHoldTime;
        return anim;
    }

    public int length; // number of frames in the animation
    public int frameSize;  // width and height of a frame, in pixels

    public int startFrame; // starting frame of animation
    public int numFrames; // number of frames to animate through

    public int swingFrameTime; // milliseconds per frame of the swing
    public int holdFrameTime; // milliseconds to hold the last frame
    public int timeSinceAnimStart; // milliseconds since the start of the animation

    public int getCurrentFrame() {
        int swungFrames = Math.min(numFrames-1, timeSinceAnimStart / swingFrameTime);
        return (startFrame + swungFrames) % length;
    }

    public boolean isDone() {
        return timeSinceAnimStart > swingFrameTime*(numFrames-1) + holdFrameTime;
    }

    public boolean isStarted() {
        return timeSinceAnimStart >= 0;
    }

    public boolean isAnimating() {
        return !isDone() && isStarted();
    }

    public WeaponAnimationComponent() {
        reset();
    }

    @Override
    public void reset() {
        length = 0;
        frameSize = 0;
        startFrame = 0;
        numFrames = 0;
        swingFrameTime = 0;
        holdFrameTime = 0;
        timeSinceAnimStart = 0;
    }
}
