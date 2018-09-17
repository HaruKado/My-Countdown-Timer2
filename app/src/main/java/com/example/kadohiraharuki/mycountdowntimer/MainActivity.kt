package com.example.kadohiraharuki.mycountdowntimer

import android.content.IntentSender
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //SoundPoolクラスインスタンスとサウンドファイルのリソースIDを保持するプロパティを宣言
    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    //コトリンでインナークラスを使うとき、innerを先頭に加える
    //CountDownTimerを継承したMyCountDownTimerクラスを作成
    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval){
    //カウントダウン中か停止中か表すフラグ
        var isRunning = false

        //onTickメソッド：コンストラクタで指定した間隔で分、秒を読み出す
        override fun onTick(millisUntilFinished: Long){
            //残り時間の分と秒を取り出してテキストビューに表示
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%1d:%2$02d".format(minute, second)
        }

        override fun onFinish() {
            timerText.text = "0:00"
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "3.00"
        //タイマー継続時間3分、0.1秒ごとに更新
        val timer = MyCountDownTimer(3 * 60 * 1000, 100)
        playStop.setOnClickListener{
            when(timer.isRunning){
                //カウント中だった場合、カウントを停止し、フローティングアクションボタン表示
                true -> timer.apply{
                    isRunning = false
                    cancel()
                    playStop.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                }
                //カウント中でなければ、カウントを続行、ストップマークに変更
                false -> timer.apply{
                    isRunning = true
                    start()
                    playStop.setImageResource(R.drawable.ic_stop_black_24dp)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //アクテビティが表示された時にメモリにロード
        super.onResume()
        //アクテビティが画面に表示された時に実行されるonResumeメソッド内で、SoundPoolのインスタンスを作成
        soundPool =
                //Build.VERSION.SDK_INTには実行中のOSバージョン番号が入っている、Lollipopの場合は21が格納されている
                //バージョンが5.0より前の場合
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
                    //非推奨のメソッドを使っているが、対応済みのため検査不要と示唆する
                    @Suppress("DEPRECATION")
                    SoundPool(2, AudioManager.STREAM_ALARM, 0)
                }
                else {
                    val audioAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build()
                    SoundPool.Builder()
                            //同時に音を流す数を設定
                            .setMaxStreams(1)
                            .setAudioAttributes(audioAttributes)
                            .build()
                }
                //loadメソッドを使って、登録したサウンドリソースを読み込む
                    soundResId = soundPool.load(this, R.raw.bellsound, 1)

    }

    override fun onPause() {
        //他のアプリを起動するなどしてアクテビティが非表示になった時メモリを開放
        super.onPause()
        soundPool.release()
    }
}
