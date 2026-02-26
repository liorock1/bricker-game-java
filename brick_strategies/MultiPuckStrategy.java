package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * The MultiPuckStrategy class implements the CollisionStrategy interface.
 * It handles collisions by creating additional pucks at the collision point.
 * This class extends the functionality of a base collision strategy.
 */
class MultiPuckStrategy implements CollisionStrategy {
    private static final int PUCK_DIMENSION = BrickerGameManager.getBallRadius() * 3 / 4;
    private static final int NUMBER_OF_PUCKS = 2;
    private final Renderable puckRenderable;
    private final Sound puckCreationSound;
    private final CollisionStrategy collisionStrategy;
    private final BrickerGameManager gameManager;

    /**
     * Constructs a MultiPuckStrategy instance.
     *
     * @param baseStrategy      The base collision strategy to extend.
     * @param puckRenderable    The renderable for the puck.
     * @param puckCreationSound The sound to play when a puck is created.
     * @param gameManager       The game manager instance managing the game.
     */
    public MultiPuckStrategy(CollisionStrategy baseStrategy,
                             Renderable puckRenderable,
                             Sound puckCreationSound,
                             BrickerGameManager gameManager) {
        this.collisionStrategy = baseStrategy;
        this.puckRenderable = puckRenderable;
        this.puckCreationSound = puckCreationSound;
        this.gameManager = gameManager;
    }

    /**
     * Handles the collision event by delegating to the base strategy
     * and then creating additional pucks.
     *
     * @param collider The first game object involved in the collision.
     * @param collidee The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject collider, GameObject collidee) {
        collisionStrategy.onCollision(collider, collidee);
        spawnAdditionalPucks(collider.getCenter());
    }

    /**
     * Spawns additional pucks at the given position.
     *
     * @param spawnPosition The position where new pucks should be created.
     */
    private void spawnAdditionalPucks(Vector2 spawnPosition) {
        for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
            createAndAddPuck(spawnPosition);
        }
    }

    /**
     * Creates a new puck and adds it to the game.
     *
     * @param position The position where the puck should be created.
     */
    private void createAndAddPuck(Vector2 position) {
        Puck newPuck = new Puck(Vector2.ZERO,
                new Vector2(PUCK_DIMENSION, PUCK_DIMENSION),
                puckRenderable,
                puckCreationSound,
                BrickerGameManager.PUCK_TAG,
                gameManager);
        newPuck.setCenter(position);
        gameManager.addObject(newPuck, Layer.DEFAULT);
        newPuck.setVelocity(generateRandomVelocity());
    }

    /**
     * Generates a random velocity vector for a puck.
     *
     * @return A random velocity vector.
     */
    private Vector2 generateRandomVelocity() {
        Random rng = new Random();
        double randomAngle = rng.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(randomAngle) * BrickerGameManager.getBallSpeed();
        float velocityY = (float) Math.sin(randomAngle) * BrickerGameManager.getBallSpeed();
        return new Vector2(velocityX, velocityY);
    }
}
