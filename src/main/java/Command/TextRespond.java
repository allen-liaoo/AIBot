/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command;

import Constants.Emoji;
import java.util.HashMap;
import Constants.Constants;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TextRespond {
    
    public static HashMap<String, Respond> responds = new HashMap<String, Respond>();
    public static HashMap<String, PrefixRespond> prefixResponds = new HashMap<String, PrefixRespond>();
    
    public TextRespond() {
        addResponds();
        addPrefixResponds();
    }
    
    public void checkRespond(String msg, MessageReceivedEvent e) {
        if(responds.containsKey(msg))
            responds.get(msg).execute(e);
    }
    
    public void checkPrefixRespond(String args[], MessageReceivedEvent e) {
        if(args.length>0 && prefixResponds.containsKey(args[0]))
            prefixResponds.get(args[0]).execute(args, e);
    }
    
    private void addResponds()
    {
        responds.put("ayy", new ayy());
        responds.put("lmao", new lmao());
        responds.put("hi", new hi());
    }
    
    private void addPrefixResponds()
    {
        prefixResponds.put("say", new say());
    }
    
    private interface Respond {
        void execute(MessageReceivedEvent e);
    }
    
        private class ayy implements Respond {
            @Override
            public void execute (MessageReceivedEvent e) {
                e.getChannel().sendMessage("Um... should I say lmao?").queue();
            }
        }
        
        private class lmao implements Respond {
            @Override
            public void execute (MessageReceivedEvent e) {
                e.getChannel().sendMessage("Yeah yeah yeah...").queue();
            }
        }
        
        private class hi implements Respond {
            @Override
            public void execute (MessageReceivedEvent e) {
                e.getMessage().addReaction(Emoji.EYES).queue();
            }
        }
        
    private interface PrefixRespond {
        void execute(String args[], MessageReceivedEvent e);
    }
        private class say implements PrefixRespond {
            @Override
            public void execute (String args[], MessageReceivedEvent e) {
                if(!e.getMember().isOwner() ||
                    !Constants.D_ID.equals(e.getAuthor().getId()) ||
                    !e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS))
                    return;
                MessageBuilder tts = new MessageBuilder();
                tts.setTTS(true);
                tts.append(args[1]);
                e.getChannel().sendMessage(tts.build()).queue();
            }
        }
}
