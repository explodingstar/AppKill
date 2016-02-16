package appkill.explodingstar.com.appkill;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by myxom on 16/02/14.
 */
public abstract class CountDownTimerWithPause extends AppKillTimer {

    private long mStopTimeInFuture; //ms since boot, alarm trigger
    private long mMillisInFuture; //real time until trigger
    private final long mTotalCountDown; //total time on timer @ start
    private final long mCountDownInterval; //interval(ms) when callbacks return
    private long mPauseTimeRemaining; //time remaining @ pause
    private boolean mTimerRunning; //true if timer running

    public abstract void onTick(long millisUntilFinished);
    public abstract void onFinish();
    private static final int MSG = 1;

    public CountDownTimerWithPause(long millisOnTimer, long countDownInterval,
                                   boolean runAtStart){
        mMillisInFuture = millisOnTimer;
        mTotalCountDown = mMillisInFuture;
        mCountDownInterval = countDownInterval;
        mTimerRunning = runAtStart;
    }

    public final void cancel(){
        mHandler.removeMessages(MSG);
    }

    public synchronized final CountDownTimerWithPause create(){
        if (mMillisInFuture<=0){
            onFinish();
        } else {
            mPauseTimeRemaining=mMillisInFuture;
        }
        if (mTimerRunning){
            resume();
        }
        return this;
    }

    public void pause(){
        if (isRunning()){
            mPauseTimeRemaining=timeLeft();
            cancel();
        }
    }

    public void resume(){
        if (isPaused()){
            mMillisInFuture=mPauseTimeRemaining;
            mStopTimeInFuture=SystemClock.elapsedRealtime()+mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mPauseTimeRemaining=0;
        }
    }

    public boolean isPaused(){
        return (mPauseTimeRemaining>0);
    }

    public boolean isRunning(){
        return(!isPaused());
    }

    public long timeLeft(){
        long millisUntilFinished;
        if (isPaused()){
            millisUntilFinished=mPauseTimeRemaining;
        } else {
            millisUntilFinished=mStopTimeInFuture - SystemClock.elapsedRealtime();
            if (millisUntilFinished<0) {
                millisUntilFinished=0;
            }
        }
        return millisUntilFinished;
    }

    public long getTotalCountDown(){
        return mTotalCountDown;
    }

    public long timePassed(){
        return mTotalCountDown - timeLeft();
    }

    public boolean hasBeenStarted(){
        return(mPauseTimeRemaining<=mMillisInFuture);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            synchronized (CountDownTimerWithPause.this){
                long millisLeft = timeLeft();

                if (millisLeft<=0){
                    cancel();
                    onFinish();
                } else if (millisLeft<mCountDownInterval){
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    long delay = mCountDownInterval - (SystemClock.elapsedRealtime() - lastTickStart);
                    while (delay<0) delay+=mCountDownInterval;
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
