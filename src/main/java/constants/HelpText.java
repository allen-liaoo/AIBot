/* 
 * AIBot by AlienIdeology
 * 
 * HelpText
 * Help message for HelpCommand
 */
package constants;

/**
 *
 * @author liaoyilin
 */
public class HelpText {
    
    /*
    * Information Module
    */
    public final static String INFO_CMD = 
            "**help (h)**\n"
            + "**invite**\n"
            + "**botinfo (bi)**\n"
            + "**serverinfo (si)**\n"
            + "**channelinfo (ci)**\n\n"
            + "**userinfo (ui)**\n"
            + "**list (l)**\n\n"
            + "*list server (l server)*\n"
            + "*list member (l mem)*\n"
            + "*list role (l role)*\n"
            + "*list channel (l channel)*\n"
            + "**prefix**\n"
            + "**ping**\n"
            + "**about**\n"
            + "**status**\n"
            + "**support**\n";
    
    public final static String INFO_DES = 
            "Get a list of commands. *\n"
            + "Get a invite link for this bot. *\n"
            + "Get bot info. *\n"
            + "Get server info.\n"
            + "Get channel info. \n(Text/Voice Channel)\n"
            + "Get user info. *\n"
            + "Get a list of servers, members\n"
            + "roles, or channels.\n"
            + "A list of servers. *\n"
            + "A list of members of this server.\n"
            + "A list of roles of this server.\n"
            + "A list of text and voice channels.\n"
            + "Get the bot's prefix. *\n"
            + "Pong. *\n"
            + "Bot description. *\n"
            + "Bot status and uptime. *\n"
            + "Related servers and bots. *\n";
    
    /*
    * Moderation Module
    */
    public final static String MOD_CMD = 
            "**prune**\n"
            + "**kick**\n"
            + "**warn**\n"
            + "**ban**\n"
            + "**unban**\n"
            + "**softban**\n\n";
    
    public final static String MOD_DES = 
            "Delete messages.\n"
            + "Kick member(s).\n"
            + "Warn a user in dm.\n"
            + "Ban member(s) by Mention.\n"
            + "Unban member(s) by ID.\n"
            + "kick and clean up member(s)\n messages.\n";
    
    /*
    * utility Module
    */
    public final static String UTIL_CMD = 
            "***number (n)***\n\n"
            + "*num random (n r)*\n"
            + "*num count (n c)*\n"
            + "*num roll  (n roll)*\n"
            + "*num coinflip (n cf)*\n"
            + "**math**\n\n"
            + "**say**\n\n"
            + "**emoji (e)**\n\n"
            + "**weather (w)**\n"
            //command Group- Search
            + "***search***\n"
            + "*google (g)*\n"
            + "*wiki*\n"
            + "*urban (ub)*\n"
            + "*github (git)*\n"
            + "*imdb*\n"
            //command Group- Image
            + "***image***\n"
            + "*imgur*\n"
            + "*gif*\n"
            + "*meme*\n";
    
    public final static String UTIL_DES = 
            "Generate random numbers, \nroll a dice, or flip a coin. *\n"
            + "Generate random numbers.\n"
            + "Count numbers.\n"
            + "Roll the dice.\n"
            + "Flip a coin.\n"
            + "Let the bot to solve a \nmath operation for you. *\n"
            + "Let the bot say something \nfor you. Support Embed Message. *\n"
            + "Translate words into the \nEmoji language. *\n"
            + "Get the weather. *\n"
            //command Group- Search
            + "Search Custom sites. *\n"
            + "Google Search.\n"
            + "Wikipedia.\n"
            + "Urban Dictionary.\n"
            + "Github.\n"
            + "Internet Movie Database\n"
            //command Group- Image
            + "Search images. *\n"
            + "Search from Imgur.\n"
            + "Search from Giphy.\n"
            + "Search from KnowYourMeme.\n";
            
    /*
    * Fun Module
    */
    public final static String FUN_CMD = 
            "**8ball**\n"
            + "**face**\n"
            + "**spam**\n"
            //command Group- game
            + "***game***\n"
            + "*rockpaperscissors (rps)*\n\n"
            + "*guessnum (gn)*\n"
            + "*tictactoe (ttt)*\n"
            + "*hangman (hm)*\n"
            + "*hangmancheater (hmc)*\n\n";
    
    public final static String FUN_DES = 
            "Ask the magic 8ball a question. *\n"
            + "( ͡° ͜ʖ ͡°) *\n"
            + "Spam yummy spams! *\n"
            //command Group- game
            + "Play a game. *\n"
            + "Play Rock Paper Scissors \nwith the bot. *\n"
            + "Guess a number! *\n"
            + "Play a Tic Tac Toe game XO!!\n"
            + "Play HangMan. *\n"
            + "Hang Man Cheater base on \nthe unknown word and missed letters. *\n";
    
    /*
    * Music Module
    */
    public final static String MUSIC_CMD = 
            "**music (m)**\n"
            + "**join (j)**\n"
            + "**leave**\n"
            + "**player (pl)**\n"
            + "**play (p)**\n"
            + "**fm**\n"
            + "**radio**\n"
            + "**autoplay (ap)**\n"
            + "**pause (ps)**\n"
            + "**queue (q)**\n\n"
            + "**skip**\n"
            + "**previous (pre)**\n"
            + "**nowplaying (np)**\n\n"
            + "**volume**\n"
            + "**jump (jp)**\n"
            + "**shuffle (sf)**\n"
            + "**repeat (rp)**\n"
            + "**stop**\n"
            + "**dump**\n"
            + "**lyrics**\n";
    
    public final static String MUSIC_DES = 
            "Get a list of music commands.\n"
            + "Add bot to a voice channel.\n"
            + "Remove bot from a voice channel.\n"
            + "Music player controller.\n"
            + "Play a song from YouTube.\n"
            + "Play automatic playlists.\n"
            + "Stream from a radio station.\n"
            + "YouTube AutoPlay mode.\n"
            + "Pause/Resume the current playing song.\n"
            + "Show the playlist and what's \ncoming next.\n"
            + "Skip a song.\n"
            + "Replay previous song.\n"
            + "Get the information of the \ncurrent playing song.\n"
            + "Set the volume of the bot.\n"
            + "Jump to a position of the song.\n"
            + "Shuffle the queue.\n"
            + "Repeat the queued songs.\n"
            + "Dump the queue.\n"
            + "Stop the player.\n"
            + "Show the lyrics of a song.\n";
    
    /*
    * Restricted Module
    */
    public final static String RESTRICT_CMD = 
            "Require Special Permissions\n"
            + "**shutdown**\n\n"
            + "**setNick**\n"
            + "**setStatus**\n"
            + "**setGame**\n"
            + "**source**\n"
            + "**log**\n";
    
    public final static String RESTRICT_DES = 
            "Bot Owner, Server Owner, etc.\n"
            + "Shut down the bot remotely. \n(Bot Owner Only) * ^\n"
            + "Set the server nickname of the bot. *\n"
            + "Set the status of the bot. * ^\n"
            + "Set the game of the bot. * ^\n"
            + "Get the source code. *\n"
            + "Get the logs of this bot. *\n";
    
    /*
    * Note
    */
    public final static String NOTE = 
            "Adding the command/module name after `help` will show the command/module usage.\n"
            + "* - Available directly from PrivateConstant Message. (PrivateConstant Channel)\n"
            + "^ - Available directly from Console.";
}
