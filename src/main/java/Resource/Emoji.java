/* 
 * AIBot by AlienIdeology
 * 
 * Emoji
 * Static fields for global uses of Emoji
 */
package Resource;

import com.vdurmont.emoji.*;
import java.util.List;

/**
 *
 * @author liaoyilin
 */
public class Emoji {
    
    //General
    public final static String error = EmojiParser.parseToUnicode(":fire:");
    public final static String up = EmojiParser.parseToUnicode(":arrow_up:");
    public final static String down = EmojiParser.parseToUnicode(":arrow_down:");
    public final static String invite = EmojiParser.parseToUnicode(":postbox:");
    
    //-Faces
    public final static String face_tongue = EmojiParser.parseToUnicode(":stuck_out_tongue:");
    public final static String face_blush = EmojiParser.parseToUnicode(":blush:");
    
    //-Numbers
    public final static String one = EmojiParser.parseToUnicode(":one:");
    public final static String two = EmojiParser.parseToUnicode(":two:");
    public final static String three = EmojiParser.parseToUnicode(":three:");
    public final static String four = EmojiParser.parseToUnicode(":four:");
    public final static String five = EmojiParser.parseToUnicode(":five:");
    public final static String six = EmojiParser.parseToUnicode(":six:");
    public final static String seven = EmojiParser.parseToUnicode(":seven:");
    public final static String eight = EmojiParser.parseToUnicode(":eight:");
    public final static String nine = EmojiParser.parseToUnicode(":nine:");
    public final static String zero = EmojiParser.parseToUnicode(":zero:");
    public final static String hundred = EmojiParser.parseToUnicode(":100:");
    
    //Information Commands
    public final static String envelope = EmojiParser.parseToUnicode(":incoming_envelope:");
    public final static String ping = EmojiParser.parseToUnicode(":ping_pong:");
    public final static String stopwatch = EmojiParser.parseToUnicode(":stopwatch:");
    public final static String status = EmojiParser.parseToUnicode(":vertical_traffic_light:");
    public final static String guilds = EmojiParser.parseToUnicode(":card_file_box:");
    public final static String shards = EmojiParser.parseToUnicode(":file_cabinet:");
    public final static String text = EmojiParser.parseToUnicode(":speech_balloon:");
    public final static String privatespy = EmojiParser.parseToUnicode(":spy:");
    
    //Utility Commands
    //-NumberCommand
    public final static String success = EmojiParser.parseToUnicode(":white_check_mark:");
    public final static String number = EmojiParser.parseToUnicode(":1234:");
    public final static String print = EmojiParser.parseToUnicode(":printer:");
    public final static String roll = EmojiParser.parseToUnicode(":game_die:");
    
    //-WeatherCommand
    public final static String temp = EmojiParser.parseToUnicode(":thermometer:");
    public final static String wind = EmojiParser.parseToUnicode(":dash:");
    //--Condition
    public final static String sunny = EmojiParser.parseToUnicode(":sunny:");
    public final static String cloud = EmojiParser.parseToUnicode(":cloud:");
    public final static String cloud_part = EmojiParser.parseToUnicode(":white_sun_small_cloud:");
    public final static String cloudy = EmojiParser.parseToUnicode(":white_sun_behind_cloud:");
    public final static String cloudy_rain = EmojiParser.parseToUnicode(":white_sun_behind_cloud_rain:");
    public final static String cloud_rain = EmojiParser.parseToUnicode(":cloud_rain:");
    public final static String cloud_thunder_rain = EmojiParser.parseToUnicode(":thunder_cloud_rain:");
    public final static String cloud_tornado = EmojiParser.parseToUnicode(":cloud_tornado:");
    public final static String snow = EmojiParser.parseToUnicode(":cloud_snow:");
    public final static String windy = EmojiParser.parseToUnicode(":blowing_wind:");
    public final static String snowman = EmojiParser.parseToUnicode(":snowing_snowman:");
    public final static String sweat = EmojiParser.parseToUnicode(":sweat:");
    public final static String press = EmojiParser.parseToUnicode(":compression:");
    public final static String eyes = EmojiParser.parseToUnicode(":eyes:");
    //-EmojiCommmand
    public final static String abc = EmojiParser.parseToUnicode(":abc:");
    public final static String abcd = EmojiParser.parseToUnicode(":abcd:");
    public final static String vs = EmojiParser.parseToUnicode(":vs:");
    public final static String cool = EmojiParser.parseToUnicode(":cool:");
    public final static String ok = EmojiParser.parseToUnicode(":ok:");
    public final static String symbols = EmojiParser.parseToUnicode(":symbols:");
    public final static String new_word = EmojiParser.parseToUnicode(":new:");
    public final static String free = EmojiParser.parseToUnicode(":free:");
    public final static String mark_question = EmojiParser.parseToUnicode(":grey_question:");
    public final static String mark_exclamation = EmojiParser.parseToUnicode(":exclamation:");
    public final static String mark_hash = EmojiParser.parseToUnicode(":hash:");
    public final static String mark_asterisk = EmojiParser.parseToUnicode(":keycap_asterisk:");
    public final static String mark_dollar_sign = EmojiParser.parseToUnicode(":heavy_dollar_sign:");
    public final static String mark_plus_sign = EmojiParser.parseToUnicode(":heavy_plus_sign:");
    public final static String mark_minus_sign = EmojiParser.parseToUnicode(":heavy_minus_sign:");
    public final static String mark_divide_sign = EmojiParser.parseToUnicode(":heavy_division_sign:");
    public final static String dot = EmojiParser.parseToUnicode(":black_circle_for_record:");
    
