package com.bigstark.progressbar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SampleActivity extends ActionBarActivity {

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
