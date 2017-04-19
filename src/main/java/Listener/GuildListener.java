/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Listener;

import Main.Main;
import Resource.Constants;
import Utility.UtilTool;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuildListener extends ListenerAdapter {

    private final EmbedBuilder embedmsg = new EmbedBuilder();
    private final String welcome = "Use `=help 1, =help 2, =help 3, or =help 4` for more commands.\n"
                                + "Use `=about` to see the basic informations about this bot.\n"
                                + "Want to invite me to your server? type `=invite` to get the invite link.\n"
                                + "By the way, if you wanna see some awesome servers, just type in `=support`!\n"
                                + "Enjoy the awesome features like hangman, fm(automatic playlist, or radio~~\n";
    
    private final String links = "[Discord Bots Link](https://bots.discord.pw/bots/294327785512763392)\n"
                                + "[Invite Link](https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567)\n"
                                + "[GitHub](https://github.com/AlienIdeology/AIBot/)\n"
                                + "[Support Server](https://discordapp.com/invite/EABc8Kc) for bug reports, suggestions and help";
    
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        super.onGuildJoin(event);
        welcome(event.getGuild().getPublicChannel());
        
        System.out.println("Joined guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        Main.setGame("default");
    }
    
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        super.onGuildLeave(event);
        System.out.println("Left guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        Main.setGame("default");
    }
    
    public void welcome(TextChannel c) {
        embedmsg.setAuthor("Thanks for Adding AIBot!!", Constants.B_GITHUB, Constants.B_AVATAR);
        embedmsg.setColor(UtilTool.randomColor());
        embedmsg.setDescription(welcome);
        embedmsg.setThumbnail(Constants.B_AVATAR);
        embedmsg.addField("Links", links, false);
        c.sendMessage(embedmsg.build()).queue();
    }
    
}
