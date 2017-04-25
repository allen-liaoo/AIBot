/* 
 * AIBot by AlienIdeology
 * 
 * UtilBot
 * All Bot utilities
 */
package Utility;

import Main.Main;
import Constants.Constants;
import Constants.Emoji;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
        //If-else for no GAME
        Game g = null;
        if (set != null) {
            g = Game.of(set);
        } else {
            set = "No Game";
        }
        Main.jda.getPresence().setGame(g);
        AILogger.updateLog("Bot Game set to " + set);
        return set;
    }

    /**
     * Set the STATUS of the bot
     * @param stat
     * @return
     */
    public static OnlineStatus setStatus(String stat) {
        OnlineStatus status;
        switch (stat.toLowerCase()) {
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
        AILogger.updateLog("Bot Status set to " + status.toString());
        return status;
    }
    
    /**
     * Get the OnlineStatus with Emojis
     * @param mem
     * @return
     */
    public static String getStatusString(OnlineStatus stat)
    {
        String status = "";
        switch (stat) {
            case ONLINE:
                status = Emoji.GUILD_ONLINE;
                break;
            case IDLE:
                status = Emoji.GUILD_IDLE;
                break;
            case DO_NOT_DISTURB:
                status = Emoji.GUILD_DND;
                break;
            case INVISIBLE:
                status = Emoji.GUILD_OFFLINE;
                break;
            case OFFLINE:
                status = Emoji.GUILD_OFFLINE;
                break;
            default:
                status = Emoji.GUILD_OFFLINE;
        }
        status += " " + UtilString.VariableToString("_",stat.getKey());
        return status;
    }
    
    /**
     * Get the guild list of this bot (Sorted by member count)
     * @return
     */
    public static List<Guild> getServerList()
    {
        List<Guild> guildsList = new ArrayList(Main.jda.getGuilds());
        
            Collections.sort(guildsList, (Guild g1, Guild g2) -> 
                    g1.getMembers().size() > g2.getMembers().size() ? -1 : (g1.getMembers().size() < g2.getMembers().size() ) ? 1 : 0 );
        return guildsList;
    }
    
    /**
     * Get the member list of this guild (Sorted by role position and alphabetical order)
     * @param e
     * @return
     */
    public static List<Member> getMemberList(MessageReceivedEvent e)
    {
        List<Member> memsList = new ArrayList(e.getGuild().getMembers());
        
        Collections.sort(memsList, new Comparator<Member>() {
            @Override
            public int compare(Member m1, Member m2) { 
                if(!m1.getRoles().isEmpty() && !m2.getRoles().isEmpty())
                    return m1.getRoles().get(0).getPosition() > m2.getRoles().get(0).getPosition() ? -1 : (m1.getRoles().get(0).getPosition() < m2.getRoles().get(0).getPosition()) ? 1 : (m1.getEffectiveName().compareTo(m2.getEffectiveName()));
                else if (!m1.getRoles().isEmpty() && m2.getRoles().isEmpty())
                    return -1;
                else if (m1.getRoles().isEmpty() && !m2.getRoles().isEmpty())
                    return 1;
                return m1.getEffectiveName().compareTo(m2.getEffectiveName());
            }
        });
        return memsList;
    }
    
    /**
     * Get the role list of this guild (Sorted by role position)
     * @param e
     * @return
     */
    public static List<Role> getRoleList(MessageReceivedEvent e)
    {
        List<Role> roleList = new ArrayList(e.getGuild().getRoles());
        
        Collections.sort(roleList, (Role r1, Role r2) -> 
                    r1.getPosition() > r2.getPosition() ? -1 : (r1.getPosition() < r2.getPosition() ) ? 1 : 0 );
        
        return roleList;
    }
    
    /**
     * Get the TextChannel list of this guild (Sorted by position)
     * @param e
     * @return
     */
    public static List<Channel> getTextChannelList(MessageReceivedEvent e)
    {
        List<Channel> tcList = new ArrayList(e.getGuild().getTextChannels());
        Collections.sort(tcList, (Channel t1, Channel t2) -> 
                    t1.getPosition() > t2.getPosition() ? 1 : (t1.getPosition() < t2.getPosition() ) ? -1 : 0 );
        return tcList;
    }
    
    /**
     * Get the VoiceChannel list of this guild (Sorted by position)
     * @param e
     * @return
     */
    public static List<Channel> getVoiceChannelList(MessageReceivedEvent e)
    {
        List<Channel> vcList = new ArrayList(e.getGuild().getVoiceChannels());
        Collections.sort(vcList, (Channel v1, Channel v2) -> 
                    v1.getPosition() > v2.getPosition() ? 1 : (v1.getPosition() < v2.getPosition() ) ? -1 : 0 );
        return vcList;
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
    
    /**
     * Check if an user is a majority in a voice channel
     * @param mem
     * @return
     */
    public static boolean isMajority(Member mem)
    {
        if(mem.getVoiceState().getChannel() != null) {
            //Only count non-Bot Users
            double mems = 0;
            List<Member> members = mem.getVoiceState().getChannel().getMembers();
            for(Member m : members) {
                if(!m.getUser().isBot())
                    mems++;
            }
            
            if(1/mems>0.5)
                return true;
        }
        return false;
    }

    /**
     * Generate Random Color
     * @return Color
     */
    public static Color randomColor() {
        Random colorpicker = new Random();
        int red;
        int green;
        int blue;
        red = colorpicker.nextInt(255) + 1;
        green = colorpicker.nextInt(255) + 1;
        blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }
    
    /**
     * Get the hex code of a color
     * @param color
     * @return
     */
    public static String getHexCode(Color color) {
        return "#"+Integer.toHexString(color.getRGB()).substring(2);
    }
    
}
