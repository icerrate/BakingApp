package com.icerrate.bakingapp.view.step;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.utils.MeasureUtils;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_RECIPE_ID;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_SELECTED_STEP;

/**
 * @author Ivan Cerrate.
 */

public class StepDetailFragment extends BaseFragment implements StepDetailView, Player.EventListener {

    public static String KEY_VIDEO_TIME = "VIDEO_TIME_KEY";

    public static String KEY_VIDEO_AUTO_PLAY = "VIDEO_AUTO_PLAY_KEY";

    public static String KEY_STEP_DETAIL = "STEP_DETAIL_KEY";

    @BindView(R.id.video_container)
    public RelativeLayout videoContainer;

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

    private StepDetailPresenter presenter;

    public static StepDetailFragment newInstance(Integer recipeId, Integer selectedStep) {
        Bundle bundle = new Bundle();
        if (recipeId != null) {
            bundle.putInt(KEY_RECIPE_ID, recipeId);
        }
        if (selectedStep != null) {
            bundle.putInt(KEY_SELECTED_STEP, selectedStep);
        }
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new StepDetailPresenter(this,
                InjectionUtils.bakingRepository(getContext()));
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
            Integer recipeId = getArguments().getInt(KEY_RECIPE_ID);
            Integer selectedStep = getArguments().getInt(KEY_SELECTED_STEP);
            presenter.setRecipeId(recipeId);
            presenter.setSelectedStep(selectedStep);
        } else {
            restoreInstanceState(savedInstanceState);
        }
        presenter.loadStepDetail();
        refreshViewRotation();
    }

    private void refreshViewRotation() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_90: case Surface.ROTATION_270: //Landscape
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, display.getHeight());
                videoExoPlayerView.setLayoutParams(layoutParams);
                if (presenter.containsVideo()) {
                    if (getActivity() instanceof StepDetailActivity) {
                        hideSystemUi();
                        fragmentListener.setToolbarVisibility(View.GONE);
                    }
                    descriptionCardView.setVisibility(View.GONE);
                } else {
                    fragmentListener.setToolbarVisibility(View.VISIBLE);
                    descriptionCardView.setVisibility(View.VISIBLE);
                }
                break;
            default: // Portrait
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(250));
                videoExoPlayerView.setLayoutParams(layoutParams);
                showSystemUi();
                fragmentListener.setToolbarVisibility(View.VISIBLE);
                descriptionCardView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putInt(KEY_RECIPE_ID, presenter.getRecipeId());
        outState.putInt(KEY_SELECTED_STEP, presenter.getSelectedStep());
        outState.putParcelable(KEY_STEP_DETAIL, presenter.getStepDetail());
        outState.putLong(KEY_VIDEO_TIME, presenter.getVideoTime());
        outState.putBoolean(KEY_VIDEO_AUTO_PLAY, presenter.getVideoAutoPlay());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Integer recipeId = savedState.getInt(KEY_RECIPE_ID);
        Integer selectedStep = savedState.getInt(KEY_SELECTED_STEP);
        Step stepDetail = savedState.getParcelable(KEY_STEP_DETAIL);
        Long videoTime = savedState.getLong(KEY_VIDEO_TIME);
        Boolean videoAutoPlay = savedState.getBoolean(KEY_VIDEO_AUTO_PLAY);
        presenter.loadPresenterState(recipeId, selectedStep, stepDetail, videoTime, videoAutoPlay);
    }

    private void setupView() {
        videoExoPlayerView.requestFocus();
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "StepVideo");

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

    private void initializePlayer(long videoTime, boolean videoAutoPlay) {
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
            exoPlayer.seekTo(videoTime);
            exoPlayer.setPlayWhenReady(videoAutoPlay);
        }
    }

    public void releasePlayer() {
        presenter.setVideoTime(exoPlayer.getCurrentPosition());
        presenter.setVideoAutoPlay(exoPlayer.getPlayWhenReady());
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
    public void onResume() {
        super.onResume();
        if (presenter.containsVideo()) {
            initializePlayer(presenter.getVideoTime(), presenter.getVideoAutoPlay());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter.containsVideo()) {
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

    @Override
    public void showShortDescription(String shortDescription) {
        if (getActivity() instanceof StepDetailActivity) {
            fragmentListener.setTitle(shortDescription);
        }
    }

    @Override
    public void loadThumbnailSource(String thumbnailUrl) {
        GlideApp.with(getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.rectangle_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        thumbnailImageView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        thumbnailImageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(thumbnailImageView);
    }

    @Override
    public void showThumbnail(boolean show) {
        thumbnailImageView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loadVideoSource(String videoUrl, long videoTime, boolean videoAutoPlay) {
        initializeMediaSession();
        initializePlayer(videoTime, videoAutoPlay);
    }

    @Override
    public void showVideo(boolean show) {
        videoContainer.setVisibility(show ? View.VISIBLE : View.GONE);
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
}
