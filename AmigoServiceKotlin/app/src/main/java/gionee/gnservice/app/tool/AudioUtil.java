package gionee.gnservice.app.tool;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
public class AudioUtil {

    public static void playMusic(Context context, int resId) {
        MediaPlayer player = MediaPlayer.create(context, resId);
        player.start();
    }
}
