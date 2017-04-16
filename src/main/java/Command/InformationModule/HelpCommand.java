/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Resource.Emoji;
import Resource.HelpText;
import Setting.Prefix;
import Resource.Info;
import Command.FunModule.EightBallCommand;
import Command.*;
import Command.ModerationModule.*;
import Command.UtilityModule.*;
import Command.MusicModule.*;
import Command.FunModule.*;
import Command.RestrictedModule.*;
import Utility.UtilTool;
import Utility.SmartLogger;

import java.awt.Color;
import java.time.Instant;

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
                               + "Parameter: `-h | -dm [Page Number] | Command/Module Name  | [Page Number] | null`\n"
                               + "MarkDown Type: __**Module**__, ***command group***, **command**, **(alter command)**, *sub command*, ~~(Under Development)~~";
    
    public static MessageEmbed me = embed.build();
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
        embedusage.setFooter("Command Help/Usage", Info.I_HELP);
        embedusage.setTimestamp(Instant.now());

        MessageEmbed eu = embedusage.build();
        e.getChannel().sendMessage(eu).queue();
        embedusage.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        SmartLogger.commandLog(e, this.getClass().getName(), "Triggered!");
        
        //Default
        if(args.length == 0 || args.length == 1 && Character.isDigit(args[0].charAt(0))) //Parameter null
        {
            int page = 1;
            if(args.length != 0)
               page = Integer.parseInt(args[0]);
            
            EmbedBuilder emhelp = helpText(e, page);
            me = emhelp.build();
            e.getChannel().sendMessage(me).queue();
            emhelp.clearFields();
        }
        
        //Parameter -dm
        else if("-dm".equals(args[0]) && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            e.getTextChannel().sendMessage(Emoji.envelope + " A full list of AIBot commands has been sent. Check your Direct Message.").queue();
            
            int page = 1;
            if(args.length == 2 && Character.isDigit(args[1].charAt(0)))
                page = Integer.parseInt(args[1]);
            else
            {
                for(int i = 0; i < 4; i++)
                {
                    EmbedBuilder emhelp = helpText(e, i+1);
                    
                    me = emhelp.build();
                    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage("Help is on its way...").complete().editMessage(me).submit());
                    emhelp.clearFields();
                }
            }
        }
        
        //Parameter -h
        else if("-h".equals(args[0])) 
        {
            help(e);
        }
        
        else //Parameter commands name
        {
            helpCustom(args[0], e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    public static EmbedBuilder helpText(MessageReceivedEvent e, int page)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("**There are more commands! Do `=help 1, 2, 3, or 4`**");
        
        //Assign fields to a certain page
        switch (page) {
            case 2:
                embed.addField("Utility Module", HelpText.UTIL_CMD, true);
                embed.addField("Description", HelpText.UTIL_DES, true);
                break;
            case 3:
                embed.addField("Fun Module", HelpText.FUN_CMD, true);
                embed.addField("Description", HelpText.FUN_DES, true);

                embed.addField("Music Module", HelpText.MUSIC_CMD, true);
                embed.addField("Description", HelpText.MUSIC_DES, true);
                break;
            case 4:
                embed.addField("Restricted Module", HelpText.RESTRICT_CMD, true);
                embed.addField("Description", HelpText.RESTRICT_DES, true);
                
                embed.addField("Notes", HelpText.NOTE, false);
                break;
            default:
            case 1:
                page = 1;
                embed.addField("Information Module", HelpText.INFO_CMD, true);
                embed.addField("Description", HelpText.INFO_DES, true);
                
                embed.addField("Moderation Module", HelpText.MOD_CMD, true);
                embed.addField("Description", HelpText.MOD_DES, true);
                break;
        }
        
        embed.setColor(UtilTool.randomColor());
        embed.setAuthor("AIBot Command List | Page " + page, Info.B_GITHUB, Info.I_HELP);
        embed.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setFooter("Commands List", null);
        
        return embed;
    }
    
    public void helpCustom(String cmdtitle, MessageReceivedEvent e)
    {
        String cmdhelp = Emoji.error + " Cannot find such command/module.", cmdhelp2 = "";
        boolean isMod = false; //Check if this is module or command.

        switch (cmdtitle.toLowerCase()) {
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
            case "status": cmdhelp = StatusCommand.HELP;
                break;
            case "uptime": cmdhelp = StatusCommand.HELP;
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
            case "emoji":
            case "emote":
            case "e":
                cmdhelp = EmojiCommand.HELP;
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
            case "imdb":
                cmdhelp = IMDbCommand.HELP;
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
            case "guessnum": 
            case "gn": 
                cmdhelp = GuessNumberCommand.HELP;
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
            case "summon":
            case "j": 
                cmdhelp = JoinCommand.HELP;
                break;
            case "leave":
            case "l":
                cmdhelp = LeaveCommand.HELP;
                break;
            case "play":
                cmdhelp = PlayCommand.HELP;
                break;
            case "fm":
                cmdhelp = FMCommand.HELP;
                break;
            case "radio":
                cmdhelp = RadioCommand.HELP;
                break;
            case "pause":
            case "resume":
            case "unpause":
                cmdhelp = PauseCommand.HELP;
                break;
            case "skip":
                cmdhelp = SkipCommand.HELP;
                break;
            case "nowplaying":
            case "current":
            case "np":
                cmdhelp = SongCommand.HELP;
                break;
            case "queue":
                cmdhelp = QueueCommand.HELP;
                break;
            case "volume":
                cmdhelp = VolumeCommand.HELP;
                break;
            case "stop":
                cmdhelp = StopCommand.HELP;
                break;
            case "lyrics":
                cmdhelp = LyricsCommand.HELP;
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

        embedHelp.setAuthor("AIBot Help -" + morc, Info.B_GITHUB,null); //Set title for command
        embedHelp.setColor(UtilTool.randomColor());

        embedHelp.addField(cmdtitle, cmdhelp, true);
        if(isMod == true)
            embedHelp.addField("Discription", cmdhelp2, true);
        embedHelp.setFooter(morc + " Help/Usage", Info.I_HELP);
        embedHelp.setTimestamp(Instant.now());

        MessageEmbed meh = embedHelp.build();
        e.getChannel().sendMessage(meh).queue();
        embedHelp.clearFields(); //Refresh EmbedMessage
    }
    
}
