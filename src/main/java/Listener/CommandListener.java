/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import Resource.Emoji;
import Resource.Prefix;
import Main.*;
import Setting.GuildSetting;
import Audio.Music;
import Command.InformationModule.HelpCommand;
import static Main.Main.commands;
import Setting.SmartLogger;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;

/**
 *
 * @author liaoyilin
 */
public class CommandListener extends ListenerAdapter {
    
    private String prefix;
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        //Reject Commands from Bots
        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;
        
        // Create GuildSetting for each Guild
        if(!e.isFromType(e.getChannelType().PRIVATE) && !Main.guilds.containsKey(e.getGuild().getId()))
        {
            Music.musicStartup();
            GuildSetting newGuild = new GuildSetting(Music.playerManager, e.getGuild().getId(), "=", 50);
            Main.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            SmartLogger.updateLog("\tNew Server - Name: " + Main.jda.getGuildById(e.getGuild().getId()).getName());
        }
        
        // Detect Command
        if(e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
        }
        
        // Detect mention
        if(e.getMessage().getMentionedUsers().size() > 0)
        {
            if(e.getMessage().isMentioned(e.getJDA().getSelfUser()) && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
            {
                String contain = e.getMessage().getContent();

                if(contain.length() < 20 && (contain.contains("help") || contain.contains("welp")))
                {
                    HelpCommand.helpText(e);
                    HelpCommand.me = HelpCommand.embed.build();
                    e.getChannel().sendMessage(Emoji.envelope + " You need help? Check private message!").complete();
                    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage("Help is on its way...").complete().editMessage(HelpCommand.me).submit());
                    HelpCommand.embed.clearFields();
                }
            }
        }
    }
    
    public static void handleCommand(CommandParser.CommandContainer cmd)
    {
        if(commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
        
            if(safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }
    
    @Override
    public void onReady(ReadyEvent e) {
        System.out.println("Status - Logged in as: " + e.getJDA().getSelfUser().getName());
    }
    
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        super.onGuildAvailable(event); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Guild Avaliable:" + event.getGuild().getName());
    }
}
