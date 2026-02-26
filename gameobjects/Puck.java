package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents a Puck in the game, which is a specialized type of Ball.
 * The Puck has additional behavior managed by the BrickerGameManager.
 */
public class Puck extends Ball {

    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new Puck instance.
     *
     * @param topLeftCorner      Top left corner of the game screen.
     * @param dimensions         Dimensions of the game object.
     * @param renderable         The renderable representing the puck.
     * @param collisionSound     The sound that is played when the puck is hit.
     * @param tag                The tag of the Puck object.
     * @param brickerGameManager The game manager handling the game's state and behavior.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound,
                String tag, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, collisionSound, tag);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Updates the puck's state each frame.
     * This method is called once per frame, and it updates the position,
     * checks for collisions, and handles any game-specific behavior.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkPuckOutOfBounds();
    }

    /**
     * Checks if the puck is out of bounds and removes it from the game if it is.
     */
    private void checkPuckOutOfBounds() {
        if (this.brickerGameManager.checkOutOfBounds(this)) {
            this.brickerGameManager.eraseObject(this, Layer.DEFAULT);
        }
    }
}
