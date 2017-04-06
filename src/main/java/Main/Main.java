/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import Setting.GuildSetting;
import Resource.Private;
import Resource.Prefix;
import Command.*;
import Command.InformationModule.*;
import Command.ModerationModule.*;
import Command.UtilityModule.*;
import Command.MusicModule.*;
import Command.FunModule.*;
import Command.RestrictedModule.*;
import Listener.*;
import Audio.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.entities.Game;

import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Main {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<String, Command>();
    public static HashMap<String, GuildSetting> guilds = new HashMap<String, GuildSetting>();
    public static long timeStart = 0;
    SimpleDateFormat dateformatter = new SimpleDateFormat("M/dd/yyyy, h:mm:ss a 'UTC'");
    public static Logger startLogger = Logger.getLogger(Main.class.getName());  
    public static Logger errorLogger = Logger.getLogger("Error");  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .addListener(new CommandListener())
                    .setToken(Private.BOT_TOKEN)
                    .buildBlocking();
            jda.getPresence().setGame(Game.of(Prefix.getDefaultPrefix() + "help | Developed by Ayy™"));
            jda.setAutoReconnect(true);
            
            timeStart = System.currentTimeMillis();
            
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
            Main.updateLog("Exception thrown while logging.");
        }
        startUp();
    }
    
    public static void startUp()
    {
        addCommands();
        Music.musicStartup();
        ConsoleListener console = new ConsoleListener();
        
        Main.updateLog("Bot Start Up. Commands Added.");
    }
    
    public static void shutdown()
    {
        Main.updateLog("Bot Shut Down Successfully");
        jda.shutdown();
        System.exit(0);
    }
    
    public static void updateLog(String msg)
    {
        try
        {
            FileHandler fh = new FileHandler("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Resource/LogMain.txt");
            startLogger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            startLogger.setUseParentHandlers(false);
            
            startLogger.info(msg);
            fh.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static void errorLog(Exception ex, MessageReceivedEvent e, String cause)
    {
        try
        {
            FileHandler fhe = new FileHandler("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Resource/LogError.txt");
            errorLogger.addHandler(fhe);
            SimpleFormatter formatter = new SimpleFormatter();
            fhe.setFormatter(formatter);
            errorLogger.setUseParentHandlers(false);
            
            errorLogger.log(Level.WARNING, "Guild: " + e.getGuild().getName() + " | Exception Cause: " + cause, ex);
            fhe.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
        
    private static void addCommands() {
        // Information Commands
        commands.put("help", new HelpCommand());
        commands.put("h", new HelpCommand());
        commands.put("invite", new InviteCommand());
        
        commands.put("botinfo", new InfoBotCommand());
        commands.put("bi", new InfoBotCommand());
        commands.put("serverinfo", new InfoServerCommand());
        commands.put("si", new InfoServerCommand());
        commands.put("channelinfo", new InfoChannelCommand());
        commands.put("ci", new InfoChannelCommand());
        commands.put("userinfo", new InfoUserCommand());
        commands.put("ui", new InfoUserCommand());
        
        commands.put("prefix", new PrefixCommand());
        commands.put("ping", new PingCommand());
        
        commands.put("about", new AboutCommand());
        commands.put("status", new StatusCommand("status"));
        commands.put("uptime", new StatusCommand("uptime"));
        commands.put("support", new SupportCommand());
        
        // Moderation Commands
        commands.put("prune", new PruneCommand());
        commands.put("p", new PruneCommand());
        
        commands.put("kick", new KickCommand());
        commands.put("k", new KickCommand());
        
        commands.put("ban", new BanCommand());
        commands.put("b", new BanCommand());
        commands.put("unban", new UnbanCommand());
        commands.put("ub", new UnbanCommand());
        
        //Utility Commands
        commands.put("number", new NumberCommand());
        commands.put("num", new NumberCommand());
        commands.put("n", new NumberCommand());
        
        commands.put("math", new MathCommand());
        commands.put("calc", new MathCommand());
        commands.put("m", new MathCommand());
        
        commands.put("say", new SayCommand());
        
        commands.put("emoji", new EmojiCommand());
        commands.put("emote", new EmojiCommand());
        commands.put("weather", new WeatherCommand());
        commands.put("w", new WeatherCommand());
        
        commands.put("search", new SearchCommand("search"));
        commands.put("google", new SearchCommand("google"));
        commands.put("g", new SearchCommand("google"));
        commands.put("wiki", new SearchCommand("wiki"));
        commands.put("urban", new SearchCommand("ub"));
        commands.put("github", new SearchCommand("git"));
        commands.put("git", new SearchCommand("git"));
        
        commands.put("image", new ImageCommand("image"));
        commands.put("imgur", new ImageCommand("imgur"));
        commands.put("imgflip", new ImageCommand("imgflip"));
        commands.put("gif", new ImageCommand("gif"));
        commands.put("meme", new ImageCommand("meme"));
        
        commands.put("8ball", new EightBallCommand());
        commands.put("face", new FaceCommand());
        commands.put("game", new GameCommand());
        commands.put("lenny", new FaceCommand());
        commands.put("f", new FaceCommand());
        commands.put("rockpaperscissors", new RPSCommand());
        commands.put("rps", new RPSCommand());
        commands.put("tictactoe", new TicTacToeCommand());
        commands.put("ttt", new TicTacToeCommand());
        commands.put("hangman", new HangManCommand());
        commands.put("hm", new HangManCommand());
        commands.put("hangmancheater", new HangManCheaterCommand());
        commands.put("hmc", new HangManCheaterCommand());
        
        // Music Commands
        commands.put("join", new JoinCommand());
        commands.put("summon", new JoinCommand());
        commands.put("j", new JoinCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("l", new LeaveCommand());
        commands.put("play", new PlayCommand());
        commands.put("pause", new PauseCommand("pause"));
        commands.put("resume", new PauseCommand("resume"));
        commands.put("unpause", new PauseCommand("resume"));
        commands.put("skip", new SkipCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("stop", new StopCommand());
        
        //Restricted Commands
        commands.put("shutdown", new ShutDownCommand());
        commands.put("source", new SourceCommand());
    }
}