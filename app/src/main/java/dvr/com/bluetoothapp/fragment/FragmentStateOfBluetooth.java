package dvr.com.bluetoothapp.fragment;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import java.util.List;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStateOfBluetooth extends Fragment {
    private RecyclerView recData;
    private List<String> dataList = new ArrayList<>();
    private List<String> spinnerList = new ArrayList<>();
    private View view;
    private Spinner spinner;
    Paint p2;
    Paint p1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_state_of_bluetooth, container, false);
        spinner = view.findViewById(R.id.spinner);
        recData = view.findViewById(R.id.rcData);
        if (getArguments() != null){
            if (getArguments().containsKey("title")){
                TextView textView = view.findViewById(R.id.tvfragtype);
                textView.setText(getArguments().get("title").toString());
            }
        }
        GraphView graph = view.findViewById(R.id.any_chart_view);
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//      graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);      // x min
        graph.getViewport().setMaxX(50);     // x max

//      graph.getViewport().setYAxisBoundsManual(true);
//      graph.getViewport().setMinY(200);      // y mi
//      graph.getViewport().setMaxY(2000);     // y max
        p2 = new Paint();
        p1 = new Paint();

        p2.setColor(getResources().getColor(R.color.red));
        p1.setColor(getResources().getColor(R.color.white));

        p1.setStrokeWidth(4);     //10
        p2.setStrokeWidth(4);    //12

        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.grey));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.grey));
        graph.getGridLabelRenderer().setTextSize(18);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),

                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
                new DataPoint(1, 1),
        });
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 5),
                new DataPoint(6, 3),
                new DataPoint(7, 8),
                new DataPoint(8, 1),
                new DataPoint(9, 8),
                new DataPoint(10, 6),
                new DataPoint(11, 5),
                new DataPoint(12, 3),
                new DataPoint(13, 2),
                new DataPoint(14, 6)
        });
        series.setCustomPaint(p2);
        series1.setCustomPaint(p1);
        graph.addSeries(series1);
        graph.addSeries(series);
        addStaticValueOnRecycler();

        return view;

    }

    private void addStaticValueOnRecycler() {

        String[] years = {"1 Inch", "2 Inch", "3 Inch", "4 Inch"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.row_spinner, spinnerList);
        spinner.setAdapter(new SpinnerAdapter(view.getContext(), R.layout.row_spinner, years));
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        dataList.add("928,865,202");
        recData.setAdapter(new StateAdpter(view.getContext(), dataList));
    }

    //todo ============================ Adapter class =====================================

    class StateAdpter extends RecyclerView.Adapter<StateAdpter.StateViewHolder> {
        private Context context;
        private List<String> stateList;

        public StateAdpter(Context context, List<String> stateList) {
            this.context = context;
            this.stateList = stateList;
        }

        @NonNull
        @Override
        public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_bluetooth, parent, false);
            return new StateViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {

            holder.value.setText(stateList.get(position));
        }

        @Override
        public int getItemCount() {
            return stateList.size();
        }

        class StateViewHolder extends RecyclerView.ViewHolder {

            private TextView value;

            public StateViewHolder(@NonNull View itemView) {
                super(itemView);
                value = itemView.findViewById(R.id.tvdta);
            }
        }

    }

}
