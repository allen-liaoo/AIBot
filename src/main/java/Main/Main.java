/* 
 * AIBot by AlienIdeology
 * 
 * Main
 * Startup tasks, add commands, and bot configuration
 */
package Main;

import Setting.GuildWrapper;
import Private.Private;
import Command.*;
import Command.InformationModule.*;
import Command.ModerationModule.*;
import Command.UtilityModule.*;
import Command.MusicModule.*;
import Command.FunModule.*;
import Command.RestrictedModule.*;
import Listener.*;
import Audio.*;
import Constants.Constants;
import AISystem.AILogger;
import com.mashape.unirest.http.Unirest;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.entities.Game;

import java.util.HashMap;
import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Main {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static final TextRespond respond = new TextRespond();
    public static HashMap<String, Command> commands = new HashMap<String, Command>();
    public static HashMap<String, GuildWrapper> guilds = new HashMap<String, GuildWrapper>();
    public static long timeStart = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Music.musicStartup();
            
            jda = new JDABuilder(AccountType.BOT)
                    .addEventListener(new GuildListener())
                    .addEventListener(new CommandListener())
                    .addEventListener(new SelectorListener())
                    .setToken(Private.BOT_TOKEN)
                    .setAutoReconnect(true)
                    .setMaxReconnectDelay(300)
                    .buildBlocking();
            
            jda.getPresence().setGame(Game.of(Constants.B_GAME_DEFAULT + " | " + jda.getGuilds().size() + " Servers"));
            
            startUp();
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
            AILogger.updateLog("Exception thrown while logging.");
        }
    }
    
    public static void startUp()
    {
        timeStart = System.currentTimeMillis();
        addCommands();
        ConsoleListener console = new ConsoleListener();
        
        AILogger.updateLog("Bot Start Up. Commands Added.");
    }
    
    public static void shutdown() throws IOException
    {
        System.out.println("Bot Shut Down Successfully");
        AILogger.updateLog("Bot Shut Down Successfully");
        
        Unirest.shutdown();
        jda.shutdown();
        System.exit(0);
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
        
        commands.put("list", new ListCommand());
        commands.put("prefix", new PrefixCommand());
        commands.put("ping", new PingCommand());
        
        commands.put("about", new AboutCommand());
        commands.put("status", new StatusCommand("status"));
        commands.put("uptime", new StatusCommand("uptime"));
        commands.put("support", new SupportCommand());
        
        // Moderation Commands
        commands.put("prune", new PruneCommand());
        commands.put("kick", new KickCommand());
        commands.put("warn", new WarnCommand());
        
        commands.put("ban", new BanCommand());
        commands.put("unban", new UnbanCommand());
        
        //Utility Commands
        commands.put("number", new NumberCommand());
        commands.put("num", new NumberCommand());
        commands.put("n", new NumberCommand());
        commands.put("math", new MathCommand());
        commands.put("calc", new MathCommand());
        
        commands.put("say", new SayCommand());
        commands.put("emoji", new EmojiCommand());
        commands.put("emote", new EmojiCommand());
        commands.put("e", new EmojiCommand());
        commands.put("weather", new WeatherCommand());
        commands.put("w", new WeatherCommand());
        
        commands.put("search", new SearchCommand("search"));
        commands.put("google", new SearchCommand("google"));
        commands.put("g", new SearchCommand("google"));
        commands.put("wiki", new SearchCommand("wiki"));
        commands.put("urban", new SearchCommand("ub"));
        commands.put("ub", new SearchCommand("ub"));
        commands.put("github", new SearchCommand("git"));
        commands.put("git", new SearchCommand("git"));
        commands.put("imdb", new IMDbCommand());
        
        commands.put("image", new ImageCommand("image"));
        commands.put("imgur", new ImageCommand("imgur"));
        commands.put("imgflip", new ImageCommand("imgflip"));
        commands.put("gif", new ImageCommand("gif"));
        commands.put("meme", new ImageCommand("meme"));
        
        //Fun Commands
        commands.put("8ball", new EightBallCommand());
        commands.put("face", new FaceCommand());
        commands.put("lenny", new FaceCommand());
        commands.put("f", new FaceCommand());
        commands.put("spam", new SpamCommand());
        commands.put("game", new GameCommand());
        commands.put("rockpaperscissors", new RPSCommand());
        commands.put("rps", new RPSCommand());
        commands.put("guessnum", new GuessNumberCommand());
        commands.put("gn", new GuessNumberCommand());
        commands.put("tictactoe", new TicTacToeCommand());
        commands.put("ttt", new TicTacToeCommand());
        commands.put("hangman", new HangManCommand());
        commands.put("hm", new HangManCommand());
        commands.put("hangmancheater", new HangManCheaterCommand());
        commands.put("hmc", new HangManCheaterCommand());
        
        // Music Commands
        commands.put("music", new MusicCommand());
        commands.put("m", new MusicCommand());
        commands.put("join", new JoinCommand());
        commands.put("summon", new JoinCommand());
        commands.put("j", new JoinCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("l", new LeaveCommand());
        commands.put("player", new PlayerCommand());
        commands.put("pl", new PlayerCommand());
        commands.put("play", new PlayCommand());
        commands.put("p", new PlayCommand());
        commands.put("fm", new FMCommand());
        commands.put("radio", new RadioCommand());
        commands.put("pause", new PauseCommand("pause"));
        commands.put("resume", new PauseCommand("resume"));
        commands.put("unpause", new PauseCommand("resume"));
        commands.put("skip", new SkipCommand());
        commands.put("nowplaying", new SongCommand());
        commands.put("song", new SongCommand());
        commands.put("np", new SongCommand());
        commands.put("queue", new QueueCommand());
        commands.put("q", new QueueCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("jump", new JumpCommand());
        commands.put("shuffle", new ShuffleCommand());
        commands.put("repeat", new RepeatCommand());
        commands.put("stop", new StopCommand());
        commands.put("dump", new DumpCommand());
        commands.put("lyrics", new LyricsCommand());
        
        //Restricted Commands
        commands.put("shutdown", new ShutDownCommand());
        commands.put("setNick", new PresenceCommand("setNick"));
        commands.put("setStatus", new PresenceCommand("setStatus"));
        commands.put("setGame", new PresenceCommand("setGame"));
        commands.put("source", new SourceCommand());
        commands.put("log", new LogCommand());
    }
}