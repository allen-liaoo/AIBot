package command.music;

import audio.AudioTrackWrapper;
import audio.QueueList;
import constants.Emoji;
import constants.Global;
import main.AIBot;
import command.Command;
import setting.Prefix;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by liaoyilin on 5/5/17.
 */
public class PreviousCommand extends Command {

    public final static String HELP = "This command is for replaying the previous song.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"previous` or `"+ Prefix.getDefaultPrefix() +"pre`\n"
                                    + "Parameter: `-h | list/queue | null`\n"
                                    + "list/queue: Get a list of previous songs. (Maximum of 5 songs)\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Previous -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }

        QueueList preList = AIBot.getGuild(e.getGuild()).getGuildPlayer().getPreQueue();
        if(preList.isEmpty()) {
            e.getChannel().sendMessage(Emoji.ERROR + " No previous song.\n" +
                    "Use `"+ Prefix.getDefaultPrefix()+"pre list` to see previous songs.").queue();
            return;
        }

        if(args.length>0 && ("list".equals(args[0]) || "queue".equals(args[0]))) {
            preQueueList(e);
        }
        else {
            AudioTrackWrapper previous = preList.get(0);
            if (UtilBot.isMod(e.getMember()) ||
                    e.getAuthor().getName().equals(previous.getRequester())) {
                AIBot.getGuild(e.getGuild()).getGuildPlayer().playPrevious();
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner, members with `Administrator` permission "
                        + "\nand the song requester can replay the previous song.").queue();
            }
        }
    }

    /**
     * Previous Queue List
     * @param e
     */
    public void preQueueList(MessageReceivedEvent e)
    {
        QueueList preQueue = AIBot.getGuild(e.getGuild()).getGuildPlayer().getPreQueue();
        if(preQueue == null || preQueue.isEmpty()) {
            e.getChannel().sendMessage("No previous songs.").queue();
            return;
        }

        String list = "";
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Previous Songs", Global.B_INVITE, Global.B_AVATAR)
                .setColor(UtilBot.randomColor())
                .setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());

        for (int i = 0; i < preQueue.size(); i++) {
            AudioTrackWrapper wrap = preQueue.get(i);
            String title = wrap.getTrack().getInfo().title;
            String url = wrap.getTrack().getInfo().uri;
            list += "`" + (i+1) + ".` **[" + title + "](" + url + ")**\n";
        }

        embed.appendDescription(list);
        e.getChannel().sendMessage(embed.build()).queue();
    }
}
