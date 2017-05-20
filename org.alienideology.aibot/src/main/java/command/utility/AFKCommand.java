package command.utility;

import command.Command;
import constants.Emoji;
import main.AIBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;

/**
 * @author AlienIdeology
 */
public class AFKCommand extends Command {

    public final static String HELP = "This command is for setting the afk status. If someone mentioned you, "
                                    + "the bot will automatically respond with your AFK text.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"afk`\n"
                                    + "Parameter: `-h | [AFK Text] | off | null`\n";


    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Utility Module", null);
        embed.addField("AFK -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if (args.length == 0) e.getChannel().sendMessage(help(e).build()).queue();
        else if ("off".equals(args[0])) {
            if(AIBot.globalWatchDog.removeAFK(e.getAuthor()))
                e.getChannel().sendMessage(Emoji.GUILD_ONLINE + " Removed AFK status. Welcome back!").queue();
            else
                e.getChannel().sendMessage(Emoji.ERROR + " AFK was off.").queue();
        }
        else {
            String message = "";
            for(int i = 0; i < args.length; i++) { message += i == args.length-1 ? args[i] : args[i]+" "; }
            AIBot.globalWatchDog.addAFK(e.getAuthor(), message);
            e.getChannel().sendMessage(Emoji.GUILD_IDLE + " Added AFK status. Go in peace~").queue();
        }
    }

}
