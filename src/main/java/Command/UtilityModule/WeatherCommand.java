/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Utility.AILogger;
import com.github.fedy2.weather.*;
import com.github.fedy2.weather.data.*;
import com.github.fedy2.weather.data.unit.*;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import javax.xml.bind.JAXBException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class WeatherCommand implements Command{

    public final static  String HELP = "This command is for getting the weather of a specific city.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"weather`\n"
                                     + "Parameter: `-h | City Name | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedw = new EmbedBuilder();
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.addField("Weather -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        
        String title, link, wind, atmos, con;
        
        //Wind
        String chill, direction, speed;
        
        //Atmosphere
        String humidity, pressure, visibility;
        
        //Condition
        String temp, tempF, condes, date;
        int concode;
        
        if(args.length > 0 && !"-h".equals(args[0]))
        {
            String input = "";
            for(int i = 0; i < args.length; i++)
            {
                input += args[i];
            }
            
            try {
                
                YahooWeatherService service = new YahooWeatherService();
                YahooWeatherService.LimitDeclaration limit = service.getForecastForLocation(input, DegreeUnit.CELSIUS);
                
                List<Channel> list = limit.first(1);
                Channel city = list.get(0);
                
                //General Constants
                title = city.getTitle();
                title = title.substring(16);
                link = city.getLink();
                
                //Wind
                chill = city.getWind().getChill() + "°"; //Wind Chill in degrees
                direction = city.getWind().getDirection() + "°"; //Wind direction in degrees
                speed = city.getWind().getSpeed() + " km/h"; //Wind speed
                wind = " Chill: " + chill + "\nDirection: " + direction + "\nSpeed: " + speed;
                
                //Atmosphere
                humidity = city.getAtmosphere().getHumidity() + "%"; //Humidity in percents
                double pRound = city.getAtmosphere().getPressure();
                pressure = String.format("%.0f", pRound) + " psi"; //Pressure
                visibility = city.getAtmosphere().getVisibility()/100 + " miles"; //Visibility (Actuall value *100) 
                //I.e. visibility 16.5 miles will be specified as 1650.
                
                //Condition
                condes = city.getItem().getCondition().getText();
                concode = city.getItem().getCondition().getCode();
                temp = city.getItem().getCondition().getTemp() + "°C";
                date = city.getItem().getCondition().getDate().toString();
                
                // Temperature in degree F
                double temper = (double) Math.round((city.getItem().getCondition().getTemp() * 1.8 + 32)*100)/100;
                tempF = temper + "°F";
                
                String EmojiCon = setConditionEmoji(concode);
                
                embedw.setAuthor("Weather of " + title, link, Constants.I_INFO);
                embedw.setColor(Color.blue);
                embedw.setThumbnail(null);
                embedw.setTimestamp(Instant.now());
                embedw.setFooter("Weather of " + title + " | " + date, null);
                embedw.setTimestamp(null);

                embedw.addField(Emoji.TEMP + "Temperature", temp + "/" + tempF, true);
                embedw.addField(Emoji.WIND + "Wind", wind, true);
                embedw.addField(Emoji.SWEAT + "Humidity", humidity, true);
                embedw.addField(Emoji.PRESS + "Pressure", pressure, true);
                embedw.addField(Emoji.EYES + "Visibility", visibility, true);
                embedw.addField(EmojiCon + "Condition", condes, true);
                
                MessageEmbed mew = embedw.build();
                
                e.getChannel().sendMessage(mew).queue();
                embedw.clearFields();
            } catch (JAXBException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "Occurs parsing the response.");
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "Occurs communicating with the service.");
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " No result.").queue();
                AILogger.errorLog(ioobe, e, this.getClass().getName(), "Could not get an variable.");
            }

            
        }
        
        else if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
    }

    
    public String setConditionEmoji(int code)
    {
        String condition = "";
        switch (code) {
            //Sunny
            case 34:
            case 32: 
                condition = Emoji.SUNNY;
                break;
                
            //Cloudy
            case 26:
            case 27:
            case 28:
                condition = Emoji.CLOUDY;
                break;
            
            //Partly Cloudy
            case 29:
            case 30:
            case 44:
                condition = Emoji.CLOUD_PART;
                break;
                
            //Rain 1
            case 6:
            case 10:
            case 17:
            case 35:
            case 40:
                condition = Emoji.CLOUDY_RAIN;
                break;
                
            //Rain 2
            case 5:
            case 7:
            case 9:
            case 11:
            case 12:
            case 18:
                condition = Emoji.CLOUD_RAIN;
                break;
                
            //Thunder Storm
            case 3:
            case 4:
            case 37:
            case 38:
            case 39:
            case 45:
            case 47:
                condition = Emoji.CLOUD_THUNDER_RAIN;
                break;
                
            //Snow
            case 13:
            case 14:
            case 15:
            case 16:
            case 46:
                condition = Emoji.SNOW;
                break;
                
            //Tornado
            case 0:
            case 1:
            case 2:
                condition = Emoji.CLOUD_TORNADO;
                break;
                
            //Dusty, WINDY, foggy, smoke
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
                condition = Emoji.WINDY;
                break;
                
            case 21:
            case 25:
            case 41:
            case 42:
                condition = Emoji.SNOWMAN;
                break;
                
            //Hot
            case 36:
                condition = Emoji.ERROR;
                
            default: condition = Emoji.CLOUD;
                break;
                
        }
        
        return condition;
    }
}
