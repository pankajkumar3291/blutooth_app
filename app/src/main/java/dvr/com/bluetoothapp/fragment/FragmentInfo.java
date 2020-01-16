package dvr.com.bluetoothapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.Util.MyUtil;

public class FragmentInfo extends Fragment implements Validator.ValidationListener {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.btsave)
    Button btSaveinfo;
    @NotEmpty
    @BindView(R.id.edpobj)
    EditText edPobj;
    @BindView(R.id.edcorrect)
    EditText edCorrect;
    @BindView(R.id.edextra)
    EditText edextra;
    @BindView(R.id.edplace)
    EditText edplace;
    @BindView(R.id.edlocation)
    EditText edloc;
    @BindView(R.id.edby)
    EditText edby;
    @BindView(R.id.edrefrence)
    EditText edrefrence;

    private Validator validator;
    private static String TAG = "FragmentInfo";
    public FragmentInfo(){

    }
    // TODO: Rename and change types and number of parameters
    public static FragmentInfo newInstance(String param1, String param2){
        FragmentInfo fragment = new FragmentInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_info, container, false);
        ButterKnife.bind(this, view);
        findViewById();
        validator = new Validator(this);
        validator.setValidationListener(this);
        edCorrect.setText(MyUtil.getCorrection(getActivity()));
        edloc.setText(MyUtil.getLocation(getActivity()));
        edby.setText(MyUtil.getBy(getActivity()));

        if (Hawk.get("pobject", null) != null){
            edPobj.setText(Hawk.get("pobject", ""));
        }
        if (Hawk.get("refrence", null) != null){
            edrefrence.setText(Hawk.get("refrence", ""));
        }
        if (Hawk.get("location", null) != null){
            edloc.setText(Hawk.get("location", ""));
        }
        if (Hawk.get("place", null) != null){
            edplace.setText(Hawk.get("place", ""));
        }
        if (Hawk.get("by", null) != null){
            edby.setText(Hawk.get("by", ""));
        }
        if (Hawk.get("extra", null) != null){
            edextra.setText(Hawk.get("extra", ""));
        }
        return view;
    }

    private void findViewById(){
        View linev = getActivity().findViewById(R.id.tvline);
        TextView tvProjName = getActivity().findViewById(R.id.tv_proj);
        TextView tvProjName2 = getActivity().findViewById(R.id.tvpname);
        tvProjName.setVisibility(View.GONE);
        tvProjName2.setVisibility(View.GONE);
        linev.setVisibility(View.VISIBLE);
    }
    @Override
    public void onValidationSucceeded(){
        Hawk.put("pobject", edPobj.getText().toString());
        Hawk.put("refrence", edrefrence.getText().toString());
        Hawk.put("location", edloc.getText().toString());
        Hawk.put("place", edplace.getText().toString());
        Hawk.put("by", edby.getText().toString());
        Hawk.put("extra", edextra.getText().toString());
        Hawk.put("correction", edCorrect.getText().toString());

        MyUtil.setCorrection(getActivity(),edCorrect.getText().toString());
        MyUtil.setLocation(getActivity(),edloc.getText().toString());
        MyUtil.setBy(getActivity(),edby.getText().toString());
        Log.i(TAG, "correction Saved -->" + MyUtil.getCorrection(getActivity()));
        TastyToast.makeText(getActivity(), "Saved Succesfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors){
        for (ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText){
                ((EditText) view).setError(message);
            } else{
            }
        }
    }
    @OnClick(R.id.btsave)
    public void setBtSaveinfo() {
        validator.validate();
    }
}
