## Information Commands
[Information Commands Description](command/information/InformationCommands.md) <br />
- help (h) - Get a list of commands. *
- invite - Get a invite link for this bot. *
- botinfo (bi) - Get bot info. *
- serverinfo (si) - Get server info.
- channelinfo (ci) - Get channel info (Text/Voice Channel).
- userinfo (ui) - Get user info. *
- list (l) - Get a list of servers, members, roles or channels.
    - list server (l server) - Get a list of servers this bot is in. *
    - list member (l mem) - Get a list of members in a server.
    - list role (l role) - Get a list of roles in a server.
    - list channel (l channel) - Get a list of text and voice channels in a server.
- member - A list of members in this server.
- prefix - Set the server prefix. (Under Development)
- ping - Pong. *
- about - Bot description. *
- status - Bot status or uptime. *
- support - Related servers and bots. *

## Moderation Commands
[Moderation Commands Description](command/moderation/ModerationCommands.md) <br />
- mods - Get a list of mods.
- prune - Delete messages.
- clean - Advance prune options.
- kick - Kick member(s).
- warn - Warn a user in direct message.
- ban - Ban member(s) by Mention.
- unban - Unban member(s) by ID.
- softban - Kick and clean up member(s) messages.

## Utility Commands
- number (n) - Generate random numbers, roll a dice, or flip a coin. *
    - num random (n r) - Generate random numbers.
    - num count (n c) - Count numbers.
    - num roll - Roll the dice.
    - num coinflip (n cf) - Flip a coin.
- discrim - Search users with a given discriminator.
- avatar - Get the avatar image of a user.
- afk - Set afk status. *
- math - Let the bot to solve a math operation for you. *
- say - Let the bot say something for you. Support Embed Message. * 
- emoji (e)- Translate words into the Emoji language. *
- weather (w) - Get the weather. *
- search - Search sites using Google Search Engine. *
    - google (g) - Google Search. 
    - wiki - Search Wikipedia. 
    - urban (ub) - Search Urban Dictionary. 
    - github (git) - Search Github. 
    - imdb - Search IMDb.
- image - Search images from Imgur, Giphy, and KnowYourMeme. * 
    - imgur - Search from Imgur.
    - gif - Search from Giphy.
    - meme - Search from KnowYourMeme.

## Fun Commands
[Fun Commands Description](command/fun/FunCommands.md) <br />
- 8ball - Ask the magic 8ball a question. *
- Ascii - Turn plain text to Ascii Art. *
- face - Lenny * 
- spam - Spam yummy spams! *
- game - Play a game. 
    - rockpaperscissors (rps) - Play Rock Paper Scissors with the bot. * 
    - guessnum (gn) - Guess a number! *
    - tictactoe (ttt) - Play a Tic Tac Toe game XO!! 
    - hangman (hm) - Play HangMan. * 
    - hangmancheater (hmc) - Hang Man Cheater base on the unknown word and missed letters. *

## Music Commands
- music (m) - Get a list of music commands.
- join (j) - Add bot to a voice channel.
- leave - Remove bot from a voice channel.
- player (pl) - Music player controller.
- play - Play a song from YouTube.
- fm - Play automatic playlists.
- radio - Stream from a radio station.
- autoplay (ap) - YouTube AutoPlay mode.
- pause (ps) - Pause/Resume the current playing song.
- queue (q) - Show the playlist and what's coming next.
- skip - Skip the song.
- previous (pre) - Replay previous song.
- move (mv) - Skip to play a song in queue.
- nowplaying (np) - Get the information of the current playing song.
- volume - Set the volume of the bot.
- jump (jp) - Jump to a position of the song.
- shuffle (sf) - Shuffle the queue.
- repeat (rp) - Repeat the queued songs.
- stop - Stop the player.
- dump - Dump the queue.
- lyrics - Show the lyrics of a song. *

## Restricted Commands <br />
These commands are for **Server Owner** or **Bot Owner** only. <br />
- shutdown - Shut down the bot remotely. (Bot Owner) * ^
- Presence - Set the nickname, status or game of the bot. * ^
    - setNick - Set the nickname of the bot. * ^ 
    - setStatus - Set the status of the bot. * ^
    - setGame - Set the game of the bot. * ^
- source - Get the source code of a command class. * 
- log - Get the logs of this bot. *
- eval - Evaluate java code. (Bot Owner) *

## Note: <br />
- @Mention the bot with commands will also trigger the respond. <br />
- Adding the command/module name after help will show the command/module usage. <br />
- Adding `-h` after a command will show the command usage. <br />
- \* - Available directly from secret Message. (secret Channel) <br />
- ^ - Available directly from Console. <br />