    //-SearchCommand
    public static String search = EmojiParser.parseToUnicode(":mag:");
    //--IMDbCommand
    public final static String film_projector = EmojiParser.parseToUnicode(":film_projector:");
    public final static String film_frames = EmojiParser.parseToUnicode(":film_frames:");
    public final static String date = EmojiParser.parseToUnicode(":date:");
    public final static String star = EmojiParser.parseToUnicode(":star:");
    public final static String stars = EmojiParser.parseToUnicode(":stars:");
    public final static String trophy = EmojiParser.parseToUnicode(":trophy:");
    public final static String book = EmojiParser.parseToUnicode(":book:");
    
    //Music
    public final static String music = EmojiParser.parseToUnicode(":musical_keyboard:");
    public final static String player = EmojiParser.parseToUnicode(":black_right_pointing_triangle_with_double_vertical_bar:");
    public final static String globe = EmojiParser.parseToUnicode(":globe_with_meridians:");
    public final static String notes = EmojiParser.parseToUnicode(":notes:");
    public final static String pause = EmojiParser.parseToUnicode(":double_vertical_bar:");
    public final static String resume = EmojiParser.parseToUnicode(":arrow_forward:");
    public final static String shuffle = EmojiParser.parseToUnicode(":twisted_rightwards_arrows:");
    public final static String repeat = EmojiParser.parseToUnicode(":arrows_counterclockwise:");
    public final static String stop = EmojiParser.parseToUnicode(":black_square_for_stop:");
    public final static String next_track = EmojiParser.parseToUnicode(":black_right_pointing_double_triangle_with_vertical_bar:");
    public final static String up_vote = EmojiParser.parseToUnicode(":arrow_up_small:");
    public final static String volume_low = EmojiParser.parseToUnicode(":sound:");
    public final static String volume_high = EmojiParser.parseToUnicode(":loud_sound:");
    
    //Fun
    //-Game
    public final static String game = EmojiParser.parseToUnicode(":video_game:");
    public final static String hanged_face = EmojiParser.parseToUnicode(":confounded:");
    public final static String eight_ball = EmojiParser.parseToUnicode(":8ball:");
    //--RPSCommand
    public final static String rock = EmojiParser.parseToUnicode(":last_quarter_moon:");
    public final static String paper = EmojiParser.parseToUnicode(":rolled_up_newspaper:");
    public final static String scissors = EmojiParser.parseToUnicode(":scissors:");
    public final static String tie = EmojiParser.parseToUnicode(":necktie:");
    
    //Global Server Emojis
    public final static String guild_online = EmojiParser.parseToUnicode("<:vpOnline:212789758110334977>");
    public final static String guild_idle = EmojiParser.parseToUnicode("<:vpAway:212789859071426561>");
    public final static String guild_dnd = EmojiParser.parseToUnicode("<:vpDnD:236744731088912384>");
    public final static String guild_offline = EmojiParser.parseToUnicode("<:vpOffline:212790005943369728>");
    public final static String guild_streaming = EmojiParser.parseToUnicode("<:vpStreaming:212789640799846400>");
    
