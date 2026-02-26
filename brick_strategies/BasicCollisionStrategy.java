package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * The BasicCollisionStrategy class implements the CollisionStrategy interface.
 * It provides a basic implementation for handling collisions between game objects
 * in the Bricker game.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    private final Counter bricksCounter;

    /**
     * Constructs a new BasicCollisionStrategy instance.
     *
     * @param brickerGameManager The game manager that handles the overall game state and behavior.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager, Counter bricksCounter) {
        this.brickerGameManager = brickerGameManager;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Handles the collision between two game objects. If the objects should collide,
     * this method removes the first object from the game and decrements the brick counter
     * in the game manager.
     *
     * @param thisObj  The first game object involved in the collision.
     * @param otherObj The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (this.brickerGameManager.eraseObject(thisObj, Layer.STATIC_OBJECTS)) {
            this.bricksCounter.decrement();
        }
    }
}
