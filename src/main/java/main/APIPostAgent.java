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

    /* Discord Bots */
    private String DiscordBotsURL = "https://bots.discord.pw/api/bots/"+ PrivateConstant.BOT_ID + "/stats";

    /* Discord Bots List */
    private String DiscordBotsListURL = "https://discordbots.org/api/bots/"+ PrivateConstant.BOT_ID + "/stats";


    public APIPostAgent(List<Shard> shards) {
        this.shards = shards;
    }

    public APIPostAgent postAllAPI() {
        toDiscordBots();
        toDiscordBotsList();
        return this;
    }

    public void toDiscordBots() {
        try {
            String response = Unirest.post(DiscordBotsURL)
                   .header("Authorization", PrivateConstant.DiscordBotsToken)
                   .header("Content-Type ", "application/json")
                   .field("server_count", shards.get(0).getJda().getGuilds().size())
                   .asString().getBody();

            System.out.println("APIPostAgent#toDiscordBots --> Status text " + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void toDiscordBotsList() {
        try {
            String response = Unirest.post(DiscordBotsListURL)
                    .header("Authorization", PrivateConstant.DiscordBotsListToken)
                    .field("server_count", shards.get(0).getJda().getGuilds().size())
                    .asJson().getStatusText();

            System.out.println("APIPostAgent#toDiscordBotsList --> Status text" + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
