package com.bigstark.progressbar.sample;

import android.app.Activity;
import android.os.Bundle;

import com.bigstark.progressbar.BigStarProgressBar;


public class SampleActivity extends Activity {

  private BigStarProgressBar progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);

    progress = (BigStarProgressBar) findViewById(R.id.progress);
    progress.setDuration(800);
    progress.setRadius(80f);
    progress.start();
  }

}
