package dad.app.ts.com.tablayouttest1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;

public class VideoActivity extends BaseActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_plyer);
        VideoView videoView = findViewById(R.id.video);
        MediaController controller = new MediaController(this);
        videoView.setVideoURI(Uri.parse("http://video.chinanews.com/tvmining//News/MP4ZXW/CCYVNEWS/2019/02/22/Ls3weyy_1550791844721_QtRwogT_6359.mp4"));
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);


    }
}
