/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

//Setted to SUPPORT PRIVATE CHANNEL.

import Command.FunModule.EightBallCommand;
import Command.*;
import Command.ModerationModule.*;
import Command.UtilityModule.*;
import Command.MusicModule.*;
import Command.FunModule.*;
import Command.RestrictedModule.*;
import Config.*;
import Main.*;

import java.awt.Color;
import java.time.Instant;
import java.util.Random;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HelpCommand implements Command {
    
    public final static String HELP = "This command is for getting this bot's commands.\n"
                               + "Command Usage: `" + Prefix.getDefaultPrefix() + "help` or `" + Prefix.getDefaultPrefix() + "h`\n"
                               + "Parameter: `-h | -dm | Command/Module Name  | -m | null`\n"
                               + "MarkDown Type: __**Module**__, ***command group***, **command**, **(alter command)**, *sub command*, ~~(Under Development)~~";
    
    private final EmbedBuilder embed = new EmbedBuilder();
    MessageEmbed me = embed.build();
    private final EmbedBuilder embedusage = new EmbedBuilder();
    private final EmbedBuilder embedHelp = new EmbedBuilder();

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embedusage.setColor(Color.red);
        embedusage.setTitle("Information Module", null);
        embedusage.addField("Help -Help", HELP, true);
        embedusage.setFooter("Command Help/Usage", Info.I_help);
        embedusage.setTimestamp(Instant.now());

        MessageEmbed eu = embedusage.build();
        e.getChannel().sendMessage(eu).queue();
        embedusage.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) //Parameter null
        {
            embed.setColor(setColor());
            embed.setAuthor("AIBot Help", null, Info.I_help);
           
            embed.addField("Information Module", HelpText.INFO_CMD, true);
            embed.addField("Description", HelpText.INFO_DES, true);
            
            embed.addField("Moderation Module", HelpText.MOD_CMD, true);
            embed.addField("Description", HelpText.MOD_DES, true);
            
            embed.addField("Utility Module", HelpText.UTIL_CMD, true);
            embed.addField("Description", HelpText.UTIL_DES, true);
            
            embed.addField("Fun Module", HelpText.FUN_CMD, true);
            embed.addField("Description", HelpText.FUN_DES, true);
            
            embed.addField("Music Module", HelpText.MUSIC_CMD, true);
            embed.addField("Description", HelpText.MUSIC_DES, true);
            
            embed.addField("Restricted Module", HelpText.RESTRICT_CMD, true);
            embed.addField("Description", HelpText.RESTRICT_DES, true);
            
            embed.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
            embed.setFooter("Commands List", null);
            embed.setTimestamp(Instant.now());

            me = embed.build();
            e.getChannel().sendMessage(me).queue();
            embed.clearFields(); //Refresh EmbedMessage
        }
        
        else if("-dm".equals(args[0]) && e.getChannelType() != e.getChannelType().PRIVATE) //Parameter -dm
        {
            e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(me).queue());
            embedHelp.clearFields(); //Refresh EmbedMessage
            e.getTextChannel().sendMessage(Emoji.envelope + " A list of AIBot commands has been sent. Check your Direct Message.").queue();
        }
        
        else if("-h".equals(args[0])) //Parameter -h
        {
            help(e);
        }
        
        /*else if("-m".equals(args[0]) ) //Parameter more
        {
            embedm.setColor(setColor());
            embedm.setAuthor("AIBot Help", null, Info.I_help);
            embedm.setDescription(HELP_TEXT_M);
            embedm.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
            embedm.setFooter("Commands List (More)", null);
            embedm.setTimestamp(Instant.now());

            MessageEmbed mem = embedm.build();
            e.getChannel().sendMessage(mem).queue();
            embedm.clearFields(); //Refresh EmbedMessage
        }*/
        
        else //Parameter commands name
        {
            String cmdhelp = Emoji.error + " Cannot find such command/module.", cmdhelp2 = "", cmdtitle = args[0];
            boolean isMod = false; //Check if this is module or command.
            
            switch (cmdtitle) {
                //Information Module
                case "information":
                case "info":
                    cmdhelp = HelpText.INFO_CMD;
                    cmdhelp2 = HelpText.INFO_DES;
                    isMod = true;
                    break;
                case "invite": cmdhelp = InviteCommand.HELP;
                    break;
                case "botinfo":
                case "bi":
                    cmdhelp = InfoBotCommand.HELP;
                    break;
                case "serverinfo":
                case "si":
                    cmdhelp = InfoServerCommand.HELP;
                    break;
                case "channelinfo":
                case "ci":
                    cmdhelp = InfoChannelCommand.HELP;
                    break;
                case "userinfo":
                case "ui":
                    cmdhelp = InfoUserCommand.HELP;
                    break;
                case "prefix": cmdhelp = PrefixCommand.HELP;
                    break;
                case "ping": cmdhelp = PingCommand.HELP;
                    break;
                case "about": cmdhelp = AboutCommand.HELP;
                    break;
                case "support": cmdhelp = SupportCommand.HELP;
                    break;
                    
                //Moderation Module
                case "moderation":
                case "mod":
                    cmdhelp = HelpText.MOD_CMD;
                    cmdhelp2 = HelpText.MOD_DES;
                    isMod = true;
                    break;
                case "prune": cmdhelp = PruneCommand.HELP;
                    break;
                case "kick":
                case "k": cmdhelp = KickCommand.HELP;
                    break;
                case "ban": 
                case "b": cmdhelp = BanCommand.HELP;
                    break;
                case "unban": 
                case "ub": cmdhelp = UnbanCommand.HELP;
                    break;

                //Utility Module
                case "utility":
                case "util":
                    cmdhelp = HelpText.UTIL_CMD;
                    cmdhelp2 = HelpText.UTIL_DES;
                    isMod = true;
                    break;
                //Command Group- Number
                case "number":
                case "num":
                case "n":
                    cmdhelp = NumberCommand.HELP;
                    break;
                case "math":
                case "calc":
                case "m":
                    cmdhelp = MathCommand.HELP;
                    break;
                case "say":
                    cmdhelp = SayCommand.HELP;
                    break;
                case "weather":
                case "w":
                    cmdhelp = WeatherCommand.HELP;
                    break;
                //Command Group- Search
                case "search":
                case "google":
                case "g":
                case "wiki":
                case "urban":
                case "github":
                case "git":
                    cmdhelp = SearchCommand.HELP;
                    break;
                //Command Group- Image
                case "image":
                case "imgur":
                case "gif":
                case "meme":
                    cmdhelp = ImageCommand.HELP;
                    break;
                    
                //Fun Module
                case "fun":
                    cmdhelp = HelpText.FUN_CMD;
                    cmdhelp2 = HelpText.FUN_DES;
                    isMod = true;
                    break;
                case "8ball": cmdhelp = EightBallCommand.HELP;
                    break;
                case "face": 
                case "f": 
                    cmdhelp = FaceCommand.HELP;
                    break;
                //Command Group- Game
                case "game": cmdhelp = GameCommand.HELP;
                    break;
                case "rockpaperscissors": 
                case "rps": 
                    cmdhelp = RPSCommand.HELP;
                    break;
                case "tictactoe": 
                case "ttt": 
                    cmdhelp = TicTacToeCommand.HELP;
                    break;
                case "hangman": 
                case "hm": 
                    cmdhelp = HangManCommand.HELP;
                    break;
                case "hangmancheater": 
                case "hmc": 
                    cmdhelp = HangManCheaterCommand.HELP;
                    break;
                    
                //Music Module
                case "music":
                    cmdhelp = HelpText.MUSIC_CMD;
                    cmdhelp2 = HelpText.MUSIC_DES;
                    isMod = true;
                    break;
                case "join":
                case "j": 
                    cmdhelp = JoinCommand.HELP;
                    break;
                case "leave":
                case "l":
                    cmdhelp = LeaveCommand.HELP;
                    break;
                    
                //Restricted Module
                case "restricted":
                case "restrict":
                    cmdhelp = HelpText.RESTRICT_CMD;
                    cmdhelp2 = HelpText.RESTRICT_DES;
                    isMod = true;
                    break;
                case "shutdown": cmdhelp = ShutDownCommand.HELP;
                    break;
                case "source": cmdhelp = SourceCommand.HELP;
                    break;
                    
                default: break;
                    
            }
        
            
            
            String morc = ""; //Set fields' text to command/module.
            
            if(isMod == false) //If this is a command
            {
                morc = "Command";
            }
            else
            {
                morc = "Module";
            }
            
            //Set EmbedMessage addField title.
            if(cmdhelp.equals(Emoji.error + " Cannot find such command/module."))
                    cmdtitle = "NA";
            else 
            {
                cmdtitle = cmdtitle.substring(0, 1).toUpperCase()+ cmdtitle.substring(1); //Make the first letter to Upper case.
                cmdtitle += " -Help";
            }
            
            embedHelp.setTitle("AIBot Help -" + morc, null); //Set title for command
            embedHelp.setColor(setColor());
            
            embedHelp.addField(cmdtitle, cmdhelp, true);
            if(isMod == true)
                embedHelp.addField("Discription", cmdhelp2, true);
            embedHelp.setFooter(morc + " Help/Usage", Info.I_help);
            embedHelp.setTimestamp(Instant.now());

            MessageEmbed meh = embedHelp.build();
            e.getChannel().sendMessage(meh).queue();
            embedHelp.clearFields(); //Refresh EmbedMessage
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    public static Color setColor()
    {
        Random colorpicker = new Random();
        
        int red;
        int green;
        int blue;
        
        red = colorpicker.nextInt(255) + 1;
        green = colorpicker.nextInt(255) + 1;
        blue = colorpicker.nextInt(255) + 1;
        
        return new Color(red,green,blue);
    }
    
}
