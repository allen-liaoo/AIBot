/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import audio.GuildPlayer;
import main.AIBot;
import audio.Music;
import command.Command;
import constants.Emoji;
import setting.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class VolumeCommand extends Command {
    
    public final static String HELP = "This command is for playing an youtube music in the voice channel.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"play`\n"
                                    + "Parameter: `-h | [Youtube Url] | null`";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Play -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        else if(args.length == 0)
        {
            int currentVolume = AIBot.getGuild(e.getGuild()).getPlayer().getVolume();
            e.getTextChannel().sendMessage("Current volume: " + Integer.toString(currentVolume)).queue();
        }
        else
        {
            int volume = 50;
            try {
                volume = Integer.parseInt(args[0]);
                
                if(volume < 0 || volume > 100)
                {
                    e.getTextChannel().sendMessage(Emoji.ERROR + " Please enter a number between 0 to 100.").queue();
                    return;
                }

                GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
                AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());
                player.getPlayer().setVolume(volume);
                if(volume < 50)
                    e.getTextChannel().sendMessage(Emoji.VOLUME_LOW + " Volume set to " + volume).queue();
                else
                    e.getTextChannel().sendMessage(Emoji.VOLUME_HIGH + " Volume set to " + volume).queue();
            } catch(NumberFormatException ex){
                e.getTextChannel().sendMessage(Emoji.ERROR + " Please enter a valid number.").queue();
                return;
            }
        }
    }

}
