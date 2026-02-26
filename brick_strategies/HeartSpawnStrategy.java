package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The HeartSpawnStrategy class implements the CollisionStrategy interface.
 * It handles collisions by creating a heart object at the collision point,
 * which adds an extra life to the player when collected.
 */
public class HeartSpawnStrategy implements CollisionStrategy {

    private static final Vector2 HEART_FALL_SPEED = new Vector2(0, 100);
    private final Renderable heartRenderable;
    private final CollisionStrategy collisionStrategy;
    private final BrickerGameManager gameManager;
    private final Counter heartCounter;

    /**
     * Constructs a HeartSpawnStrategy instance.
     *
     * @param collisionStrategy The base collision strategy.
     * @param heartRenderable   The renderable image for the heart.
     * @param heartCounter      Counter for the hearts in the game.
     * @param gameManager       The game manager instance.
     */
    public HeartSpawnStrategy(
            CollisionStrategy collisionStrategy,
            Renderable heartRenderable,
            Counter heartCounter,
            BrickerGameManager gameManager
    ) {
        this.collisionStrategy = collisionStrategy;
        this.heartRenderable = heartRenderable;
        this.heartCounter = heartCounter;
        this.gameManager = gameManager;
    }

    /**
     * Handles the collision between game objects and spawns a heart at the collision point.
     *
     * @param thisObject  The first GameObject involved in the collision.
     * @param otherObject The second GameObject involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObject, GameObject otherObject) {
        collisionStrategy.onCollision(thisObject, otherObject);
        Vector2 collisionPosition = thisObject.getCenter();
        spawnHeart(collisionPosition);
    }

    /**
     * Spawns a heart at the specified position.
     *
     * @param position The position where the heart should be spawned.
     */
    private void spawnHeart(Vector2 position) {
        Heart newHeart = createHeart(position);
        setHeartVelocity(newHeart);
        addHeartToGame(newHeart);
    }

    /**
     * Creates a heart object at the specified position.
     *
     * @param position The position where the heart should be created.
     * @return A new Heart object.
     */
    private Heart createHeart(Vector2 position) {
        Heart heart = new Heart(Vector2.ZERO,
                new Vector2(BrickerGameManager.getHeartSize(), BrickerGameManager.getHeartSize()),
                heartRenderable,
                heartCounter,
                gameManager);
        heart.setCenter(position);
        return heart;
    }

    /**
     * Sets the velocity for the heart object.
     *
     * @param heart The heart object whose velocity is to be set.
     */
    private void setHeartVelocity(Heart heart) {
        heart.setVelocity(HEART_FALL_SPEED);
    }

    /**
     * Adds the heart object to the game.
     *
     * @param heart The heart object to be added to the game.
     */
    private void addHeartToGame(Heart heart) {
        gameManager.addObject(heart, Layer.DEFAULT);
    }
}
