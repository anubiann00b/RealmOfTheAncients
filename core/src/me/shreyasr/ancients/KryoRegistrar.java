package me.shreyasr.ancients;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.kryo.Kryo;
import me.shreyasr.ancients.components.*;
import me.shreyasr.ancients.packet.ClientPlayerRemovePacket;
import me.shreyasr.ancients.packet.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.packet.ServerPlayerUpdatePacket;

public class KryoRegistrar {

    public static void register(Kryo kryo) {
        kryo.register(ClientPlayerUpdatePacket.class);
        kryo.register(ClientPlayerRemovePacket.class);
        kryo.register(ServerPlayerUpdatePacket.class);

        kryo.register(Component[].class);
        kryo.register(CustomUUID.class);

        kryo.register(LastUpdateTimeComponent.class);
        kryo.register(MyPlayerComponent.class);
        kryo.register(PositionComponent.class);
        kryo.register(SpeedComponent.class);
        kryo.register(SquareAnimationComponent.class);
        kryo.register(SquareDirectionComponent.class);
        kryo.register(SquareDirectionComponent.Direction.class);
        kryo.register(TextureComponent.class);
        kryo.register(TextureTransformComponent.class);
        kryo.register(UUIDComponent.class);
        kryo.register(VelocityComponent.class);
    }
}
