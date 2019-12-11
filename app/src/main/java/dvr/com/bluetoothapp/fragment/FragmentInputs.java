package dvr.com.bluetoothapp.fragment;


import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
import dvr.com.bluetoothapp.other_classes.PDFHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInputs extends Fragment implements View.OnClickListener {
    private ConstraintLayout constraintLayout;
    private View view;
    private Spinner sptopDropDown;
    private String tvSpText;

    private TextView oneP_textView, twoP_textView, threeP_textView, fourP_TextView, fiveP_text_View,
            oneS_textView, twoS_textView, threeS_textView, fourS_TextView, fiveS_text_View, tvFore, tvAFT;
    public FragmentInputs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_inputs, container, false);
        constraintLayout = view.findViewById(R.id.constraintLayout11);
        constraintLayout.setOnClickListener(this);
        sptopDropDown = view.findViewById(R.id.spinner4);
        AllViewListners();
        findIdOfViews();
        String[] value = {"Feet and Inches", "mm"};
        sptopDropDown.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_spinner, value));
        tvSpText = sptopDropDown.getSelectedItem().toString();
        return view;
    }

    private void findIdOfViews() {
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
            public void onClick(View view){
                openInputsActivity("FOP");
            }
        });

        view.findViewById(R.id.textView44).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                openInputsActivity("FOS");
            }
        });
        view.findViewById(R.id.textView49).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputsActivity("MSP");
            }
        });
        view.findViewById(R.id.textView47).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputsActivity("MSS");
            }
        });
        view.findViewById(R.id.textView53).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputsActivity("AFP");
            }
        });
        view.findViewById(R.id.textView52).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputsActivity("AFS");
            }
        });
    }
    private void openInputsActivity(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        FragmentStateBluetooth fragmentStateOfBluetooth = new FragmentStateBluetooth();
        fragmentStateOfBluetooth.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentStateOfBluetooth).commit();
    }

    @Override
    public void onClick(View view){
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
        tvTitle.setText(fromvalue+" - INPUT DATA");
        Spinner spinner = deleteDialogView.findViewById(R.id.spinner3);
        TextView tvtext = deleteDialogView.findViewById(R.id.textView33);
        ConstraintLayout viewLayout = deleteDialogView.findViewById(R.id.constraintLayout21);

        String[] value = {"T", "CNO", "Measurement"};
        spinner.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_spinner, value));
        if (sptopDropDown.getSelectedItem().toString().equalsIgnoreCase("Feet and Inches"))
            tvtext.setText("Inch");
        else
            tvtext.setText(sptopDropDown.getSelectedItem().toString());
        EditText etEditText = deleteDialogView.findViewById(R.id.editText5);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Measurement")) {
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
                break;
            case "1S":
                oneS_textView.setText(selectedValue);
                break;
            case "2P":
                twoP_textView.setText(selectedValue);
                break;
            case "2S":
                twoS_textView.setText(selectedValue);
                break;
            case "3P":
                threeP_textView.setText(selectedValue);
                break;
            case "3S":
                threeS_textView.setText(selectedValue);
                break;
            case "4P":
                fourP_TextView.setText(selectedValue);
                break;
            case "4S":
                fourS_TextView.setText(selectedValue);
                break;
            case "5P":
                fiveP_text_View.setText(selectedValue);
                break;
            case "5S":
                fiveS_text_View.setText(selectedValue);
                break;
            case "FORE":
                tvFore.setText(selectedValue);
                break;
            case "AFT":
                tvAFT.setText(selectedValue);
                break;
        }
    }
}
