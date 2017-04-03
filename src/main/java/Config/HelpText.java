/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import Command.InformationModule.HelpCommand;

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
            + "**prefix**\n\n"
            + "**ping**\n"
            + "**about**\n"
            + "**support**\n";
    
    public final static String INFO_DES = 
            "Get a list of commands. *\n"
            + "Get a invite link for this bot. *\n"
            + "Get bot info. *\n"
            + "Get server info.\n"
            + "Get channel info. \n(Text/Voice Channel)\n"
            + "Get user info. *\n"
            + "Set the server prefix. \n~~(Under Development)~~\n"
            + "Pong. *\n"
            + "Bot description. *\n"
            + "Related servers and bots. *\n";
    
    /*
    * Moderation Module
    */
    public final static String MOD_CMD = 
            "**prune (p)**\n"
            + "**kick (k)**\n"
            + "**ban (b)**\n**"
            + "unban (ub)**\n";
    
    public final static String MOD_DES = 
            "Delete messages.\n"
            + "Kick member(s).\n"
            + "Ban member(s) by Mention.\n"
            + "Unban member(s) by ID.\n";
    
    /*
    * Utility Module
    */
    public final static String UTIL_CMD = 
            "**number (n)**\n\n"
            + "**math (m)**\n\n"
            + "**say**\n\n"
            + "**weather (w)**\n"
            //Command Group- Search 
            + "**search**\n"
            + "*google (g)*\n"
            + "*wiki*\n"
            + "*urban*\n"
            + "*github (git)*\n"
            //Command Group- Image
            + "**image**\n"
            + "*imgur*\n"
            + "*gif*\n"
            + "*meme*\n";
    
    public final static String UTIL_DES = 
            "Generate random numbers, \nroll a dice, or flip a coin. *\n"
            + "Let the bot to solve a \nmath operation for you. *\n"
            + "Let the bot say something \nfor you. Support Embed Message. *\n"
            + "Get the weather. *\n"
            //Command Group- Search 
            + "Search Custom sites. *\n"
            + "Google Search.\n"
            + "Wikipedia.\n"
            + "Urban Dictionary.\n"
            + "Github.\n"
            //Command Group- Image
            + "Search images. *\n"
            + "Search from Imgur.\n"
            + "Search from Giphy.\n"
            + "Search from KnowYourMeme.\n";
    
    public final static String UTIL_NUM = "*num random (n r)* - Generate random numbers.\n"
                                    + "*num count (n c)* - Count numbers.\n"
                                    + "*num roll* - Roll the dice.\n"
                                    + "*num coinflip (n cf)* - Flip a coin.\n";
            
    /*
    * Fun Module
    */
    public final static String FUN_CMD = 
            "**8ball**\n"
            + "**face**\n"
            //Command Group- Game
            + "**game**\n"
            + "*rockpaperscissors (rps)*\n\n"
            + "*tictactoe (ttt)*\n"
            + "*hangman (hm)*\n"
            + "*hangmancheater (hmc)*\n\n";
    
    public final static String FUN_DES = 
            "Ask the magic 8ball a question. *\n"
            + "( ͡° ͜ʖ ͡°) *\n"
            //Command Group- Game
            + "Play a game. *\n"
            + "Play Rock Paper Scissors \nwith the bot. *\n"
            + "Play a Tic Tac Toe Game XO!!\n"
            + "Play HangMan. *\n"
            + "Hang Man Cheater base on \nthe unknown word and missed letters. *\n";
    
    /*
    * Music Module
    */
    public final static String MUSIC_CMD = 
            "**join (j)**\n"
            + "**leave  (l)**\n";
    
    public final static String MUSIC_DES = 
            "Add bot to a voice channel.\n"
            + "Remove bot from a voice channel.\n";
    
    /*
    * Restricted Module
    */
    public final static String RESTRICT_CMD = 
            "**Server Owner** or\n"
            + "**shutdown**\n\n"
            + "**source**\n";
    
    public final static String RESTRICT_DES = 
            "**Bot Owner** only.\n"
            + "Shut down the bot remotely. \n(Bot Owner) *\n"
            + "Get the source code of a \ncommand class. *\n";
    
    
    /*
    * Note
    */
    public final static String NOTE = 
            "Adding the command/module name after `help` will show the command/module usage.\n"
            + "* - Available directly from Private Message. (Private Channel)";
}
