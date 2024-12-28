package com.plus.navanguilla;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class Speecher implements TextToSpeech.OnInitListener {

    private static final String TAG = "TextToSpeechController";
    private TextToSpeech myTTS;
    private String textToSpeak;
    private Context context;

    private static Speecher singleton;

    public static Speecher getInstance(Context ctx) {
        if (singleton == null)
            singleton = new Speecher(ctx);
        return singleton;
    }

    private Speecher(Context ctx) {
        context = ctx;
    }

    public void speak(String text) {
        textToSpeak = text;

        if (myTTS == null) {
            // currently can\'t change Locale until speech ends
            try {
                // Initialize text-to-speech. This is an asynchronous operation.
                // The OnInitListener (second argument) is called after
                // initialization completes.
                myTTS = new TextToSpeech(context, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sayText();

    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.UK);
            myTTS.setPitch((float) 0.9);
        }

        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (initStatus == TextToSpeech.SUCCESS) {
            int result = myTTS.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "TTS missing or not supported (" + result + ")");
                // Language data is missing or the language is not supported.
                // showError(R.string.tts_lang_not_available);

            } else {
                // Initialization failed.
                Log.e(TAG, "Error occured");
            }

        }
    }

    private void sayText() {
        HashMap<String, String> myHash = new HashMap<String, String>();
        myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "done");
        String[] splitspeech = this.textToSpeak.split("\\\\");

        for (int i = 0; i < splitspeech.length; i++) {

            if (i == 0) { // Use for the first splited text to flush on audio stream
                myTTS.speak(splitspeech[i].toString().trim(), TextToSpeech.QUEUE_FLUSH,     myHash);

            } else { // add the new test on previous then play the TTS

                myTTS.speak(splitspeech[i].toString().trim(), TextToSpeech.QUEUE_ADD,     myHash);
            }
            myTTS.playSilence(100, TextToSpeech.QUEUE_FLUSH,  null);
        }

    }

    public void stopTTS() {
        if (myTTS != null) {
            myTTS.shutdown();
            myTTS.stop();
            myTTS = null;
        }
    }

}
