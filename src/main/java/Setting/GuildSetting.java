/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Setting;

import Audio.AudioPlayerSendHandler;
import Audio.TrackScheduler;
import Audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildSetting {
  /**
   * Audio player for the guild.
   */
  public static AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  /**
   * @param voteSkip The vote for skipping current song
   */
  private String guildId, prefix;
  private int voteSkip;
  
  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   * @param guildId Guild ID
   * @param prefix Custom Guild prefix
   * @param volume Custom Guild Music player volume
   */
  public GuildSetting(AudioPlayerManager manager, String guildId, String prefix, int volume) {
    player = manager.createPlayer();
    player.setVolume(volume);
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
    
    this.guildId = guildId;
    this.prefix = prefix;
    voteSkip = 0;
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

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    
    public int getVoteSkip()
    {
        return voteSkip;
    }
    
    public void addVoteSkip()
    {
        voteSkip++;
    }
}