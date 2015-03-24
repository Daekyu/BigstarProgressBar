package com.bigstark.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import java.util.Map;

/**
 * Created by BigStarK on 15. 3. 23..
 */
public class BigStarProgressBar extends View {
  private final long DURATION_DEFAULT = 1000;
  private final float RADIUS_DEFAULT = 100f;

  private long duration = DURATION_DEFAULT;

  private float radius = RADIUS_DEFAULT;

  private float length = radius * (float) Math.PI * 3 / 4;
  private float t1 = length * duration / (2 * (float) Math.PI * radius + length);
  private float t2 = duration - t1;

  private float offset = 0f;
  private float offset1 = t1 * 3 * (float) Math.PI * radius / (4 * length * duration);
  private float offset2 = offset1 * 5 / 3;
  private float offsetT1 = t1 / duration;
  private float offsetT2 = t2 / duration;

  public BigStarProgressBar(Context context) {
    super(context);
    initValues();
  }

  public BigStarProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initValues();
  }

  public BigStarProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initValues();
  }

  private void initValues() {
    offset = 0;
    length = radius * (float) Math.PI * 3 / 4;
    t1 = length * duration / (2 * (float) Math.PI * radius + length);
    t2 = duration - t1;
    offset1 = t1 * 3 * (float) Math.PI * radius / (4 * length * duration);
    offset2 = offset1 * 5 / 3;

    Log.v("TAG", String.format("t1 : %f, t2 : %f, offset1 : %f, offset2 : %f", t1, t2, offset1, offset2));
  }


  /*
   * first circle is degree 135 to 0, radius is origin.
   * second circle is degree 0 to -180, radius is origin / 2.
   * third circle is degree -180 to -315, radius is origin.
   */
  private Path makePath() {
    Path path = new Path();

    float firstCenterX = radius / (float) Math.sqrt(2);
    float firstCenterY = radius;
    RectF firstCircle = new RectF(firstCenterX - radius, firstCenterY - radius,
        firstCenterX + radius, firstCenterY + radius);
    path.addArc(firstCircle, 135f, -135f);

    float secondCenterX = radius / (float) Math.sqrt(2) + 0.5f * radius;
    float secondCenterY = radius;
    RectF secondCircle = new RectF(secondCenterX - radius / 2, secondCenterY - radius / 2,
        secondCenterX + radius / 2, secondCenterY + radius / 2);
    path.addArc(secondCircle, 0f, -180f);

    float thirdCenterX = radius / (float) Math.sqrt(2) + radius;
    float thirdCenterY = radius;
    RectF thirdCircle = new RectF(thirdCenterX - radius, thirdCenterY - radius,
        thirdCenterX + radius, thirdCenterY + radius);
    path.addArc(thirdCircle, -180f, -135f);

    return path;
  }

  private Path updateBarPath() {
    Path path = new Path();


    float firstCenterX = radius / (float) Math.sqrt(2);
    float firstCenterY = radius;
    RectF firstCircle = new RectF(firstCenterX - radius, firstCenterY - radius,
        firstCenterX + radius, firstCenterY + radius);

    float firstStartAngle = offset < offsetT1 ? 135f : 135f * (1 - (offset - offsetT1) / offset1);
    firstStartAngle = firstStartAngle > 135f? 135f : (firstStartAngle < 0f ? 0f : firstStartAngle);
    float firstSweepAngle = offset < offset1 ? -135f * offset / offset1 : -firstStartAngle;
    firstSweepAngle = firstSweepAngle < -135f ? -135f : (firstSweepAngle > 0f ? 0f : firstSweepAngle);
    path.addArc(firstCircle, firstStartAngle, firstSweepAngle);

    if (offset < offset1) {
      return path;
    }

    float secondCenterX = radius / (float) Math.sqrt(2) + 0.5f * radius;
    float secondCenterY = radius;
    RectF secondCircle = new RectF(secondCenterX - radius / 2, secondCenterY - radius / 2,
        secondCenterX + radius / 2, secondCenterY + radius / 2);

    float secondStartAngle = offset < offsetT1 + offset1 ? 0 : -180 * (offset - offsetT1 - offset1) / (offset2 - offset1);
    secondStartAngle = secondStartAngle > 0 ? 0 : (secondStartAngle < -180f ? -180f : secondStartAngle);
    float secondSweepAngle = offset < offsetT1 + offset1 ? -180f * (offset - offset1) / (offset2 - offset1) : -180f - secondStartAngle;
    secondSweepAngle = secondSweepAngle < -180f ? -180f : (secondSweepAngle > 0 ? 0 : secondSweepAngle);
    path.addArc(secondCircle, secondStartAngle, secondSweepAngle);
    if (offset >= offset1 && offset < offset2) {
      return path;
    }

    float thirdCenterX = radius / (float) Math.sqrt(2) + radius;
    float thirdCenterY = radius;
    RectF thirdCircle = new RectF(thirdCenterX - radius, thirdCenterY - radius,
        thirdCenterX + radius, thirdCenterY + radius);

    float thirdStartAngle = offset < offsetT1 + offset2 ? -180f : -180f + (-135f * (offset - offsetT1 - offset2) / (1 - offsetT1 - offset2));
    thirdStartAngle = thirdStartAngle > -180f ? -180f : (thirdStartAngle < -315f ? -315f : thirdStartAngle);
    float thirdSweepAngle = offset < offsetT2 ? -135f * (offset - offset2) / (offsetT2 - offset2) : -315f - thirdStartAngle;
    thirdSweepAngle = thirdSweepAngle < -135f ? -135f : (thirdSweepAngle > 0f ? 0f : thirdSweepAngle);
    path.addArc(thirdCircle, thirdStartAngle, thirdSweepAngle);
    if (offset >= offset2 && offset < offsetT2) {
      return path;
    }

    return path;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    Path path = makePath();
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(15);
    paint.setColor(Color.parseColor("#AAAAAA"));

    canvas.drawPath(path, paint);

    path = updateBarPath();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(15);
    paint.setColor(Color.parseColor("#5A3296"));

    canvas.drawPath(path, paint);
  }

  public void start() {
    ObjectAnimator animator = ObjectAnimator.ofFloat(this, "scale", 1f);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        offset = animation.getAnimatedFraction();
        postInvalidate();
      }
    });
    animator.setDuration(duration);
    animator.setRepeatCount(ObjectAnimator.INFINITE);
    animator.start();
  }

  public void setDuration(long duration) {
    this.duration = duration;
    initValues();
    postInvalidate();
  }

  public void setRadius(float radius) {
    this.radius = radius;
    initValues();
    postInvalidate();
  }
}