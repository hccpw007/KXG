package com.cqts.kxg.views;


import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * 发送验证码倒计时
 */
public class CodeCountDownTimer extends CountDownTimer {
    TextView text;
    Boolean isGoing = false;
    Boolean init = false;

    public CodeCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isGoing = true;
        if (null != text && !init) {
            text.setText(millisUntilFinished / 1000 + "s后重新获取");
            text.setEnabled(false);
        }
    }

    @Override
    public void onFinish() {
        isGoing = false;
        if (null != text) {
            text.setText("再次发送验证码");
            text.setEnabled(true);
        }
    }


    /**
     * 增加TextView
     *
     * @param text
     */
    public void setTextView(TextView text) {
        this.text = text;
    }

    /**
     * 启动倒计时
     */
    public void going() {
        if (!isGoing||init) {
            start();
        }
        init = false;
    }

    /**
     * 初始化 计数器返回0
     */
    public void setInit() {
        init = true;
        if (null != text) {
            text.setEnabled(true);
            text.setText("再次发送验证码");
        }
    }
}
