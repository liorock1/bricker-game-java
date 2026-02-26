package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * A Paddle class representing the paddle in the Bricker game. It extends the GameObject class
 * and adds functionality specific to the paddle, such as movement control based on user input.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 400;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int BORDER_WIDTH;

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner   Position of the paddle, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderable      The renderable representing the paddle. Can be null, in which case
     *                        the Paddle will not be rendered.
     * @param inputListener   The input listener to listen for user input to move the paddle.
     * @param tag             The tag to identify this game object.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, String tag, int borderWidth, Vector2 windowDimensions)
    {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.BORDER_WIDTH = borderWidth;
        this.setTag(tag);
    }

    /**
     * Updates the state of the Paddle. This method is called once per frame and
     * checks for user input to move the paddle left or right.
     * It also ensures that the paddle does not move out of the game board boundaries.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = getMovementDirection();
        Vector2 newVelocity = movementDir.mult(MOVEMENT_SPEED);
        setVelocity(newVelocity);

        Vector2 newTopLeftCorner = calculateNewPosition(deltaTime, newVelocity);
        enforceBoundaries(newTopLeftCorner);
    }

    /**
     * Determines the movement direction based on user input.
     *
     * @return A Vector2 representing the movement direction.
     */
    private Vector2 getMovementDirection() {
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        return movementDir;
    }

    /**
     * Calculates the new position of the paddle based on its current velocity and delta time.
     *
     * @param deltaTime   The time, in seconds, that passed since the last invocation of this method.
     * @param newVelocity The new velocity of the paddle.
     * @return A Vector2 representing the new top-left corner position of the paddle.
     */
    private Vector2 calculateNewPosition(float deltaTime, Vector2 newVelocity) {
        return getTopLeftCorner().add(newVelocity.mult(deltaTime));
    }

    /**
     * Enforces the boundaries of the game board, ensuring the paddle does not move out of bounds.
     *
     * @param newTopLeftCorner The new top-left corner position of the paddle.
     */
    private void enforceBoundaries(Vector2 newTopLeftCorner) {
        float leftBoundary = BORDER_WIDTH;
        float rightBoundary = windowDimensions.x() - BORDER_WIDTH - getDimensions().x();

        if (newTopLeftCorner.x() < leftBoundary) {
            setTopLeftCorner(new Vector2(leftBoundary, getTopLeftCorner().y()));
            setVelocity(Vector2.ZERO);
        } else if (newTopLeftCorner.x() > rightBoundary) {
            setTopLeftCorner(new Vector2(rightBoundary, getTopLeftCorner().y()));
            setVelocity(Vector2.ZERO);
        } else {
            setTopLeftCorner(newTopLeftCorner);
        }
    }
}
