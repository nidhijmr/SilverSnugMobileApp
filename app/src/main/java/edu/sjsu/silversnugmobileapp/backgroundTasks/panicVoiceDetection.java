package edu.sjsu.silversnugmobileapp.backgroundTasks;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;

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
    private float[][] outputData = new float[1][3];

    private RestClient restApiClient;
    private String userName = "";

    public panicVoiceDetection() {
        model = PanicVoiceDetectionModel.getInstance();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            userName = intent.getExtras().getString("userName");
        }
        catch (Exception ex){
            Log.e("Error: ", "No user name in location tracker");
        }

        N = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        restApiClient = new RestClient();
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
        float caretaker_request = 0;
        float panic_request = 0;
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
                    System.out.println(outputData[i][0]+"::"+outputData[i][1]+"::"+outputData[i][2]);
                    panic_request = panic_request+outputData[i][0];
                    caretaker_request = caretaker_request+outputData[i][2];
                }


                if(panic_request>3.2 && caretaker_request>5 ){
                    System.out.println("!!!!!!!!!!!!!!!!!Panic Detected (" + panic_request + " ::::" + caretaker_request + ")!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    SystemClock.sleep(5000);
                    sendPanic();
//                        TODO Panic request Pipeline
                }
                else {
                    if (panic_request > 3.2) {
                        System.out.println("!!!!!!!!!!!!!!!!!Panic Detected (" + panic_request + " ::::" + caretaker_request + ")!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        SystemClock.sleep(5000);
                        sendPanic();
//                        TODO Panic request Pipeline
                    }
//                    if (caretaker_request > 4.5) {
//                        System.out.println("!!!!!!!!!!!!!!!!!Care Taker Requested (" + panic_request + " ::::" + caretaker_request + ")!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                        SystemClock.sleep(5000);
////                        TODO Caretaker request Pipleine
//                        }
                    }
                caretaker_request = caretaker_request/8;
                panic_request = panic_request/8;
                System.out.println("#######################################################################");
                System.out.println("Caretaker = "+caretaker_request+"   Panic = "+panic_request);

                System.out.println("#######################################################################");
//
            }
        }
    }


    public void sendPanic(){
        Log.i("sending panic for: ", userName);
        String url = "/SilverSnug/Panic/SendPanicNotification?userName=" + userName;
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) throws JSONException {
                if(jsonResponse.getString("").equals("panic notification sent successfully"))
                {
                    Log.i("panic status: ", "panic sent successfully");
                }
            }

            @Override
            public void onError(String message) {

                Log.e("panic status: ", message);
            }
        });
    }

}
