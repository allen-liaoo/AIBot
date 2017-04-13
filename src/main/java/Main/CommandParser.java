/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     * Parsing normal commands
     * @param rw the raw message
     * @param e the MessageReceivedEvent
     */
    public CommandContainer parse(String rw, MessageReceivedEvent e) {
        ArrayList<String> split = new ArrayList<String>();
        String raw = rw;
        String beheaded = raw.replaceFirst(Prefix.getDefaultPrefix(), "");
        String[] splitBeheaded = beheaded.split(" ");
        
        for(String s : splitBeheaded) 
        {
            split.add(s);
        }
        
        String invoke = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        
        return new CommandContainer(raw, beheaded, splitBeheaded, invoke, args, e);
    }
    
    /**
     * Parsing @mention commands
     * @param rw the raw message
     * @param e the MessageReceivedEvent
     */
    public CommandContainer parseMention(String rw, MessageReceivedEvent e) {
        ArrayList<String> split = new ArrayList<String>();
        String raw = rw;
        String beheaded = raw.replaceFirst("@AIBot ", "");
        String[] splitBeheaded = beheaded.split(" ");
        
        for(String s : splitBeheaded) 
        {
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
        String[] splitBeheaded = rw.split(" ");
        
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
