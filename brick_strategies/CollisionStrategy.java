package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The CollisionStrategy interface defines a method for handling collisions between
 * game objects in the Bricker game. Implementing classes will provide specific
 * collision handling behavior.
 */
public interface CollisionStrategy {
    /**
     * Handles the collision between two game objects.
     *
     * @param object1 The first game object involved in the collision.
     * @param object2 The second game object involved in the collision.
     */
    void onCollision(GameObject object1, GameObject object2);
}
