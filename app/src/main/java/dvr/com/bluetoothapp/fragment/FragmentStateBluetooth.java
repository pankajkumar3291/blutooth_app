package dvr.com.bluetoothapp.fragment;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dvr.com.bluetoothapp.MainActivity;
import dvr.com.bluetoothapp.Model.DataBluetooth;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.Util.MyUtil;
import dvr.com.bluetoothapp.adapters.BluetoothAdapter;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
import dvr.com.bluetoothapp.local_data_base.DatabaseHandler;
import dvr.com.bluetoothapp.local_data_base.GraphDataModelClass;
import dvr.com.bluetoothapp.other_classes.InfoModel;
import dvr.com.bluetoothapp.other_classes.InputFinalRecord;
import dvr.com.bluetoothapp.other_classes.InputModel;
import dvr.com.bluetoothapp.other_classes.InternalStorage;

import static dvr.com.bluetoothapp.Model.Datakeys.FEET_AND_INCHES_DATA;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_FINAL_RECORD;
import static dvr.com.bluetoothapp.Model.Datakeys.MM_DATA;

public class FragmentStateBluetooth extends Fragment {

    private static final String TAG = "FragmentStateBluetooth";

    @BindView(R.id.floatingActionButton)
    ImageView imgPlayPause;


    private boolean isPlaying = false;

    @BindView(R.id.tvfragtype)
    TextView tvFragtype;

    @BindView(R.id.tvdatetype)
    TextView tvDateType;

    @BindView(R.id.tvnumthirddata)
    TextView tvThirdData;

    @BindView(R.id.rcData)
    RecyclerView recyclerView;

    @BindView(R.id.edCinta)
    EditText edCinta;

    @BindView(R.id.any_chart_view)
    GraphView graph;

    @BindView(R.id.textView59)
    TextView tvDatCalculated;

    @BindView(R.id.tvDatacintaandmore)
    TextView tvBFirstValue;

    @BindView(R.id.textView12)
    TextView tvBSecondVlue;

    private OnFragmentInteractionListener mListener;

    LinearLayoutManager linearLayoutManager;

    private static LineGraphSeries<DataPoint> mSeries1;
    private static LineGraphSeries<DataPoint> mSeries2;
    private static LineGraphSeries<DataPoint> mSeries3;
    private static LineGraphSeries<DataPoint> mSeries4;

    private static double graph2LastXValue = 5d;
    private static double graph1LastXValue = 5d;
    private static double graph3LastXValue = 5d;
    private static double graph4LastXValue = 5d;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;   // could be fop fsp mmp
    private String mParam2;
    List<DataBluetooth> dataBluetoothList = null;
    android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    BluetoothAdapter bluetoothAdapter;
    float xAxis = 0;

    private ArrayList<Entry> dataset1;
    private ArrayList<Entry> dataset2;
    private ArrayList<ILineDataSet> lines;
    private String dataSpecific = "";
    private String dataLastLine = "";
    private boolean isConnected = false;
    private ImageView imageView;
    private Spinner spinner;
    private EditText etfeet, etInches;
    private View view;
    private static Paint p2;
    private static Paint p1;
    private static Paint p3;
    private static Paint p4;
    private Context context;

    public FragmentStateBluetooth() {
        // Required empty public constructor
    }

