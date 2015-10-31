package me.shreyasr.ancients.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.minlog.Log;

import java.util.Arrays;
import java.util.HashMap;

import me.shreyasr.ancients.components.NameComponent;

public class CommandHandler {

    private static final HashMap<String, CommandProcessor> processors;

    static {
        processors = new HashMap<String, CommandProcessor>();
//        processors.put("/setCooldown", new EntityValueSetCommandProcessor() {
//            @Override
//            void setEntityVal(Entity entity, int val) {
//                AttackComponent atk = AttackComponent.MAPPER.get(entity);
//                if (atk != null && atk.attack instanceof BasicWeaponAttack) {
//                    ((BasicWeaponAttack)atk.attack).cooldownTime = val;
//                }
//            }
//        });
    }

    public static void process(String commandStr, Engine engine) {
        String commandName;
        String[] params;
        if (!commandStr.contains(" ")) {
            commandName = commandStr;
            params = new String[0];
        } else {
            commandName = commandStr.substring(0, commandStr.indexOf(' '));
            params = commandStr.substring(commandStr.indexOf(' ')+1).split(" ");
        }

        CommandProcessor processor = processors.get(commandName);
        Log.info("Processing: " + processor + " with " + Arrays.toString(params));
        if (processor != null) processor.process(params, engine);
    }

    private static abstract class CommandProcessor {

        abstract void process(String[] params, Engine engine);
    }

    private static abstract class EntityCommandProcessor extends CommandProcessor {

        private final Family nameFamily = Family.all(NameComponent.class).get();

        void process(String[] params, Engine engine) {
            if (params.length < 1) return;
            String entityName = params[0];

            String[] newParams = Arrays.copyOfRange(params, 1, params.length);

            ImmutableArray<Entity> namedEntities = engine.getEntitiesFor(nameFamily);
            for(Entity e : namedEntities) {
                if (NameComponent.MAPPER.get(e).str.equals(entityName)) {
                    processEntity(e, newParams, engine);
                    break;
                }
            }
        }

        abstract void processEntity(Entity entity, String[] params, Engine engine);
    }

    private static abstract class EntityValueSetCommandProcessor extends EntityCommandProcessor {

        void processEntity(Entity entity, String[] params, Engine engine) {
            if (params.length == 0) return;
            try {
                setEntityVal(entity, Integer.valueOf(params[0]));
            } catch (NumberFormatException ignored) { }
        }

        abstract void setEntityVal(Entity entity, int val);
    }
}
