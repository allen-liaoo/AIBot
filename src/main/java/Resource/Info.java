/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import Main.Main;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author liaoyilin
 */
public class Info {
    //Main
    public static String VERSION = "[0.1.9]";
    
    //Icon
    public static String I_INFO = "https://maxcdn.icons8.com/Share/icon/Very_Basic//info1600.png";
    public static String I_HELP = "https://maxcdn.icons8.com/Share/icon/Programming//help1600.png";
    
    //Bot
    //-Bot Info
    public static String B_NAME = Main.jda.getSelfUser().getName();
    public static String B_AVATAR = Main.jda.getSelfUser().getEffectiveAvatarUrl();
    public static String B_DISCRIMINATOR = Main.jda.getSelfUser().getDiscriminator();
    public static String B_ID = Main.jda.getSelfUser().getId();
    public static String B_GAME = Prefix.DIF_PREFIX + "help | Dev: Ayy\u2122";
    
    //-Bot Links
    public static String B_DISCORD_BOT = "https://bots.discord.pw/bots/294327785512763392";
    public static String B_INVITE = "https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567";
    public static String B_SERVER = "https://discord.gg/EABc8Kc";
    public static String B_GITHUB = "https://github.com/AlienIdeology/AIBot/";
    
    //Link
    public static String L_MUSIC_HUB = "https://discord.gg/UMCqtZN";
    public static final String LYRICSURL = "https://genius.com/";

    public static Color setColor() {
        Random colorpicker = new Random();
        int red;
        int green;
        int blue;
        red = colorpicker.nextInt(255) + 1;
        green = colorpicker.nextInt(255) + 1;
        blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }
    /* Private Message Help
    HelpCommand.helpText(e);
    HelpCommand.me = HelpCommand.embed.build();
    e.getChannel().sendMessage(Emoji.envelope + " You need help? Check private message!").complete();
    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage("Help is on its way...").complete().editMessage(HelpCommand.me).submit());
    HelpCommand.embed.clearFields();
     */
}
