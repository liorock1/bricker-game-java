package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A Ball class representing a ball in the Bricker game. It extends the GameObject class
 * and adds functionality specific to the ball, such as collision handling and a collision counter.
 */
public class Ball extends GameObject {

    private final Sound collisionSound;
    private Counter collisionCounter;

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the ball, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the ball. Can be null, in which case
     *                      the Ball will not be rendered.
     * @param collisionSound The sound to play upon collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, String tag)
    {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = new Counter();
        this.setTag(tag);
    }

    /**
     * Called when this Ball collides with another GameObject.
     * This method updates the collision counter, reverses the ball's velocity based on the collision normal,
     * and plays the collision sound.
     *
     * @param other The other GameObject this Ball collided with.
     * @param collision The details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision)
    {
        this.collisionCounter.increment();
        super.onCollisionEnter(other, collision);
        setVelocity(getVelocity().flipped(collision.getNormal()));
        this.collisionSound.play();
    }

    /**
     * Gets the number of collisions this Ball has encountered.
     *
     * @return The collision counter.
     */
    public int getCollisionCounter() {

        return collisionCounter.value();
    }

    /**
     * Sets the collision counter for this Ball.
     *
     * @param collisionCounter The collision counter to set.
     */
    public void setCollisionCounter(int collisionCounter) {
        this.collisionCounter = new Counter(collisionCounter);
    }
}
