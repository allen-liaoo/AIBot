package command.restricted;

import command.Command;
import constants.Emoji;
import constants.Global;
import main.AIBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;
import utility.UtilBot;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.Instant;
import java.util.concurrent.*;


/**
 * @author AlienIdeology
 */
public class EvalCommand extends Command {

    public final static String HELP = "This command is for evaluating a java code snippet using Nashorn javascript engine."
                                    + "This commands is only for `Bot Owner` only.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"eval`\n"
                                    + "Parameter: `-h | [Script] | [Script Code Block] | null`\n"
                                    + "[Script Code Block]: Accepts codes inside a code block. ```java\n\n[Script]```";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Restricted Module", null);
        embed.addField("Eval -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        if (args.length == 0) {
            e.getChannel().sendMessage(help(e).build()).queue();
        } else if (e.getAuthor().getId().equals(Global.D_ID)){

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

            /* Imports */
            try {
                engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util);");
            } catch (ScriptException ex) {
                ex.printStackTrace();
            }

            /* Put string representations */
            engine.put("aibot", AIBot.class);
            engine.put("jda", e.getJDA());
            engine.put("api", e.getJDA());

            engine.put("message", e.getMessage());
            engine.put("guild", e.getGuild());
            engine.put("server", e.getGuild());
            engine.put("channel", e.getChannel());
            engine.put("vc", e.getMember().getVoiceState().getChannel());
            engine.put("player", AIBot.getGuild(e.getGuild()).getPlayer());
            engine.put("gp", AIBot.getGuild(e.getGuild()).getGuildPlayer());

            engine.put("author", e.getAuthor());
            engine.put("member", e.getMember());
            engine.put("self", e.getGuild().getSelfMember().getUser());
            engine.put("selfmem", e.getGuild().getSelfMember());

            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

            ScheduledFuture<?> future = service.schedule(() -> {

                /* Initialize Objects */
                long startExec = System.currentTimeMillis();
                Object out = null;
                EmbedBuilder message = new EmbedBuilder()
                    .setColor(UtilBot.randomColor()).setAuthor("AIBot Eval", null, e.getGuild().getSelfMember().getUser().getEffectiveAvatarUrl())
                    .setFooter("Bot Owner " + e.getMember().getEffectiveName() + " Only", e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(Instant.now());

                try {
                    /* Build input script */
                    String script = "";
                    for (int i = 0; i < args.length; i++) {
                        args[i] = args[i].replace("```java", "").replace("```", "");
                        script += i == args.length-1 ? args[i]:args[i]+" ";
                    }
                    message.addField(Emoji.IN + " Input", "```java\n\n" + script + "```", false);

                    /* Output */
                    out = engine.eval(script);
                    message.addField(Emoji.OUT + " Output", "```java\n\n" + out.toString() + "```", false);

                /* Exception */
                } catch (Exception ex) {
                    message.addField(Emoji.ERROR + " Error", "```java\n\n" + ex.getMessage() + "```", false);
                }

                /* Timer */
                message.addField(Emoji.STOPWATCH + " Timing", System.currentTimeMillis()-startExec + " milliseconds", false);
                e.getChannel().sendMessage(message.build()).queue();

                service.shutdownNow();

            }, 0, TimeUnit.MILLISECONDS);

        } else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for `Bot Owner` only!").queue();
        }

    }

}
