package edu.sjsu.silversnugmobileapp.backgroundTasks;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;

public class panicVoiceDetection extends Service {
    private Handler handler;
    private AudioRecord recorder = null;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_FLOAT;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    int BufferElements2Rec = 8192;
    // want to play 2048 (2K) since 2 bytes we use only 1024
    int N=0;
    int BytesPerElement = 2; // 2 bytes in 16bit format
    PanicVoiceDetectionModel model = null;
    private float[][] inputData =  new float[1000][1];
    private float[][] outputData = new float[1][2];
    public panicVoiceDetection() {
        model = PanicVoiceDetectionModel.getInstance();
//        this.parent  = parent;


    }


    public static File getUniqueFile(String base, String ext, int index) {
        File f = new File(String.format("%s-%03d.%s", base, index, ext));
        return f.exists() ? getUniqueFile(base, ext, index + 1) : f;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Panic Detected !!!", Toast.LENGTH_LONG).show();
            }
        });
//        System.out.println("in on start command");
        File _f = getUniqueFile("VoiceData","csv",00);
        N = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        System.out.println(N);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, N*10);


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
        short[][] buffers  = new short[256][8000];
        int ix = 0;
//        short sData[] = new short[BufferElements2Rec];
        double v = 65536.0;
        while (isRecording) {
            inputData = new float[2000][1];
            outputData = new float[1][2];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                short[] buffer = buffers[ix++ % buffers.length];
                N = recorder.read(buffer,0,buffer.length);

                for(int i=0,j=0;i<8000;i=i+4,j=j+1) {
                    float mmean =  (buffer[i]+buffer[i+1]+buffer[i+2]+buffer[i+3])/4;
                    inputData[j][0] = (float) (mmean+32768)/65536;
                }
                model.performAction(this.getAssets(), inputData, outputData);
                System.out.println(outputData[0][0]+":::::"+outputData[0][1]);
                if(outputData[0][0]>.6) {
                    System.out.println("SHOOT!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }

            }
        }
    }


}
