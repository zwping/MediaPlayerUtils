package win.zwping.mediaplayer_lib;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * // TODO: 2017/10/18 怎么做到播放记录的保存（媒体流的唯一值）
 * <p>describe：基于voice封装的mediaPlayer
 * <p>    note：列表播放请使用{@link MpListUtils#getMediaPlayerUtils()}获取对象
 * <p> @author：zwp on 2017/10/18 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
public class MediaPlayerUtils implements Runnable, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener {

    public MediaPlayerUtils() {
    }

    private static final int DELAY_MILLIS = 10;
    private MediaPlayer mediaPlayer;
    /**
     * 资源是否准备完成
     */
    private boolean isPrepare;
    /**
     * 自动播放
     */
    private boolean autoPlay;
    /**
     * 紧一次自动开始播放
     */
    private boolean onlyAutoStartPlay;
    /**
     * 搜索完成后是否自动播放
     */
    private boolean seekCompleterAutoPlay;

    /**
     * 定时器任务，用于获取当前进度
     */
    private Handler handler;

    private OnPlayerStateListener mOnPlayerStateListener;
    private OnSeekListener mOnSeekListener;
    private OnErrorListener mOnErrorListener;
    private OnCompletionListener mOnCompletionListener;
    private OnBufferingListener mOnBufferingListener;
    private OnPlayerProgressListener mOnPlayerProgressListener;

    private boolean getMpUnNull() {
        return null != mediaPlayer;
    }

    /**
     * 设置流媒体资源
     *
     * @param url        http / rtsp
     * @param streamType 流媒体类型 {@link android.media.AudioManager#STREAM_MUSIC}
     */
    public void setData(@NonNull String url, @NonNull int streamType) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(streamType);
            mediaPlayer.prepareAsync();
            isPrepare = false;
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnInfoListener(this);
        } catch (IOException e) {
            if (null != mOnErrorListener) {
                mOnErrorListener.onError(mediaPlayer, -1, -1);
            }
        }
    }


    public void start() {
        if (getMpUnNull()) {
            if (getPrepared()) {
                mediaPlayer.start();
                startPoolTimer();
            } else {
                onlyAutoStartPlay = true;
                if (null != mOnBufferingListener) {
                    mOnBufferingListener.onBufferStart(mediaPlayer);
                }
            }
            if (null != mOnPlayerStateListener) {
                mOnPlayerStateListener.onMediaPlayerState(true);
            }
        }
    }

    public void pause() {
        if (getMpUnNull() && getPrepared() && isPlayer()) {
            mediaPlayer.pause();
        }
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onMediaPlayerState(false);
        }
        stopPoolTimer();
    }

    public void stop() {
        if (getMpUnNull() && getPrepared()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopPoolTimer();
    }

    public boolean isPlayer() {
        return getMpUnNull() && getPrepared() && mediaPlayer.isPlaying();
    }

    /**
     * 资源是否准备完成
     *
     * @return
     */
    public boolean getPrepared() {
        return isPrepare;
    }

    /**
     * 设置搜索完成后是否自动播放 <br />
     * 建议在seekBar中onStartTrackingTouch第一位使用 <br />
     * 在{@link #pause()}前面使用
     *
     * @param auto 是否需要自动播放 <br />
     *             false 试isPlayer而定 <br />
     *             true seekCompleter后自动播放
     */
    public void setSeekCompleterAutoPlay(boolean auto) {
        seekCompleterAutoPlay = auto || isPlayer();
    }

    /**
     * 搜索指定位置
     *
     * @param msec
     */
    public void seekTo(int msec) {
        if (getMpUnNull() && getPrepared() && msec <= mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(msec);
        } else {
            if (null != mOnSeekListener) {
                mOnSeekListener.onSeekError(mediaPlayer);
            }
        }
    }

    public void setLooping(boolean looping) {
        if (getMpUnNull()) {
            mediaPlayer.setLooping(looping);
        }
    }

    public boolean isLooping() {
        return getMpUnNull() && mediaPlayer.isLooping();
    }

    public void setSeekListener(OnSeekListener listener) {
        this.mOnSeekListener = listener;
    }

    public void setErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setBufferingListener(OnBufferingListener listener) {
        this.mOnBufferingListener = listener;
    }

    /**
     * 动态设置view对应的seekBar
     *
     * @param listener
     */
    public void setPlayerProgressListener(OnPlayerProgressListener listener) {
        this.mOnPlayerProgressListener = listener;
    }

    /**
     * 动态设置view对应的状态
     *
     * @param listener
     */
    public void setPlayerStateListener(OnPlayerStateListener listener) {
        this.mOnPlayerStateListener = listener;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (seekCompleterAutoPlay) {
            start();
        }
        if (null != mOnSeekListener) {
            mOnSeekListener.onSeekComplete(mp);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepare = true;
        if (null != mOnBufferingListener) {
            mOnBufferingListener.onBufferCompletion(mp);
        }
        if (autoPlay || onlyAutoStartPlay) {
            onlyAutoStartPlay = false;
            mediaPlayer.start();
            startPoolTimer();
        }
        if (null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onDuration(mediaPlayer.getDuration());
        }
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onPrepared();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPoolTimer();
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onMediaPlayerState(false);
        }
        if (null != mOnCompletionListener) {
            mOnCompletionListener.onCompletion(mp);
        }
        if (null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onProgress(mediaPlayer.getDuration());
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (null != mOnBufferingListener) {
            mOnBufferingListener.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            //错误交叉
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                if (null != mOnErrorListener) {
                    mOnErrorListener.onError(mp, -1, -1);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (null != mOnBufferingListener) {
                    mOnBufferingListener.onBufferStart(mp);
                }
                stopPoolTimer();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (null != mOnBufferingListener) {
                    mOnBufferingListener.onBufferCompletion(mp);
                }
                startPoolTimer();
                break;
            //媒体信息不可搜索
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                if (null != mOnSeekListener) {
                    mOnSeekListener.onSeekError(mp);
                }
                break;
            //开始渲染第一帧  //voice不会执行这个方法
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                if (null != mOnPlayerProgressListener) {
                    mOnPlayerProgressListener.onDuration(mp.getDuration());
                }
                break;
            default:
                break;
        }
        System.out.println("啥问题啊？" + what + "---" + extra);
        return false;
    }

    @Override
    public void run() {
        int current = mediaPlayer.getCurrentPosition();
        if (getMpUnNull() && null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onProgress(current);
        }
        handler.postDelayed(this, DELAY_MILLIS);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        stopPoolTimer();
        if (getMpUnNull()) {
            mediaPlayer.reset();
        }
        stop();
        if (null != mOnErrorListener) {
            mOnErrorListener.onError(mp, what, extra);
        }
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onMediaPlayerState(false);
        }
        System.out.println("出错：" + what + "---" + extra);
        return true;
    }

    /**
     * 开始定时任务
     */
    private void startPoolTimer() {
        if (null == handler) {
            handler = new Handler();
        }
        stopPoolTimer();
        handler.postDelayed(this, DELAY_MILLIS);
    }

    /**
     * 停止定时任务
     */
    private void stopPoolTimer() {
        if (null != handler) {
            handler.removeCallbacks(this);
        }
    }

    public interface OnErrorListener {
        /**
         * 播放错误
         *
         * @param mp
         * @param what
         * @param extra
         */
        void onError(MediaPlayer mp, int what, int extra);
    }

    public interface OnCompletionListener {
        /**
         * 播放完成
         *
         * @param mp
         */
        void onCompletion(MediaPlayer mp);
    }

    public interface OnBufferingListener {
        /**
         * 缓冲开始
         *
         * @param mp
         */
        void onBufferStart(MediaPlayer mp);

        /**
         * 缓冲中
         *
         * @param mp
         * @param percent 缓冲百分比
         */
        void onBufferingUpdate(MediaPlayer mp, int percent);

        /**
         * 缓冲完成
         *
         * @param mp
         */
        void onBufferCompletion(MediaPlayer mp);
    }

    public interface OnPlayerProgressListener {
        /**
         * 总时间
         *
         * @param duration
         */
        void onDuration(int duration);

        /**
         * 当前播放进度
         *
         * @param progress
         */
        void onProgress(int progress);
    }

    public interface OnPlayerStateListener {
        /**
         * 播放状态
         *
         * @param play 播放 / 未播放
         */
        void onMediaPlayerState(boolean play);

        /**
         * view对mp的操作全部放在prepared后
         */
        void onPrepared();
    }

    public interface OnSeekListener {
        /**
         * 搜索完成
         *
         * @param mp
         */
        void onSeekComplete(MediaPlayer mp);

        /**
         * 搜索失败
         *
         * @param mp
         */
        void onSeekError(MediaPlayer mp);
    }
}
