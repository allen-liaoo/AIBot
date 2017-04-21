/* 
 * AIBot by AlienIdeology
 * 
 * UtilBot
 * All Bot utilities
 */
package Utility;

import Main.Main;
import Resource.Constants;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilBot {

    public static String setGame(String game) {
        String set;
        switch (game.replaceAll(" ", "").toLowerCase()) {
            case "default":
                set = Constants.B_GAME_DEFAULT + " | " + Main.jda.getGuilds().size() + " Servers";
                break;
            case "update":
                set = Constants.B_GAME_UPDATE;
                break;
            case "fix":
                set = Constants.B_GAME_FIXING;
                break;
            case "null":
            case "":
                set = null;
                break;
            default:
                set = game;
        }
        //If-else for no game
        Game g = null;
        if (set != null) {
            g = Game.of(set);
        } else {
            set = "No Game";
        }
        Main.jda.getPresence().setGame(g);
        SmartLogger.updateLog("Bot Game set to " + set);
        return set;
    }

    public static OnlineStatus setStatus(String stat) {
        OnlineStatus status;
        switch (stat) {
            case "online":
                status = OnlineStatus.ONLINE;
                setGame("default");
                break;
            case "idle":
                status = OnlineStatus.IDLE;
                setGame("update");
                break;
            case "dnd":
                status = OnlineStatus.DO_NOT_DISTURB;
                setGame("fix");
                break;
            case "invisible":
                status = OnlineStatus.INVISIBLE;
                setGame("null");
                break;
            case "offline":
                status = OnlineStatus.OFFLINE;
                setGame("null");
                break;
            default:
                status = OnlineStatus.UNKNOWN;
                setGame("null");
                break;
        }
        Main.jda.getPresence().setStatus(status);
        SmartLogger.updateLog("Bot Status set to " + status.toString());
        return status;
    }
    
    /**
     * Get the connected voice channels
     * @return
     */
    public static List<VoiceChannel> getConnectedVoiceChannels()
    {
        List<Guild> gui = Main.jda.getGuilds();
        List<VoiceChannel> vc = new ArrayList();
        
        for(Guild g : gui) {
            if (g.getSelfMember().getVoiceState().inVoiceChannel() &&
                Main.guilds.get(g.getId()).getPlayer().getPlayingTrack() != null &&
                g.getSelfMember().getVoiceState().getChannel() != null)
                vc.add(g.getSelfMember().getVoiceState().getChannel());
        }
        
        return vc;
    }
    
}
