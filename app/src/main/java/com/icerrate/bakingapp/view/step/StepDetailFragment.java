package com.icerrate.bakingapp.view.step;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.utils.MeasureUtils;
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

    @BindView(R.id.description_card)
    public CardView descriptionCardView;

    @BindView(R.id.description)
    public TextView descriptionTextView;

    private SimpleExoPlayer exoPlayer;

    private MediaSessionCompat mediaSession;

    private PlaybackStateCompat.Builder stateBuilder;

    private boolean showDescription = true;

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
        descriptionCardView.setVisibility(showDescription ? View.VISIBLE : View.GONE);
        if (!showDescription) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, display.getHeight());
            videoExoPlayerView.setLayoutParams(layoutParams);
        }
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
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

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

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        videoExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @SuppressLint("InlinedApi")
    private void showSystemUi() {
        videoExoPlayerView.setSystemUiVisibility(View.VISIBLE);
    }

    public void onConfigurationChanged() {
        Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_90: case Surface.ROTATION_270:
                showDescription = false;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mDisplay.getHeight());
                videoExoPlayerView.setLayoutParams(layoutParams);
                hideSystemUi();
                fragmentListener.setToolbarVisibility(View.GONE);
                descriptionCardView.setVisibility(View.GONE);
                break;
            default:
                showDescription = true;
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(250));
                videoExoPlayerView.setLayoutParams(layoutParams);
                showSystemUi();
                fragmentListener.setToolbarVisibility(View.VISIBLE);
                descriptionCardView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setLayout(int rotation) {
        /*View view = null;
        if (rotation == 2)
            view = mInflater.inflate(R.layout.segment_dettaglio_evento_land, null);
        else if (rotation == 1)
            view = mInflater.inflate(R.layout.segment_dettaglio_evento, null);

        if (rotation == 2 || rotation == 1) {
            ViewGroup viewGroup = (ViewGroup) mRoot.findViewById(R.id.dettaglio_evento_root);
            viewGroup.removeAllViews();
            viewGroup.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.initComponent();
        }*/
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
