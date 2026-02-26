package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.Color;

/**
 * A LifeCounter class that represents the life counter in the Bricker game. It extends the GameObject class
 * and adds functionality specific to displaying and updating the player's remaining lives.
 */
public class LifeCounter extends GameObject {
    private static final int MAX_LIVES = 4;
    private static final int INITIAL_LIVES = 3;
    private static final int PADDING = 30;
    private static final int HEARTS_PADDING = 10;
    private static final int NUM_LOCATION = 50;
    private static final int NUM_SIZE = 30;
    private static final int HEART_SIZE = BrickerGameManager.getHeartSize();
    private final Counter currentLives;
    private final GameObject[] heartIcons;
    private final TextRenderable numericDisplayRenderable;
    private final ImageRenderable heartImage;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final BrickerGameManager brickerGameManager;

    /**
     * Construct a new LifeCounter instance.
     *
     * @param position            The position of the life counter, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          The dimensions of the game window.
     * @param renderable          The renderable representing the life counter. Can be null, in which case
     *                            the LifeCounter will not be rendered.
     * @param heartImage          The image representing a heart icon.
     * @param gameObjects         The collection of game objects.
     * @param brickerGameManager  The game manager instance.
     */
    public LifeCounter(Vector2 position, Vector2 dimensions, danogl.gui.rendering.Renderable renderable,
                       ImageRenderable heartImage, GameObjectCollection gameObjects,
                       BrickerGameManager brickerGameManager) {
        super(position, dimensions, renderable);
        this.currentLives = new Counter(INITIAL_LIVES);
        this.heartIcons = new GameObject[MAX_LIVES];
        this.gameObjects = gameObjects;
        this.windowDimensions = dimensions;
        this.heartImage = heartImage;
        this.brickerGameManager = brickerGameManager;

        for (int i = 0; i < INITIAL_LIVES; i++) {
            createHeart(i);
        }

        numericDisplayRenderable = new TextRenderable(String.valueOf(currentLives.value()));
        GameObject numericDisplay = new GameObject(
                new Vector2(windowDimensions.x() - NUM_LOCATION, windowDimensions.y() - NUM_LOCATION),
                new Vector2(NUM_SIZE, NUM_SIZE),
                numericDisplayRenderable);
        numericDisplay.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(numericDisplay, Layer.UI);
        updateDisplay();
    }

    /**
     * Creates a heart icon at the specified index.
     *
     * @param i The index at which to create the heart icon.
     */
    private void createHeart(int i) {
        heartIcons[i] = new GameObject(
                new Vector2(HEARTS_PADDING + i * PADDING, windowDimensions.y() - PADDING),
                new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage);
        this.brickerGameManager.addObject(heartIcons[i], Layer.UI);
    }

    /**
     * Decreases the current lives by one and updates the display.
     */
    public void loseLife() {
        if (currentLives.value() > 0) {
            currentLives.decrement();
            gameObjects.removeGameObject(heartIcons[currentLives.value()], Layer.UI);
            updateDisplay();
        }
    }

    /**
     * Adds a life when the paddle collides with a heart and updates the display.
     */
    public void addLife() {
        if (currentLives.value() < MAX_LIVES) {
            currentLives.increment();
            createHeart(currentLives.value() - 1);
            updateDisplay();
        }
    }

    /**
     * Checks if the game is over (i.e., no lives left).
     *
     * @return true if no lives are left, false otherwise.
     */
    public boolean isGameOver() {
        return currentLives.value() <= 0;
    }

    /**
     * Updates the display of the life counter, including the heart icons and the numeric display.
     */
    private void updateDisplay() {
        numericDisplayRenderable.setString(String.valueOf(currentLives.value()));
        if (currentLives.value() >= 3) {
            numericDisplayRenderable.setColor(Color.GREEN);
        } else if (currentLives.value() == 2) {
            numericDisplayRenderable.setColor(Color.YELLOW);
        } else if (currentLives.value() == 1) {
            numericDisplayRenderable.setColor(Color.RED);
        }
    }

    /**
     * Returns the amount of lives the player has at the moment.
     *
     * @return The current number of lives.
     */
    public Counter getCurrentLives() {
        return currentLives;
    }
}
