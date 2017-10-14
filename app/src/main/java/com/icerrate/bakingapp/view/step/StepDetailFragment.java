package com.icerrate.bakingapp.view.step;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class StepDetailFragment extends BaseFragment implements StepDetailView, Player.EventListener, PlaybackControlView.VisibilityListener {

    public static String KEY_STEP_DETAIL = "STEP_DETAIL_KEY";

    @BindView(R.id.video)
    public SimpleExoPlayerView videoExoPlayerView;

    @BindView(R.id.video_progress)
    public ProgressBar videoProgressView;

    @BindView(R.id.thumbnail)
    public ImageView thumbnailImageView;

    @BindView(R.id.description)
    public TextView descriptionTextView;

    private SimpleExoPlayer exoPlayer;

    private MediaSessionCompat mediaSession;

    private PlaybackStateCompat.Builder stateBuilder;

    private StepDetailPresenter presenter;

    public static StepDetailFragment newInstance(Step step) {
        Bundle bundle = new Bundle();
        if (step != null) {
            bundle.putParcelable(KEY_STEP_DETAIL, step);
        }
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new StepDetailPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        if (savedInstanceState == null) {
            Step recipeDetail = getArguments().getParcelable(KEY_STEP_DETAIL);
            presenter.setStepDetail(recipeDetail);
        } else {
            restoreInstanceState(savedInstanceState);
        }
        presenter.loadStepDetail();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STEP_DETAIL, presenter.getStepDetail());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Step stepDetail = savedState.getParcelable(KEY_STEP_DETAIL);
        presenter.loadPresenterState(stepDetail);
    }

    private void setupView() {
        videoExoPlayerView.setControllerVisibilityListener(this);
        videoExoPlayerView.requestFocus();
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "RecipeStepSinglePageFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void initializePlayer() {
        if (exoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            videoExoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            Uri mediaUri = Uri.parse(presenter.getStepDetail().getVideoURL());
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    private void updateButtonVisibilities() {
        /*debugRootView.removeAllViews();

        retryButton.setVisibility(inErrorState ? View.VISIBLE : View.GONE);
        debugRootView.addView(retryButton);

        if (player == null) {
            return;
        }

        MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(this);
                int label;
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }
                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(this);
                debugRootView.addView(button, debugRootView.getChildCount() - 1);
            }
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void showThumbnail(String thumbnailUrl) {
        thumbnailImageView.setVisibility(View.VISIBLE);
        //TODO: Image
    }

    @Override
    public void hideThumbnail() {
        thumbnailImageView.setVisibility(View.GONE);
    }

    @Override
    public void showVideo(String videoUrl) {
        videoExoPlayerView.setVisibility(View.VISIBLE);
        //TODO: ExoPlayer
        initializeMediaSession();
        initializePlayer();
    }

    @Override
    public void hideVideo() {
        videoExoPlayerView.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        descriptionTextView.setText(description);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        //Needs to be empty
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        //Needs to be empty
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        //Needs to be empty
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                videoProgressView.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_READY:
                videoProgressView.setVisibility(View.GONE);
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
                break;
            case Player.STATE_ENDED:
                videoProgressView.setVisibility(View.GONE);
                stateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, exoPlayer.getCurrentPosition(), 1f);
                break;
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        //Needs to be empty
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        //Needs to be empty
    }

    @Override
    public void onPositionDiscontinuity() {
        //Needs to be empty
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        //Needs to be empty
    }

    @Override
    public void onVisibilityChange(int visibility) {

    }
}
