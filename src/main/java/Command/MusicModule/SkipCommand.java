/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.Music;
import Command.Command;
import static Command.Command.embed;
import Resource.Emoji;
import Resource.Info;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SkipCommand implements Command {
    public final static String HELP = "This command is for skipping the current song.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"skip`\n"
                                    + "Parameter: `-h | -f | null`\n"
                                    + "null: Vote to skipp current song.\n"
                                    + "-f: Force skipp current song. Requires Administrator or Manage Channel Permission.";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Skip -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        
        if(args.length == 1 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length == 0)
        {
            //Prevent users that is not in the same voice channel from skipping the song
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel()) {
                e.getChannel().sendMessage(Emoji.error + " You and I are not in the same voice channel.").queue();
                return;
            }
            
            int skip = Music.skip(e, 0, false);
            if(skip > 0)
                e.getChannel().sendMessage(Emoji.success + " Added your vote. Still require " + skip + " votes to skip the song.").queue();
            else if(skip == 0)
                e.getChannel().sendMessage(Emoji.success + " Track skipped.").queue();
            else if(skip == -1)
                e.getChannel().sendMessage(Emoji.error + " Your vote is already added.").queue();
            else if(skip == -2)
                e.getChannel().sendMessage(Emoji.error + " There is no song playing.\nSkip what? Don't skip school.").queue();
        }
        
        else if("-f".equals(args[0]))
        {
            if(e.getMember().isOwner() || 
                    e.getMember().hasPermission(Permission.ADMINISTRATOR) || 
                    e.getMember().hasPermission(Permission.MANAGE_CHANNEL) || 
                    Info.D_ID.equals(e.getAuthor().getId()))
            {
                Music.skip(e, 0, true);
                e.getChannel().sendMessage(Emoji.success + " Track skipped.").queue();
            }
            else
            {
                e.getChannel().sendMessage(Emoji.error + " Only server owner and members with \n"
                        + "`Administrator` or `Manage Channel` permission can force skip a song.").queue();
            }
        }
        
        else if(args[0].length() == 1 && Character.isDigit(args[0].charAt(0)))
        {
            int skip = Music.skip(e, Integer.parseInt(args[0]), false);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
}
