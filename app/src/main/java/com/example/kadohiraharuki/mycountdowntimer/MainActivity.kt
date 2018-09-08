package com.example.kadohiraharuki.mycountdowntimer

import android.content.IntentSender
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //コトリンでインナークラスを使うとき、innerを先頭に加える
    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval){
    //カウントダウン中か停止中か表すフラグ
        var isRunning = false

        override fun onTick(millisUntilFinished: Long){
            //残り時間の分と秒を取り出してテキストビューに表示
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%id:%2$02d".format(minute, second)
        }

        override fun onFinish() {
            timerText.text = "0:00"
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
}
