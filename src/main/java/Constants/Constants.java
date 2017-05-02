/* 
 * AIBot by AlienIdeology
 * 
 * Constants
 * All informations and links about the bot and developer
 */
package Constants;

import Setting.Prefix;
import Main.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author liaoyilin
 */
public class Constants {
    //Main
    public final static String VERSION = "[0.2.0]";
    
    //Icon
    public final static String I_INFO = "https://maxcdn.icons8.com/Share/icon/Very_Basic//info1600.png";
    public final static String I_HELP = "https://maxcdn.icons8.com/Share/icon/Programming//help1600.png";
    
    //Bot
    //-Bot Constants
    public final static String B_NAME = Main.jda.getSelfUser().getName();
    public final static String B_AVATAR = Main.jda.getSelfUser().getEffectiveAvatarUrl();
    public final static String B_DISCRIMINATOR = Main.jda.getSelfUser().getDiscriminator();
    public final static String B_ID = Main.jda.getSelfUser().getId();
    public final static String B_GAME_DEFAULT = "=help | " + Main.jda.getGuilds().size() + " Servers";
    public final static String B_GAME_UPDATE = "\u203C Updating AIBot";
    public final static String B_GAME_FIXING = "\u2049 Fixing AIBot";
    
    //-Bot Links
    public final static String B_DISCORD_BOT = "https://bots.discord.pw/bots/294327785512763392";
    public final static String B_INVITE = "https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567";
    public final static String B_SERVER = "https://discord.gg/EABc8Kc";
    public final static String B_GITHUB = "https://github.com/AlienIdeology/AIBot/";
    
    //Link
    public final static String L_MUSIC_HUB = "https://discord.gg/UMCqtZN";
    public final static String LYRICSURL = "https://genius.com/";
    
    //Permissions
    public final static Collection<Permission> PERM_MOD = new ArrayList<Permission> 
            (Arrays.asList(new Permission[] {Permission.ADMINISTRATOR, Permission.MANAGE_SERVER}));
    
    //Bot Developer ID
    public final static String D_ID = "248214880379863041";    
    
}
