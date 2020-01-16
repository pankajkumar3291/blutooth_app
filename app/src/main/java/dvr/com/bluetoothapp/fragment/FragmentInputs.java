package dvr.com.bluetoothapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
import dvr.com.bluetoothapp.local_data_base.DatabaseHandler;
import dvr.com.bluetoothapp.local_data_base.GraphDataModelClass;
import dvr.com.bluetoothapp.other_classes.InfoModel;
import dvr.com.bluetoothapp.other_classes.InputFinalRecord;
import dvr.com.bluetoothapp.other_classes.InputFinalRecordType;
import dvr.com.bluetoothapp.other_classes.InputModel;
import dvr.com.bluetoothapp.other_classes.InternalStorage;

import static dvr.com.bluetoothapp.Model.Datakeys.FEET_AND_INCHES_DATA;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_FINAL_RECORD;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_SELECTED_TYPE;
import static dvr.com.bluetoothapp.Model.Datakeys.MM_DATA;

/**
 * A simple {@link Fragment} subclass.
 */


public class FragmentInputs extends Fragment implements View.OnClickListener {
    private ConstraintLayout constraintLayout;
    private View view;
    private Context context;
    private Spinner sptopDropDown;
    private String tvSpText;
    //    private TextView
    private DatabaseHandler feet;
    private GraphDataModelClass feetAndInches;
    private GraphDataModelClass mm;
    private InputFinalRecord feetAndInchesFinal;
    private InputFinalRecord mmFinal;
    private InputFinalRecordType inputFinalRecordType;
    private EditText etNote;
    private TextView oneP_textView, twoP_textView, threeP_textView, fourP_TextView, fiveP_text_View,
            oneS_textView, twoS_textView, threeS_textView, fourS_TextView, fiveS_text_View, tvFore, tvAFT, tvfop, tvfos, tvmsp, tvmss, tvafp, tvafs, tvfam, tvfsm, tvmom, tvqtm, tvheight, tvqtr;

