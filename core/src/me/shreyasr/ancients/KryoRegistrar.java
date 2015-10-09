package me.shreyasr.ancients;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import me.shreyasr.ancients.components.*;
import me.shreyasr.ancients.packet.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.packet.ServerPlayerUpdatePacket;

public class KryoRegistrar {

    public static void register(Kryo kryo) {
        kryo.register(ClientPlayerUpdatePacket.class);
        kryo.register(ServerPlayerUpdatePacket.class);
        kryo.register(Component[].class);
        kryo.register(Array.class);
        kryo.register(Object[].class);
        kryo.register(PeerComponent.class);
        kryo.register(PlayerComponent.class);
        kryo.register(PositionComponent.class);
        kryo.register(SpeedComponent.class);
        kryo.register(SquareAnimationComponent.class);
        kryo.register(SquareDirectionComponent.class);
        kryo.register(SquareDirectionComponent.Direction.class);
        kryo.register(TextureComponent.class);
        kryo.register(TextureTransformComponent.class);
        kryo.register(VelocityComponent.class);
    }
}
