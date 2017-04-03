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
    
    public final static String SHORT_INFO = 
            "**help (h)**\t\t\t**invite**\n"
            + "**botinfo (bi)**\t\t\t**serverinfo (si)**\n"
            + "**channelinfo (ci)**\t\t\t**userinfo (ui)**\n"
            + "**prefix**\t\t\t**ping**\n"
            + "**about**\t\t\t**support**\n";
    
    public final static String SHORT_MOD = "**prune (p)**\n**kick (k)**\n";
    public final static String SHORT_MOD2 = "**ban (b)**\n**unban (ub)**\n";
    public final static String SHORT_MIS = "**number (n)**\n**math (m)**\n**say**\n**weather (w)**\n";
    public final static String SHORT_MIS2 = "**search**\n**image**\n**8ball**\n**face**\n**game**";
    public final static String SHORT_MUSIC = "**prune (p)**\n**kick (k)**\n";
    public final static String SHORT_MUSIC2 = "**ban (b)**\n**unban (ub)**\n";
    
    public final static String INFO = "__**Information Commands**__\n"
                                    + "**help (h)** - Get a list of commands. *\n"
                                    + "**invite** - Get a invite link for this bot. *\n"
                                    + "**botinfo (bi)** - Get bot info. *\n"
                                    + "**serverinfo (si)** - Get server info.\n"
                                    + "**channelinfo (ci)** - Get channel info (Text/Voice Channel).\n"
                                    + "**userinfo (ui)** - Get user info. *\n"
                                    + "**prefix** - Set the server prefix. ~~(Under Development)~~\n"
                                    + "**ping** - Pong. *\n"
                                    + "**about** - Bot description. *\n"
                                    + "**support** - Related servers and bots. *\n";
    
    public final static String MOD = "__**Moderation Commands**__\n"
                                    + "**prune (p)** - Delete messages.\n"
                                    + "**kick (k)** - Kick member(s).\n"
                                    + "**ban (b)** - Ban member(s) by Mention.\n"
                                    + "**unban (ub)** - Unban member(s) by ID.\n";
    
    public final static String MISS = "__**Miscellaneous Commands**__\n"
                                    + "**number (n)** - Generate random numbers, roll a dice, or flip a coin. *\n";
    
    public final static String MISS_NUM = "*num random (n r)* - Generate random numbers.\n"
                                    + "*num count (n c)* - Count numbers.\n"
                                    + "*num roll* - Roll the dice.\n"
                                    + "*num coinflip (n cf)* - Flip a coin.\n";
    
    public final static String MISS2 = "**math (m)** - Let the bot to solve a math operation for you. *\n"
                                    + "**say** - Let the bot say something for you. Support Embed Message. *\n"
                                    + "**weather (w)** - Get the weather. *\n"
                                    + "***search*** - Search Google, Wikipedia, Urban Dictionary, Github, and custom sites. *\n"
                                    + "   *google (g)* - Search via Google Search Engine.\n"
                                    + "   *wiki* - Search Wikipedia.\n"
                                    + "   *urban* - Search Urban Dictionary.\n"
                                    + "   *github (git)* - Search Github.\n"
                                    + "***image*** - Search images from Imgur, Giphy, and KnowYourMeme. *\n"
                                    + "   *imgur* - Search from Imgur.\n"
                                    + "   *gif* - Search from Giphy.\n"
                                    + "   *meme* - Search from KnowYourMeme.\n"
                                    + "**8ball** - Ask the magic 8ball a question. *\n"
                                    + "**face** - ( ͡° ͜ʖ ͡°) *\n"
                                    + "***game*** - Play a game. \n"
                                    + "   *rockpaperscissors (rps)* - Play Rock Paper Scissors with the bot. *\n"
                                    + "   *tictactoe (ttt)* - Play a Tic Tac Toe Game XO!!\n"
                                    + "   *hangman (hm)* - Play HangMan. *\n";
    
    public final static String MISS_HMC = "   *hangmancheater (hmc)* - Hang Man Cheater base on the unknown word and missed letters. *\n";
    
    public final static String MUSIC = "__**Music Commands**__\n"
                                    + "**join (j)** - Add bot to a voice channel.\n"
                                    + "**leave  (l)** - Remove bot from a voice channel.\n";
    
    public final static String RESTRICT = "__**Restricted Commands**__\n"
                                    + "These commands are for **Server Owner** or **Bot Owner** only.\n"
                                    + "**shutdown** - Shut down the bot remotely. (Bot Owner) *\n"
                                    + "**source** - Get the source code of a command class. *\n";
    
    public final static String NOTE = "__**Note:**__\n"
                                    + "Adding the command/module name after `help` will show the command/module usage.\n"
                                    + "* - Available directly from Private Message. (Private Channel)";
}
