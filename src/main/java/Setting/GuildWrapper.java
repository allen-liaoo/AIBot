/* 
 * AIBot by AlienIdeology
 * 
 * GuildWrapper
 * Custom settings per guild
 */
package Setting;

import Audio.AudioPlayerSendHandler;
import Audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildWrapper {
  /**
   * Audio player for the guild.
   */
  private final AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  private final TrackScheduler scheduler;
  /**
   * Binded Voice Channel for the guild.
   */
  private VoiceChannel vc;
  
  private TextChannel tc;

  /**
   * voteSkip The vote for skipping current song
   */
  private String guildId, prefix;
  
  /**
   * Creates a player and a track scheduler
   * @param manager Audio player manager to use for creating the player.
   * @param guildId Guild ID
   * @param prefix Custom Guild prefix
   */
  public GuildWrapper(AudioPlayerManager manager, String guildId, String prefix) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
    
    this.guildId = guildId;
    this.prefix = prefix;
  }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() 
    {
      return new AudioPlayerSendHandler(player);
    }

    public TrackScheduler getScheduler()
    {
        return scheduler;
    }

    public AudioPlayer getPlayer()
    {
        return player;
    }
    
    public VoiceChannel getVc() {
        return vc;
    }

    public void setVc(VoiceChannel vc) {
        this.vc = vc;
    }

    public TextChannel getTc() {
        return tc;
    }
    
    public void setTc(TextChannel tc) {
        this.tc = tc;
        scheduler.setTc(tc);
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    
}