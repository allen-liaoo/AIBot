/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Main.Main;
import Resource.Emoji;
import Setting.SmartLogger;
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
    private static EmbedBuilder embedstart = new EmbedBuilder();
    private static EmbedBuilder embedend = new EmbedBuilder();
    private static EmbedBuilder embedgame = new EmbedBuilder();
    public static User starter;
    
    private static String letter;
    private static ArrayList<String> word = new ArrayList<String>();
    private static ArrayList<String> guessed = new ArrayList<String>(); //All guesses
    private static ArrayList<String> missed = new ArrayList<String>(); //Wrong guesses
    private static ArrayList<String> right = new ArrayList<String>(); //Right guesses
    
    private static final int limit = 7;
    
    public static String hangman = "```_____________   \n"
                                 + "|           |   \n"
                                 + "|          " + Emoji.hanged_face + "  \n"
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
            
            //Clear Last game's data
            clear();
            
            //Initialize            
            for(int i = 0; i < ranword.length(); i ++)
            {
                word.add(ranword.substring(i,i+1));
                right.add("_");
            }
            ranword = "";
            
        } catch (IOException ioe) {
            SmartLogger.errorLog(ioe, e, this.getClass().getName(), "BufferReeader at startGame()");
        }
        
        embedstart.setColor(Color.green);
        embedstart.addField(Emoji.game + " Hang Man: Game Mode ON!", 
                "Starter: " + starter.getAsMention()
                + "\nWord length: " + word.size()
                + "\n" + hangman, true);
        MessageEmbed me = embedstart.build();
        e.getChannel().sendMessage(me).queue();
        embedstart.clearFields();
        
        printRightLetter();
    }

    @Override
    public void endGame() { //End the game
        embedend.setColor(Color.green);
        embedend.setTitle(Emoji.game + " Hang Man: Game Mode OFF!", null);
        embedend.setFooter(e.getAuthor().getName() + " ended the game.", null);
        MessageEmbed me = embedend.build();
        e.getChannel().sendMessage(me).queue();
        embedend.clearFields();
        
        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
        clear();
    }

    @Override
    public void sendInput(String[] in, MessageReceivedEvent event) { //Get input
        e = event;
        if(in.length > 1 || in[0].length() != 1)
            e.getChannel().sendMessage(Emoji.error + " One letter at a time!").queue();
        
        else if(!Character.isLetter(in[0].charAt(0)))
            e.getChannel().sendMessage(Emoji.error + " Please enter a valid letter.").queue();
        
        else if(word.size() == 0)
            e.getChannel().sendMessage(Emoji.error + " Game haven't started yet!").queue();
        
        else
        {   
            letter = in[0];
            if(!guessed.contains(letter))
                guessed.add(letter);
            else
            {
                e.getChannel().sendMessage(Emoji.error + " This letter has been guessed before.").queue();
                return;
            }
            
            int countmiss = 0;
            for(int i = 0; i < word.size(); i ++)
            {
                if(letter.equals(word.get(i)))
                {
                    right.set(i, letter);
                }
                else
                {
                    countmiss++;
                }
            }
            
            if(countmiss == word.size())
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
    
    public boolean checkWin() //Check for winner
    {
        for(int i = 0; i < word.size(); i++)
        {
            if(!right.get(i).equals(word.get(i)))
                return false;
        }
        
        embedend.setColor(Color.green);
        embedend.setTitle(Emoji.game + " Hang Man: Game Mode OFF!", null);
        embedend.setFooter(e.getAuthor().getName() + " is the winner!", null);
        MessageEmbed me = embedend.build();
        e.getChannel().sendMessage(me).queue();
        embedend.clearFields();
        
        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
        return true;
    }
    
    public void clear() //Clear all arraylist.
    {
        guessed.clear();
        missed.clear();
        word.clear();
        right.clear();
    }
    
    public void print() //Print out the result
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
        embedgame.setTitle(Emoji.game + " Current Man (Hanged!?)", null);
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
                         + "|          " + Emoji.hanged_face + "  \n"
                         + "|               \n"
                         + "|                \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 2:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |   \n"
                         + "|                \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 3:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |   \n"
                         + "|           |    \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 4:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |  \n"
                         + "|          /|    \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 5:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |   \n"
                         + "|          /|\\   \n"
                         + "|                \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 6:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |   \n"
                         + "|          /|\\   \n"
                         + "|          /     \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            case 7:
                hangman =   "```_____________   \n"
                         + "|           |   \n"
                         + "|           " + Emoji.hanged_face + "  \n"
                         + "|           |   \n"
                         + "|          /|\\   \n"
                         + "|          / \\   \n"
                         + "|                \n"
                         + "___              ```\n";
                break;
            default:
                hangman = " ";
        }
        return hangman;
    }

    @Override
    public void switchTurn() {
        
    }
    
}
