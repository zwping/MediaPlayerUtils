package win.zwping.mediaplayer_lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>describe：mediaPlayer集合管理器
 * <p>    note：如果需要集合中其余mediaPlayer暂停或停止操作，需要使用改utils进行操作
 * <p>  @author：zwp on 2017/10/19 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
public class MpListUtils {

    private static MpListUtils instance = null;
    private MpListUtils() {
    }
    public static MpListUtils getInstance() {
        if (null == instance) {
            instance = new MpListUtils();
        }
        return instance;
    }

    private List<MediaPlayerUtils> list = new ArrayList<>();

    /**
     * 获取mp对象
     * @return
     */
    public MediaPlayerUtils getMediaPlayerUtils() {
        list.add(new MediaPlayerUtils());
        return list.get(list.size() - 1);
    }

    public void start(MediaPlayerUtils mp) {
        pauseOtherMp(mp);
        mp.start();
    }

    public void seekTo(MediaPlayerUtils mp, int mesc) {
        pauseOtherMp(mp);
        mp.seekTo(mesc);
    }

    public void pauseAllMp() {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).pause();
            }
        }
    }
    private void pauseOtherMp(MediaPlayerUtils mediaPlayerUtils) {
        for (int i = 0; i < list.size(); i++) {
            MediaPlayerUtils mp = list.get(i);
            if (mp != mediaPlayerUtils && mp.isPlayer()) {
                mp.pause();
            }
        }
    }
    public void stopAllMp() {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).stop();
            }
        }
    }
}
