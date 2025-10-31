
# Backend for UNO-like Game

Just a random project that I might eventually make playable. Uses popular UNO "house" rules.
- Play cards with same color/number
- Only other +2 cards can be chained on top of +2 cards





## Technical Features

- Priority based event scheduling
- Simple build with gradle


## Interface (GameController.java)

Currently a controller class responsible for
- Allowing players to join/leave
- Player game actions 
- Returning Game state and player information

Primarily uses JSONObject (https://mvnrepository.com/artifact/org.json/json) as arguments for these methods. 

### Player

Calling a method for any player action or requesting player state will require a JSONObject with an "id" field. Player objects are created/removed when players join/leave through methods in the interface. Player objects will be created with the "id" fields given by provided JSONObject on method call. Player states will be returned in a JSONObject with,
| key | value | type |
|-----------|-----------|-----------|
| id | Id of player  | String |
| active | Current acting player | boolean  |

Also contains the JSONArray of Card, Cards are represented by JSONObjects with
| key | value | type |
|-----------|-----------|-----------|
| type | number/add/reverse/skip/change  | String |
| color | Current acting player | String  |
| value | number/add value | String  |

### Game

There exists methods for starting a game and getting game state, which returns a JSONObject with,
| key | value | type |
|-----------|-----------|-----------|
| active-player | id of active player  | String |
| top-card | top card on play stack | JSON  |
| deck-size | size of remaining deck | String  |
