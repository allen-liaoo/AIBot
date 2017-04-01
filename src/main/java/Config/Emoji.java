/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// EMoji can be found here: https://github.com/vdurmont/emoji-java

package Config;

import com.vdurmont.emoji.*;

/**
 *
 * @author liaoyilin
 */
public class Emoji {
    //General
    public static String E_error = EmojiParser.parseToUnicode(":fire:");
    public static String E_up = EmojiParser.parseToUnicode(":arrow_up:");
    public static String E_down = EmojiParser.parseToUnicode(":arrow_down:");
    public static String E_invite = EmojiParser.parseToUnicode(":postbox:");
    
    //Information Commands
    public static String E_envelope = EmojiParser.parseToUnicode(":incoming_envelope:");
    public static String E_ping = EmojiParser.parseToUnicode(":ping_pong:");
    
    //Utility Commands
    //-NumberCommand
    public static String E_success = EmojiParser.parseToUnicode(":white_check_mark:");
    public static String E_number = EmojiParser.parseToUnicode(":1234:");
    public static String E_output = EmojiParser.parseToUnicode(":printer:");
    public static String E_roll = EmojiParser.parseToUnicode(":game_die:");
    public static String E_1 = EmojiParser.parseToUnicode(":one:");
    public static String E_2 = EmojiParser.parseToUnicode(":two:");
    public static String E_3 = EmojiParser.parseToUnicode(":three:");
    public static String E_4 = EmojiParser.parseToUnicode(":four:");
    public static String E_5 = EmojiParser.parseToUnicode(":five:");
    public static String E_6 = EmojiParser.parseToUnicode(":six:");
    
    //-WeatherCommand
    public static String E_temp = EmojiParser.parseToUnicode(":thermometer:");
    public static String E_wind = EmojiParser.parseToUnicode(":dash:");
    //--Condition
    public static String E_sunny = EmojiParser.parseToUnicode(":sunny:");
    public static String E_cloud = EmojiParser.parseToUnicode(":cloud:");
    public static String E_cloud_part = EmojiParser.parseToUnicode(":white_sun_small_cloud:");
    public static String E_cloudy = EmojiParser.parseToUnicode(":white_sun_behind_cloud:");
    public static String E_cloudy_rain = EmojiParser.parseToUnicode(":white_sun_behind_cloud_rain:");
    public static String E_cloud_rain = EmojiParser.parseToUnicode(":cloud_rain:");
    public static String E_cloud_thunder_rain = EmojiParser.parseToUnicode(":thunder_cloud_rain:");
    public static String E_cloud_tornado = EmojiParser.parseToUnicode(":cloud_tornado:");
    public static String E_snow = EmojiParser.parseToUnicode(":cloud_snow:");
    public static String E_windy = EmojiParser.parseToUnicode(":blowing_wind:");
    public static String E_snowman = EmojiParser.parseToUnicode(":snowing_snowman:");
    
    public static String E_sweat = EmojiParser.parseToUnicode(":sweat:");
    public static String E_press = EmojiParser.parseToUnicode(":compression:");
    public static String E_eyes = EmojiParser.parseToUnicode(":eyes:");
    
    //-SearchCommand
    public static String E_search = EmojiParser.parseToUnicode(":mag:");
    
    //-Game
    public static String E_game = EmojiParser.parseToUnicode(":video_game:");
    
    //Music
    public static String E_globe = EmojiParser.parseToUnicode(":globe_with_meridians:");
    
}
