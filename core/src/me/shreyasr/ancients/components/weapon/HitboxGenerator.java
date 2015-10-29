package me.shreyasr.ancients.components.weapon;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.PositionComponent;

public class HitboxGenerator {

    public enum AttackType { STAB, SLASH }

    private int weapSize;
    private int stabReach;
    private int animFrameSize;
    private AttackType attackType;

    public static HitboxGenerator create(int weapSize, int stabReach, int animFrameSize, AttackType attackType) {
        HitboxGenerator hitboxGenerator = new HitboxGenerator();
        hitboxGenerator.weapSize = weapSize;
        hitboxGenerator.stabReach = stabReach;
        hitboxGenerator.attackType = attackType;
        hitboxGenerator.animFrameSize = animFrameSize;
        return hitboxGenerator;
    }

    public void updateHitbox(HitboxComponent hitbox, int frame, PositionComponent pos) {
        int dx;
        int dy;
        switch (attackType) {
            case SLASH:
                dx = frame>=7||frame<=1 ? 1 : (frame>=3&&frame<=5)?-1:0;
                dy = frame>=1&&frame<=3 ? 1 : (frame>=5&&frame<=7)?-1:0;

                hitbox.x = 64 * dx + pos.x - animFrameSize*2 + 64;
                hitbox.y = 64 * dy + pos.y - animFrameSize*2 + 64;
                hitbox.w = weapSize;
                hitbox.h = weapSize;
                break;
            case STAB:
                dx = frame==0||frame==1 ? 1 : (frame==4||frame==5)?-1:0;
                dy = frame==2||frame==3 ? 1 : (frame==6||frame==7)?-1:0;
                boolean stabbing = frame%2==1;

                hitbox.x = 64 * dx + pos.x - stabReach;
                hitbox.y = 64 * dy + pos.y - stabReach;
                hitbox.w = stabbing && dx != 0 ? stabReach : weapSize;
                hitbox.h = stabbing && dy != 0 ? stabReach : weapSize;
                break;
        }
    }
}
