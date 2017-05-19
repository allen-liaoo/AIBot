/* 
 * AIBot by AlienIdeology
 * 
 * UtilBot
 * All jda utilities
 */
package utility;

import com.mashape.unirest.http.exceptions.UnirestException;
import main.AIBot;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import org.json.XML;
import system.AILogger;
import constants.Global;
import constants.Emoji;
import java.awt.Color;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilBot {

    public static String setGame(String game) {
        String set;
        switch (game.replaceAll(" ", "").toLowerCase()) {
            case "default":
                set = Global.defaultGame();
                break;
            case "update":
                set = Global.B_GAME_UPDATE;
                break;
            case "fix":
                set = Global.B_GAME_FIXING;
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
            set = "No game";
        }
        AIBot.setGame(g);
        AILogger.updateLog("jda game set to " + set);
        return set;
    }

    /**
     * Set the STATUS of the jda
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
        AIBot.setStatus(status);
        AILogger.updateLog("jda Status set to " + status.toString());
        return status;
    }
    
    /**
     * Get the OnlineStatus with Emojis
     * @param stat
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
     * Get the guild list of this jda (Sorted by member count)
     * @return
     */
    public static List<Guild> getServerList()
    {
        List<Guild> guildsList = new ArrayList(AIBot.getGuilds());
        
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
        List<Guild> gui = AIBot.getGuilds();
        List<VoiceChannel> vc = new ArrayList<>();
        
        for(Guild g : gui) {
            if (g.getSelfMember().getVoiceState().inVoiceChannel() &&
                AIBot.getGuild(g).getPlayer().getPlayingTrack() != null &&
                g.getSelfMember().getVoiceState().getChannel() != null)
                vc.add(g.getSelfMember().getVoiceState().getChannel());
        }
        
        return vc;
    }

    /**
     * Check if a member is mod: Have Administrator or Manage Server Permission, is server owner, or is jda owner.
     * @param mem
     * @return true if the user is mod
     */
    public static boolean isMod(Member mem)
    {
        return mem.isOwner() || mem.hasPermission(Global.PERM_MOD) || Global.D_ID.equals(mem.getUser().getId());
    }
    
    /**
     * Check if an user is a majority in a voice channel
     * @param mem
     * @return
     */
    public static boolean isMajority(Member mem)
    {
        if(mem.getVoiceState().getChannel() != null) {
            //Only count non-jda Users
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
     * Only delete message that is from Text Channel and has proper permission
     * @param msg
     */
    public static void deleteMessage(Message msg) {
        if(msg.isFromType(ChannelType.PRIVATE))
            return;

        if(msg.getAuthor().getId().equals(msg.getGuild().getSelfMember().getUser().getId())) {
            msg.delete().queue();
        } else if(msg.getGuild().getSelfMember().hasPermission(msg.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            msg.delete().queue();
        }
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

    public static String getJDAVersion() {
        return "JDA `" + JDAInfo.VERSION + "`";
    }

    public static String getLavaPlayerVersion() {
        StringBuilder verson = new StringBuilder("Lavaplayer `");
        try {
            verson.append(XML.toJSONObject(Unirest.get("https://dl.bintray.com/sedmelluq/com.sedmelluq/com/sedmelluq/lavaplayer/maven-metadata.xml").asString().getBody()).getJSONObject("metadata")
                    .getJSONObject("versioning").getString("release")).append("`");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return verson.toString();
    }

    public static synchronized void setUnirestCookie()
    {
        //Set Unirest cookie
        RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpclient);
    }

    /**
     * Post the status of this bot
     * @param jda
     * @return EmbedBuilder to post status to
     */
    public static EmbedBuilder postStatus(JDA jda)
    {
        String avatar, status, uptime, tc, vc, privateChan, jdaVer, lavaVer, more;
        int guild, shard, users, bots;

        /* Basic */
        avatar = jda.getSelfUser().getAvatarUrl();
        uptime = UtilString.formatTime(System.currentTimeMillis() - AIBot.timeStart);
        status = getStatusString(jda.getPresence().getStatus()) + ", " + UtilString.VariableToString("_", jda.getStatus().name());

        /* Statistic */
        guild = AIBot.getGuilds().size();
        shard = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardTotal();
        users = jda.getUsers().stream().filter(user -> !user.isBot()).toArray().length;
        bots = jda.getUsers().stream().filter(User::isBot).toArray().length;

        /* Channels */
        tc = "Text `" + jda.getTextChannels().size() + "`";
        vc = "Voice `" + (getConnectedVoiceChannels() == null ? 0 : getConnectedVoiceChannels().size()) + "`";
        privateChan = "Private `" + jda.getPrivateChannels().size() + "`";

        /* System Info */
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        double loadAverage = Math.round(os.getSystemLoadAverage()*1000)/1000;
        String osversion = os.getVersion();

        MemoryUsage osx = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        String used = UtilString.convertBytes(osx.getUsed(), true);
        String available = UtilString.convertBytes(osx.getCommitted(), true);
        String memory = used + "/" + available;
        more = "Memory `" + memory + "`\nLoad `" + loadAverage + "`";

        EmbedBuilder embedstatus = new EmbedBuilder()
            .setColor(Color.blue).setTitle("AIBot Status", null).setThumbnail(avatar).setTimestamp(Instant.now());;

        embedstatus.addField(Emoji.STOPWATCH + " Uptime", uptime, true)
            .addField(Emoji.STATUS + " Status", status, true)
            .addField(Emoji.STATISTIC + " Statistic", "Servers `" + String.valueOf(guild) + "` | Shards `" + shard + "`\nUsers `" + users + "` | Bots `" + bots + "`", true)
            .addField(Emoji.TEXT + " Channel", tc+" | "+vc+"\n"+privateChan, true)
            .addField("Version", "Mac OS `" + osversion + "`\n" + getJDAVersion()+"\n"+getLavaPlayerVersion(), true)
            .addField("System", more, true);

        return embedstatus;
    }
}
