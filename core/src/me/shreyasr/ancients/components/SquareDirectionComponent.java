package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class SquareDirectionComponent implements Component, Pool.Poolable {

    public static ComponentMapper<SquareDirectionComponent> MAPPER
            = ComponentMapper.getFor(SquareDirectionComponent.class);

    public static SquareDirectionComponent create() {
        return new SquareDirectionComponent();
    }

    public enum Direction {

        RIGHT(0), UP(1), LEFT(2), DOWN(3);

        public final int index;

        Direction(int index) {
            this.index = index;
        }

        public int getX() {
            return -(index-1) % 2;
        }

        public int getY() {
            return -(index-2) % 2;
        }
    }



    public Direction dir;

    public SquareDirectionComponent() {
        reset();
    }

    @Override
    public void reset() {
        dir = Direction.DOWN;
    }
}