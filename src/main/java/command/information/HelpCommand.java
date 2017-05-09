/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import constants.Global;
import constants.HelpText;
import setting.Prefix;
import command.fun.EightBallCommand;
import command.*;
import command.moderation.*;
import command.utility.*;
import command.music.*;
import command.fun.*;
import command.restricted.*;
import system.AILogger;
import system.selector.EmojiSelection;
import listener.SelectorListener;
import utility.UtilBot;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.rmi.CORBA.Util;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HelpCommand extends Command {
    
    public final static String HELP = "This command is for getting this bot's commands.\n"
                               + "Command Usage: `" + Prefix.getDefaultPrefix() + "help` or `" + Prefix.getDefaultPrefix() + "h`\n"
                               + "Parameter: `-h | -dm [Page Number] | command/Module Name  | [Page Number] | null`\n"
                               + "MarkDown Type: __**Module**__, ***command group***, **command**, **(alter command)**, *sub command*, ~~(Under Development)~~";

    private static final List<String> reactions = Arrays.asList(Emoji.ONE, Emoji.TWO, Emoji.THREE, Emoji.FOUR, Emoji.CLOSE);

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Help -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        AILogger.commandLog(e, this.getClass().getName(), "Triggered!");
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        //Default
        if(args.length == 0 || args.length == 1 && Character.isDigit(args[0].charAt(0))) //Parameter null
        {
            try {
                int page = 1;
                if(args.length != 0)
                   page = Integer.parseInt(args[0]);

                EmbedBuilder emhelp = helpText(e, page);
                e.getChannel().sendMessage(emhelp.build()).queue( (Message msg) -> {
                    SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                        @Override
                        public void action(int chose) {
                            switch(chose) {
                                case 0:
                                    HelpCommand hc = new HelpCommand();
                                    hc.action(new String[] {1+""},e);
                                    break;
                                case 1:
                                    HelpCommand hc1 = new HelpCommand();
                                    hc1.action(new String[] {2+""},e);
                                    break;
                                case 2:
                                    HelpCommand hc2 = new HelpCommand();
                                    hc2.action(new String[] {3+""},e);
                                    break;
                                case 3:
                                    HelpCommand hc3 = new HelpCommand();
                                    hc3.action(new String[] {4+""},e);
                                    break;
                                case 4:
                                    break;
                                default:
                                    return;
                            }
                            UtilBot.deleteMessage(msg);
                        }
                    });
                });
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Invalid page number! Use `=help 1, 2, 3, or 4`").queue();
            }
        }
        
        //Parameter -dm
        else if("-dm".equals(args[0]) && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            e.getTextChannel().sendMessage(Emoji.ENVELOPE + " A full list of AIBot commands has been sent. Check your Direct Message.").queue();
            
            int page = 1;
            if(args.length == 2 && Character.isDigit(args[1].charAt(0)))
                page = Integer.parseInt(args[1]);
            else
            {
                for(int i = 0; i < 4; i++)
                {
                    EmbedBuilder emhelp = helpText(e, i+1);
                    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage("Help is on its way...").complete().
                        editMessage(emhelp.build()).queue( (Message msg) -> {
                            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                                @Override
                                public void action(int chose) {
                                    switch(chose) {
                                        case 0:
                                            HelpCommand hc = new HelpCommand();
                                            hc.action(new String[] {1+""},e);
                                            break;
                                        case 1:
                                            HelpCommand hc1 = new HelpCommand();
                                            hc1.action(new String[] {2+""},e);
                                            break;
                                        case 2:
                                            HelpCommand hc2 = new HelpCommand();
                                            hc2.action(new String[] {3+""},e);
                                            break;
                                        case 4:
                                            HelpCommand hc3 = new HelpCommand();
                                            hc3.action(new String[] {4+""},e);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        }));
                }
            }
        }
        
        else //Parameter commands name
        {
            helpCustom(args[0], e);
        }
    }

    
    public static EmbedBuilder helpText(MessageReceivedEvent e, int page)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("**There are more commands! Do `=help 1, 2, 3, or 4`**");
        
        //Assign fields to a certain page
        switch (page) {
            case 2:
                embed.addField("utility Module", HelpText.UTIL_CMD, true);
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
        
        embed.setColor(UtilBot.randomColor());
        embed.setAuthor("AIBot command List | Page " + page, Global.B_GITHUB, Global.I_HELP);
        embed.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setFooter("Commands List", null);
        
        return embed;
    }
    
    public void helpCustom(String cmdtitle, MessageReceivedEvent e)
    {
        String cmdhelp = Emoji.ERROR + " Cannot find such command/module.", cmdhelp2 = "";
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
            case "list":
            case "server":
            case "member":
            case "role":
            case "channel":
                cmdhelp = ListCommand.HELP;
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
            case "warn": cmdhelp = WarnCommand.HELP;
                break;
            case "ban": 
            case "b": cmdhelp = BanCommand.HELP;
                break;
            case "unban": 
            case "ub": cmdhelp = UnbanCommand.HELP;
                break;
            case "softban": cmdhelp = SoftBanCommand.HELP;
                break;

            //utility Module
            case "utility":
            case "util":
                cmdhelp = HelpText.UTIL_CMD;
                cmdhelp2 = HelpText.UTIL_DES;
                isMod = true;
                break;
            //command Group- Number
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
            //command Group- Search
            case "search":
            case "google":
            case "g":
            case "wiki":
            case "urban":
            case "github":
            case "git":
                cmdhelp = SearchCommand.HELP;
                break;
            case "imdb":
                cmdhelp = IMDbCommand.HELP;
                break;
            //command Group- Image
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
            case "spam":
                cmdhelp = SpamCommand.HELP;
                break;
            //command Group- game
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
            case "player":
            case "pl":
                cmdhelp = PlayerCommand.HELP;
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
            case "autoplay":
            case "ap":
                cmdhelp = AutoPlayCommand.HELP;
                break;
            case "pause":
            case "resume":
            case "unpause":
                cmdhelp = PauseCommand.HELP;
                break;
            case "skip":
                cmdhelp = SkipCommand.HELP;
                break;
            case "previous":
            case "pre":
                cmdhelp = PreviousCommand.HELP;
                break;
            case "nowplaying":
            case "song":
            case "np":
                cmdhelp = SongCommand.HELP;
                break;
            case "queue":
            case "q":
                cmdhelp = QueueCommand.HELP;
                break;
            case "volume":
                cmdhelp = VolumeCommand.HELP;
                break;
            case "jump":
                cmdhelp = JumpCommand.HELP;
                break;
            case "shuffle":
                cmdhelp = ShuffleCommand.HELP;
                break;
            case "repeat":
                cmdhelp = RepeatCommand.HELP;
                break;
            case "stop":
                cmdhelp = StopCommand.HELP;
                break;
            case "dump":
                cmdhelp = DumpCommand.HELP;
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
            case "shutdown": 
                cmdhelp = ShutDownCommand.HELP;
                break;
            case "presence":
            case "setNick":
            case "setStatus":
            case "setGame":
                cmdhelp = PresenceCommand.HELP;
                break;
            case "source": 
                cmdhelp = SourceCommand.HELP;
                break;
            case "log": 
                cmdhelp = LogCommand.HELP;
                break;

            default: break;

        }

        String morc = ""; //Set fields' TEXT to command/module.

        if(isMod == false) //If this is a command
        {
            morc = "command";
        }
        else
        {
            morc = "Module";
        }

        //Set EmbedMessage addField title.
        if(cmdhelp.equals(Emoji.ERROR + " Cannot find such command/module."))
                cmdtitle = "NA";
        else 
        {
            cmdtitle = cmdtitle.substring(0, 1).toUpperCase()+ cmdtitle.substring(1); //Make the first letter to Upper case.
            cmdtitle += " -Help";
        }
        
        EmbedBuilder embedHelp = new EmbedBuilder();

        embedHelp.setAuthor("AIBot Help -" + morc, Global.B_GITHUB,null); //Set title for command
        embedHelp.setColor(UtilBot.randomColor());

        embedHelp.addField(cmdtitle, cmdhelp, true);
        if(isMod == true)
            embedHelp.addField("Discription", cmdhelp2, true);
        embedHelp.setFooter(morc + " Help/Usage", Global.I_HELP);
        embedHelp.setTimestamp(Instant.now());

        MessageEmbed meh = embedHelp.build();
        e.getChannel().sendMessage(meh).queue();
        embedHelp.clearFields();
    }
    
}
