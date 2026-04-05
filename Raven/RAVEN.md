# Raven
This module contains Minecraft server implementation written using minestom

The main reason I created this was to practice how to properly design systems that work together

## Features
At this stage the project has these features:

- Basic Castle Siege minigame
- Lobby service
- Player data saving system
- Player specific instances (worlds owned by players)
- Linked chat with discord via redis pubsub

## Redis
Redis is used for discord integration and data saving.

Discord service does not require redis database - it just wont work.

But saving player data requires running redis database.

The server wont start without it.

Host and port is hardcoded, so just run it on localhost:6379
