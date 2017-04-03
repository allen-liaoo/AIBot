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
            + "**channelinfo (ci)**\n"
            + "**userinfo (ui)**\n"
            + "**prefix**\n"
            + "**ping**\n"
            + "**about**\n"
            + "**support**\n";
    
    public final static String INFO_DES = 
            "Get a list of commands. *\n"
            + "Get a invite link for this bot. *\n"
            + "Get bot info. *\n"
            + "Get server info.\n"
            + "Get channel info (Text/Voice Channel).\n"
            + "Get user info. *\n"
            + "Set the server prefix. ~~(Under Development)~~\n"
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
    * Miscellaneous Module
    */
    public final static String MIS_CMD = 
            "**number (n)**\n"
            + "**math (m)**\n"
            + "**say**\n"
            + "**weather (w)**\n"
            //Command Group- Search 
            + "**search**\n"
            + "   *google (g)*\n"
            + "   *wiki*\n"
            + "   *urban*\n"
            + "   *github (git)*"
            //Command Group- Image
            + "**image**\n"
            + "*imgur*\n"
            + "*gif*\n"
            + "*meme*"
            + "**8ball**\n"
            + "**face**\n"
            //Command Group- Game
            + "**game**\n"
            + "   *rockpaperscissors (rps)*"
            + "   *tictactoe (ttt)*"
            + "   *hangman (hm)*\n"
            + "   *hangmancheater (hmc)*\n";
    
    public final static String MIS_DES = 
            "Generate random numbers, roll a dice, or flip a coin. *\n"
            + "Let the bot to solve a math operation for you. *\n"
            + "Let the bot say something for you. Support Embed Message. *\n"
            + "Get the weather. *\n"
            //Command Group- Search 
            + "Search Google, Wikipedia, Urban Dictionary, Github, and custom sites. *\n"
            + "Search via Google Search Engine.\n"
            + "Search Wikipedia.\n"
            + "Search Urban Dictionary.\n"
            + "Search Github.\n"
            //Command Group- Image
            + "Search images from Imgur, Giphy, and KnowYourMeme. *\n"
            + "Search from Imgur.\n"
            + "Search from Giphy.\n"
            + "Search from KnowYourMeme.\n"
            + "Ask the magic 8ball a question. *\n"
            + "( ͡° ͜ʖ ͡°) *\n"
            //Command Group- Game
            + "Play a game. \n"
            + "Play Rock Paper Scissors with the bot. *\n"
            + "Play a Tic Tac Toe Game XO!!\n"
            + "Play HangMan. *\n"
            + "Hang Man Cheater base on the unknown word and missed letters. *\n";
    
    public final static String MISS_NUM = "*num random (n r)* - Generate random numbers.\n"
                                    + "*num count (n c)* - Count numbers.\n"
                                    + "*num roll* - Roll the dice.\n"
                                    + "*num coinflip (n cf)* - Flip a coin.\n";
    
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
            "These commands are for **Server Owner** or **Bot Owner** only.\n"
            + "**shutdown**\n"
            + "**source**\n";
    
    public final static String RESTRICT_DES = 
            "Shut down the bot remotely. (Bot Owner) *\n"
            + "Get the source code of a command class. *\n";
    
    
    /*
    * Note
    */
    public final static String NOTE = 
            "Adding the command/module name after `help` will show the command/module usage.\n"
            + "* - Available directly from Private Message. (Private Channel)";
}
