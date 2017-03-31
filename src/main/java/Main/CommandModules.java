/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Command.HelpCommand;

/**
 *
 * @author liaoyilin
 */
public class CommandModules {
    public final static String HELP_INFO = "__**Information Commands**__\n"
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
    
    public final static String HELP_MOD = "__**Moderation Commands**__\n"
                                    + "**prune (p)** - Delete messages.\n"
                                    + "**kick (k)** - Kick member(s).\n"
                                    + "**ban (b)** - Ban member(s) by Mention.\n"
                                    + "**unban (ub)** - Unban member(s) by ID.\n";
    
    public final static String HELP_MISS = "__**Miscellaneous Commands**__\n"
                                    + "**number (n)** - Generate random numbers, roll a dice, or flip a coin. *\n";
    
    public final static String HELP_MISS_NUM = "*num random (n r)* - Generate random numbers.\n"
                                    + "*num count (n c)* - Count numbers.\n"
                                    + "*num roll* - Roll the dice.\n"
                                    + "*num coinflip (n cf)* - Flip a coin.\n";
    
    public final static String HELP_MISS2 = "**math (m)** - Let the bot to solve a math operation for you. *\n"
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
                                    + "***game*** - Play a game. *\n"
                                    + "   **tictactoe (ttt)** - Play a Tic Tac Toe Game XO!!\n";
    
    public final static String HELP_MUSIC = "__**Music Commands**__\n"
                                    + "**join (j)** - Add bot to a voice channel.\n"
                                    + "**leave  (l)** - Remove bot from a voice channel.\n";
    
    public final static String HELP_RESTRICT = "__**Restricted Commands**__\n"
                                    + "These commands are for **Server Owner** or **Bot Owner** only.\n"
                                    + "**shutdown** - Shut down the bot remotely. (Bot Owner) *\n"
                                    + "**source** - Get the source code of a command class. *\n";
    
    public final static String HELP_NOTE = "__**Note:**__\n"
                                    + "Adding the command/module name after `help` will show the command/module usage.\n"
                                    + "* - Available directly from Private Message. (Private Channel)";
}
