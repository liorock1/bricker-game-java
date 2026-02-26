package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

/**
 * The CameraStrategy class implements the CollisionStrategy interface.
 * It defines a strategy where the camera follows the ball when a collision occurs.
 * This class extends the functionality of a base collision strategy.
 */
public class CameraStrategy implements CollisionStrategy {
    private static final float SCALE_FACTOR = 1.2f;
    private final BrickerGameManager gameManager;
    private final Ball ballObject;
    private final CollisionStrategy collisionStrategy;
    private final Vector2 windowDimensions;

    /**
     * Constructs a CameraStrategy instance.
     *
     * @param baseStrategy The base collision strategy to extend.
     * @param ballObject The ball object in the game.
     * @param gameManager The game manager instance managing the game.
     */
    public CameraStrategy(
            CollisionStrategy baseStrategy,
            Ball ballObject,
            BrickerGameManager gameManager,
            Vector2 dimensions
    ) {
        this.collisionStrategy = baseStrategy;
        this.ballObject = ballObject;
        this.gameManager = gameManager;
        this.windowDimensions = dimensions;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves the ball and the camera is not already set,
     * it sets the camera to follow the ball.
     *
     * @param firstObject The first game object involved in the collision.
     * @param secondObject The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        collisionStrategy.onCollision(firstObject, secondObject);
        if (isBallCollision(secondObject)) {
            if (isCameraNotSet()) {
                setCameraToFollowBall(secondObject);
                resetBallCollisionCounter();
            }
        }
    }

    /**
     * Checks if the collision involves the ball.
     *
     * @param object The game object to check.
     * @return True if the object is the ball, false otherwise.
     */
    private boolean isBallCollision(GameObject object) {
        return object.getTag().equals(BrickerGameManager.BALL_TAG);
    }

    /**
     * Checks if the camera is not already set.
     *
     * @return True if the camera is not set, false otherwise.
     */
    private boolean isCameraNotSet() {
        return gameManager.camera() == null;
    }

    /**
     * Sets the camera to follow the specified game object.
     *
     * @param object The game object for the camera to follow.
     */
    private void setCameraToFollowBall(GameObject object) {
        gameManager.setCamera(
                new Camera(
                        object,
                        Vector2.ZERO,
                        windowDimensions.mult(SCALE_FACTOR),
                        windowDimensions
                )
        );
    }

    /**
     * Resets the ball's collision counter.
     */
    private void resetBallCollisionCounter() {
        ballObject.setCollisionCounter(0);
    }
}
