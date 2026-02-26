# Bricker: A Brick-Breaking Arcade Game (Java)

A classic brick-breaker game inspired by *Arkanoid*: control the paddle, bounce the ball, and clear all bricks — with extra brick behaviors like multi-ball, camera effects, extra lives, and more.

---

## Overview
**Bricker** was built as part of an Object-Oriented Programming assignment.  
The game focuses on clean OOP design and extensibility using patterns (especially Strategy) to easily add new brick collision behaviors.

---

## Features

### Classic Gameplay
- Move the paddle left/right to keep the ball from falling
- Break all bricks to win
- Lives counter (lose when lives reach zero)

### Special Brick Behaviors (Collision Strategies)
Depending on the brick type, collisions can trigger:
- **Multi-ball** (spawn additional balls)
- **Second paddle** (extra controllable paddle)
- **Camera strategy** (dynamic camera effect)
- **Heart / extra life drops**
- Extensible design: adding new brick behaviors is straightforward

---

bricker-game-java/
├─ main/
│ └─ BrickerGameManager.java # Game entry point / manager
├─ gameobjects/
│ ├─ Ball.java # Ball behavior + movement
│ ├─ Paddle.java # Player paddle
│ ├─ SecondPaddle.java # Extra paddle logic
│ ├─ Brick.java # Brick object
│ ├─ Puck.java # Additional ball-like object (if used)
│ ├─ Heart.java # Extra-life collectible
│ └─ LifeCounter.java # Tracks and displays remaining lives
└─ brick_strategies/
├─ CollisionStrategy.java # Strategy interface
├─ BasicCollisionStrategy.java # Default brick behavior
├─ CameraStrategy.java # Camera effect on collision
├─ HeartSpawnStrategy.java # Spawns heart on collision
├─ MultiPuckStrategy.java # Spawns multiple pucks/balls
├─ SecondPaddleStrategy.java # Spawns second paddle
└─ CollisionFactory.java # Chooses/builds strategies

---

## Technologies Used
- **Java**
- **DanoGameLab framework** (for rendering, game loop, physics utilities)
- **Object-Oriented Design**
- **Design Patterns**
  - **Strategy Pattern**: brick collision behaviors are interchangeable modules
  - (Optional, if relevant in your implementation) Observer-style updates for UI components like life counter

---

## Installation & Execution

### Run with IntelliJ (recommended)
1. Open IntelliJ → **File → Open**
2. Select the project folder (`bricker-game-java`)
3. Open `main/BrickerGameManager.java`
4. Click the green **Run** button
