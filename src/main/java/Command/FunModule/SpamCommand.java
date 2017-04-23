/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.FunModule;

import Command.Command;
import Resource.Constants;
import Resource.Emoji;
import Setting.Prefix;
import Utility.UtilNum;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SpamCommand implements Command {

    public final static String HELP = "Spam yummy spams!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "spam`\n"
                                    + "Parameter: `-h | [Number] | @Mention(s) | null`\n"
                                    + "[Number]: Spam an amount of spams.\n"
                                    + "@Mention(s): Spam spams to mentioned member(s) in dm.\n";
    
    public static final String IMAGE_SPAM_1 = "https://upload.wikimedia.org/wikipedia/commons/0/09/Spam_can.png";
    public static final String IMAGE_SPAM_50 = "http://laab.cl/blog/wp-content/uploads/2015/08/spam.jpg";
    public static final String IMAGE_SPAM_10 = "http://marketingland.com/wp-content/ml-loads/2016/07/ss-spam.jpg";
    public static final String IMAGE_SPAM_100 = "http://www.roadpickle.com/wp-content/uploads/wall-of-spam.jpg";
    
    private HashMap<String, Integer> spamcount = new HashMap<String, Integer>();
    

    @Override
    public void help(MessageReceivedEvent e) {
        
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            //No number specified
            if(args.length == 0) {
                e.getChannel().sendMessage(IMAGE_SPAM_1).queue();
            }
            //Number of spams specified
            else if(UtilNum.isInteger(args[0])) {
                int num = Integer.parseInt(args[0]);

                e.getChannel().sendMessage(getSpamByAmount(num)).queue();
            }
            //Target Specified
            else 
            {
                if(spamcount.containsKey(e.getAuthor().getId()) && spamcount.get(e.getAuthor().getId()) > 3) {
                    e.getChannel().sendMessage(Emoji.error + " You can only spam 5 times!\nOtherwise the spam will stink!").queue();
                    return;
                }
                
                List<User> mention = e.getMessage().getMentionedUsers();

                for(User u : mention) {
                    if(u.getId().equals(e.getJDA().getSelfUser().getId()))
                        continue;

                    if(Constants.D_ID.equals(u.getId())) {
                        e.getChannel().sendMessage(Emoji.eyes + " How dare you? Spamming my creator!?").queue();
                        u = e.getAuthor();
                    }
                    
                    u.openPrivateChannel().queue(PrivateChannel -> 
                            PrivateChannel.sendMessage("***Warning***\n**" + e.getMember().getEffectiveName() + "#" + e.getAuthor().getDiscriminator()
                            + "** from **" + e.getGuild().getName()
                            + "** has spammed you!\n" + IMAGE_SPAM_100).queue());
                }
                
                if(!mention.isEmpty()) {
                    e.getChannel().sendMessage("Spammed spams to " + mention.size() + " user(s)!").queue();
                    if(spamcount.containsKey(e.getAuthor().getId()))
                        spamcount.put(e.getAuthor().getId(), spamcount.get(e.getAuthor().getId())+1);
                    else
                        spamcount.put(e.getAuthor().getId(), 0);
                }
                else {
                    e.getChannel().sendMessage(Emoji.error + " Invalid spam! So I spammed **YOU**!").queue();
                    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> 
                            PrivateChannel.sendMessage("**Punishing Spam**\n" + IMAGE_SPAM_100).queue());
                }
            }
            
        }
    }
    
    private String getSpamByAmount(int amount) {
        if(UtilNum.isBetween(amount, 1, 9))
            return IMAGE_SPAM_1;
        else if(UtilNum.isBetween(amount, 10, 30))
            return IMAGE_SPAM_10;
        else if(UtilNum.isBetween(amount, 30, 70))
            return IMAGE_SPAM_50;
        else if(UtilNum.isBetween(amount, 70, Integer.MAX_VALUE))
            return IMAGE_SPAM_100;
        
        return IMAGE_SPAM_1;
    }
    
}
