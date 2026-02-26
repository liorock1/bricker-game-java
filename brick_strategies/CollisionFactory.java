package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * The CollisionFactory class is responsible for creating various collision strategies
 * based on randomly generated conditions.
 */
public class CollisionFactory {

    private enum CollisionStrategies {
        PUCK_STRATEGY,
        SECOND_PADDLE_STRATEGY,
        HEARTS_STRATEGY,
        CAMERA_STRATEGY,
        DOUBLE_STRATEGY
    }

    private final Random random = new Random();
    private final Renderable puckImage;
    private final Sound puckSound;
    private final Renderable extraPaddleImage;
    private final UserInputListener inputListener;
    private final Renderable heartImage;
    private final Counter heartsCounter;
    private final Counter bricksCounter;
    private final BrickerGameManager brickerGameManager;
    private final Ball ball;
    private boolean isDoubleStrategyGenerated = false;
    private final Vector2 windowDimensions;
    private final int BORDER_WIDTH;

    /**
     * Constructs a CollisionFactory instance with necessary resources.
     *
     * @param puckImage           The renderable image for the puck.
     * @param puckSound           The sound for puck collisions.
     * @param extraPaddleImage    The renderable image for the extra paddle.
     * @param inputListener       The input listener for user input.
     * @param heartImage          The renderable image for hearts.
     * @param heartsCounter       Counter for the hearts in the game.
     * @param bricksCounter       Counter for the bricks in the game.
     * @param brickerGameManager  The brickerGameManager instance.
     * @param ball                The ball instance in the game.
     */
    public CollisionFactory(
            Renderable puckImage,
            Sound puckSound,
            Renderable extraPaddleImage,
            UserInputListener inputListener,
            Renderable heartImage,
            Counter heartsCounter,
            Counter bricksCounter,
            BrickerGameManager brickerGameManager,
            Ball ball,
            Vector2 windowDimensions,
            int BORDER_WIDTH
    ) {
        this.puckImage = puckImage;
        this.puckSound = puckSound;
        this.extraPaddleImage = extraPaddleImage;
        this.inputListener = inputListener;
        this.heartImage = heartImage;
        this.heartsCounter = heartsCounter;
        this.bricksCounter = bricksCounter;
        this.brickerGameManager = brickerGameManager;
        this.ball = ball;
        this.windowDimensions = windowDimensions;
        this.BORDER_WIDTH = BORDER_WIDTH;
    }

    /**
     * Builds and returns a collision strategy based on randomly generated conditions.
     *
     * @return A CollisionStrategy instance representing the generated collision strategy.
     */
    public CollisionStrategy buildCollisionStrategy() {
        CollisionStrategy collisionStrategy = new BasicCollisionStrategy(brickerGameManager, bricksCounter);
        boolean isSpecialStrategy = random.nextBoolean();

        if (isSpecialStrategy) {
            int generatedNumber = random.nextInt(CollisionStrategies.values().length);
            return buildSpecialCollisionStrategy(collisionStrategy,
                    CollisionStrategies.values()[generatedNumber]);
        }
        return collisionStrategy;
    }

    /**
     * Builds a special collision strategy based on the specified strategy type.
     *
     * @param collisionStrategy The base collision strategy.
     * @param strategy          The type of special collision strategy to build.
     * @return A CollisionStrategy instance representing the generated special collision strategy.
     */
    private CollisionStrategy buildSpecialCollisionStrategy(CollisionStrategy collisionStrategy,
                                                            CollisionStrategies strategy) {
        switch (strategy) {
            case PUCK_STRATEGY:
                collisionStrategy = new MultiPuckStrategy(
                        collisionStrategy,
                        puckImage,
                        puckSound,
                        brickerGameManager);
                break;
            case SECOND_PADDLE_STRATEGY:
                collisionStrategy = new SecondPaddleStrategy(
                        collisionStrategy,
                        extraPaddleImage,
                        inputListener,
                        brickerGameManager,
                        windowDimensions,
                        BORDER_WIDTH);
                break;
            case HEARTS_STRATEGY:
                collisionStrategy = new HeartSpawnStrategy(
                        collisionStrategy,
                        heartImage,
                        heartsCounter,
                        brickerGameManager
                );
                break;
            case CAMERA_STRATEGY:
                collisionStrategy = new CameraStrategy(
                        collisionStrategy,
                        ball,
                        brickerGameManager,
                        windowDimensions);
                break;
            case DOUBLE_STRATEGY:
                CollisionStrategies firstStrategy;
                CollisionStrategies secondStrategy;
                if (isDoubleStrategyGenerated) {
                    firstStrategy = CollisionStrategies.values()[random.nextInt(
                            CollisionStrategies.values().length - 1)];
                    secondStrategy = CollisionStrategies.values()[random.nextInt(
                            CollisionStrategies.values().length - 1)];
                } else {
                    isDoubleStrategyGenerated = true;
                    firstStrategy = CollisionStrategies.values()[random.nextInt(
                            CollisionStrategies.values().length)];
                    secondStrategy = CollisionStrategies.values()[random.nextInt(
                            CollisionStrategies.values().length)];
                    if ((firstStrategy == CollisionStrategies.DOUBLE_STRATEGY) &&
                            (secondStrategy == CollisionStrategies.DOUBLE_STRATEGY)) {
                        secondStrategy = CollisionStrategies.values()[random.nextInt(
                                CollisionStrategies.values().length - 1)];
                    }
                }
                collisionStrategy = buildSpecialCollisionStrategy(collisionStrategy, firstStrategy);
                collisionStrategy = buildSpecialCollisionStrategy(collisionStrategy, secondStrategy);
                break;
        }
        return collisionStrategy;
    }
}
