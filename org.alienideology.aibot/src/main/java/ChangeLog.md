# Change Log
All notable changes to this project will be documented in this file.
Log type: Added, Changed, Deprecated, Removed, Fixed, Security

#### [0.3.5] - 2017-05-18 ~ 2017-05-29
##### Added
- AFKCommand for setting AFK status
- UserWrapper, GlobalWatchDog for AFK status
- EvalCommand to evaluate java code. Use Nashorn script engine.
- AvatarCommand, a simple command to get link and attachment of a avatar.
- ModsCommand for listing and autopin mod(s).
- AsciiCommand for turning plain text into Ascii Arts.
##### Fixed
- Embed message for LyricsCommand, looks better.
- Divide lyrics sections into embed fields using regexs. 

#### [0.3.4] - 2017-05-7 ~ 2017-05-11
##### Added
- CleanCommand which cleans messages by specific targets.
- MoveCommand which skip to a position in the queue.
- Shard system (Reconstructed the main bot system).
##### Fixed
- Change TrackScheduler to GuildPlayer, and add most Music static methods into Guild Player.
- SkipCommand to work with searching the queue and skip.
- Local Playlist(FM) from txt to Json format.
- GuildListener onVoiceJoin/Leave/Move to resume or pause properly.
- Radio status methods to normal methods. Created subclass RadioStation and load them on startup.

#### [0.3.2] - 2017-05-01 ~ 2017-05-06
##### Added
- AutoPlayCommand which works like YouTube autoplay mode.
- Music#checkMode for the logic from switching between modes.
- QueueList, a custom implementation of Collection(LinkedList) for track queue.
- PreviousCommand that will replay the previous song. Added a QueueList for storing up to 5 previous songs.
- SoftBanCommand that will kick and clean up member(s) messages.

#### [0.3.0] - 2017-04-12 ~ 2017-04-30
##### Added
- ShuffleCommand to shuffle the queue
- TextRespond for hidden responds to some trigger words. (For example: "lmao")
- ListCommand that includes listing servers, members, roles, and channels.
- RepeatCommand for repeating the songs.
- JumpCommand for jumping to a certain position of the current playing song.
- PlayerCommand for a neat player and emoji reaction controller.
- Add emoji reaction controller for PlayerCommmand, QueueCommand, ListCommand. (Planning to add it to SongCommand)
##### Fixed
- Change command.java from interface to abstract class to reduce code.
- PlayerCommand now have reaction control function
- Massively add/change the short aliases for MusicModule

#### [0.2.5] - 2017-04-16 ~ 2017-04-21
##### Added
- The bot will automatically resume or pause when a user join or leave the Voice Channel
- UtilTool: capTokens for capitalizing every first letter of the regex split.
- DumpCommand for clearing the queue.
- UtilBot for bot utilities like setting status, game or getting connected voice channels
- ServerCommand for getting a well organized list of servers the bot is in, in a code block.
##### Fixed
- Prevent the bot to do any action when an member is not in the same voice channel 
  as the bot, but request the bot to skip, stop, join, or leave.
- Added isEmpty() for AudioTrackWrapper to fix null pointers
- Enhance prune command
- Add npe check for unsupported private messages
- Enhance delete command call
- Deleted `Called()` and `Execute()` because they are never used in any command Class.

#### [0.2.5] - 2017-04-15
##### Added 
- FM Local Play lists: Christina Perri, Favorite, and Troll
- AudioTrackWrapper, remove requester array
- Bot is now Rate Limited. Not finished yet.
##### Fixed
- When FM, normal request, radio all mix together, cannot skip, queue, or get song info properly.
- Load Youtube Playlist Successfully
- setStatus and setGame reduce cose
- 2 Weather Emojis to proper switch-case
- Uptime hours to proper readable form
- @Mention Prefix now support server nickname.

#### [0.2.3] - 2017-04-12, and 2017-04-13
##### Added 
- FM System which play automatic loaded play lists
- Radio System which stream a radio station
- Vote Skip System
- YouTube Thumbnails for NowPlayingCommand
##### Fixed
- Force Skip for server owners and administrators
- Several bugs in Music Module and Information Module

#### [0.2.0] - 2017-04-11
##### Added 
- GuildSetting: Added TextChannel and VoiceChannel instance so the bot is able to detect 
  onTrackStart() event and send a message saying "Now playing ...".
- PlayCommand dynamic command for YouTube search (Show top 5 results)
- MusicCommand for getting a list of music commands.
##### Fixed
- NowPlayingCommand and QueueCommand was not able to get current track information.
- Several NullPointerExceptions from Music Module. Moved Music.musicStartup before jda start up.
- RPSCommand bugged switch statement

#### [0.1.9] - 2017-04-09
##### Added 
- Number to Emoji Function for Number#RollADice
- IMDbCommand: Get specific informations about a movie/tv-show. Added Emojis
- EmojisCommand: Add SayEmoji Function in which the bot will talk in Emoji Language.
- InfoServerCommand: Get Server Info By ID.

#### [0.1.8] - 2017-04-08
##### Added 
- NowPlayingCommand for getting the title, link, and duration of the now playing song.
- QueueCommand for getting the gueue list.
- LyricsCommand support search.
- IMDbCommand for getting results from IMDb.
##### Fixed
- PresenceCommand to work with @Mention prefix.
- HangManCommand: Start game properly, and allow multiple start game to refresh the game.
- HangManCheaterCommand to change the output based on number of results.

#### [0.1.7] - 2017-04-07
##### Added 
- GuessNumber game and GuessNumberCommand. Enjoy~
##### Changed
- Log System from input `String guild` to `MessageReceivedEvent event` in order to supports secret Message Logging.
- Changed the CommandListener to listen to commands with mention. 

#### [0.1.6] - 2017-04-06
##### Added 
- PresenceCommand : setStatus and setGame commands for both discord and console thread.
- LyricsCommand for getting lyrics from Genius.com via JSoup. Search Lyrics function will be implemented in the future.

#### [0.1.5] - 2017-04-05
##### Added 
- EmojiCommand for getting informations about an emote.
- Log System implemented for exception and status tracking. (Changed try-catch in about 10 files.)
- ConsoleListener implemented for listening to commands from console. Current command: shutdown. There are more to come.

#### [0.1.2] - 2017-04-04
##### Added
- Add Play, Pause, Skip, Volume, and Stop command for Music Module. Pretty straight forward.
- Bot can detect mention from CommandListener and send help message.
##### Changed
- Reconstruct the Search System, added the new class SearchResult. Remove Web#searchImage and combine it 
  with only one search method.
- Reconstruct help message so CommandListener can use it as static methods.

#### [0.1.0] - 2017-04-03
##### Changed
- Reconstruct the full HelpCommand from one crunch of String to two fields.
##### Added
- Add StatusCommand, this command includes Bot's uptime, guild, shard, and Operating System info. Will add CPU usage.