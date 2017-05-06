package Audio;

import Utility.UtilString;

/**
 * Created by liaoyilin on 5/5/17.
 */
public enum PlayerMode {
    DEFAULT,    //Default Mode, nothing playing

    /* Modes that can be override */
    NORMAL,     //Normal Mode, playing track radio, or playlist

    /* Overriding Mode */
    AUTO_PLAY,  //AutoPlay Mode, play the next song from YouTube AutoPlay mode
    REPEAT,     //Repeat Mode, retrieve the first queue and add to the last
    REPEAT_SINGLE,     //Repeat Single Mode, repeat the single song

    /* Unique Modes */
    FM;         //FM Mode, play automatic playlist

    @Override
    public String toString() {
        return UtilString.VariableToString("_", name());
    }

    /* Check if the player... */
    public boolean canAutoPlay() {
        return this == AUTO_PLAY || this == NORMAL || this == DEFAULT;
    }
    public boolean canRepeat() {
        return this == REPEAT || this == NORMAL || this == DEFAULT;
    }
    public boolean canRepeatSingle() {
        return this == REPEAT_SINGLE || this == NORMAL || this == DEFAULT;
    }
    public boolean canFM() {
        return this == FM || this == DEFAULT;
    }
}
