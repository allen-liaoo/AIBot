/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.*;
import static Command.Command.embed;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Utility.AILogger;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HangManCheaterCommand implements Command {
    public final static String HELP = "Hang Man Cheater based on the Word Bank.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "hangmancheater` or `" + Prefix.getDefaultPrefix() + "hmc`\n"
                                    + "Parameter: `-h | [Unknown Word] [Missed Letters] [Page] | null`\n"
                                    + "[Unknown Word] [Missed Letters] [Page]: Use `?` for unknow letters in the [Unknown Word]. "
                                    + "Type in the incorrect letters for [Missed Letters], and the page number you want. (50 words per page)";
            
    private ArrayList<String> wordpo = new ArrayList<String>();
    private ArrayList<String> missed = new ArrayList<String>();
    private ArrayList<String> nomissed = new ArrayList<String>();
    private ArrayList<String> result = new ArrayList<String>();
    
    private EmbedBuilder embedre = new EmbedBuilder();
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("HangMan Cheater -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length < 3 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            AILogger.commandLog(e, this.getClass().getName(), "Called by " + e.getAuthor().getName());
            //Initialize
            String word = args[0], miss = args[1];
            
            for(int i = 0; i < word.length(); i ++)
            {
                wordpo.add(word.substring(i, i+1));
            }
                
            for(int i = 0; i < miss.length(); i ++)
            {
                missed.add(miss.substring(i, i+1));
            }
            
            int page = 1; //Result pages
            if(args.length == 3)
                page = Integer.parseInt(args[2]);
            
            //Check missed letters
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/WordBank.txt"));
                
                String ranword = "";
                while((ranword = reader.readLine()) != null)
                {
                    if(ranword.length() != word.length())
                        continue;
                    boolean contain = false;
                    for(int i = 0; i < missed.size(); i ++)
                    {
                        if(ranword.contains(missed.get(i)))
                        {
                            contain = true;
                            break;
                        }
                    }
                    if(contain == false)
                        nomissed.add(ranword);
                }
            
                } catch (IOException ioe) {
                AILogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at Checking missed letters.");
            }
            
            //Check if the known position has the same letters
            for(int i = 0; i < nomissed.size(); i++)
            {
                boolean wrong = true;
                for(int j = 0; j < wordpo.size(); j++)
                {
                    if(!"?".equals(wordpo.get(j)))
                    {
                        if(!wordpo.get(j).equals(nomissed.get(i).substring(j, j+1)))
                        {
                            wrong = true;
                            break;
                        }
                        else
                            wrong = false;
                    }
                }
                if(wrong == false)
                        result.add(nomissed.get(i));
            }
            
            //Print out result
            String possibleword = "", pages = "\nPages ";
            
            if(result.isEmpty())
            {
                e.getChannel().sendMessage(Emoji.ERROR + " No results.").queue();
                return;
            }
            else if(result.size() == 1)
                possibleword = "__** Found " + result.size() + " Possible Word: **__\n";
            else
                possibleword = "__** Found " + result.size() + " Possible Words: **__\n";
            
            pages += page;
            
            int to = page * 50, from = to - 50, count = 0;
            possibleword += "Pages " + page + ", from word #"+ (from +1) + " to " + to + "\n`";
            for(String s : result)
            {
                count++;
                if(count > from)
                {
                    s = s.substring(0, 1).toUpperCase() + s.substring(1);
                    possibleword += s + ", ";
                }
                if(count >= to)
                    break;
            }
            possibleword = possibleword.substring(0, possibleword.length()-2) + "`";
            
            embedre.setColor(Color.green);
            embedre.setTitle(Emoji.FACE_BLUSH + " HangMan: Cheater Mode ON!", null);
            embedre.setDescription(possibleword);
            embedre.setFooter("Requested by cheater " + e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
            embedre.setTimestamp(Instant.now());

            MessageEmbed me = embedre.build();
            e.getChannel().sendMessage(me).queue();
            embedre.clearFields();
            
            clearList();
        }
    }

    
    private void clearList()
    {
        wordpo.clear();
        nomissed.clear();
        missed.clear();
        result.clear();
    }
    
}