    public FragmentInputs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_inputs, container, false);
        context = view.getContext();
        findIdOfViews();
        AllViewListners();
        constraintLayout = view.findViewById(R.id.constraintLayout11);
        constraintLayout.setOnClickListener(this);
        sptopDropDown = view.findViewById(R.id.spinner4);

        if (InternalStorage.isFileContains(context, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(context, MM_DATA) && Hawk.get(INPUT_FINAL_RECORD) != null) {

            inputFinalRecordType = Hawk.get(INPUT_FINAL_RECORD);
            try {
                feetAndInches = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                mm = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
                feetAndInchesFinal = inputFinalRecordType.getFeet_and_inches();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            addDataToList();
        }

        String[] value = {"Feet and Inches", "mm"};
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), R.layout.row_spinner, value);
        sptopDropDown.setAdapter(spinnerAdapter);

        if (Hawk.get(INPUT_SELECTED_TYPE) != null) {
            int pos = spinnerAdapter.getPosition(Hawk.get(INPUT_SELECTED_TYPE));
            sptopDropDown.setSelection(pos);
        } else
            Hawk.put(INPUT_SELECTED_TYPE, sptopDropDown.getSelectedItem().toString());

        tvSpText = sptopDropDown.getSelectedItem().toString();

        sptopDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sptopDropDown.getSelectedItem().toString().equalsIgnoreCase("Feet and Inches")) {
                    displayalData(mm);
                    setfinalcount(inputFinalRecordType.getFeet_and_inches());

                } else {
                    displayalData(mm);
                    setfinalcount(inputFinalRecordType.getFeet_and_inches());
                }
                Hawk.put(INPUT_SELECTED_TYPE, sptopDropDown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                feetAndInchesFinal.setNote(etNote.getText().toString());
                inputFinalRecordType.setFeet_and_inches(feetAndInchesFinal);


                Hawk.put("note", etNote.getText().toString());
            }
        });

        return view;
    }

    private void overallCalculation() {


        double heighttemp = 0;
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
            heighttemp = Double.valueOf(TextUtils.isEmpty(list.get(10).getValue()) ? "0.0" : list.get(10).getValue());
        }
        DecimalFormat df2 = new DecimalFormat("#.###");

        double fop1 = (Double.valueOf(TextUtils.isEmpty(tvfop.getText().toString().replaceAll("[^.,0-9]", "")) ? "0" : tvfop.getText().toString().replaceAll("[^.,0-9]", "").replace(",", ".")));

        double fos1 = (Double.valueOf(TextUtils.isEmpty(tvfos.getText().toString().replaceAll("[^.,0-9]", "")) ? "0" : tvfos.getText().toString().replaceAll("[^.,0-9]", "").replace(",", ".")));

        double msp1 = (Double.valueOf(TextUtils.isEmpty(tvmsp.getText().toString().replaceAll("[^,.0-9]", "")) ? "0" : tvmsp.getText().toString().replaceAll("[^.,0-9]", "").replace(",", ".")));

        double mss1 = (Double.valueOf(TextUtils.isEmpty(tvmss.getText().toString().replaceAll("[^.,0-9]", "")) ? "0" : tvmss.getText().toString().replaceAll("[^.,0-9]", "").replace(",", ".")));

        double afp1 = (Double.valueOf(TextUtils.isEmpty(tvafp.getText().toString().replaceAll("[^,.0-9]", "")) ? "0" : tvafp.getText().toString().replaceAll("[^,.0-9]", "").replace(",", ".")));

        double afs1 = (Double.valueOf(TextUtils.isEmpty(tvafs.getText().toString().replaceAll("[^,.0-9]", "")) ? "0" : tvafs.getText().toString().replaceAll("[^.,0-9]", "").replace(",", ".")));

        double fam = (fop1 + fos1 + afp1 + afs1) / 4;
        double msm = (mss1 + msp1) / 2;
        double mom = (fam + msm) / 2;
        double qtm = (msm + mom) / 2;

        double height = 0;
        if (Hawk.get("height").toString() != null || !TextUtils.isEmpty(Hawk.get("height").toString()))
            height = Double.valueOf(Hawk.get("height").toString().replace(",", "."));
        double qtr = height - qtm;

        inputFinalRecordType.setFeet_and_inches(new InputFinalRecord(df2.format(fam), df2.format(msm), df2.format(mom), df2.format(qtm), df2.format(height), df2.format(qtr), ""));
        Hawk.put(INPUT_FINAL_RECORD, inputFinalRecordType);
    }

    private void setfinalcount(InputFinalRecord inputfrecord) {
        tvfam.setText("A_FAM: " + inputfrecord.getFam());
        tvfsm.setText("A_MSM: " + inputfrecord.getMsm());
        tvmom.setText("A_MOM: " + inputfrecord.getMom());
        tvqtm.setText("A_QTR: " + inputfrecord.getQtm());
        tvheight.setText("HEIGHT: " + inputfrecord.getHeigh());
        tvqtr.setText("QTR: " + inputfrecord.getQtr());

        if (Hawk.get("note") != null)
            etNote.setText(Hawk.get("note"));
        else {
            etNote.setText("");
            Hawk.put("note", "");
        }

    }


    @Override
    public void onDestroy() {

        overallCalculation();
        super.onDestroy();


    }

    private void displayalData(GraphDataModelClass dataList) {


        if (sptopDropDown.getSelectedItem().toString().equalsIgnoreCase("Feet and Inches")) {
            tvfop.setText(dataList.getFop().getValue_first());
            tvfos.setText(dataList.getFos().getValue_first());
            tvmsp.setText(dataList.getMsp().getValue_first());
            tvmss.setText(dataList.getMss().getValue_first());
            tvafp.setText(dataList.getAfp().getValue_first());
            tvafs.setText(dataList.getAfs().getValue_first());

        } else {
            tvfop.setText(dataList.getFop().getValue_third());
            tvfos.setText(dataList.getFos().getValue_third());
            tvmsp.setText(dataList.getMsp().getValue_third());
            tvmss.setText(dataList.getMss().getValue_third());
            tvafp.setText(dataList.getAfp().getValue_third());
            tvafs.setText(dataList.getAfs().getValue_third());
        }
        tvAFT.setText(dataList.getAft());
        tvFore.setText(dataList.getFere());
        oneP_textView.setText(dataList.getOneP());
        oneS_textView.setText(dataList.getOneS());
        twoP_textView.setText(dataList.getTwoP());
        twoS_textView.setText(dataList.getTwoS());
        threeP_textView.setText(dataList.getThreeP());
        threeS_textView.setText(dataList.getThreeS());
        fourP_TextView.setText(dataList.getFourP());
        fourS_TextView.setText(dataList.getFourS());
        fiveP_text_View.setText(dataList.getFiveP());
        fiveS_text_View.setText(dataList.getFiveS());

        overallCalculation();
    }

    private void addDataToList() {


        if (inputFinalRecordType == null)
            inputFinalRecordType = new InputFinalRecordType();

        feetAndInches = new GraphDataModelClass(new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "");
        mm = new GraphDataModelClass(new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), new InputModel("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "");
        try {
            InternalStorage.writeObject(context, FEET_AND_INCHES_DATA, feetAndInches);
            InternalStorage.writeObject(context, MM_DATA, mm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFinalRecordType.setFeet_and_inches(new InputFinalRecord("0", "0", "0", "0", "0", "0", ""));

        feetAndInchesFinal = inputFinalRecordType.getFeet_and_inches();
        Hawk.put(INPUT_FINAL_RECORD, inputFinalRecordType);
//        Hawk.put(FEET_AND_INCHES_DATA, feetAndInches);
//        Hawk.put(MM_DATA, mm);
    }

    private void findIdOfViews() {
        tvfam = view.findViewById(R.id.textView54);
        tvfsm = view.findViewById(R.id.textView55);
        tvmom = view.findViewById(R.id.textView56);
        tvqtm = view.findViewById(R.id.textView57);
        tvheight = view.findViewById(R.id.textView58);
        tvqtr = view.findViewById(R.id.textView62);

        tvfop = view.findViewById(R.id.textView43);
        tvfos = view.findViewById(R.id.textView42);
        tvmsp = view.findViewById(R.id.textView48);
        tvmss = view.findViewById(R.id.textView46);
        tvafp = view.findViewById(R.id.textView51);
        tvafs = view.findViewById(R.id.textView50);
        etNote = view.findViewById(R.id.editText4);

        oneP_textView = view.findViewById(R.id.textView17);
        oneS_textView = view.findViewById(R.id.textView20);
        twoP_textView = view.findViewById(R.id.textView21);
        twoS_textView = view.findViewById(R.id.textView23);

        threeP_textView = view.findViewById(R.id.textView25);
        threeS_textView = view.findViewById(R.id.textView27);
        fourP_TextView = view.findViewById(R.id.textView29);
        fourS_TextView = view.findViewById(R.id.textView31);

        fiveP_text_View = view.findViewById(R.id.textView34);
        fiveS_text_View = view.findViewById(R.id.textView36);
        tvFore = view.findViewById(R.id.textView38);
        tvAFT = view.findViewById(R.id.textView40);
    }

    private void AllViewListners() {
        view.findViewById(R.id.constraintLayout12).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout14).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout13).setOnClickListener(this);
        view.findViewById(R.id.layoutthreep).setOnClickListener(this);
        view.findViewById(R.id.layout_three_s).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout16).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout15).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout18).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout17).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout9).setOnClickListener(this);
        view.findViewById(R.id.constraintLayout10).setOnClickListener(this);
        view.findViewById(R.id.textView45).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openInputsActivity("FOP");
                BluetoohFragment("FOP");
            }
        });
        view.findViewById(R.id.textView44).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoohFragment("FOS");
