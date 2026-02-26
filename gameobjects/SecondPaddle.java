package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The SecondPaddle class represents a second paddle that is being
 * added to the game for a certain amount of actions (hits).
 * inherits from the Paddle class.
 */
public class SecondPaddle extends Paddle {

    private static final int MAX_HITS = 4;
    private final Counter hitsCounter;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new SecondPaddle instance.
     *
     * @param topLeftCorner     The top-left corner of the paddle's bounding box in window coordinates.
     * @param dimensions        The dimensions (width and height) of the paddle.
     * @param renderable        The renderable representing the paddle. Can be null.
     * @param inputListener     The input listener to receive user inputs.
     * @param tag               The tag assigned to this paddle.
     * @param brickerGameManager The game manager to handle game-wide operations.
     */
    public SecondPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                        Renderable renderable, UserInputListener inputListener,
                        String tag, BrickerGameManager brickerGameManager,
                        int borderWidth, Vector2 windowDimensions)
    {
        super(topLeftCorner, dimensions, renderable, inputListener, tag, borderWidth, windowDimensions);
        this.brickerGameManager = brickerGameManager;
        this.setTag(tag);
        this.hitsCounter = new Counter();
    }

    /**
     * Handles the event when a collision occurs with another game object.
     *
     * @param other         the other gameObject that cased the collision.
     * @param collision     data about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        boolean isBallOrPuck = other.getTag().equals(BrickerGameManager.BALL_TAG) ||
                other.getTag().equals(BrickerGameManager.PUCK_TAG);
        if (isBallOrPuck) {
            this.hitsCounter.increment();
        }
    }

    /**
     * Updates the paddle state each frame. If the paddle has been hit the maximum number of times,
     * it is removed from the game.
     *
     * @param deltaTime Time passed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (hitsCounter.value() == MAX_HITS) {
            brickerGameManager.eraseObject(this, Layer.DEFAULT);
            brickerGameManager.setSecondPaddleExist(false);
        }
    }
}
