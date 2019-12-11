package dvr.com.bluetoothapp.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import dvr.com.bluetoothapp.R;

public class RealtimeUpdates extends Fragment {

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 5d;
    private double graph1LastXValue = 5d;
    double mLastRandom = 2;
    Random mRand = new Random();
    Paint paintone;
    Paint painttwo;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        paintone = new Paint();
        paintone.setColor(getResources().getColor(R.color.red));
        // GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();
        //  graph.addSeries(mSeries1);


        paintone.setStrokeWidth(10);
        mSeries1.setCustomPaint(paintone);


        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);
        mSeries2 = new LineGraphSeries<>();


        graph2.addSeries(mSeries2);
        graph2.addSeries(mSeries1);
        graph2.getViewport().setXAxisBoundsManual(true);

        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(40);


        graph2.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.white));
        graph2.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.white));


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mTimer2 = new Runnable() {
            @Override
            public void run() {


                graph2LastXValue += 1d;
                graph1LastXValue += 1d;


                mSeries1.appendData(new DataPoint(graph1LastXValue, getRandom()), true, 40);
                mSeries2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);


                mHandler.postDelayed(this, 200); // run current thread little slow
            }
        };
        mHandler.postDelayed(mTimer2, 1000); // run after every second
    }

    @Override
    public void onPause() {
        // mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }


    private double getRandom() {
        return mLastRandom += mRand.nextDouble() * 0.5 - 0.25;
    }
}