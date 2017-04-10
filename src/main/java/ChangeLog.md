# Change Log
All notable changes to this project will be documented in this file.
Log type: Added, Changed, Deprecated, Removed, Fixed, Security

#### [0.1.0] - 2017-04-03
##### Changed
- Reconstruct the full HelpCommand from one crunch of String to two fields.
##### Added
- Add StatusCommand, this command includes Bot's uptime, guild, shard, and Operating System info. Will add CPU usage.

#### [0.1.2] - 2017-04-04
##### Added
- Add Play, Pause, Skip, Volume, and Stop Command for Music Module. Pretty straight forward.
- Bot can detect mention from CommandListener and send help message.
##### Changed
- Reconstruct the Search System, added the new class SearchResult. Remove Web#searchImage and combine it 
  with only one search method.
- Reconstruct help message so CommandListener can use it as static methods.

#### [0.1.5] - 2017-04-05
##### Added 
- EmojiCommand for getting informations about an emote.
- Log System implemented for exception and status tracking. (Changed try-catch in about 10 files.)
- ConsoleListener implemented for listening to commands from console. Current command: shutdown. There are more to come.

#### [0.1.8] - 2017-04-06
##### Added 
- PresenceCommand : setStatus and setGame commands for both discord and console thread.
- LyricsCommand for getting lyrics from Genius.com via JSoup. Search Lyrics function will be implemented in the future.

#### [0.1.6] - 2017-04-07
##### Added 
- GuessNumber Game and GuessNumberCommand. Enjoy~
##### Changed
- Log System from input `String guild` to `MessageReceivedEvent event` in order to supports Private Message Logging.
- Changed the CommandListener to listen to commands with mention. 

#### [0.1.9] - 2017-04-08
##### Added 
- NowPlayingCommand for getting the title, link, and duration of the now playing song.
- QueueCommand for getting the gueue list.
- LyricsCommand support search.
- IMDbCommand for getting results from IMDb.
##### Fixed
- PresenceCommand to work with @Mention prefix.
- HangManCommand: Start Game properly, and allow multiple start game to refresh the game.
- HangManCheaterCommand to change the output based on number of results.

#### [0.1.9] - 2017-04-09
##### Added 
- Number to Emoji Function for Number#RollADice
- IMDbCommand: Get specific informations about a movie/tv-show. Added Emojis
- EmojisCommand: Add SayEmoji Function in which the bot will talk in Emoji Language.
- InfoServerCommand: Get Server Info By ID.