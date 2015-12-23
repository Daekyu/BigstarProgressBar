BigstarProgressBar
================
<a href="https://www.youtube.com/watch?v=68c2mPPUze0" target="_blank" title="Show Animation from YouTube">Show Animation from YouTube</a>

##Include your project
add build.gradle
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
```
dependencies {
    compile 'com.github.bigstark:BigstarProgressBar:0.1'
}
```

## Usage
``` xml
  <com.bigstark.progressbar.BigStarProgressBar
      android:id="@+id/progress"
      android:layout_width="200dp"
      android:layout_height="200dp"/>
```

``` java
    progress = (BigStarProgressBar) findViewById(R.id.progress);
    progress.setDuration(800);
    progress.setRadius(80f);
    progress.start();
```
