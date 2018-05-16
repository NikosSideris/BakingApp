package com.example.android.bakingapp.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.SelectAStepActivity;
import com.example.android.bakingapp.model.Step;

import com.example.android.bakingapp.utilities.ExoplayerUtils;
import com.example.android.bakingapp.utilities.ScreenInfo;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;


import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_View_Step.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_View_Step extends Fragment {

    private final static String TAG="FVS";
    private Context mContext;
    private static Step sStep;

    private static OnFragmentInteractionListener mListener;

    private SimpleExoPlayer mSimpleExoPlayer;
    private PlayerView mPlayerView;

    private Long videoPlayerCurrentPosition;
    private static final String KEY_CURRENT_POSITION = "current";

    private final static String KEY_WHEN_READY = "whenReady";
    private boolean exoPlayerPlayWhenReady = true;

    private String videoUrl;
    private String description;

    private TextView descr;
    ImageView imageView;
    FrameLayout frameLayout;
    LinearLayout linearLayout;

    public Fragment_View_Step() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.d("started");
        if (savedInstanceState != null) {
            videoPlayerCurrentPosition = savedInstanceState.getLong(KEY_CURRENT_POSITION, 0);
            exoPlayerPlayWhenReady = savedInstanceState.getBoolean(KEY_WHEN_READY, false);
        }

        mContext = getContext();

        if (sStep!=null){
            videoUrl=sStep.getVideoUrl();
            description=sStep.getDescription();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView;

        rootView = inflater.inflate(R.layout.fragment_view_step, container, false);

        Timber.d("FIND COMPONENTS");
        descr = rootView.findViewById(R.id.tv_step_long_description);
        imageView=rootView.findViewById(R.id.iv_novideo);
        frameLayout=rootView.findViewById(R.id.fl_media_container);
        linearLayout = rootView.findViewById(R.id.ll_buttons);

        setOrientationParameters();

        Timber.d("HANDLE PLAYER");
        if (!videoUrl.isEmpty()) {
            Timber.d("video url Not empty");

            mPlayerView = rootView.findViewById(R.id.player_view);
            initExpoPlayer();

            descr = rootView.findViewById(R.id.tv_step_long_description);
            descr.setText(description);

        }else{   //video is empty
            mPlayerView = rootView.findViewById(R.id.player_view);
            mPlayerView.setVisibility(View.GONE);

            imageView.setVisibility(View.VISIBLE);

            descr.setText(description);
        }
        Timber.d("RETURN");
        return rootView;
    }
    private void initExpoPlayer(){
        Timber.d("initExpoPlayer");
        // Checking whether SimpleExoPlayer is null
        if (mSimpleExoPlayer == null) {

            // Initialize the Media Session .
            ExoplayerUtils.initMediaSession(getActivity(), mSimpleExoPlayer);

            // Checking whether video step of recipe is valid or not
            if (sStep.getVideoUrl() != null) {
                Uri videoUrl = Uri.parse(sStep.getVideoUrl());
                mSimpleExoPlayer = ExoplayerUtils.initPlayer(videoUrl, getActivity(), mSimpleExoPlayer);

            } else {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                        (getResources(), R.drawable.novideoavailable));
            }
        }
        // Setting SimpleExoPlayer to simpleExoPlayerView
        mPlayerView.setPlayer(mSimpleExoPlayer);

        // Checking whether SimpleExoPlayer is not null to determine its current position
        if (videoPlayerCurrentPosition != null) {
            mSimpleExoPlayer.seekTo(videoPlayerCurrentPosition);
        } else {
            mSimpleExoPlayer.seekTo(0);
        }

        // Preparing SimpleExoPlayer to set Play
        mSimpleExoPlayer.setPlayWhenReady(exoPlayerPlayWhenReady);

    }

    void setOrientationParameters(){
        /*
        exists for future needs: ie changing the parameters of the video player container
         */
        Timber.d("setOrientationParameters");
        ScreenInfo device=new ScreenInfo(getContext());
        int statusBarHeight = SelectAStepActivity.statusBarHeight;
        float toolbarHeight=getResources().getDimension(R.dimen.app_bar_height);
        if (device.inLandscapeMode() && !device.isTablet()){
            int height=device.getHeight()-(int)toolbarHeight -statusBarHeight;
            int width=device.getWidth();
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(width,height));

            Timber.d("frameLayout.setLayoutParams");
        }else {
            if (device.isTablet() && device.inLandscapeMode()) {
                linearLayout.setVisibility(View.GONE);
            }
        }
    }


    public void onButtonPressed(Uri uri) {
        Timber.d("onButtonPressed");
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            videoPlayerCurrentPosition = mSimpleExoPlayer.getCurrentPosition();
            exoPlayerPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            mListener = null;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState");
        if (videoPlayerCurrentPosition != null) {
            savedInstanceState.putLong(KEY_CURRENT_POSITION, videoPlayerCurrentPosition);
            savedInstanceState.putBoolean(KEY_WHEN_READY, exoPlayerPlayWhenReady);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onDetach() {
        Timber.d("onDetach");

        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            mPlayerView = null;
            mListener = null;
        }
        super.onDetach();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static void setStep(Step step) {
        sStep = step;
    }

}
