/* 
 * AIBot by AlienIdeology
 * 
 * RateLimitter
 * Rate limited the users
 */
package Setting;

import constants.Emoji;
import utility.UtilString;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RateLimiter {
    
    private static boolean spam = false;
    private static int inform = 0;
    private static Long lastMessage = 0l;
    private static Long currentMessage = 0l;
    private static Long delay = 0l;
    
    /**
     *
     * @param e
     * @return false to call command, true to delay
     */
    public static boolean isSpam(MessageReceivedEvent e)
    {
        
        //Detect if the bot is spammed
        currentMessage = System.currentTimeMillis();
        if(spam == false && (currentMessage - lastMessage) < 500) {
            spam = true;
            delay = System.currentTimeMillis();
        }
        
        if(spam) {
            Long diff = System.currentTimeMillis() - delay;
            if(diff >= 10000) {
                spam = false;
                delay = 0l;
            }
            else {
                if(inform == 0) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Rapping? A little too fast there.\nCool down: "
                            + UtilString.formatTime(10000 - diff)).complete();
                }
                else if(inform % 2 == 0) {
                    e.getChannel().sendMessage(Emoji.ERROR + "Cool down: " + UtilString.formatTime(10000 - diff)).complete();
                }
                inform ++;
            }
        }
        lastMessage = currentMessage;
        
        return spam;
    }
}