    /**
     * Change the String of letters mixed with numbers into a String of emojis
     * @param input the String to be change to emoji
     * @return String of emojis
     */
    public static String stringToEmoji(String input)
    {
        String output = "";
        for (int i = 0; i < input.length(); i++) 
        {
            String letters = input.substring(i,i+1);
            char letterc = input.charAt(i);
            
            /*
            * Number More than one digit
            */
            //1234
            if(input.length() >= i+4 && "1234".equals(input.substring(i,i+4)))
            {
                output += Emoji.number;
                i+=3;
                continue;
            }
            //100
            if(input.length() >= i+3 && "100".equals(input.substring(i,i+3)))
            {
                output += hundred;
                i+=2;
                continue;
            }
            
            /*
            * Character more than one digit
            */
            //ABCD or abcd
            if(input.length() >= i+4 && "abcd".equalsIgnoreCase(input.substring(i,i+4)))
            {
                output += Emoji.abcd;
                i+=3;
                continue;
            }
            //ABC or abc
            else if(input.length() >= i+3 && "abc".equalsIgnoreCase(input.substring(i,i+3)))
            {
                output += Emoji.abc;
                i+=2;
                continue;
            }
            if(input.length() >= i+2 && "vs".equalsIgnoreCase(input.substring(i,i+2)))
            {
                output += Emoji.vs;
                i+=1;
                continue;
            }
            if(input.length() >= i+5 && "music".equalsIgnoreCase(input.substring(i,i+5)))
            {
                output += Emoji.notes;
                i+=4;
                continue;
            }
            if(input.length() >= i+4 && "cool".equalsIgnoreCase(input.substring(i,i+4)))
            {
                output += Emoji.cool;
                i+=3;
                continue;
            }
            if(input.length() >= i+3 && "new".equalsIgnoreCase(input.substring(i,i+3)))
            {
                output += Emoji.new_word;
                i+=2;
                continue;
            }
            if(input.length() >= i+4 && "free".equalsIgnoreCase(input.substring(i,i+4)))
            {
                output += Emoji.free;
                i+=3;
                continue;
            }
            if(input.length() >= i+2 && "ok".equalsIgnoreCase(input.substring(i,i+2)))
            {
                output += Emoji.ok;
                i+=1;
                continue;
            }
            if(input.length() >= i+2 && ".n".equalsIgnoreCase(input.substring(i,i+2)))
            {
                output += "\n";
                i+=1;
                continue;
            }
            
            /*
            * Check One Letter at a Time
            */
            if(Character.isAlphabetic(letterc))
            {
                output += lettersToEmoji(letters);
            }
            else if(Character.isDigit(letterc))
            {
                output += numToEmoji(Integer.parseInt(letters));
            }
            //Spacing
            else if(Character.isWhitespace(letterc))
            {
                output += " ";
            }
            else if(!Character.isAlphabetic(letterc))
            {
                switch(letters) {
                    case ".":
                        output += Emoji.dot;
                        break;
                    case "?":
                        output += Emoji.mark_question;
                        break;
                    case "!":
                        output += Emoji.mark_exclamation;
                        break;
                    case "#":
                        output += Emoji.mark_hash;
                        break;
                    case "*":
                        output += Emoji.mark_asterisk;
                        break;
                    case "&":
                    case "%":
                        output += Emoji.symbols;
                        break;
                    case "+":
                        output += Emoji.mark_plus_sign;
                        break;
                    case "-":
                        output += Emoji.mark_minus_sign;
                        break;
                    case "/":
                        output += Emoji.mark_divide_sign;
                        break;
                    default:
                        break;
                }
            }
        }
        return output;
    }
    
    /**
     * Change the letter(s) into a String of emojis
     * @param input the letter(s) to be change to emoji
     * @return String of letter(s) in emojis form
     */
    public static String lettersToEmoji(String input)
    {
        String output = ":regional_indicator_" + input.toLowerCase() + ":";
        return output;
    }
    
    /**
     * Change the input number into a String of emojis
     * @param num the number to be change to emojis
     * @return String of a number in emojis from
     */
    public static String numToEmoji(int digit)
    {
        String output = "";
        switch(digit) {
            case 1:
                output += Emoji.one;
                break;
            case 2:
                output += Emoji.two;
                break;
            case 3:
                output += Emoji.three;
                break;
            case 4:
                output += Emoji.four;
                break;
            case 5:
                output += Emoji.five;
                break;
            case 6:
                output += Emoji.six;
                break;
            case 7:
                output += Emoji.seven;
                break;
            case 8:
                output += Emoji.eight;
                break;
            case 9:
                output += Emoji.nine;
                break;
            default:
                output += Emoji.zero;
                break;
        }
        
        return output;
    }
}
