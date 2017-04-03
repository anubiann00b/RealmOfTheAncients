# Realm Of The Ancients

Multiplayer cross platform game.

## Controls

Key | Description
----------|------------
WASD | Move around (`w`: up, `a`: left, `s`: down, `d`: right)
Left click |  Attack (in direction of cursor)
Right click | Dash (in direction of cursor)
Tab | Show scoreboard
R | Change weapon 
Enter | Open and focus chat
Esc | Unfocus chat
Shift + Esc | Hide chat

## Gameplay

Players move around and attempt to hit each other with their weapons.

## Strategy

All of the weapons have their own advantages and disadvantages. Here are some rough strategy guides:

### Dagger

 * The dagger rapidly hits a single tile in one of the 4 cardinal directions. Its dash is a small blink.
 * The dagger hits very rapidly. If you get close to your victim, you can hit them a handful of times before they can respond. 
 * The blink allows you to escape from, catch up to, and to circle a target. You can blink out of the way of a sword dash. You can blink towards or behind a spear wielding player. You can blink in circles around a player to avoid getting hit.
 * The dagger hits a very small area. You have to be very close to someone to hit them.
 * The blink doesn't go very far. You have to use it well for positional advantage.

### Sword

 * The sword hits three connected tiles around the player. Its dash is a charge where it spins in a circle around the player.
 * The sword can hit diagonals. Use this to your advantage against dagger and spear opponents, who can only hit in the cardinal directions.
 * The dash allows you to hit multiple targets or catch up to a far away one. Be careful not to come from an angle where you can get stabbed by a spear.
 * You don't attack very fast, and you don't have the range of the spear. You have to use your diagonals to their full potential to avoid getting hit.

### Spear

 * The spear hits two tiles in a cardinal direction in a line away from the player. Its dash is a lunge with the weapon extended in front.
 * The spear has more range than any other weapon. Use that to your advantage to run and poke your opponents from afar.
 * The lunge can go through players, or hit someone further away.
 * The spear can't hit diagonals, and has a low attack rate. Try not to miss!
 * The lunge has a long stun time, where you're vulnerable.

### General gameplay advice
 * If you go off the screen, you can right click in the center to dash back.
 * Be aware of your opponents' strengths and weaknesses.
 * Try to anticipate and avoid dashes. You can use your dash to avoid someone else's, or dodge and punish them while it's down.

## Stats

Currently, players can change their own attack and dash statistics. This is in an effort to maximize balance and fun.

### Attack

##### Cooldown — Time before you can attack again (ms)
- Increase Cooldown `]`
- Decrease Cooldown `'`

##### Swing — Time of each frame of a swing (ms)
- Increase Swing `[`
- Decrease Swing `;`

##### Hold — Time of the last frame of a swing (ms)
- Increase Hold: `p`
- Decrease Hold: `l`

##### Knockback — Distance your swing knocks someone back (px)
- Increase Knockback: `o`
- Decrease Knockback: `k`

### Dash

##### Duration — Time it takes for a dash (ms)
- Increase Duration: `i`
- Decrease Duration: `j`

##### Distance — Distance covered by a dash (px)
- Increase Distance: `u`
- Decrease Distance: `h`

##### Cooldown — Time before a dash can be used again again (ms)
- Increase Cooldown: `y`
- Decrease Cooldown: `g`

##### Stun — Time incapacitated after a dash (ms)
- Increase Stun: `t`
- Decrease Stun: `f`

## Contributors

- [anubiann00b](https://github.com/anubiann00b): Wrote all the code.
- [onlineth](https://github.com/onlineth): Wrote the first revision of this README.
- [xrisk](https://github.com/xrisk): Added periods to onlineth's sentences.
