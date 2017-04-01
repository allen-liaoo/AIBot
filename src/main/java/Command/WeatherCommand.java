/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import Config.Emoji;
import Config.Info;
import Config.Prefix;
import Main.*;
import com.github.fedy2.weather.*;
import com.github.fedy2.weather.data.*;
import com.github.fedy2.weather.data.unit.*;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.addField("BotInfo -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
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
                
                //General Info
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
                double temper = city.getItem().getCondition().getTemp() * 1.8 + 32;
                tempF = temper + "°F";
                
                String EmojiCon = setConditionEmoji(concode);
                
                embedw.setAuthor("Weather of " + title, link, Info.I_info);
                embedw.setColor(Color.blue);
                embedw.setThumbnail(null);
                embedw.setTimestamp(Instant.now());
                embedw.setFooter("Weather of " + title + " | " + date, null);
                embedw.setTimestamp(null);

                embedw.addField(Emoji.E_temp + "Temperature", temp + "/" + tempF, true);
                embedw.addField(Emoji.E_wind + "Wind", wind, true);
                embedw.addField(Emoji.E_sweat + "Humidity", humidity, true);
                embedw.addField(Emoji.E_press + "Pressure", pressure, true);
                embedw.addField(Emoji.E_eyes + "Visibility", visibility, true);
                embedw.addField(EmojiCon + "Condition", condes, true);
                
                MessageEmbed mew = embedw.build();
                
                e.getChannel().sendMessage(mew).queue();
                embedw.clearFields();
            } catch (JAXBException ex) {
                System.out.println("JAXBException: An error occurs parsing the response.");
                Logger.getLogger(WeatherCommand.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("IOException: If an error occurs communicating with the service.");
                Logger.getLogger(WeatherCommand.class.getName()).log(Level.SEVERE, null, ex);
            }

            
        }
        
        else if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    public String setConditionEmoji(int code)
    {
        String condition = "";
        switch (code) {
            //Sunny
            case 32: condition = Emoji.E_sunny;
                break;
                
            //Cloudy
            case 26:
            case 27:
            case 28:
                condition = Emoji.E_cloudy;
                break;
            
            //Partly Cloudy
            case 29:
            case 30:
            case 44:
                condition = Emoji.E_cloud_part;
                break;
                
            //Rain 1
            case 6:
            case 10:
            case 17:
            case 35:
            case 40:
                condition = Emoji.E_cloudy_rain;
                break;
                
            //Rain 2
            case 5:
            case 7:
            case 9:
            case 11:
            case 12:
            case 18:
                condition = Emoji.E_cloud_rain;
                break;
                
            //Thunder Storm
            case 3:
            case 4:
            case 37:
            case 38:
            case 39:
            case 45:
            case 47:
                condition = Emoji.E_cloud_thunder_rain;
                break;
                
            //Snow
            case 13:
            case 14:
            case 15:
            case 16:
            case 46:
                condition = Emoji.E_snow;
                break;
                
            //Tornado
            case 0:
            case 1:
            case 2:
                condition = Emoji.E_cloud_tornado;
                break;
                
            //Dusty, windy, foggy, smoke
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
                condition = Emoji.E_windy;
                break;
                
            case 21:
            case 25:
            case 41:
            case 42:
                condition = Emoji.E_snowman;
                break;
                
            //Hot
            case 36:
                condition = Emoji.E_error;
                
            default: condition = Emoji.E_cloud;
                break;
                
        }
        
        return condition;
    }
}
