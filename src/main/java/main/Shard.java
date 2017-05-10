package main;

import constants.Global;
import listener.BotListener;
import listener.CommandListener;
import listener.GuildListener;
import listener.SelectorListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import setting.MessageFilter;
import system.AILogger;
import utility.UtilBot;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

/**
 * Created by liaoyilin on 5/9/17.
 */
public class Shard {

    private JDA jda;
    private final int ID;
    private final String token;
    public HashMap<String, GuildWrapper> guilds = new HashMap<>();

    public Shard(int shardID, String token) {
        this.token = token;
        buildShard();
        ID = shardID;
    }

    public void buildShard()
    {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new BotListener(), new MessageFilter(),
                            new GuildListener(), new CommandListener(), new SelectorListener())
                    .setAutoReconnect(true)
                    .setMaxReconnectDelay(300)
                    .setEnableShutdownHook(true)
                    .buildBlocking();
            shardStartUp();

        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            AILogger.updateLog("Exception thrown while building shard: "+ID);
            System.out.println("Exception on Shard: "+ID);
            e.printStackTrace();
        }
    }

    public void shardStartUp()
    {
        /* Add guilds to GuildWrapper */
        for(Guild g : jda.getGuilds()) {
            GuildWrapper newGuild = new GuildWrapper(jda, AIBot.playerManager, g.getId(), "=");
            guilds.put(g.getId(), newGuild);
            g.getAudioManager().setSendingHandler(newGuild.getSendHandler());
        }
        UtilBot.setUnirestCookie();
    }

    public void revive()
    {
        guilds.clear();
        jda.shutdown(false);
        buildShard();
    }

    public JDA getJda() {
        return jda;
    }

    public int getID() {
        return ID;
    }

    public HashMap<String, GuildWrapper> getGuilds() {
        return guilds;
    }

    public GuildWrapper getGuild(Guild guild)
    {
        return guilds.get(guild.getId());
    }

}
