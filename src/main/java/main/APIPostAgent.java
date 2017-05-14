package main;

import secret.PrivateConstant;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.JDA;

import java.util.List;

/**
 * Created by liaoyilin on 5/4/17.
 */
public class APIPostAgent {

    private List<Shard> shards;
    private int shardCount;
    private int serverCount;

    /* Discord Bots */
    private String DiscordBotsURL = "https://bots.discord.pw/api/bots/"+ PrivateConstant.BOT_ID + "/stats";

    /* Discord Bots List */
    private String DiscordBotsListURL = "https://discordbots.org/api/bots/"+ PrivateConstant.BOT_ID + "/stats";

    /* DiscordList Bots */
    private String DiscordListBots = "https://bots.discordlist.net/api";

    public APIPostAgent(List<Shard> shards, int serverCount) {
        this.shards = shards;
        this.shardCount = shards.size();
        this.serverCount = serverCount;
    }

    public APIPostAgent postAllAPI() {
        toDiscordBots()
            .toDiscordBotsList()
            .toDiscordListBots();
        return this;
    }

    private APIPostAgent toDiscordBots() {
        try {
            String response = Unirest.post(DiscordBotsURL)
                   .header("Authorization", PrivateConstant.DiscordBotsToken)
                   .header("Content-Type ", "application/json")
                   .field("server_count", serverCount)
                   .asString().getBody();

            System.out.println("APIPostAgent#toDiscordBots --> " + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return this;
    }

    private APIPostAgent toDiscordBotsList() {
        try {
            String response = Unirest.post(DiscordBotsListURL)
                    .header("Authorization", PrivateConstant.DiscordBotsListToken)
                    .field("server_count", serverCount)
                    .asJson().getStatusText();

            System.out.println("APIPostAgent#toDiscordBotsList --> " + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return this;
    }

    private APIPostAgent toDiscordListBots() {
        try {
            String response = Unirest.post(DiscordListBots)
                    .header("Authorization", PrivateConstant.DiscordListBotsToken)
                    .field("token", PrivateConstant.DiscordListBotsToken)
                    .field("servers", serverCount)
                    .asString().getStatusText();

            System.out.println("APIPostAgent#toDiscordList_Bots --> " + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return this;
    }

}
