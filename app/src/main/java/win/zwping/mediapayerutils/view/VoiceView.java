package win.zwping.mediapayerutils.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import win.zwping.mediapayerutils.R;
import win.zwping.mediaplayer_lib.MediaPlayerUtils;
import win.zwping.mediaplayer_lib.MpListUtils;


/**
 * <p>describe：
 * <p>    note：
 * <p>  @author：zwp on 2017/10/19 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
public class VoiceView extends RelativeLayout implements View.OnClickListener, MediaPlayerUtils.OnPlayerProgressListener, MediaPlayerUtils.OnBufferingListener, MediaPlayerUtils.OnCompletionListener, MediaPlayerUtils.OnErrorListener, SeekBar.OnSeekBarChangeListener, MediaPlayerUtils.OnPlayerStateListener {

    public VoiceView(Context context) {
        super(context);
        initView();
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private AppCompatSeekBar mSeekBar;
    private TextView mCurrent;
    private TextView mDuration;
    private ImageView mPlay;
    private RelativeLayout mBuffer;
    private TextView mPercent;
    private RelativeLayout mError;

    private MediaPlayerUtils mediaPlayer;
    private MpListUtils mpListUtils;

    private void initView() {
        inflate(getContext(), R.layout.view_voice_layout, this);
        mCurrent = (TextView) findViewById(R.id.current);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        mDuration = (TextView) findViewById(R.id.duration);
        mBuffer = (RelativeLayout) findViewById(R.id.buffer);
        mError = (RelativeLayout) findViewById(R.id.error);
        mPercent = (TextView) findViewById(R.id.percent);
        mPlay = (ImageView) findViewById(R.id.play);
        mSeekBar.setEnabled(false);
        mPlay.setEnabled(false);
        mPlay.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

        mpListUtils = MpListUtils.getInstance();

        mediaPlayer = mpListUtils.getMediaPlayerUtils();
        mediaPlayer.setPlayerProgressListener(this);
        mediaPlayer.setBufferingListener(this);
        mediaPlayer.setCompletionListener(this);
        mediaPlayer.setErrorListener(this);
        mediaPlayer.setPlayerStateListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (mediaPlayer.isPlayer()) {
                    mediaPlayer.pause();
                } else {
                    mpListUtils.start(mediaPlayer);
                }
                break;
            default:
                break;
        }
    }

    public void setData(String url) {
        //这里没有使用自动播放，如果需要自动播放的话mediaPlayer也需要listUtils统一管理
        mediaPlayer.setData(url, AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onDuration(int duration) {
        mDuration.setText(long2String(duration));
        mSeekBar.setMax(duration);
    }

    @Override
    public void onProgress(int progress) {
        mCurrent.setText(long2String(progress));
        mSeekBar.setProgress(progress);
    }

    @Override
    public void onBufferStart(MediaPlayer mp) {
        mBuffer.setVisibility(VISIBLE);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mPercent.setText(percent + "%");
    }

    @Override
    public void onBufferCompletion(MediaPlayer mp) {
        mBuffer.setVisibility(GONE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onError(MediaPlayer mp, int what, int extra) {
        System.out.println("音频出错");
        mError.setVisibility(VISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarProgress = progress;
        mCurrent.setText(long2String(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.setSeekCompleterAutoPlay(true);
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mpListUtils.seekTo(mediaPlayer, seekBarProgress);
    }

    private int seekBarProgress;

    @Override
    public void onMediaPlayerState(boolean play) {
        mPlay.setSelected(play);
    }

    @Override
    public void onPrepared() {
        mSeekBar.setEnabled(true);
        mPlay.setEnabled(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mediaPlayer.pause();
    }

    private String long2String(long time) {
        if (time <= 0) {
            return "00:00";
        }
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }
}
