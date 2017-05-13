/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import constants.Global;
import constants.HelpText;
import main.AIBot;
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

import java.awt.*;
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
        if(AIBot.commands.containsKey(cmdtitle.toLowerCase())) {
            e.getChannel().sendMessage(AIBot.commands.get(cmdtitle).help(e).build()).queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder()
                .setFooter("Module Help/Usage", Global.I_HELP)
                .setColor(Color.RED)
                .setTimestamp(Instant.now());
            String name = "", des = "";

            switch (cmdtitle.toLowerCase()) {
                case "information":
                case "info":
                    name = HelpText.INFO_CMD;
                    des = HelpText.INFO_CMD;
                    break;
                case "moderation":
                case "mod":
                    name = HelpText.MOD_CMD;
                    des = HelpText.MOD_DES;
                    break;
                case "utility":
                case "util":
                    name = HelpText.UTIL_CMD;
                    des = HelpText.UTIL_DES;
                    break;
                case "fun":
                    name = HelpText.FUN_CMD;
                    des = HelpText.FUN_DES;
                    break;
                case "restricted":
                case "restrict":
                    name = HelpText.RESTRICT_CMD;
                    des = HelpText.RESTRICT_DES;
                    break;
                default:
                    e.getChannel().sendMessage(Emoji.ERROR + " Cannot find the command or module.\n"
                            + "Use `=help [Page]` to see full command list.").queue();
                    return;
            }

            embed.addField(cmdtitle.substring(0,1).toUpperCase()+cmdtitle.substring(1)+" Module", name, true);
            embed.addField("Description", des, true);
            e.getChannel().sendMessage(embed.build()).queue();
        }
    }
    
}
