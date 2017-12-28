package win.zwping.mediapayerutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import win.zwping.mediapayerutils.view.VoiceView;
import win.zwping.mediaplayer_lib.MpListUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VoiceView voice = (VoiceView) findViewById(R.id.voice);
        VoiceView voice1 = (VoiceView) findViewById(R.id.voice1);
        VoiceView voice2 = (VoiceView) findViewById(R.id.voice2);
        VoiceView voice3 = (VoiceView) findViewById(R.id.voice3);
        VoiceView voice4 = (VoiceView) findViewById(R.id.voice4);
        VoiceView voice5 = (VoiceView) findViewById(R.id.voice5);
        VoiceView voice6 = (VoiceView) findViewById(R.id.voice6);
        VoiceView voice7 = (VoiceView) findViewById(R.id.voice7);
        VoiceView voice8 = (VoiceView) findViewById(R.id.voice8);
        VoiceView voice9 = (VoiceView) findViewById(R.id.voice9);
        VoiceView voice10 = (VoiceView) findViewById(R.id.voice10);
        VoiceView voice11 = (VoiceView) findViewById(R.id.voice11);
        VoiceView voice12 = (VoiceView) findViewById(R.id.voice12);
        VoiceView voice13 = (VoiceView) findViewById(R.id.voice13);
        voice.setData("http://dtznpic.oss-cn-shenzhen.aliyuncs.com/audio/0-1514450638120크러쉬 (Crush) - 잠 못드는 밤 (铃声).mp3");
        voice1.setData("http://dtznpic.oss-cn-shenzhen.aliyuncs.com/audio/0-1512631873453Audio.amr");
        voice2.setData("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");
        voice3.setData("http://sc1.111ttt.cn:8282/2017/1/11m/11/304112004168.m4a?tflag=1514460511&pin=2556c34e90248fc1dda2a22721e4e863&ip=119.137.54.54#.mp3");
        voice4.setData("http://dtznpic.oss-cn-shenzhen.aliyuncs.com/audio/0-1514450638120%ED%81%AC%EB%9F%AC%EC%89%AC%20(Crush)%20-%20%EC%9E%A0%20%EB%AA%BB%EB%93%9C%EB%8A%94%20%EB%B0%A4%20(%E9%93%83%E5%A3%B0).mp3");
        voice5.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice6.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice7.setData("http://video.jiecao.fm/11/18/xu/%E6%91%87%E5%A4%B4.mp4");
        voice8.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice9.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice10.setData("http://video.jiecao.fm/11/18/xu/%E6%91%87%E5%A4%B4.mp4");
        voice11.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice12.setData("http://other.web.rh01.sycdn.kuwo.cn/resource/n1/82/32/217069489.mp3");
        voice13.setData("http://video.jiecao.fm/11/18/xu/%E6%91%87%E5%A4%B4.mp4");

        findViewById(R.id.all_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MpListUtils.getInstance().stopAllMp();
                MpListUtils.getInstance().pauseAllMp();
            }
        });

    }
}
