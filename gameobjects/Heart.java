package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class implements the heart objects in the game.
 * Hearts are collectible objects that increase the player's life count when collected.
 */
public class Heart extends GameObject {

    private final BrickerGameManager brickerGameManager;
    private final Counter heartsCounter;
    private static final int MAX_REVOKE = 4;

    /**
     * Constructs a new Heart instance.
     *
     * @param topLeftCorner      The top left corner of the heart's initial position in the game screen (0,0).
     * @param dimensions         Dimensions of the heart object.
     * @param renderable         Renderable object for the heart's appearance.
     * @param heartsCounter      Counter for the hearts in the game.
     * @param brickerGameManager The game manager handling the game's state and behavior.
     */
    public Heart(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            Counter heartsCounter,
            BrickerGameManager brickerGameManager
    ) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
        this.heartsCounter = heartsCounter;
    }

    /**
     * Handles the collision event when another game object collides with this heart.
     * If the heart counter is less than the maximum allowed, it increases the player's life count.
     * The heart object is then removed from the game.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.heartsCounter.value() < MAX_REVOKE) {
            brickerGameManager.addLife();
        }
        this.brickerGameManager.eraseObject(this, Layer.DEFAULT);
    }

    /**
     * Updates the heart's state each frame.
     * This method is called once per frame, and it updates the position,
     * checks for collisions, and handles any game-specific behavior.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.brickerGameManager.checkOutOfBounds(this)) {
            this.brickerGameManager.eraseObject(this, Layer.DEFAULT);
        }
    }

    /**
     * Determines whether this heart should collide with another game object.
     *
     * @param other The other GameObject.
     * @return      True if the heart should collide with the other GameObject, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other.getTag().equals(BrickerGameManager.PADDLE_TAG);
    }
}
