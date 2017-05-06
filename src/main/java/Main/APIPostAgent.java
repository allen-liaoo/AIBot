package Main;

import Private.Private;
import Utility.UtilBot;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.JDA;

/**
 * Created by liaoyilin on 5/4/17.
 */
public class APIPostAgent {

    private JDA jda;

    /* Discord Bots */
    private String DiscordBotsURL = "https://bots.discord.pw/api/bots/"+Private.BOT_ID+"/stats";

    /* Discord Bots List */
    private String DiscordBotsListURL = "https://discordbots.org/api/bots/"+Private.BOT_ID+"/stats";


    public APIPostAgent(JDA jda) {
        this.jda = jda;

        if(!Main.isBeta)
            postAllAPI();
    }

    public void postAllAPI() {
        toDiscordBots();
        toDiscordBotsList();
    }

    public void toDiscordBots() {
        try {
            String response = Unirest.post(DiscordBotsURL)
                   .header("Authorization", Private.DiscordBotsToken)
                   .header("Content-Type ", "application/json")
                   .field("server_count", jda.getGuilds().size())
                   .asString().getBody();

            System.out.println("APIPostAgent#toDiscordBots --> Status text " + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void toDiscordBotsList() {
        try {
            String response = Unirest.post(DiscordBotsListURL)
                    .header("Authorization", Private.DiscordBotsListToken)
                    .field("server_count", jda.getGuilds().size())
                    .asJson().getStatusText();

            System.out.println("APIPostAgent#toDiscordBotsList --> Status text" + response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
