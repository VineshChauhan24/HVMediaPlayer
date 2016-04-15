package org.geeklub.hvmediaplayer.widgets.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.VideoView;

/**
 * Created by HelloVass on 16/3/24.
 */
public class HVVideoView extends VideoView {

  private static final String TAG = HVVideoView.class.getSimpleName();

  private UpdatePlayableTimer mUpdatePlayableTimer;

  private Mediator mHVVideoPlayer;

  public HVVideoView(Context context) {
    super(context);
    init();
  }

  public void setHVVideoPlayer(Mediator HVVideoPlayer) {
    mHVVideoPlayer = HVVideoPlayer;
  }

  public void resetTimer() {
    mUpdatePlayableTimer = new UpdatePlayableTimer(getDuration(), 250L);
    mUpdatePlayableTimer.start();
  }

  public void stopTimer() {
    mUpdatePlayableTimer.cancel();
    mUpdatePlayableTimer = null;
  }

  private void init() {

    setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

      @Override public void onPrepared(MediaPlayer mp) {
        mHVVideoPlayer.onPrepared(mp);
      }
    });

    setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override public void onCompletion(MediaPlayer mp) {
        mHVVideoPlayer.onCompletion(mp);
      }
    });

    setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override public boolean onError(MediaPlayer mp, int what, int extra) {

        if (mHVVideoPlayer != null) {
          mHVVideoPlayer.onError(mp,what,extra);
          return true;
        }

        return false;
      }
    });
  }

  private class UpdatePlayableTimer extends CountDownTimer {

    public UpdatePlayableTimer(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override public void onTick(long millisUntilFinished) {

      if (isPlaying()) {
        float progress = (float) getCurrentPosition() / (float) getDuration();
        mHVVideoPlayer.updateCurrentTimeWhenPlaying((int) (progress * 100), getBufferPercentage());
      }
    }

    @Override public void onFinish() {

    }
  }
}