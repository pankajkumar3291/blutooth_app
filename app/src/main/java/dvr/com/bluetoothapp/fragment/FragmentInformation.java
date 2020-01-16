package dvr.com.bluetoothapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dvr.com.bluetoothapp.MainActivity;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.Util.MyUtil;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
import dvr.com.bluetoothapp.other_classes.InfoBottomData;
import dvr.com.bluetoothapp.other_classes.InfoModel;
import dvr.com.bluetoothapp.other_classes.InternalStorage;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_SELECTED_TYPE;
import static dvr.com.bluetoothapp.Model.Datakeys.MM_DATA;
import static dvr.com.bluetoothapp.Model.Datakeys.REC_INCH_FIFTH;
import static dvr.com.bluetoothapp.Model.Datakeys.UOM;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInformation extends Fragment implements SetVisibilityInerface {

    private RecyclerView rec_information;
    private InformationAdapter informationAdapter;
    private static List<InfoModel> desplayList = new ArrayList<>();
    private ConstraintLayout nestedScrollView;
    private Spinner spinner;
    private Context context;
    private static EditText tvfirstValue, tvsecondValue, tvthirdValue, tvFourthValue, tvfifthValue, etmmfirst, etmmsecond, etmmthird, etmmfourth, etmmfifth;
    private static TextView tvFirstSymbol, tvSecondSymbol, tvThirdSymbol, tvFourthSymbol, tvFifthSymbol;
    private static SpinnerAdapter spinnerAdapter;
    private static List<InfoBottomData> bottomList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (InternalStorage.isFileContains(getActivity(), "infoList")) {
            List<InfoModel> list = null;
            try {
                list = (List<InfoModel>) InternalStorage.readObject(context, "infoList");
                if (InternalStorage.isFileContains(context, REC_INCH_FIFTH)) {
                    InfoBottomData bottomList = (InfoBottomData) InternalStorage.readObject(context, REC_INCH_FIFTH);
                    showBottomData(bottomList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (list != null) {
                desplayList.clear();
                desplayList.addAll(list);
                rec_information.setAdapter(informationAdapter);
                informationAdapter.notifyDataSetChanged();
            }
        } else {
            desplayList.clear();
            addInfoData();
            rec_information.setAdapter(informationAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_information, container, false);
        findViewById(view);
        context = view.getContext();
        rec_information = view.findViewById(R.id.rec_information);
        nestedScrollView = view.findViewById(R.id.nestedScrollView3);
        spinner = view.findViewById(R.id.spinner2);
        String[] value = {"Ft and In", "mm"};
        spinnerAdapter = new SpinnerAdapter(getActivity(), R.layout.row_spinner, value);
        spinner.setAdapter(spinnerAdapter);
        rec_information.setHasFixedSize(true);
        informationAdapter = new InformationAdapter(getActivity(), desplayList, this);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.title)
                        .content(R.string.content)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                cleardata();

                                if (InternalStorage.isFileContains(context, "infoList")) {
                                    try {
                                        InternalStorage.deleteFolder(context, "infoList");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (InternalStorage.isFileContains(context, REC_INCH_FIFTH)) {
                                    try {
                                        InternalStorage.deleteFolder(context, REC_INCH_FIFTH);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (InternalStorage.isFileContains(view.getContext(), MM_DATA)) {

                                    try {
                                        InternalStorage.deleteFolder(view.getContext(), MM_DATA);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Hawk.put(REC_INCH_FIFTH, "");
                                desplayList.clear();
                                if (null != Hawk.get("FOPlast", null))
                                    Hawk.delete("FOPlast");

                                if (null != Hawk.get("FOSlast", null))
                                    Hawk.delete("FOSlast");
                                if (null != Hawk.get("MSPlast", null))
                                    Hawk.delete("MSPlast");
                                if (null != Hawk.get("MSSlast", null))
                                    Hawk.delete("MSSlast");
                                if (null != Hawk.get("AFPlast", null))
                                    Hawk.delete("AFPlast");
                                if (null != Hawk.get("AFSlast", null))
                                    Hawk.delete("AFSlast");
                                if (null != Hawk.get("AFMlast", null))
                                    Hawk.delete("AFMlast");
                                if (null != Hawk.get("FOP", null))
                                    Hawk.delete("FOP");
                                if (null != Hawk.get("FOS", null))
                                    Hawk.delete("FOS");
                                if (null != Hawk.get("MSP", null))
                                    Hawk.delete("MSP");
                                if (null != Hawk.get("MSS", null))
                                    Hawk.delete("MSS");
                                if (null != Hawk.get("AFP", null))
                                    Hawk.delete("AFP");
                                if (null != Hawk.get("AFS", null))
                                    Hawk.delete("AFS");
                                if (null != Hawk.get("AFM", null))
                                    Hawk.delete("AFM");
                                addInfoData();

                                String folder_name = "Ds Haritz Pictures";
                                // Get File directory
                                File directory = new File(Environment.getExternalStorageDirectory(), folder_name);
                                deleteRecursive(directory);

                                if (directory.exists()) {
                                    directory.delete();
                                }
                                rec_information.setAdapter(informationAdapter);
                            }
                        })
                        .show();
//                desplayList.addAll(infoList);height
            }
        });
        return view;
    }


    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    private void showBottomData(InfoBottomData infoBottomData) {
        etmmfirst.setText(infoBottomData.getA_hl());
        etmmsecond.setText(infoBottomData.getB_hl_wl());
        etmmthird.setText(infoBottomData.getC_hl_end_rs());
        etmmfourth.setText(infoBottomData.getD_hl_edge_mh());
        etmmfifth.setText(infoBottomData.getE_end_mh_rake_b());
        tvfirstValue.setText(infoBottomData.getInch_first());
        tvsecondValue.setText(infoBottomData.getInch_second());
        tvthirdValue.setText(infoBottomData.getInch_third());
        tvFourthValue.setText(infoBottomData.getInch_fourth());
        tvfifthValue.setText(infoBottomData.getInch_fifth());
    }

    private void cleardata() {
        etmmfirst.setText("");
        etmmsecond.setText("");
        etmmthird.setText("");
        etmmfourth.setText("");
        etmmfifth.setText("");
        tvfirstValue.setText("");
        tvsecondValue.setText("");
        tvthirdValue.setText("");
        tvFourthValue.setText("");
        tvfifthValue.setText("");
        if (InternalStorage.isFileContains(context, REC_INCH_FIFTH)) {
            try {
                InternalStorage.deleteFolder(context, REC_INCH_FIFTH);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addInfoData() {
        desplayList.add(new InfoModel("BY:", ""));
        desplayList.add(new InfoModel("CORRECTION", ""));
        desplayList.add(new InfoModel("BARGE N", ""));
        desplayList.add(new InfoModel("CONDITION", "LIGHT"));
        desplayList.add(new InfoModel("VESSEL", ""));
        desplayList.add(new InfoModel("COMODITY", "No Value"));
        desplayList.add(new InfoModel("FILE NR", ""));
        desplayList.add(new InfoModel("FLEET:", ""));
        desplayList.add(new InfoModel("LAUNCH:", "No Value"));
        desplayList.add(new InfoModel("UOM:", "Ft and In"));
        desplayList.add(new InfoModel("HEIGHT:", ""));
        desplayList.add(new InfoModel("TYPE:", "No Value"));
        desplayList.add(new InfoModel("BARGE TYPE:", "Box"));

        if (Hawk.get("correction") != null) {
            desplayList.get(1).setValue(Hawk.get("correction"));
        }
        if (Hawk.get("condition") != null) {
            desplayList.get(3).setValue(Hawk.get("condition"));
        }
//        rec_information.setAdapter(informationAdapter);
    }

    private void findViewById(View view) {
        View linev = getActivity().findViewById(R.id.tvline);
        TextView tvProjName = getActivity().findViewById(R.id.tv_proj);
        TextView tvProjName2 = getActivity().findViewById(R.id.tvpname);
        tvProjName.setVisibility(View.GONE);
        tvProjName2.setVisibility(View.GONE);
        linev.setVisibility(View.VISIBLE);
        tvfirstValue = view.findViewById(R.id.textView6);
        tvFirstSymbol = view.findViewById(R.id.textView65);
        tvsecondValue = view.findViewById(R.id.textView60);
        tvSecondSymbol = view.findViewById(R.id.textView61);
        tvthirdValue = view.findViewById(R.id.textView601);
        tvThirdSymbol = view.findViewById(R.id.textView62);
        tvFourthValue = view.findViewById(R.id.textView602);
        tvFourthSymbol = view.findViewById(R.id.textView63);
        tvfifthValue = view.findViewById(R.id.textView603);
        tvFifthSymbol = view.findViewById(R.id.textView64);

        etmmfirst = view.findViewById(R.id.textView13);
        etmmsecond = view.findViewById(R.id.textView130);
        etmmthird = view.findViewById(R.id.textView132);
        etmmfourth = view.findViewById(R.id.textView133);
        etmmfifth = view.findViewById(R.id.textView134);
    }

    private void saveInfo() {
        if (TextUtils.isEmpty(desplayList.get(0).getValue())) {
            displayMessage("By");
            return;
        } else if (TextUtils.isEmpty(desplayList.get(1).getValue())) {
            displayMessage("CORRECTION");
            return;
        } else if (TextUtils.isEmpty(desplayList.get(2).getValue())) {
            displayMessage("BARGE N");
            return;
        } else if (TextUtils.isEmpty(desplayList.get(3).getValue())) {
            displayMessage("CONDITION");
            return;
        } else if (TextUtils.isEmpty(desplayList.get(5).getValue())) {
            displayMessage("COMODITY");
            return;
        } else if (TextUtils.isEmpty(desplayList.get(8).getValue())) {
            displayMessage("LAUNCH");
            return;
        }
        if (desplayList.get(9).getValue().equalsIgnoreCase("IMPERIAL")) {
            double heightTotal = 0;
            double heightinch = 0;

            if (Hawk.get(REC_INCH_FIFTH) == null)
                Hawk.put(REC_INCH_FIFTH, "0");

            if (Hawk.get(REC_INCH_FIFTH) != null)
                heightinch = Double.valueOf(TextUtils.isEmpty(Hawk.get(REC_INCH_FIFTH)) ? "0" : Hawk.get(REC_INCH_FIFTH));

            else if (Hawk.get(REC_INCH_FIFTH) != null)
                heightinch = Double.valueOf(Hawk.get(REC_INCH_FIFTH));
            heightTotal = (Double.valueOf(Double.valueOf(TextUtils.isEmpty(desplayList.get(10).getValue()) ? "0" : desplayList.get(10).getValue()) * 12) + heightinch) / 12;
            DecimalFormat df2 = new DecimalFormat("#.###");

            Hawk.put("height", String.valueOf(df2.format(heightTotal)));
            //  desplayList.get(10).setValue(String.valueOf(df2.format(heightTotal)));
            Hawk.put(INPUT_SELECTED_TYPE, "Feet and Inches");
        } else if (desplayList.get(9).getValue().equalsIgnoreCase("METRIC")) {
            DecimalFormat df2 = new DecimalFormat("#.###");
            Hawk.put(INPUT_SELECTED_TYPE, "mm");
            Hawk.put("height", Double.valueOf(TextUtils.isEmpty(desplayList.get(10).getValue()) ? "0" : desplayList.get(10).getValue()));
        }
        try {
            if (InternalStorage.isFileContains(context, "infoList")) {
                InternalStorage.deleteFolder(context, "infoList");
            }
            if (InternalStorage.isFileContains(context, REC_INCH_FIFTH)) {
                InternalStorage.deleteFolder(context, REC_INCH_FIFTH);
            }
            InternalStorage.writeObject(context, "infoList", desplayList);

            if (desplayList.get(12).getValue().equalsIgnoreCase("Rake")) {
                bottomList.clear();
                InfoBottomData infoBottomData = new InfoBottomData();
                infoBottomData.setA_hl(etmmfirst.getText().toString());
                infoBottomData.setB_hl_wl(etmmsecond.getText().toString());
                infoBottomData.setC_hl_end_rs(etmmthird.getText().toString());
                infoBottomData.setD_hl_edge_mh(etmmfourth.getText().toString());
                infoBottomData.setE_end_mh_rake_b(etmmfifth.getText().toString());
                infoBottomData.setInch_first(tvfirstValue.getText().toString());
                infoBottomData.setInch_second(tvsecondValue.getText().toString());
                infoBottomData.setInch_third(tvthirdValue.getText().toString());
                infoBottomData.setInch_fourth(tvFourthValue.getText().toString());
                infoBottomData.setInch_fifth(tvfifthValue.getText().toString());
                infoBottomData.setRec_inch_fifth("");
                InternalStorage.writeObject(context, REC_INCH_FIFTH, infoBottomData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        Hawk.put("infoList", desplayList);
//        Hawk.put(UOM, desplayList.get(9).getValue());
        Hawk.put("correction", desplayList.get(1).getValue());
        Hawk.put("condition", desplayList.get(3).getValue());
        MyUtil.setCorrection(getActivity(), desplayList.get(1).getValue());
        MyUtil.setLocation(getActivity(), desplayList.get(4).getValue());
        MyUtil.setBy(getActivity(), desplayList.get(0).getValue());
        Log.i(TAG, "correction Saved -->" + MyUtil.getCorrection(getActivity()));
        Toast.makeText(getActivity(), "Saved Succesfully", Toast.LENGTH_SHORT).show();
    }

    private void displayMessage(String message) {
        Toast.makeText(getContext(), "Please enter " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibility(String type) {

        switch (type) {
            case "Rake":
                nestedScrollView.setVisibility(View.VISIBLE);
                if (Hawk.get(UOM) != null) {
                    int posistion = spinnerAdapter.getPosition(Hawk.get(UOM).toString());
                    spinner.setSelection(posistion);
                }
                break;
            case "Box":
                nestedScrollView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void refereshAdapter(int pos, String ftType) {
//        desplayList.get(pos).setValue(ftType);
//
//        rec_information.setAdapter(informationAdapter);

        if (desplayList.get(12).getValue().equalsIgnoreCase("Rake")) {
            switch (ftType) {

                case "IMPERIAL":
                    etmmfirst.setHint("ft");
                    etmmsecond.setHint("ft");
                    etmmthird.setHint("ft");
                    etmmfourth.setHint("ft");
                    etmmfifth.setHint("ft");

                    tvfirstValue.setVisibility(View.VISIBLE);
                    tvFirstSymbol.setVisibility(View.VISIBLE);

                    tvsecondValue.setVisibility(View.VISIBLE);
                    tvSecondSymbol.setVisibility(View.VISIBLE);

                    tvthirdValue.setVisibility(View.VISIBLE);
                    tvThirdSymbol.setVisibility(View.VISIBLE);

                    tvFourthValue.setVisibility(View.VISIBLE);
                    tvFourthSymbol.setVisibility(View.VISIBLE);

                    tvfifthValue.setVisibility(View.VISIBLE);
                    tvFifthSymbol.setVisibility(View.VISIBLE);

                    break;
                case "METRIC":

                    etmmfirst.setHint("mm");
                    etmmsecond.setHint("mm");
                    etmmthird.setHint("mm");
                    etmmfourth.setHint("mm");
                    etmmfifth.setHint("mm");
                    tvfirstValue.setVisibility(View.GONE);
                    tvFirstSymbol.setVisibility(View.GONE);

                    tvsecondValue.setVisibility(View.GONE);
                    tvSecondSymbol.setVisibility(View.GONE);

                    tvthirdValue.setVisibility(View.GONE);
                    tvThirdSymbol.setVisibility(View.GONE);

                    tvFourthValue.setVisibility(View.GONE);
                    tvFourthSymbol.setVisibility(View.GONE);

                    tvfifthValue.setVisibility(View.GONE);
                    tvFifthSymbol.setVisibility(View.GONE);

                    break;
            }
        }
    }
    //todo  ====================== Adapter class ============================

    class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {
        private Context context;
        private List<InfoModel> infolist;
        private SetVisibilityInerface tempList;
        private boolean isfirst = false;

        public InformationAdapter(Context context, List<InfoModel> infolist, SetVisibilityInerface tempList) {
            this.tempList = tempList;
            this.infolist = infolist;
            this.context = context;
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_informaion, parent, false);
            return new InformationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {

            InfoModel infoModel = infolist.get(position);
            holder.tvname.setText(infoModel.getName());

            if (position == 1) {
                holder.etValue.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else {
                holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if (position == 10 && desplayList.get(9).getValue().equalsIgnoreCase("IMPERIAL")) {
                holder.etComma.setVisibility(View.VISIBLE);
                holder.etInches.setVisibility(View.VISIBLE);
                if (Hawk.get(REC_INCH_FIFTH) != null) {
                    holder.etInches.setText(Hawk.get(REC_INCH_FIFTH));
                } else {
                    holder.etInches.setText("");
                }
                holder.etValue.setHint("ft");
            } else if (position == 10 && desplayList.get(9).getValue().equalsIgnoreCase("METRIC")) {
                holder.etComma.setVisibility(View.GONE);
                holder.etInches.setVisibility(View.GONE);
                holder.etValue.setHint("mm");
                Hawk.delete(REC_INCH_FIFTH);
            } else {
                holder.etValue.setHint("");
            }
            holder.etValue.setText(infoModel.getValue().toString());

            holder.etValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {


                    desplayList.get(position).setValue(holder.etValue.getText().toString());
                }
            });

            holder.etInches.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (position == 10 && desplayList.get(9).getValue().equalsIgnoreCase("IMPERIAL")) {
                        Hawk.put(REC_INCH_FIFTH, holder.etInches.getText().toString());
                    }
                }
            });
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (holder.spinner.getSelectedItem().toString().equalsIgnoreCase("IMPERIAL") && position == 9 && isfirst) {
                        desplayList.get(position).setValue(holder.spinner.getSelectedItem().toString());
                        notifyItemChanged(10);

                        tempList.refereshAdapter(position, holder.spinner.getSelectedItem().toString());

                    } else if (holder.spinner.getSelectedItem().toString().equalsIgnoreCase("METRIC") && position == 9 && isfirst) {
                        notifyItemChanged(10);
                        tempList.refereshAdapter(position, holder.spinner.getSelectedItem().toString());
                    } else if (position == 9 && !isfirst) {
                        isfirst = true;
                    }
                    desplayList.get(position).setValue(holder.spinner.getSelectedItem().toString());

                    if (position == 12 && holder.spinner.getSelectedItem().toString().equalsIgnoreCase("Rake") || position == 12 && holder.spinner.getSelectedItem().toString().equalsIgnoreCase("Box")) {
                        desplayList.get(position).setValue(holder.spinner.getSelectedItem().toString());
                        tempList.setVisibility(holder.spinner.getSelectedItem().toString());
                        notifyItemChanged(9);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            switch (position) {
                case 3: {
                    String[] value = {"LIGHT", "HEAVY"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }
                break;
                case 5: {
                    String[] value = {"No Value", "Fertilizers", "Coal", "Pet Coke", "DRI", "Iron One", "Others"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }
                break;
                case 8: {
                    String[] value = {"No Value", "Turn Service", "belle chasse", "Portship", "Weber", "Crescent", "Artco", "Others"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }


                break;
                case 9: {
                    String[] value = {"IMPERIAL", "METRIC"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }
                case 10: {
                    holder.etValue.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                break;
                case 11: {
                    String[] value = {"No Value", "Steel Lift", "Fiber Lift", "Roll Top", "Open", "Close"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }
                break;
                case 12: {
                    String[] value = {"Box", "Rake"};
                    setSpinnerValue(holder, value, infoModel);
                    setvisibility(holder);
                }
                break;
//                    case 12: {
//                        String[] value = {"Feet and Inches"};
//                        setSpinnerValue(holder, value, infoModel);
//                        setvisibility(holder);
//                    }
//                    break;
                default: {
                    holder.editLayout.setVisibility(View.VISIBLE);
                    holder.dropLayout.setVisibility(View.GONE);
                }
            }
        }

        private void setvisibility(InformationViewHolder holder) {
            holder.editLayout.setVisibility(View.GONE);
            holder.dropLayout.setVisibility(View.VISIBLE);
        }

        private void setSpinnerValue(InformationViewHolder holder, String[] value, InfoModel infoModel) {
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, R.layout.row_spinner, value);
            holder.spinner.setAdapter(spinnerAdapter);
            if (!TextUtils.isEmpty(infoModel.getValue())) {
                int pos = spinnerAdapter.getPosition(infoModel.getValue());
                holder.spinner.setSelection(pos);
            }
        }

        @Override
        public int getItemCount() {
            return infolist.size();
        }

        class InformationViewHolder extends RecyclerView.ViewHolder {
            private TextView tvname;
            private ConstraintLayout dropLayout, editLayout;
            private Spinner spinner;
            private EditText etValue, etInches, etComma;
            RecyclerView recInfo, recSubChild;

            public InformationViewHolder(@NonNull View itemView) {
                super(itemView);
                tvname = itemView.findViewById(R.id.textView11);
                dropLayout = itemView.findViewById(R.id.drop_layout);
                editLayout = itemView.findViewById(R.id.constraintLayout5);
                spinner = itemView.findViewById(R.id.spinner2);
                etValue = itemView.findViewById(R.id.textView13);
                etInches = itemView.findViewById(R.id.textView6);
                etComma = itemView.findViewById(R.id.textView65);
            }
        }
    }
}

interface SetVisibilityInerface {
    void setVisibility(String type);

    void refereshAdapter(int pos, String ftType);
}
