package bricker.brick_strategies;

import bricker.gameobjects.SecondPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The SecondPaddleStrategy class implements the CollisionStrategy interface.
 * It handles collisions by creating a second paddle at the center of the screen
 * when a collision occurs, provided that a second paddle does not already exist.
 */
public class SecondPaddleStrategy implements CollisionStrategy {
    private final Renderable secondPaddleImage;
    private final UserInputListener inputListener;
    private final CollisionStrategy collisionStrategy;
    private final BrickerGameManager gameManager;
    private final Vector2 windowDimensions;
    private final int BORDER_WIDTH;

    /**
     * Constructs a SecondPaddleStrategy instance.
     *
     * @param collisionStrategy  The base collision strategy to extend.
     * @param secondPaddleImage  The image of the second paddle (identical to the regular paddle).
     * @param inputListener      Responsible for user input directions.
     * @param gameManager        The game manager instance managing the game.
     */
    public SecondPaddleStrategy(
            CollisionStrategy collisionStrategy,
            Renderable secondPaddleImage,
            UserInputListener inputListener,
            BrickerGameManager gameManager,
            Vector2 windowDimensions,
            int BORDER_WIDTH
    ) {
        this.collisionStrategy = collisionStrategy;
        this.secondPaddleImage = secondPaddleImage;
        this.inputListener = inputListener;
        this.gameManager = gameManager;
        this.windowDimensions = windowDimensions;
        this.BORDER_WIDTH = BORDER_WIDTH;
    }

    /**
     * Handles the collision event by delegating to the base strategy
     * and then creating a second paddle if it does not already exist.
     *
     * @param thisObj   The first game object involved in the collision.
     * @param otherObj  The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        collisionStrategy.onCollision(thisObj, otherObj);
        if (!gameManager.isSecondPaddleExist()) {
            createAndAddSecondPaddle();
            gameManager.setSecondPaddleExist(true);
        }
    }

    /**
     * Creates and adds a second paddle to the game.
     */
    private void createAndAddSecondPaddle() {
        SecondPaddle newPaddle = createSecondPaddle();
        Vector2 centerPosition = calculateCenterPosition();
        newPaddle.setCenter(centerPosition);
        gameManager.addObject(newPaddle, Layer.DEFAULT);
    }

    /**
     * Creates a new second paddle object.
     *
     * @return A new SecondPaddle object.
     */
    private SecondPaddle createSecondPaddle() {
        return new SecondPaddle(
                Vector2.ZERO,
                new Vector2(BrickerGameManager.getPaddleWidth(), BrickerGameManager.getPaddleHeight()),
                secondPaddleImage,
                inputListener,
                BrickerGameManager.SECOND_PADDLE_TAG,
                gameManager,
                BORDER_WIDTH,
                windowDimensions
        );
    }

    /**
     * Calculates the center position of the game window.
     *
     * @return A Vector2 representing the center position of the game window.
     */
    private Vector2 calculateCenterPosition() {
        return new Vector2(
                windowDimensions.x() / 2,
                windowDimensions.y() / 2
        );
    }
}
