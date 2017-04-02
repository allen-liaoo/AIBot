/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Config.Emoji;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HangMan implements Game{

    private static MessageReceivedEvent e;
    private static EmbedBuilder embedstatus = new EmbedBuilder();
    private static EmbedBuilder embedgame = new EmbedBuilder();
    private static User starter;
    
    private static String letter;
    private static String[] word;
    private static ArrayList<String> guessed = new ArrayList<String>(); //All guesses
    private static ArrayList<String> missed = new ArrayList<String>(); //Wrong guesses
    private static String[] right; //Right guesses
    
    private static final int limit = 6;
    
    public static String hangman = "```_____________   \n"
                                 + "|           |   \n"
                                 + "|          " + Emoji.E_hanged_face + "  \n"
                                 + "|           |   \n"
                                 + "|          /|\\  \n"
                                 + "|          / \\  \n"
                                 + "|               \n"
                                 + "___             ```\n";
    
    public HangMan(MessageReceivedEvent event)
    {
        e = event;
        
        startGame();
    }
    
    @Override
    public void startGame() 
    {
        starter = e.getAuthor();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/WordBank.txt"));
            
            String ranword;
            long random = (long) (Math.random() * 58109) + 1;
            long count = 0;
            
            while((ranword = reader.readLine() ) != null)
            {
                count++;
                if(random == count) break;
            }
            reader.close();
            System.out.println(ranword);
            
            //Initialize
            word = new String[ranword.length()]; 
            right = new String[word.length];
            Arrays.fill(right, "_");
            
            for(int i = 0; i < word.length; i ++)
            {
                word[i] = ranword.substring(i,i+1);
            }
            
        } catch (IOException io) {
            io.printStackTrace();
        }
        
        embedstatus.setColor(Color.green);
        embedstatus.addField(Emoji.E_game + " Hang Man: Game Mode ON!", 
                "Starter: " + starter.getAsMention()
                + "\nWord length: " + word.length, true);
        MessageEmbed me = embedstatus.build();
        e.getChannel().sendMessage(me).queue();
        embedstatus.clearFields();
        
        printRightLetter();
        e.getChannel().sendMessage(hangman).queue();
    }

    @Override
    public void endGame() {
        embedstatus.setColor(Color.green);
        embedstatus.setTitle(Emoji.E_game + " Hang Man: Game Mode OFF!", null);
        embedstatus.setFooter(e.getAuthor().getName() + " ended the game.", null);
        MessageEmbed me = embedstatus.build();
        e.getChannel().sendMessage(me).queue();
        embedstatus.clearFields();
        
        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
    }

    @Override
    public void sendInput(String[] in, MessageReceivedEvent event) {
        if(in.length > 1 || in[0].length() != 1)
            e.getChannel().sendMessage(Emoji.E_error + " One letter at a time!").queue();
            
        else
        {   
            letter = in[0];
            guessed.add(letter);
            
            int countmiss = 0;
            for(int i = 0; i < word.length; i ++)
            {
                if(letter.equals(word[i]))
                {
                    right[i] = letter;
                }
                else
                {
                    countmiss++;
                }
            }
            
            if(countmiss == word.length)
            {
                missed.add(letter);
                
                if(missed.size() >= limit)
                {
                    endGame();
                    return;
                }
            }
            
            boolean end = checkWin();
            if(end == false) print();
        }
    }
    
    public boolean checkWin()
    {
        for(int i = 0; i < word.length; i++)
        {
            if(!right[i].equals(word[i]))
                return false;
        }
        
        embedstatus.setColor(Color.green);
        embedstatus.setTitle(Emoji.E_game + " Hang Man: Game Mode OFF!", null);
        embedstatus.setFooter(e.getAuthor().getName() + " is the winner!", null);
        MessageEmbed me = embedstatus.build();
        e.getChannel().sendMessage(me).queue();
        embedstatus.clearFields();
        
        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
        return true;
    }
    
    public void print()
    {
        String missedletter = "";
        
        for(String s : missed)
        {
            missedletter += s + ", ";
        }
        
        if("".equals(missedletter))
            missedletter = "Missed Letters: None";
        else
            missedletter = "Missed Letters: " + missedletter.substring(0,missedletter.length()-2).toUpperCase();
        
        embedgame.setColor(Color.green);
        embedgame.setTitle(Emoji.E_game + " Current Man (Hanged!?)", null);
        embedgame.setDescription(missedletter + "\n" + printHangMan());
        embedgame.setFooter("Guessed by " + e.getAuthor().getName(), e.getAuthor().getAvatarUrl());

        MessageEmbed me = embedgame.build();
        e.getChannel().sendMessage(me).queue();
        embedgame.clearFields();
        
        printRightLetter();
        
    }
    
    public void printRightLetter()
    {
        String rightletter = "`"; 
        for(String s : right)
           {
               rightletter += s + " ";
           }
           rightletter += "`";
        e.getChannel().sendMessage(rightletter).queue();
    }
    
    public String printHangMan()
    {
        String hangman =  "";
        switch(missed.size())
        {
            case 0:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|               \n"
                         + "|               \n"
                         + "|                \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
            case 1:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|          " + Emoji.E_hanged_face + "  \n"
                         + "|               \n"
                         + "|                \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 2:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.E_hanged_face + "  \n"
                         + "|           |   \n"
                         + "|                \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 3:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.E_hanged_face + "  \n"
                         + "|           |   \n"
                         + "|           |    \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 4:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.E_hanged_face + "  \n"
                         + "|           |  \n"
                         + "|          /|    \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 5:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.E_hanged_face + "  \n"
                         + "|           |   \n"
                         + "|          /|\\   \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 6:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.E_hanged_face + "  \n"
                         + "|           |   \n"
                         + "|          /|\\   \n"
                         + "|          /     \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            default:
                hangman = " ";
        }
        return hangman;
        //e.getChannel().sendMessage(hangman).queue();
    }

    @Override
    public void switchTurn() {
        
    }
    
}
