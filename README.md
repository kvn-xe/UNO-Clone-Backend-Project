
# Intro

A simple backend recreation of the card game UNO, built in Java using Gradle.

This project models core UNO logic — including card interactions, player states, and event-driven turn handling.

## Interface (GameController.java)

Currently a controller class responsible for
- Allowing players to join/leave
- Player game actions 
- Returning Game state and player information

Uses [`org.json.JSONObject`](https://mvnrepository.com/artifact/org.json/json) for method arguments and responses.

---

### **Methods**

| Method | Argument | Type |
|--------|-----------|------|
| `playerJoin` | JSON with player ID | `JSONObject` |
| `playerLeave` | JSON with player ID | `JSONObject` |
| `getPlayerState` | JSON with player ID | `JSONObject` |
| `getGameState` | *(none)* | *(none)* |
| `gameStart` | *(none)* | *(none)* |
| `playerDraw` | JSON with player ID | `JSONObject` |
| `playerPlay` | JSON with player ID and a card | `JSONObject` |

---

### **Player Schema**

| Key | Description | Type |
|-----|--------------|------|
| `id` | Unique player ID | `String` |
| `active` | Whether this player is the current acting player | `boolean` |


---

### **Card Schema**

| Key | Description | Type |
|-----|--------------|------|
| `type` | `number`, `add`, `reverse`, `skip`, or `change` | `String` |
| `color` | Card color | `String` |
| `value` | Card number or special value | `String` |

---

### **Game Schema**

| Key | Description | Type |
|-----|--------------|------|
| `active-player` | ID of the current player | `String` |
| `top-card` | JSON representing the top card on the play stack | `JSONObject` |
| `deck-size` | Remaining number of cards in the deck | `int` |

---
## Overview
### **Core Components**
- **`EventLoop`** — Handles scheduling and dispatching of game events (e.g., player actions, card draws, turn transitions).  
  It manages a priority queue of pending actions to ensure turn order and timing consistency.
- **`Game`** — Maintains core game state, including the deck, discard pile, active player, and current direction of play.  
  It also enforces UNO-specific rules like stacking `+2` cards or color matching.
- **`GameController`** — Acts as the main interface for player input and state retrieval.  
  It wraps low-level game logic in a simple API for external clients or future frontend integration.

---

## Build Instructions (Gradle)

### **Requirements**
- Gradle   
- Java 21

Check setup:
```bash
gradle -v
java -version
```

1.
Clone the repo with
<pre> git clone https://github.com/kvn-xe/UNO-Clone-Backend-Project.git </pre>
2.
Build the project with
<pre> ./gradlew build </pre>

## 📄 License

This project is licensed under the **MIT License**.

