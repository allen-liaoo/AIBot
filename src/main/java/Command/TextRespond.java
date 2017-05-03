/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command;

import java.util.HashMap;
import Constants.Global;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TextRespond {
    
    public static HashMap<String, String> responds = new HashMap<String, String>();
    public static HashMap<String, DynamicRespond> dynamicResponds = new HashMap<String, DynamicRespond>();
    
    public TextRespond() {
        addResponds();
        addDynamicResponds();
    }
    
    public void checkRespond(String msg, MessageReceivedEvent e) {
        if(e.getChannelType().isGuild() && e.getGuild().getMembers().size()>70)
            return;

        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;

        if(responds.containsKey(msg))
            e.getChannel().sendMessage(responds.get(msg)).queue();
        else if(responds.containsValue(msg))
            e.getChannel().sendMessage(getKeyFromValue(msg)).queue();
    }
    
    public void checkDynamicRespond(String args[], MessageReceivedEvent e) {
        if(e.getChannelType().isGuild() && e.getGuild().getMembers().size()>100)
            return;
        if(args.length>0 && dynamicResponds.containsKey(args[0]))
            dynamicResponds.get(args[0]).execute(args, e);
    }
    
    private void addResponds()
    {
        responds.put("ayy", "Um... should I say lmao?");
        responds.put("lmao", "lm**f**ao");
        responds.put("wew", "lad");
        responds.put("pew pew pew", "pewww... oops");
        responds.put("owo", "mhmmmmm");
        responds.put("\\o", "o/");
        responds.put("/o", "o\\");
        responds.put("(╯°□°）╯︵ ┻━┻", "┬─┬﻿ ノ( ゜-゜ノ) We do **NOT** throw tables in this server.\n"
                + "We are civilized people!");
        responds.put("┬─┬﻿ ノ( ゜-゜ノ)", "Yep, that's the right thing to do. Clean up the server, table by table! \n┬─┬﻿ ノ( ゜-゜ノ)");
    }

    private String getKeyFromValue(String value) {
        for (String s : responds.keySet()) {
            if (responds.get(s).equals(value)) {
                return s;
            }
        }
        return null;
    }
    
    private void addDynamicResponds()
    {
        dynamicResponds.put("say", new say());
    }
    
    private interface DynamicRespond {
        void execute(String args[], MessageReceivedEvent e);
    }
        private class say implements DynamicRespond {
            @Override
            public void execute (String args[], MessageReceivedEvent e) {
                if(!e.getChannelType().isGuild() ||
                    !e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS) ||
                    !Global.D_ID.equals(e.getAuthor().getId()) ||
                    !e.getMember().isOwner())
                    return;
                MessageBuilder tts = new MessageBuilder();
                tts.setTTS(true);
                tts.append(args[1]);
                e.getChannel().sendMessage(tts.build()).queue();
            }
        }
}
