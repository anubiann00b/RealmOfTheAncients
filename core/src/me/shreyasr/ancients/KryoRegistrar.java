package me.shreyasr.ancients;

import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryo.Kryo;

public class KryoRegistrar {

    public static void register(Kryo kryo) {
        kryo.register(ImmutableArray.class);
    }
}