//                openInputsActivity("FOS");
            }
        });
        view.findViewById(R.id.textView49).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoohFragment("MSP");
//                openInputsActivity("MSP");
            }
        });
        view.findViewById(R.id.textView47).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoohFragment("MSS");
//                openInputsActivity("MSS");
            }
        });
        view.findViewById(R.id.textView53).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoohFragment("AFP");
//                openInputsActivity("AFP");
            }
        });
        view.findViewById(R.id.textView52).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoohFragment("AFS");
//                openInputsActivity("AFS");
            }
        });
    }

    private void BluetoohFragment(String type) {
        FragmentStateBluetooth fragmentStateBluetooth = FragmentStateBluetooth.newInstance(type, type, sptopDropDown.getSelectedItem().toString());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragmentStateBluetooth).commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.constraintLayout11: {
                openDialog("1P");
            }
            break;
            case R.id.constraintLayout12: {
                openDialog("1S");
            }
            break;
            case R.id.constraintLayout14: {
                openDialog("2P");
            }
            break;
            case R.id.constraintLayout13: {
                openDialog("2S");
            }
            break;
            case R.id.layoutthreep: {
                openDialog("3P");
            }
            break;
            case R.id.layout_three_s: {
                openDialog("3S");
            }
            break;
            case R.id.constraintLayout16: {
                openDialog("4P");
            }
            break;
            case R.id.constraintLayout15: {
                openDialog("4S");
            }
            break;
            case R.id.constraintLayout18: {
                openDialog("5P");
            }
            break;
            case R.id.constraintLayout17: {
                openDialog("5S");
            }
            break;
            case R.id.constraintLayout9: {
                openDialog("FORE");
            }
            break;
            case R.id.constraintLayout10:
                openDialog("AFT");
                break;
        }
    }

    private void openDialog(String fromvalue) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.dialog_input_data, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);
        TextView tvTitle = deleteDialogView.findViewById(R.id.dialog_title);
        tvTitle.setText(fromvalue + " - INPUT DATA");
        Spinner spinner = deleteDialogView.findViewById(R.id.spinner3);
        TextView tvtext = deleteDialogView.findViewById(R.id.textView33);
        ConstraintLayout viewLayout = deleteDialogView.findViewById(R.id.constraintLayout21);
        String[] value = {"---", "EMPTY", "TRC", "CNO", "MEASUREMENT"};
        spinner.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_spinner, value));
        if (sptopDropDown.getSelectedItem().toString().equalsIgnoreCase("Feet and Inches"))
            tvtext.setText("Inch");
        else
            tvtext.setText(sptopDropDown.getSelectedItem().toString());
        EditText etEditText = deleteDialogView.findViewById(R.id.editText5);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("MEASUREMENT")) {
                    viewLayout.setVisibility(View.VISIBLE);
                } else {
                    viewLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        deleteDialogView.findViewById(R.id.textView15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Measurement")) {


                    setValueonTruk(fromvalue, etEditText.getText().toString() + " " + tvtext.getText().toString());

                } else {
                    setValueonTruk(fromvalue, spinner.getSelectedItem().toString());
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.textView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    private void setValueonTruk(String fromvalue, String selectedValue) {

        switch (fromvalue) {
            case "1P":
                oneP_textView.setText(selectedValue);

                feetAndInches.setOneP(selectedValue);
                mm.setOneP(selectedValue);

                break;
            case "1S":
                oneS_textView.setText(selectedValue);
                feetAndInches.setOneS(selectedValue);
                mm.setOneS(selectedValue);
                break;
            case "2P":
                twoP_textView.setText(selectedValue);
                feetAndInches.setTwoP(selectedValue);
                mm.setTwoP(selectedValue);
                break;
            case "2S":
                twoS_textView.setText(selectedValue);
                feetAndInches.setTwoS(selectedValue);
                mm.setTwoS(selectedValue);
                break;
            case "3P":
                threeP_textView.setText(selectedValue);
                feetAndInches.setThreeP(selectedValue);
                mm.setThreeP(selectedValue);
                break;
            case "3S":
                threeS_textView.setText(selectedValue);
                feetAndInches.setThreeS(selectedValue);
                mm.setThreeS(selectedValue);
                break;
            case "4P":
                fourP_TextView.setText(selectedValue);
                feetAndInches.setFourP(selectedValue);
                mm.setFourP(selectedValue);
                break;
            case "4S":
                fourS_TextView.setText(selectedValue);
                feetAndInches.setFourS(selectedValue);
                mm.setFourS(selectedValue);
                break;
            case "5P":
                fiveP_text_View.setText(selectedValue);
                feetAndInches.setFiveP(selectedValue);
                mm.setFiveP(selectedValue);
                break;
            case "5S":
                fiveS_text_View.setText(selectedValue);
                feetAndInches.setFiveS(selectedValue);
                mm.setFiveS(selectedValue);
                break;
            case "FORE":
                tvFore.setText(selectedValue);
                feetAndInches.setFere(selectedValue);
                mm.setFere(selectedValue);
                break;
            case "AFT":
                tvAFT.setText(selectedValue);
                feetAndInches.setAft(selectedValue);
                mm.setAft(selectedValue);
                break;
        }
        try {
            if (InternalStorage.isFileContains(context, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(context, MM_DATA)) {
                InternalStorage.deleteFolder(context, FEET_AND_INCHES_DATA);
                InternalStorage.deleteFolder(context, MM_DATA);

                InternalStorage.writeObject(context, FEET_AND_INCHES_DATA, feetAndInches);
                InternalStorage.writeObject(context, MM_DATA, mm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}