    private void pauseFiveSeconds() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgPlayPause.setClickable(true);
            }
        }, 5000);
    }

    public static FragmentStateBluetooth newInstance(String param1, String param2, String fromvalue) {
        FragmentStateBluetooth fragment = new FragmentStateBluetooth();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("fromValue", fromvalue);
        fragment.setArguments(args);
        return fragment;
    }

    private GraphDataModelClass feetAndInches;
    private GraphDataModelClass mm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private InputFinalRecord feetAndInchesFinal;
    private InputFinalRecord mmFinal;


    private void setfeetInchvalue() {
        switch (mParam1) {
            case "FOP":
                tvBFirstValue.setText(feetAndInches.getFop().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getFop().getValue_second());
                tvDatCalculated.setText(feetAndInches.getFop().getValue_third());
                break;
            case "FOS":
                tvBFirstValue.setText(feetAndInches.getFos().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getFos().getValue_second());
                tvDatCalculated.setText(feetAndInches.getFos().getValue_third());
                break;
            case "MSP":
                tvBFirstValue.setText(feetAndInches.getMsp().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getMsp().getValue_second());
                tvDatCalculated.setText(feetAndInches.getMsp().getValue_third());
                break;
            case "MSS":
                tvBFirstValue.setText(feetAndInches.getMss().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getMss().getValue_second());
                tvDatCalculated.setText(feetAndInches.getMss().getValue_third());
                break;
            case "AFP":
                tvBFirstValue.setText(feetAndInches.getAfp().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getAfp().getValue_second());
                tvDatCalculated.setText(feetAndInches.getAfp().getValue_third());
                break;
            case "AFS":
                tvBFirstValue.setText(feetAndInches.getAfs().getValue_first());
                tvBSecondVlue.setText(feetAndInches.getAfs().getValue_second());
                tvDatCalculated.setText(feetAndInches.getAfs().getValue_third());
                break;
        }
    }


    private void setmmvalue() {
        switch (mParam1) {
            case "FOP":
                tvBFirstValue.setText(mm.getFop().getValue_first());
                tvBSecondVlue.setText(mm.getFop().getValue_second());
                tvDatCalculated.setText(mm.getFop().getValue_third());
                break;
            case "FOS":
                tvBFirstValue.setText(mm.getFos().getValue_first());
                tvBSecondVlue.setText(mm.getFos().getValue_second());
                tvDatCalculated.setText(mm.getFos().getValue_third());
                break;
            case "MSP":
                tvBFirstValue.setText(mm.getMsp().getValue_first());
                tvBSecondVlue.setText(mm.getMsp().getValue_second());
                tvDatCalculated.setText(mm.getMsp().getValue_third());
                break;
            case "MSS":
                tvBFirstValue.setText(mm.getMss().getValue_first());
                tvBSecondVlue.setText(mm.getMss().getValue_second());
                tvDatCalculated.setText(mm.getMss().getValue_third());
                break;
            case "AFP":
                tvBFirstValue.setText(mm.getAfp().getValue_first());
                tvBSecondVlue.setText(mm.getAfp().getValue_second());
                tvDatCalculated.setText(mm.getAfp().getValue_third());
                break;
            case "AFS":
                tvBFirstValue.setText(mm.getAfs().getValue_first());
                tvBSecondVlue.setText(mm.getAfs().getValue_second());
                tvDatCalculated.setText(mm.getAfs().getValue_third());
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_state_bluetooth, container, false);
        context = view.getContext();
        findViewById();
        ButterKnife.bind(this, view);
        spinner = view.findViewById(R.id.spinner);

        if (getArguments().containsKey("fromValue")) {
            switch (getArguments().getString("fromValue")) {
                case "Feet and Inches":
                    if (InternalStorage.isFileContains(context, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(context, MM_DATA)) {

                        try {

                            feetAndInches = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);

                            setfeetInchvalue();
                            // mm = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "mm":
                    if (InternalStorage.isFileContains(context, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(context, MM_DATA)) {

                        try {

                            //feetAndInches = (GraphDataModelClass) InternalStorage.readObject(context, FEET_AND_INCHES_DATA);
                            mm = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                            setmmvalue();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner.getSelectedItem().toString()) {
                    case "Ft-In":
                        etfeet.setVisibility(View.VISIBLE);
                        etInches.setVisibility(View.VISIBLE);
                        etfeet.setHint("Ft");
                        etInches.setHint("In");
                        break;
                    case "mm":
                        etfeet.setVisibility(View.INVISIBLE);
                        etInches.setVisibility(View.VISIBLE);
                        etInches.setHint("Enter mm");
                        break;
                    case "m":
                        etfeet.setVisibility(View.INVISIBLE);
                        etInches.setVisibility(View.VISIBLE);
                        etInches.setHint("Enter m");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        checkBluetoothConnection();
        tvFragtype.setText(mParam1);

        String[] value = {"Ft-In", "mm", "m"};
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(view.getContext(), R.layout.row_spinner, value);
        spinner.setAdapter(spinnerAdapter);


        mSeries1 = new LineGraphSeries<>();
        mSeries2 = new LineGraphSeries<>();
        mSeries3 = new LineGraphSeries<>();
        mSeries4 = new LineGraphSeries<>();

        p2 = new Paint();
        p1 = new Paint();
        p3 = new Paint();
        p4 = new Paint();

        p2.setColor(getResources().getColor(R.color.white));
        p1.setColor(getResources().getColor(R.color.red));
        p3.setColor(getResources().getColor(R.color.green));
        p4.setColor(getResources().getColor(R.color.green));

        p1.setStrokeWidth(4);   //10
        p2.setStrokeWidth(4);   //12
        p3.setStrokeWidth(4);
        p4.setStrokeWidth(4);

        mSeries1.setCustomPaint(p1);
        mSeries2.setCustomPaint(p2);
        mSeries3.setCustomPaint(p3);
        mSeries4.setCustomPaint(p4);

        graph.addSeries(mSeries2);
        graph.addSeries(mSeries1);
        graph.addSeries(mSeries3);
        graph.addSeries(mSeries4);

        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//      graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);    // x min
        graph.getViewport().setMaxX(300);    // x max

//      graph.getViewport().setYAxisBoundsManual(true);
//      graph.getViewport().setMinY(200);      // y mi
//      graph.getViewport().setMaxY(2000);     // y max

        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.grey));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.grey));
        graph.getGridLabelRenderer().setTextSize(18);

        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        tvDateType.setText(fmt.format(calendar.getTime()));
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (dataBluetoothList == null)
            dataBluetoothList = new ArrayList<>();

        bluetoothAdapter = new BluetoothAdapter(dataBluetoothList);
        recyclerView.setAdapter(bluetoothAdapter);

        if (Hawk.get(mParam1 + "list", null) != null) {   // could be fop fos etc,
//      dataSpecific = dataSpecific.concat(arrData[0]+";"+arrData[1]+";"+arrData[2]+";"+cinta+";"+correction+";"+total);

            if (dataBluetoothList.size() > 0)
                dataBluetoothList.clear();

            dataBluetoothList = Hawk.get(mParam1 + "list");

            if (dataBluetoothList.size() > 0) {
                bluetoothAdapter.notifyDataSetChanged();
                tvThirdData.setText("SW" + dataBluetoothList.get(dataBluetoothList.size() - 1).getNum2() + "mm");
                recyclerView.scrollToPosition(dataBluetoothList.size() - 1);
            }
        }
//        dataset1 = new ArrayList<Entry>();
//        dataset2 = new ArrayList<Entry>();
//        lines = new ArrayList<ILineDataSet> ();
//        LineDataSet lDataSet1 = new LineDataSet(dataset1, "Num One");
//        lDataSet1.setColor(Color.RED);
//        lDataSet1.setCircleColor(Color.RED);
//        lines.add(lDataSet1);
//        lines.add(new LineDataSet(dataset2, "Num Two"));

        Log.i(TAG, "correction get -->" + MyUtil.getCorrection(getActivity()));

//        for(int i=0;i<1000;i++)
//        {
//            dataBluetoothList.add(new DataBluetooth("item"+i, "item"+i,"item"+i));
//            bluetoothAdapter.notifyDataSetChanged();
//            recyclerView.scrollToPosition(dataBluetoothList.size() - 1);
//        }


        return view;
    }

    private void checkBluetoothConnection() {
        imageView = getActivity().findViewById(R.id.imgconnect);
        imageView.setVisibility(View.VISIBLE);
        mBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equalsIgnoreCase("DS HARITZ")) {
                    isConnected = true;
                    imageView.setImageResource(R.drawable.ic_action_bluetoothconnect);
                    break;
                }
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean isPlaying);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.floatingActionButton)
    public void setImgPlayPause() {

        if (isConnected) {
            if (!isPlaying) {
                graph.clearSecondScale();
                graph.invalidate();

                imgPlayPause.setClickable(false);
                pauseFiveSeconds();
                //start listening
                if (dataBluetoothList.size() > 0)
                    dataBluetoothList.clear();
                bluetoothAdapter.notifyDataSetChanged();

                isPlaying = true;
                mListener.onFragmentInteraction(isPlaying);
                dataSpecific = "";
                dataLastLine = "";

                imgPlayPause.setImageResource(R.drawable.ic_action_stop);
                //  dataSpecific = dataSpecific.concat(mParam1 + ":" + "\n");
                Hawk.delete(mParam1); //could me fop fos etc....
                Hawk.delete(mParam1 + "last"); //last line from any thing....
                Hawk.delete(mParam1 + "list"); //delete from any thing.... like fop fos

                startListingBlueTooth();
            } else {

                isPlaying = false;                                              //stop listening
                mListener.onFragmentInteraction(isPlaying);

                imgPlayPause.setImageResource(R.drawable.ic_action_play);

                Hawk.put(mParam1, dataSpecific); //could me fop fos etc....
                Hawk.put(mParam1 + "last", dataLastLine); //last line from any thing....
                Hawk.put(mParam1 + "list", dataBluetoothList);
                stopListeningBluetooth();


                if (getArguments() != null) {
                    if (getArguments().containsKey("fromValue")) {
                        switch (getArguments().getString("fromValue")) {
                            case "Feet and Inches":
                                GraphDataModelClass feetAndInchesData = null;
                                try {
                                    feetAndInchesData = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                                    checkType(MM_DATA, feetAndInchesData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "mm":
                                GraphDataModelClass mmData = null;
                                try {
                                    mmData = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                                    checkType(MM_DATA, mmData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }


                Log.i(TAG, "setImgPlayPause: graph and list clear");
            }
        } else {
//            TastyToast.makeText(getActivity(),"Device Not Connected",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            mBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equalsIgnoreCase("DS HARITZ")) {
                        isConnected = true;
                        imageView.setImageResource(R.drawable.ic_action_bluetoothconnect);
                        break;
                    }
                }
            }
        }
    }

    private void checkType(String key, GraphDataModelClass graphDataModelClass) {


        switch (mParam1) {
            case "FOP":
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setFop(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {
                    graphDataModelClass.setFop(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }

                break;
            case "FOS":
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setFos(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {
                    graphDataModelClass.setFos(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }


                break;
            case "MSP":
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setMsp(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {
                    graphDataModelClass.setMsp(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }

                break;
            case "MSS":

                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setMss(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {

                    graphDataModelClass.setMss(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }
                break;
            case "AFP":

                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setAfp(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {
                    graphDataModelClass.setAfp(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }

                break;
            case "AFS":
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In")) {
                    graphDataModelClass.setAfs(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                } else {
                    graphDataModelClass.setAfs(new InputModel(tvBFirstValue.getText().toString(), tvBSecondVlue.getText().toString(), tvDatCalculated.getText().toString()));
//                    Hawk.put(key, graphDataModelClass);
                }
                break;
        }

        if (InternalStorage.isFileContains(context, key)) {
            try {
                InternalStorage.deleteFolder(context, key);
                InternalStorage.writeObject(context, key, graphDataModelClass);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void stopListeningBluetooth() {
        try {
//            sendData("R");
            closeBT();
        } catch (Exception e) {
            e.printStackTrace();
            TastyToast.makeText(getActivity(), e.getLocalizedMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }
    }

    private void startListingBlueTooth() {
        try {
            findBT();
            openBT();
        } catch (Exception ex) {
            TastyToast.makeText(getActivity(), ex.getLocalizedMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }
    }

    private void findBT() {
        mBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
//            TastyToast.makeText(getActivity( ), "No bluetooth adapter available", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show( );
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equalsIgnoreCase("DS HARITZ")) {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    private void openBT() throws Exception {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        beginListenForData();
        try {
            sendData("R");     // when start listining  sending R
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();

                        if (bytesAvailable > 0) {

                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    final byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {

                                            try {
                                                if (getActivity() != null) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            final String arrData[] = data.split(";");
                                                            Log.i(TAG, "Data Recived-->> " + arrData[0] + "," + arrData[1] + "," + arrData[2]);
                                                            tvThirdData.setText("SW" + arrData[2] + "mm");

                                                            graph2LastXValue += 1d;
                                                            graph1LastXValue += 1d;
                                                            graph3LastXValue += 1d;
                                                            graph4LastXValue += 1d;

                                                            //TODO add here values to show on graph, according to that line will fluctuate
                                                            mSeries1.appendData(new DataPoint(graph1LastXValue, Double.parseDouble(arrData[0])), true, 20000000);   // try here
                                                            mSeries2.appendData(new DataPoint(graph2LastXValue, Double.parseDouble(arrData[1])), true, 20000000);
                                                            double X = Double.parseDouble(arrData[2]) / 2;
                                                            double graph3Values = Double.parseDouble(arrData[1]) + X;
                                                            double graph4Values = Double.parseDouble(arrData[1]) - X;
                                                            mSeries3.appendData(new DataPoint(graph3LastXValue, graph3Values), true, 20000000);
                                                            mSeries4.appendData(new DataPoint(graph4LastXValue, graph4Values), true, 20000000);

                                                            dataBluetoothList.add(new DataBluetooth(arrData[0], arrData[1], arrData[2]));
                                                            bluetoothAdapter.notifyDataSetChanged();
                                                            recyclerView.scrollToPosition(dataBluetoothList.size() - 1);

                                                            int correction = Integer.parseInt(MyUtil.getCorrection(getActivity()));
                                                            int num2 = Integer.valueOf(arrData[1]);
                                                            int cinta = Integer.parseInt(edCinta.getText().toString());
                                                            double bThirdTotal = 0;
                                                            double bFirstValue = 0;
                                                            double bSecondValue;
                                                            double bSecondTemp = 0;
                                                            DecimalFormat df = new DecimalFormat("#.###");
                                                            DecimalFormat df2 = new DecimalFormat("#.######");
                                                            if (spinner.getSelectedItem().toString().equalsIgnoreCase("Ft-In"))// it is ok
                                                            {
                                                                int feet = Integer.valueOf(TextUtils.isEmpty(etfeet.getText().toString()) ? "0" : etfeet.getText().toString());
                                                                int inch = Integer.valueOf(TextUtils.isEmpty(etInches.getText().toString()) ? "0" : etInches.getText().toString());
                                                                bThirdTotal = ((((feet * 12) + inch)) * 25.4) + num2 + correction;
                                                                bFirstValue = bThirdTotal / 304.8;
                                                                bSecondValue = (bThirdTotal / 25.4) / 12;
                                                                String[] arr = String.valueOf(bSecondValue).split("\\.");
                                                                bSecondTemp = Double.valueOf("0." + arr[1]) * 12;
                                                                int finalthirdValue = (int) bThirdTotal;
                                                                view.findViewById(R.id.textView61).setVisibility(View.GONE);
                                                                tvBSecondVlue.setText(arr[0] + " Ft " + df.format(bSecondTemp) + " In");
                                                                tvDatCalculated.setText(finalthirdValue + " mm");
                                                                tvBFirstValue.setText(df.format(bFirstValue) + " Ft");
                                                                Hawk.put(mParam1 + "value", tvBSecondVlue.getText().toString());

                                                            } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("mm")) // please check for mm
                                                            {
                                                                int inch = Integer.valueOf(TextUtils.isEmpty(etInches.getText().toString()) ? "0" : etInches.getText().toString());
                                                                bThirdTotal = inch + num2 + correction;
                                                                bFirstValue = bThirdTotal / 304.8;
                                                                bSecondValue = (bThirdTotal / 25.4) / 12;
                                                                String[] arr = String.valueOf(bSecondValue).split("\\.");
                                                                bSecondTemp = Double.valueOf("0." + arr[1]) * 12;
                                                                int finalthirdValue = (int) bThirdTotal;
                                                                view.findViewById(R.id.textView61).setVisibility(View.GONE);
                                                                tvBSecondVlue.setText(arr[0] + " Ft " + df.format(bSecondTemp) + " In");
                                                                tvDatCalculated.setText(finalthirdValue + " mm");
                                                                tvBFirstValue.setText(df.format(bFirstValue) + " Ft");
                                                                Hawk.put(mParam1 + "value", tvBSecondVlue.getText().toString());


                                                            } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("m")) {

                                                                int inch = Integer.valueOf(TextUtils.isEmpty(etInches.getText().toString()) ? "0" : etInches.getText().toString());
                                                                bThirdTotal = (inch * 1000) + num2 + correction;
                                                                bFirstValue = bThirdTotal / 304.8;
                                                                bSecondValue = (bThirdTotal / 25.4) / 12;
                                                                String[] arr = String.valueOf(bSecondValue).split("\\.");
                                                                bSecondTemp = Double.valueOf("0." + arr[1]) * 12;
                                                                int finalthirdValue = (int) bThirdTotal;
                                                                view.findViewById(R.id.textView61).setVisibility(View.GONE);
                                                                tvBSecondVlue.setText(arr[0] + " Ft " + df.format(bSecondTemp) + " In");
                                                                tvDatCalculated.setText(finalthirdValue + " mm");
                                                                tvBFirstValue.setText(df.format(bFirstValue) + " Ft");
                                                                Hawk.put(mParam1 + "value", tvBSecondVlue.getText().toString());


                                                            }
//                                                            int total = correction + num2 + cinta;
                                                            Log.i(TAG, "Data recived:" + "correction" + correction + "num2" + num2 + "cinta" + cinta);

//                                                            tvDatCalculated.setText(total + "");

                                                            // dataSpecific = mParam1+" " +dataSpecific.concat(arrData[0] + ";" + arrData[1] + ";" + arrData[2] + ";" + cinta + ";" + correction + ";" + bThirdTotal + "\n");
                                                            dataSpecific = dataSpecific.concat(mParam1 + ";" + arrData[0] + ";" + arrData[1] + ";" + arrData[2] + ";" /*+ cinta + ";" + correction + ";"*/ + bThirdTotal + "\n");
                                                            dataLastLine = (arrData[0] + ";" + arrData[1] + ";" + arrData[2] + ";" + cinta + ";" + correction + ";" + bThirdTotal);

                                                            Log.i(TAG, "data Specific-->>" + dataSpecific);
                                                            Log.i(TAG, "data Specific last-->>" + dataLastLine);
                                                        }
                                                    });
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.i(TAG, "Activity Crash " + e.getLocalizedMessage());
                                            }
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    private void sendData(String message) throws Exception {
        String msg = message;
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
    }

    private void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
    }

    private void findViewById() {
        etfeet = view.findViewById(R.id.editText3);
        etInches = view.findViewById(R.id.editText6);

        List<InfoModel> list = null;

        if (InternalStorage.isFileContains(view.getContext(), "infoList")) {

            try {
                list = (List<InfoModel>) InternalStorage.readObject(view.getContext(), "infoList");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (list != null) {
            TextView tvProjName2 = getActivity().findViewById(R.id.tvpname);
            tvProjName2.setText(list.get(2).getValue());
            tvProjName2.setVisibility(View.VISIBLE);
        }


        View linev = getActivity().findViewById(R.id.tvline);
        TextView tvProjName = getActivity().findViewById(R.id.tv_proj);

        tvProjName.setVisibility(View.VISIBLE);

        linev.setVisibility(View.GONE);
    }
}
