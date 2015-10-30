package me.shreyasr.ancients.components.weapon;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.PositionComponent;

public class HitboxGenerator {

    public enum AttackType { STAB, SLASH }

    private int weapWidth;
    private int weapLength;
    private int stabReach;
    private int animFrameSize;
    private AttackType attackType;

    public static HitboxGenerator create(int weapWidth, int weapLength, int stabReach, int animFrameSize, AttackType attackType) {
        HitboxGenerator hitboxGenerator = new HitboxGenerator();
        hitboxGenerator.weapWidth = weapWidth;
        hitboxGenerator.weapLength = weapLength;
        hitboxGenerator.stabReach = stabReach;
        hitboxGenerator.attackType = attackType;
        hitboxGenerator.animFrameSize = animFrameSize;
        return hitboxGenerator;
    }

    public void updateHitbox(HitboxComponent hitbox, int dir, PositionComponent pos) {
        int dx;
        int dy;
        switch (attackType) {
            case SLASH:
                dx = dir>=7||dir<=1 ? 1 : (dir>=3&&dir<=5)?-1:0;
                dy = dir>=1&&dir<=3 ? 1 : (dir>=5&&dir<=7)?-1:0;

                hitbox.x = 64 * dx + pos.x - 32 + (dx < 0 ? weapWidth-weapLength : 0);
                hitbox.y = 64 * dy + pos.y - 32 + (dy < 0 ? weapWidth-weapLength : 0);
                hitbox.w = dx == 0 ? weapWidth : weapLength;
                hitbox.h = dy == 0 ? weapWidth : weapLength;
                break;
            case STAB:
                dx = dir==0||dir==1 ? 1 : (dir==4||dir==5)?-1:0;
                dy = dir==2||dir==3 ? 1 : (dir==6||dir==7)?-1:0;
                boolean stabbing = dir%2==1;

                hitbox.x = 64 * dx + pos.x - 32;
                hitbox.y = 64 * dy + pos.y - 32;
                hitbox.w = stabbing && dx != 0 ? stabReach : weapWidth;
                hitbox.h = stabbing && dy != 0 ? stabReach : weapWidth;
                break;
        }
    }
}
