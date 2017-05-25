package command.fun;

import command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;
import utility.UtilBot;
import utility.UtilNum;
import utility.WebGetter;

import java.util.List;

/**
 * @author AlienIdeology
 */
public class AsciiCommand extends Command {
    public final static String HELP = "Turn plain text into Ascii Arts!\n"
            + "Command Usage: `" + Prefix.getDefaultPrefix() + "ascii`\n"
            + "Parameter: `-h | [Text] | null`\n"
            + "[Text]: The text to turn into ascii art. Fonts are chosen at random.\n"
            + "null: Get a list of ascii fonts.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Fun Module", null);
        embed.addField("Ascii -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if (args.length == 0) {
            e.getChannel().sendMessage(new EmbedBuilder().setColor(UtilBot.randomColor())
                .setDescription("Here is a **[full list of Ascii Fonts]("+WebGetter.asciiArtUrl+"fonts_list)**. They will be chosen randomly.").build()).queue();
        } else {
            String input = "";
            for (int i = 0; i < args.length; i++) { input += i==args.length-1?args[i]:args[i]+" "; }

            List<String> fonts = WebGetter.getAsciiFonts();
            String font = fonts.get(UtilNum.randomNum(0, fonts.size() - 1));

            try {
                String ascii = WebGetter.getAsciiArt(input, font);

                if (ascii.length()>1900) {
                    e.getChannel().sendMessage("```fix\n\nThe ascii text is too large ;-;```").queue();
                    return;
                }

                e.getChannel().sendMessage("**Font:** "+font+"\n```fix\n\n"+ascii+"```").queue();
            } catch (IllegalArgumentException iae) {
                e.getChannel().sendMessage("```fix\n\nYour text contains unknown characters!```").queue();
            }
        }
    }
}
