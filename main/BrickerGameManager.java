package bricker.main;

import bricker.brick_strategies.CollisionFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * This class is responsible for the initialization of the basic game objects and running the game.
 */
public class BrickerGameManager extends GameManager {

    /**
     * The tag for the game object - ball
     */
    public static final String BALL_TAG = "ball";

    /**
     * The tag for the game object - paddle
     */
    public static final String PADDLE_TAG = "paddle";

    /**
     * The tag for the game object - puck
     */
    public static final String PUCK_TAG = "puck";

    /**
     * The tag for the game object - second-paddle
     */
    public static final String SECOND_PADDLE_TAG = "second-paddle";

    /**
     * Default settings
     */
    private static final int SCREEN_WIDTH = 700;
    private static final int SCREEN_HEIGHT = 500;
    private static final String TITLE = "Bricker";
    private static final int NUM_OF_ARGS = 2;
    private static final int BORDER_WIDTH = 15;
    private static final int BRICK_HEIGHT = 15;
    private static final int BALL_RADIUS = 20;
    private static final int DEFAULT_BRICKS_IN_ROW = 8;
    private static final int DEFAULT_BRICKS_ROWS = 7;
    private static final int PADDLE_PADDING = 30;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(255, 204, 51));
    private static final int CAMERA_BALL_MAX = 4;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_WIDTH = 100;
    private static final int HEART_SIZE = 30;
    private static final float BALL_SPEED = 150;

    /**
     * File paths
     */
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final String BLOP_SOUND_PATH = "assets/blop.wav";
    private static final String WIN_PROMPT = "You win! Play again?";
    private static final String LOSE_PROMPT = "You Lose! Play again?";

    /**
     * Private fields
     */
    private Ball ball;
    private CollisionFactory collisionFactory;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private WindowController windowController;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private LifeCounter lifeCounter;
    private Counter bricksCounter;
    private final int bricksPerRow;
    private final int brickRows;
    private boolean isSecondPaddleExist;

    /**
     * Constructs a BrickerGameManager instance with the specified parameters.
     *
     * @param windowTitle      The window title on the top.
     * @param windowDimensions Size of the pop-up window.
     * @param bricksPerRow     Number of bricks per row.
     * @param brickRows        Number of rows of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int brickRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
    }

    /**
     * Initializes the game by creating and adding the game objects to the game objects collection.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        this.inputListener = inputListener;

        this.bricksCounter = new Counter(brickRows * bricksPerRow);

        // Initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.isSecondPaddleExist = false;

        // Create ball
        createBall();

        // Create paddle
        createPaddle(inputListener);

        // Create borders
        createBorders();

        // Create background
        createBackground();

        // Create life counter
        createLifeCounter();

        // Create collision strategy factory
        collisionStrategyFactory();

        // Create bricks
        createBricks();
    }

    /**
     * Returns the constant paddle width.
     *
     * @return The paddle width.
     */
    public static int getPaddleWidth() {
        return PADDLE_WIDTH;
    }

    /**
     * Returns the constant paddle height.
     *
     * @return The paddle height.
     */
    public static int getPaddleHeight() {
        return PADDLE_HEIGHT;
    }

    /**
     * Returns the constant ball speed.
     *
     * @return The ball speed.
     */
    public static float getBallSpeed() {
        return BALL_SPEED;
    }

    /**
     * Returns the constant heart size.
     *
     * @return The heart size.
     */
    public static int getHeartSize() {
        return HEART_SIZE;
    }

    /**
     * Returns the constant puck radius.
     *
     * @return The puck radius.
     */
    public static int getBallRadius() {
        return BALL_RADIUS;
    }

    /**
     * Returns whether a second paddle exists in the game.
     *
     * @return True if a second paddle exists, false otherwise.
     */
    public boolean isSecondPaddleExist() {
        return isSecondPaddleExist;
    }

    /**
     * Sets whether a second paddle exists in the game.
     *
     * @param secondPaddleExist True if a second paddle exists, false otherwise.
     */
    public void setSecondPaddleExist(boolean secondPaddleExist) {
        isSecondPaddleExist = secondPaddleExist;
    }

    /**
     * Removes a game object from the game objects' collection.
     *
     * @param object The game object to remove.
     * @param layer  The layer from which to remove the object.
     * @return True if the object was successfully removed, false otherwise.
     */
    public boolean eraseObject(GameObject object, int layer) {
        return super.gameObjects().removeGameObject(object, layer);
    }

    /**
     * Adds a game object to the game objects' collection.
     *
     * @param object The game object to add.
     * @param layer  The layer to which the object should be added.
     */
    public void addObject(GameObject object, int layer) {
        super.gameObjects().addGameObject(object, layer);
    }

    /**
     * Adds a life when the paddle catches a heart.
     */
    public void addLife() {
        lifeCounter.addLife();
    }

    /**
     * Checks if a game object is out of bounds.
     *
     * @param object The game object to check.
     * @return True if the object is out of bounds, false otherwise.
     */
    public boolean checkOutOfBounds(GameObject object) {
        return object.getCenter().y() > windowDimensions.y();
    }

    /**
     * This method is called once per frame and updates the state of the game.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkCamera();
        checkForLose();
        checkForWin();
    }

    /**
     * Checks if the camera should be reset based on the number of ball collisions.
     */
    private void checkCamera() {
        if (ball.getCollisionCounter() > CAMERA_BALL_MAX) {
            this.setCamera(null);
        }
    }

    /**
     * Checks if the ball has fallen below the paddle, indicating a lost life.
     * If all lives are lost, the game ends with a loss message.
     */
    private void checkForLose() {
        double ballHeight = ball.getCenter().y();

        if (ballHeight > windowDimensions.y()) {
            lifeCounter.loseLife();
            if (lifeCounter.isGameOver()) {
                if (windowController.openYesNoDialog(LOSE_PROMPT))
                    windowController.resetGame();
                else
                    windowController.closeWindow();
            } else {
                resetBall();
            }
        }
    }

    /**
     * Checks if all bricks are destroyed or if the 'W' key is pressed, indicating a win.
     * If so, the game ends with a win message.
     */
    private void checkForWin() {
        if (this.bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            if (windowController.openYesNoDialog(WIN_PROMPT)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Resets the ball to the center of the screen with a random velocity.
     */
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(0.5f));
        Random rand = new Random();
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates the background of the game and adds it to the game objects' collection.
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, true);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions),
                backgroundImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Creates the life counter and adds it to the game objects' collection.
     */
    private void createLifeCounter() {
        ImageRenderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        lifeCounter = new LifeCounter(Vector2.ZERO,
                windowDimensions, null,
                heartImage,
                gameObjects(),
                this);
        gameObjects().addGameObject(lifeCounter, Layer.UI);
    }

    /**
     * Creates the bricks and adds them to the game objects' collection.
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        Vector2 pos = new Vector2(BORDER_WIDTH + 1, BORDER_WIDTH + 1);
        float BRICK_WIDTH = (windowDimensions.x() - BORDER_WIDTH * 2 - bricksPerRow) / bricksPerRow;
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < bricksPerRow; j++) {
                gameObjects().addGameObject(new Brick(pos, new Vector2(BRICK_WIDTH, BRICK_HEIGHT),
                                            brickImage, collisionFactory.buildCollisionStrategy()),
                                            Layer.STATIC_OBJECTS);
                pos = new Vector2(pos.x() + BRICK_WIDTH + 1, pos.y());
            }
            pos = new Vector2(BORDER_WIDTH + 1, pos.y() + BRICK_HEIGHT + 1);
        }
    }

    /**
     * Creates the borders of the game field and adds them to the game objects' collection.
     */
    private void createBorders() {
        // Adding upper border
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                                    new Vector2(BORDER_WIDTH, windowDimensions.y()),
                                    BORDER_RENDERABLE));
        // Adding left border
        gameObjects().addGameObject(new GameObject(
                                    new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                                    new Vector2(BORDER_WIDTH, windowDimensions.y()),
                                    BORDER_RENDERABLE));
        // Adding right border
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                                    new Vector2(windowDimensions.x(), BORDER_WIDTH),
                                    BORDER_RENDERABLE));
    }

    /**
     * Creates the paddle and adds it to the game objects' collection.
     *
     * @param inputListener Listens for user input.
     */
    private void createPaddle(UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                                            paddleImage, inputListener, PADDLE_TAG, BORDER_WIDTH,
                                            windowDimensions);

        userPaddle.setCenter(new Vector2(windowDimensions.x() / 2,
                (int) windowDimensions.y() - PADDLE_PADDING));
        gameObjects().addGameObject(userPaddle);
    }

    /**
     * Creates the ball and adds it to the game objects' collection.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS),
                        ballImage, collisionSound, BALL_TAG);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates the collision strategy factory.
     */
    private void collisionStrategyFactory() {
        Renderable puckImage = this.imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound puckSound = this.soundReader.readSound(BLOP_SOUND_PATH);
        Renderable paddleImage = this.imageReader.readImage(PADDLE_IMAGE_PATH, false);
        Renderable heartImage = this.imageReader.readImage(HEART_IMAGE_PATH, true);
        this.collisionFactory = new CollisionFactory(puckImage, puckSound, paddleImage,
                                                     inputListener, heartImage,
                                                     lifeCounter.getCurrentLives(), bricksCounter,
                                   this, ball, windowDimensions, BORDER_WIDTH);
    }

    /**
     * The main entry point for the game.
     *
     * @param args Command-line arguments for configuring the game.
     */
    public static void main(String[] args) {
        int bricksPerRow = DEFAULT_BRICKS_IN_ROW;
        int brickRows = DEFAULT_BRICKS_ROWS;

        if (args.length == NUM_OF_ARGS) {
            bricksPerRow = Integer.parseInt(args[0]);
            brickRows = Integer.parseInt(args[1]);
        }

        new BrickerGameManager(TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                               bricksPerRow, brickRows).run();
    }
}
