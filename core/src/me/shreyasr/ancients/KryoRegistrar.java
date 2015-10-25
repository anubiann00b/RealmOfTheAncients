package me.shreyasr.ancients;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.kryo.Kryo;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SpeedComponent;
import me.shreyasr.ancients.components.SquareAnimationComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;
import me.shreyasr.ancients.packet.client.ClientPlayerRemovePacket;
import me.shreyasr.ancients.packet.client.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.packet.server.ServerPlayerUpdatePacket;

public class KryoRegistrar {

    public static void register(Kryo kryo) {
        kryo.register(ClientPlayerUpdatePacket.class);
        kryo.register(ClientPlayerRemovePacket.class);
        kryo.register(ServerPlayerUpdatePacket.class);

        kryo.register(Component[].class);
        kryo.register(CustomUUID.class);

        kryo.register(HitboxComponent.class);
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

        kryo.register(OwnerUUIDComponent.class);
        kryo.register(WeaponAnimationComponent.class);

        kryo.register(TypeComponent.Player.class);
        kryo.register(TypeComponent.Weapon.class);

        kryo.register(ServerAttackPacket.class);
        kryo.register(ClientAttackPacket.class);
    }
}
