/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package command.music;

import audio.AudioTrackWrapper;
import audio.GuildPlayer;
import audio.Music;
import audio.Radio;
import command.Command;
import constants.Emoji;
import main.AIBot;
import setting.Prefix;
import system.AILogger;

import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RadioCommand extends Command{

    public final static String HELP = "This command is for loading an automatic playlist.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"radio`\n"
                                    + "Parameter: `-h | [Station Name] | null`\n"
                                    + "[Station Name]: Load the radio station and start playing.\n"
                                    + "null: Get a list of available radio stations.\n";
    
    private EmbedBuilder embedpl = new EmbedBuilder();
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("FM -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            String stations = "```css\n\n[AIBot Radio]\n\n/* Usage: "+Prefix.DIF_PREFIX+"radio [Station Name] */\n\n";

            List<Radio.RadioStation> radios = AIBot.radio.getRadioStations();
            for(int i = 0; i < radios.size(); i++) {
                stations += i == radios.size()-1 ? radios.get(i).getName()+"```" : radios.get(i).getName()+", ";
            }
            
            e.getChannel().sendMessage(stations).queue();
        }
        
        else if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        
        else if(args.length > 0)
        {
            String input = "";
            for(int i = 0; i < args.length; i++)
            {
                if(i == 0)
                    input += args[i];
                else
                    input += " " + args[i];
            }
            
            loadRadio(input, e);
        }
    }

    public void loadRadio(String input, MessageReceivedEvent e)
    {
        String link = AIBot.radio.getUrl(input);

        if(link == null) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " Radio station not found. \nUse `" + Prefix.DIF_PREFIX + "radio` for available radio stations.").queue();
            return;
        }

        GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
        AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());

        Music.connect(e, false);
        player.play(link, e.getMember().getEffectiveName(), AudioTrackWrapper.TrackType.RADIO);

        //Log
        AILogger.commandLog(e, "FM#loadFM", "Fm loaded");
        System.out.println("Radio#loadRadio --> " + link);
    }

}
