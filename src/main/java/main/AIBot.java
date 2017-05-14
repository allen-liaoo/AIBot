/* 
 * AIBot by AlienIdeology
 * 
 * Main
 * Startup tasks, add commands, and bot configuration
 */
package main;

import audio.FM;
import audio.Radio;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import constants.Global;
import net.dv8tion.jda.core.OnlineStatus;
import secret.PrivateConstant;
import command.*;
import command.information.*;
import command.moderation.*;
import command.utility.*;
import command.music.*;
import command.fun.*;
import command.restricted.*;
import listener.*;
import system.AILogger;
import setting.*;
import utility.UtilBot;
import com.mashape.unirest.http.Unirest;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.entities.Game;

import java.util.ArrayList;
import java.util.HashMap;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AIBot {

    public static boolean isBeta = false;
    public static long timeStart = 0;

    public static List<Shard> shards = new ArrayList<>();

    public static AudioPlayerManager playerManager;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<>();
    public static final TextRespond respond = new TextRespond();
    private static APIPostAgent apiPoster;
    public static Radio radio = new Radio();
    public static FM fm = new FM();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        timeStart = System.currentTimeMillis();
        UtilBot.setUnirestCookie();
        musicStartup();

        String token = PrivateConstant.BOT_TOKEN;
        isBeta = !token.equals(PrivateConstant.BOT_TOKEN);

        for(int i = 0; i < Global.B_SHARDS; i++)  {
            shards.add(new Shard(i, token));
            System.out.println("Shard added: "+i);
        }

        addCommands();
        setGame(Game.of(Global.defaultGame()));
        if(!isBeta) startUp();
    }

    /**
     * Universal Main bot start up (Not beta)
     */
    private synchronized static void startUp() {
        /* Post API and Status */
        apiPoster = new APIPostAgent(shards, getGuilds().size()).postAllAPI();
        updateStatus();
    }

    /**
     * Universal Music startup
     */
    private synchronized static void musicStartup() {
        /* Load Source Managers */
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        /* Load FM and Radio */
        fm.loadDiscordFM();
        fm.loadLocalLibraries();
        radio.loadRadioStations();
    }

    public synchronized static void shutdown() throws IOException {
        System.out.println("Bot Shut Down Successfully");
        for(Shard shard : shards) {
            shard.getJda().shutdown(true);
        }
        Unirest.shutdown();
        System.exit(0);
    }

    public synchronized static void updateStatus() {
        Guild botServer = getGuild(Global.B_SERVER_ID);
        botServer.getTextChannelById(Global.B_SERVER_STATUS).
            editMessageById(Global.B_SERVER_STATUS_MSG, UtilBot.postStatus(botServer.getJDA()).build()).queue();
    }

    public synchronized static void setStatus(OnlineStatus status) {
        for(Shard shard : shards) {
            shard.getJda().getPresence().setStatus(status);
        }
    }

    public synchronized static void setGame(Game game) {
        for(Shard shard : shards) {
            shard.getJda().getPresence().setGame(game);
        }
    }

    public static Shard getShard(JDA jda) {
        for(Shard shard : shards) {
            if(shard.getJda().getShardInfo().getShardId() == jda.getShardInfo().getShardId())
                return shard;
        }
        return null;
    }

    public static Shard getShard(Guild guild) {
        for(Shard shard : shards) {
            for(GuildWrapper wrapper : shard.getGuilds().values()) {
                if(wrapper.getGuild().getId().equals(guild.getId()))
                    return shard;
            }
        }
        return null;
    }

    public static GuildWrapper getGuild(Guild guild) {
        for(Shard shard : shards) {
            if(shard.getGuild(guild) != null)
                return shard.getGuild(guild);
        }
        return null;
    }

    public static Guild getGuild(String guildID) {
        for(Shard shard : shards) {
            if(shard.getJda().getGuildById(guildID) != null)
                return shard.getJda().getGuildById(guildID);
        }
        return null;
    }

    public static List<Guild> getGuilds() {
        List<Guild> guilds = new ArrayList<>();
        for(Shard shard : shards) {
            guilds.addAll(shard.getJda().getGuilds());
        }
        return guilds;
    }
        
    private synchronized static void addCommands()
    {
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
        commands.put("l", new ListCommand());
        commands.put("prefix", new PrefixCommand());
        commands.put("ping", new PingCommand());
        
        commands.put("about", new AboutCommand());
        commands.put("status", new StatusCommand("status"));
        commands.put("uptime", new StatusCommand("uptime"));
        commands.put("support", new SupportCommand());
        
        // Moderation Commands
        commands.put("prune", new PruneCommand());
        commands.put("clean", new CleanCommand());
        commands.put("kick", new KickCommand());
        commands.put("warn", new WarnCommand());
        
        commands.put("ban", new BanCommand());
        commands.put("unban", new UnbanCommand());
        commands.put("softban", new SoftBanCommand());
        
        //utility Commands
        commands.put("number", new NumberCommand());
        commands.put("num", new NumberCommand());
        commands.put("n", new NumberCommand());
        commands.put("math", new MathCommand());
        commands.put("calc", new MathCommand());
        
        commands.put("say", new SayCommand());
        commands.put("discrim", new DiscrimCommand());
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
        commands.put("player", new PlayerCommand());
        commands.put("pl", new PlayerCommand());
        commands.put("play", new PlayCommand());
        commands.put("p", new PlayCommand());
        commands.put("fm", new FMCommand());
        commands.put("radio", new RadioCommand());
        commands.put("autoplay", new AutoPlayCommand());
        commands.put("ap", new AutoPlayCommand());
        commands.put("pause", new PauseCommand());
        commands.put("resume", new PauseCommand());
        commands.put("unpause", new PauseCommand());
        commands.put("ps", new PauseCommand());
        commands.put("skip", new SkipCommand());
        commands.put("previous", new PreviousCommand());
        commands.put("pre", new PreviousCommand());
        commands.put("move", new MoveCommand());
        commands.put("mv", new MoveCommand());
        commands.put("nowplaying", new SongCommand());
        commands.put("song", new SongCommand());
        commands.put("np", new SongCommand());
        commands.put("queue", new QueueCommand());
        commands.put("q", new QueueCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("jump", new JumpCommand());
        commands.put("jp", new JumpCommand());
        commands.put("shuffle", new ShuffleCommand());
        commands.put("sf", new ShuffleCommand());
        commands.put("repeat", new RepeatCommand());
        commands.put("rp", new RepeatCommand());
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