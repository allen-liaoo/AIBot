/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import Config.*;
import Main.*;
import Main.GuildSetting;
import Audio.Music;

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
        if(!Main.guilds.containsKey(e.getGuild().getId()))
        {
            GuildSetting newGuild = new GuildSetting(Music.playerManager, e.getGuild().getId(), "=", 50);
            Main.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            System.out.println("New Server Added to HashMap(guilds) - Name: " + Main.jda.getGuildById(e.getGuild().getId()).getName() + " ID: " + e.getGuild().getId());
        }
        
        if(e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            Main.handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
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
