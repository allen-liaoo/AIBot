/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

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
public class GuildMusicManager {
  /**
   * Audio player for the guild.
   */
  public static AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
  }

  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */
  public AudioPlayerSendHandler getSendHandler() {
    return new AudioPlayerSendHandler(player);
  }
}