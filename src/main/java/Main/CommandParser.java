/* 
 * AIBot by AlienIdeology
 * 
 * CommandParser
 * Parsing commands that start with prefix, mention, or no prefix(in Private Channel)
 */
package Main;

import Setting.Prefix;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
/**
 *
 * @author liaoyilin
 */
public class CommandParser {

    /**
     * Parsing normal and mention commands
     * @param rw the raw message
     * @param e the MessageReceivedEvent
     */
    public CommandContainer parse(String rw, MessageReceivedEvent e) {
        ArrayList<String> split = new ArrayList<String>();
        String raw = rw;
        
        String beheaded = "";
        if(raw.startsWith(Prefix.getDefaultPrefix()))
            beheaded = raw.replaceFirst(Prefix.getDefaultPrefix(), "");
        else if(raw.startsWith("@" + e.getGuild().getSelfMember().getEffectiveName()))
            beheaded = raw.replaceFirst("@" + e.getGuild().getSelfMember().getEffectiveName() + " ", "");
        
        String[] splitBeheaded = beheaded.split(" ");
        
        for(String s : splitBeheaded) {
            split.add(s);
        }
        
        String invoke = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        
        return new CommandContainer(raw, beheaded, splitBeheaded, invoke, args, e);
    }
    
    /**
     * Parsing PrivateChannel commands
     * @param rw the raw message
     * @param e the MessageReceivedEvent
     */
    public CommandContainer parsePrivate(String rw, MessageReceivedEvent e) {
        ArrayList<String> split = new ArrayList<String>();
        String raw = rw;
        
        String beheaded = "";
        if(raw.startsWith(Prefix.getDefaultPrefix()))
            beheaded = raw.replaceFirst(Prefix.getDefaultPrefix(), "");
        else if(raw.startsWith("@" + e.getJDA().getSelfUser().getName()))
            beheaded = raw.replaceFirst("@" + e.getGuild().getSelfMember().getEffectiveName() + " ", "");
        else
            beheaded = raw;
        
        String[] splitBeheaded = beheaded.split(" ");
        
        for(String s : splitBeheaded) 
        {
            split.add(s);
        }
        
        String invoke = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        
        return new CommandContainer(raw, null, splitBeheaded, invoke, args, e);
    }
    
    public class CommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;
        
        public CommandContainer(String rw, String Beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent e) {
            this.raw = rw;
            this.beheaded = Beheaded; 
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = e;
        }       
    }
}
