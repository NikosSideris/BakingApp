package com.example.android.bakingapp.utilities;

import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * Created by Nikos on 05/04/18.
 */
public class Beep {
    public Beep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
    }
}
