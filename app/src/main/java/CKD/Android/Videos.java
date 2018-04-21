package CKD.Android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;

public class Videos extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private Button FeaturedButton;
    private Button StrengthButton;
    private Button EnduranceButton;
    private Button RangeOfMotionButton;

    private YouTubePlayerView youTubePlayerViewFeatured;
    private YouTubePlayerView youTubePlayerViewStrength;
    private YouTubePlayerView youTubePlayerViewEndurance;
    private YouTubePlayerView youTubePlayerViewMotion;

    public static final String API_KEY = "AIzaSyCuC-fKLWjX1S6VgIqT11tnDQoLHUET39o";
    public static final String VIDEO_ID_Featured = "G1lwVhnnkoU";
    public static final String VIDEO_ID_Strength = "TOKxtgKrGCQ";
    public static final String VIDEO_ID_Endurance = "RZ6pv6xaW_w";
    public static final String VIDEO_ID_Motion = "KcdkySvCRCc";
    private String VIDEO_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        initializeElements();

        FeaturedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIDEO_ID = VIDEO_ID_Featured;
                reloadVideoFeatured();
                youTubePlayerViewFeatured.setVisibility(View.VISIBLE);
                youTubePlayerViewStrength.setVisibility(View.GONE);
                youTubePlayerViewEndurance.setVisibility(View.GONE);
                youTubePlayerViewMotion.setVisibility(View.GONE);
            }
        });

        StrengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIDEO_ID = VIDEO_ID_Strength;
                reloadVideoStrength();
                youTubePlayerViewFeatured.setVisibility(View.GONE);
                youTubePlayerViewStrength.setVisibility(View.VISIBLE);
                youTubePlayerViewEndurance.setVisibility(View.GONE);
                youTubePlayerViewMotion.setVisibility(View.GONE);
            }
        });

        EnduranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIDEO_ID = VIDEO_ID_Endurance;
                reloadVideoEndurance();
                youTubePlayerViewFeatured.setVisibility(View.GONE);
                youTubePlayerViewStrength.setVisibility(View.GONE);
                youTubePlayerViewEndurance.setVisibility(View.VISIBLE);
                youTubePlayerViewMotion.setVisibility(View.GONE);
            }
        });

        RangeOfMotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIDEO_ID = VIDEO_ID_Motion;
                reloadVideoMotion();
                youTubePlayerViewFeatured.setVisibility(View.GONE);
                youTubePlayerViewStrength.setVisibility(View.GONE);
                youTubePlayerViewEndurance.setVisibility(View.GONE);
                youTubePlayerViewMotion.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initializeElements() {

        VIDEO_ID = VIDEO_ID_Featured;
        FeaturedButton = findViewById(R.id.Videos_Btn_FeaturedVideo);
        StrengthButton = findViewById(R.id.Videos_Btn_StrengthVideo);
        EnduranceButton = findViewById(R.id.Videos_Btn_EnduranceVideo);
        RangeOfMotionButton = findViewById(R.id.Videos_Btn_MotionVideo);

        youTubePlayerViewFeatured = (YouTubePlayerView) findViewById(R.id.youtube_player_Featured);
        youTubePlayerViewStrength = (YouTubePlayerView) findViewById(R.id.youtube_player_Strength);
        youTubePlayerViewEndurance = (YouTubePlayerView) findViewById(R.id.youtube_player_Endurance);
        youTubePlayerViewMotion = (YouTubePlayerView) findViewById(R.id.youtube_player_Motion);

        youTubePlayerViewFeatured.initialize(API_KEY, this);
        youTubePlayerViewFeatured.setVisibility(View.VISIBLE);
        youTubePlayerViewStrength.setVisibility(View.GONE);
        youTubePlayerViewEndurance.setVisibility(View.GONE);
        youTubePlayerViewMotion.setVisibility(View.GONE);
    }

    private void reloadVideoFeatured(){
        youTubePlayerViewFeatured.initialize(API_KEY, this);
    }

    private void reloadVideoStrength(){
        youTubePlayerViewStrength.initialize(API_KEY, this);
    }

    private void reloadVideoEndurance(){
        youTubePlayerViewEndurance.initialize(API_KEY, this);
    }

    private void reloadVideoMotion(){
        youTubePlayerViewMotion.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failure to Initialize!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        if(!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }

    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(ErrorReason errorReason) {

        }
    };

}
