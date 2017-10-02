package app.entel.com.appentelseleccion;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import app.entel.com.appentelseleccion.includes.AppVars;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoInactivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;
    private MediaController mediaControls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        setContentView(R.layout.activity_video_inactivity);
        ButterKnife.bind(this);


        try {
            //set the media controller in the VideoView

            videoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            videoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + AppVars.pathHome + "/" + AppVars.videoname));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video

                //if we have a position on savedInstanceState, the video playback should start from here
                mediaPlayer.setLooping(true);
                videoView.seekTo(0);
                videoView.start();
                /*if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }*/
            }

        });



        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //onBackPressed();
                //finish();
                doMain();
                return false;
            }
        });




    }

    public void doMain(){
        Intent intent = new Intent(getApplicationContext(), Intro.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        //doMain();
    }
}
