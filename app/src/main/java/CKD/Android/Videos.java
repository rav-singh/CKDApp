package CKD.Android;

import android.os.Bundle;
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

    public static final String API_KEY = "AIzaSyCuC-fKLWjX1S6VgIqT11tnDQoLHUET39o";

    public static final String VIDEO_ID = "aJ7BoNG-r2c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);


        YouTubePlayerView youTubePlayerViewFeatured = (YouTubePlayerView) findViewById(R.id.youtube_player_Featured);
        youTubePlayerViewFeatured.initialize(API_KEY, this);

        /*
        YouTubePlayerView youTubePlayerViewStrength = (YouTubePlayerView) findViewById(R.id.youtube_player_Strength);
        youTubePlayerViewStrength.initialize(API_KEY, this);

        YouTubePlayerView youTubePlayerViewEndurance = (YouTubePlayerView) findViewById(R.id.youtube_player_Endurance);
        youTubePlayerViewEndurance.initialize(API_KEY, this);

        YouTubePlayerView youTubePlayerViewMotion = (YouTubePlayerView) findViewById(R.id.youtube_player_Motion);
        youTubePlayerViewMotion.initialize(API_KEY, this);
        */
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
