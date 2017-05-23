/* 
 * AIBot by AlienIdeology
 * 
 * Constants
 * All informations and links about the bot and developer
 */
package constants;

import main.AIBot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.Permission;
import setting.Prefix;

/**
 *
 * @author liaoyilin
 */
public class Global {
    //main
    public final static String VERSION = "[0.2.0]";
    
    //Bot
    //-Bot Global
    public final static String B_NAME = "AIBot";
    public final static String B_AVATAR = "https://images-ext-2.discordapp.net/eyJ1cmwiOiJodHRwczovL2Nkbi5kaXNjb3JkYXBwLmNvbS9hdmF0YXJzLzI5NDMyNzc4NTUxMjc2MzM5Mi82NGMxMDdmN2M4YzcwZjE0ZjcxOWViZDM5MDdmMTc3Ni5wbmcifQ.xxgV3xUCZ9WyxNl74WtlHVsEqgI";
    public final static String B_DISCRIMINATOR = "9987";
    public final static String B_ID = "294327785512763392";
    public final static int B_SHARDS = 1;
    public final static String B_GAME_DEFAULT = "=help | " + AIBot.getGuilds().size() + " Servers";
    public final static String B_GAME_UPDATE = "\u203C Updating AIBot";
    public final static String B_GAME_FIXING = "\u2049 Fixing AIBot";
    
    //-Bot Links
    public final static String B_DISCORD_BOT = "https://bots.discord.pw/bots/294327785512763392";
    public final static String B_INVITE = "https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567";
    public final static String B_SERVER = "https://discord.gg/EABc8Kc";
    public final static String B_GITHUB = "https://github.com/AlienIdeology/AIBot/";

    //-Bot Server
    public final static String B_SERVER_ID = "293928413029466114";
    public final static String B_SERVER_ERROR = "294487318797090816";
    public final static String B_SERVER_STATUS = "310453321972449290";
    public final static String B_SERVER_STATUS_MSG = "310458751645908992";
    
    //Link
    public final static String L_MUSIC_HUB = "https://discord.gg/UMCqtZN";
    public final static String LYRICSURL = "https://genius.com/";
    
    //Permissions
    public final static ArrayList<Permission> PERM_MOD = new ArrayList<>
            (Arrays.asList(Permission.ADMINISTRATOR, Permission.MANAGE_SERVER));

    //Bot Developer ID
    public final static String D_ID = "248214880379863041";

    public static final Pattern urlPattern = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\\\[\\\\]:!$&'()*+,;=]+)$");

    public static final String defaultGame() {
        return Prefix.getDefaultPrefix() + "help | " + AIBot.getGuilds().size() + " Servers";
    }

}
