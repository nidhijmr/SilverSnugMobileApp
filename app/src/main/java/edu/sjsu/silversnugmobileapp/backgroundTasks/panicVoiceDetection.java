package edu.sjsu.silversnugmobileapp.backgroundTasks;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

public class panicVoiceDetection extends Service {
    private Handler handler;
    private AudioRecord recorder = null;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    int N=0;
    PanicVoiceDetectionModel model = null;
    private float[][] inputData =  new float[16000][1];
    private float[][] outputData = new float[8][3];
    public panicVoiceDetection() {
        model = PanicVoiceDetectionModel.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        N = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);

        System.out.println(N);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, N*2);


        recorder.startRecording();

        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                runPrediction();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void runPrediction() {
        int caretaker_request = 0;
        int panic_request = 0;
        while (isRecording) {
            inputData = new float[16000][1];
            outputData = new float[8][3];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                short[] buffer = new short[16000];
                N = recorder.read(buffer,0,buffer.length);
                for(int i=0;i<16000;i=i+1) {
                    inputData[i][0] = (float) (buffer[i])/16384;
                }
                model.performAction(this.getAssets(), inputData, outputData);

                 caretaker_request = 0;
                 panic_request = 0;

                for(int i=0;i<8; i++) {
                    if (outputData[i][0] > 0.6) {
                        panic_request++;
                    }
                    if (outputData[i][2] > .9) {
                        caretaker_request++;

                    }
                }
                if(caretaker_request>1) {
                    System.out.println(caretaker_request+"      !!!!!!!!!!!!!!!!!Care Taker Requested!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                if(panic_request>1) {

                    System.out.println(panic_request+"     !!!!!!!!!!!!!!!!!Panic Detected!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }

            }
        }
    }


}
