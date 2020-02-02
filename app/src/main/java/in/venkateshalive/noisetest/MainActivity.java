package in.venkateshalive.noisetest;

import android.app.Activity;

import android.os.Bundle;

import android.media.MediaRecorder;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    public  double noismax=0,noismin=9000000000.0d;
    TextView mStatusView,noismaxview,noiseminview,noisebd;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();

            //noismaxview.setText(Double.toString((getAmpmaxA())) + " dB");
        };
    };
    final Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maain);
        mStatusView = (TextView) findViewById(R.id.nois);
        noisebd = (TextView)findViewById(R.id.noisebdText);


        noismaxview = (TextView)findViewById(R.id.noismaxText);
        noiseminview = (TextView)findViewById(R.id.noisminText);

        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }

    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
                     mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){
       // Double noisa = getAmplitudeEMA();
      //  mStatusView.setText(noisa + " dB");
        mStatusView.setText(Double.toString((getAmplitudeEMA())) + " dB");


        noisebd.setText(""+ soundDb((getAmplitudeEMA())) +"dB");




//String noisb = Double.toString((getAmplitudeEMA()));
       // Double noisa = getAmplitudeEMA();
       /* if (noisa>=noismax){
            noismax =noisa;
            //     bx=maxx;
        }

        if (noisa<=noismin)
        {

            noismin =noisa;
            //   ax=minx;
        }

*/

   noismaxview.setText(""+ soundDb(noismax) +"dB");
        if (mEMA>=noismax){
            noismax =mEMA;
            //     bx=maxx;
        }

        noiseminview.setText(""+ soundDb(noismin) +"dB");
        if (mEMA<=noismin&& mEMA>0)
        {

            noismin =mEMA;
            //   ax=minx;
        }
  }



    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;


       /* if (mEMA>=noismax){
            noismax =mEMA;
            //     bx=maxx;
        }

        if (mEMA<=noismin&& mEMA>0)
        {

            noismin =mEMA;
            //   ax=minx;
        }
*/
        // Double noisa = getAmplitudeEMA();
       /* if (noisa>=noismax){
            noismax =noisa;
            //     bx=maxx;
        }

        if (noisa<=noismin)
        {

            noismin =noisa;
            //   ax=minx;
        }


        noismaxview.setText(""+ noismax);
        noiseminview.setText(""+ noismin);
*/

        return mEMA;
    }

}

