package edu.sjsu.silversnugmobileapp.backgroundTasks;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PanicVoiceDetectionModel {

    private static final String TAG = "AppAssist";
    private static final String MODEL_PATH = "converted_model.tflite";
    private Interpreter tflite;

    private PanicVoiceDetectionModel(){}
    private static PanicVoiceDetectionModel model;
    public static PanicVoiceDetectionModel getInstance(){
        if(model == null)
            model = new PanicVoiceDetectionModel();

        return model;
    }

    public void performAction(AssetManager asset, float[][] inputData, float[][] outputData ) {

//        Log.i(TAG, "performing action to run model");
        if(isModelNull()) {
            try {
                tflite = new Interpreter(loadModelFile(asset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean modelResult = runModel(inputData, outputData);

        if(!modelResult) Log.i(TAG, "Issue running model");
//        else Log.i(TAG, "Model ran successfully");

    }

    public void close() {
        tflite.close();
        tflite = null;
    }

    private MappedByteBuffer loadModelFile(AssetManager asset) throws IOException {
        AssetFileDescriptor fileDescriptor = asset.openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    boolean runModel(float[][] inputData, float[][] outputData ) {
        //if (isModelNull()) return false;
        long startTime = SystemClock.uptimeMillis();
//        System.out.println(tflite);
        tflite.run(inputData, outputData);
        long endTime = SystemClock.uptimeMillis();
//        Log.d(TAG, "Time taken to run model: " + Long.toString(endTime - startTime));
        return true;
    }

    private boolean isModelNull() {
        if (tflite == null) {
            Log.e(TAG, "tflite NULL");
            return true;
        }
        return false;
    }

}
