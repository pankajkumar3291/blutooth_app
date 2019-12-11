package dvr.com.bluetoothapp.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.Util.MyUtil;
import dvr.com.bluetoothapp.adapters.SpinnerAdapter;
import dvr.com.bluetoothapp.other_classes.InfoModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class FragmentInformation extends Fragment {

    private RecyclerView rec_information;
    private List<InfoModel> infoList = new ArrayList<>();
    private InformationAdapter informationAdapter;
    private List<InfoModel> desplayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_fragment_information, container, false);
        findViewById();
        rec_information = view.findViewById(R.id.rec_information);
        infoList.add(new InfoModel("BY:",""));
        infoList.add(new InfoModel("CORRECTION",""));
        infoList.add(new InfoModel("BARGE N",""));
        infoList.add(new InfoModel("CONDITION","LIGHT"));
        infoList.add(new InfoModel("VESSEL",""));
        infoList.add(new InfoModel("COMODITY","Fertilizers"));
        infoList.add(new InfoModel("FILE NR",""));
        infoList.add(new InfoModel("FLEET:",""));
        infoList.add(new InfoModel("LAUNCH:","Turn Service"));
        infoList.add(new InfoModel("FT:","11 Ft"));
        infoList.add(new InfoModel("TYPE:","Steel Lift"));
        infoList.add(new InfoModel("BARGE TYPE:","Box"));
        infoList.add(new InfoModel("UOM:","Feet and Inches"));
        infoList.add(new InfoModel("A - HL",""));
        infoList.add(new InfoModel("B - HL - WL:",""));
        infoList.add(new InfoModel("C - HL - END RS:",""));
        infoList.add(new InfoModel("D - HL - EDGE MH:",""));
        infoList.add(new InfoModel("E - END MH - RAKE B",""));

        rec_information.setHasFixedSize(true);
        informationAdapter = new InformationAdapter(getActivity(), desplayList);
        if (Hawk.get("infoList")!=null)
        {
            desplayList.clear();
            List<InfoModel> list = Hawk.get("infoList");
            desplayList.addAll(list);
            rec_information.setAdapter(informationAdapter);
        }
        else
        {
            desplayList.clear();
            desplayList.addAll(infoList);
            rec_information.setAdapter(informationAdapter);
        }
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Hawk.deleteAll();
                desplayList.clear();
                desplayList.addAll(infoList);
                rec_information.setAdapter(informationAdapter);
            }
        });

        return view;
    }


    private void findViewById() {

        View linev = getActivity().findViewById(R.id.tvline);

        TextView tvProjName = getActivity().findViewById(R.id.tv_proj);

        TextView tvProjName2 = getActivity().findViewById(R.id.tvpname);
        tvProjName.setVisibility(View.GONE);
        tvProjName2.setVisibility(View.GONE);

        linev.setVisibility(View.VISIBLE);

    }

    private void saveInfo()
    {
        String correct="";
        String location="";
        String by="";
        String pobject="";

       for (InfoModel infoModel:desplayList)
       {
          if (TextUtils.isEmpty(infoModel.getValue()))
          {
              Toast.makeText(getContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
              Hawk.deleteAll();
              return;
          }
          else
          {
              if (infoModel.getName().equalsIgnoreCase("CORRECTION"))
              {
                  correct=infoModel.getValue();
              }
              if (infoModel.getName().equalsIgnoreCase("BY:"))
              {
                  by=infoModel.getValue();
              }

              if (infoModel.getName().equalsIgnoreCase("VESSEL"))
              {
                  location=infoModel.getValue();
              }
              if(infoModel.getName().equalsIgnoreCase("BARGE N"))
              {
                  pobject=infoModel.getValue();
                  Hawk.put("pobject",pobject);
              }
          }
       }
       Hawk.put("infoList",desplayList);
        MyUtil.setCorrection(getActivity(), correct);
        MyUtil.setLocation(getActivity(), location);
        MyUtil.setBy(getActivity(), by);
        Log.i(TAG, "correction Saved -->" + MyUtil.getCorrection(getActivity()));
       Toast.makeText(getActivity(), "Saved Succesfully", Toast.LENGTH_SHORT).show();
    }



    //todo  ====================== Adapter class ============================

    class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder>{
        private Context context;
        private List<InfoModel> infoList;

        public InformationAdapter(Context context, List<InfoModel> infoList){
            this.infoList = infoList;
            this.context = context;
        }

        @NonNull
        @Override
        public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(context).inflate(R.layout.row_informaion, parent, false);
            return new InformationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InformationViewHolder holder, int position){

            InfoModel infoModel=infoList.get(position);

            holder.tvname.setText(infoModel.getName());

            if (position==1)
            {
                holder.etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else
            {
                holder.etValue.setInputType(InputType.TYPE_CLASS_TEXT);
            }

//            if (position!=3||position!=5||position!=8||position!=9||position!=10||position!=11||position!=12)
            holder.etValue.setText(infoModel.getValue().toString());

            holder.etValue.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

                }
                @Override
                public void afterTextChanged(Editable editable){
                    desplayList.get(position).setValue(holder.etValue.getText().toString());

                }
            });

            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){

                    desplayList.get(position).setValue(holder.spinner.getSelectedItem().toString());

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            switch (position)
            {
                case  3:
                {
                    String[] value = {"LIGHT","HEAVY"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 5:
                {
                    String[] value = {"Fertilizers","Coal","Pet Coke","DRI","Iron One","Others"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 8:
                {
                    String[] value = {"Turn Service","belle chasse","Portship","Weber","Crescent","Artco","Others"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 9:
                {
                    String[] value = {"11 Ft","12 Ft","13 Ft","14 Ft"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 10:
                {
                    String[] value = {"Steel Lift","Fiber Lift","Roll Top","Open","Close"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 11:
                {
                    String[] value = {"Box","Rake"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                case 12:
                {
                    String[] value = {"Feet and Inches"};
                    setSpinnerValue(holder,value,infoModel);
                    setvisibility(holder);
                }
                break;
                default:
                {
                    holder.editLayout.setVisibility(View.VISIBLE);
                    holder.dropLayout.setVisibility(View.GONE);
                }

            }
        }

        private void setvisibility(InformationViewHolder holder) {
            holder.editLayout.setVisibility(View.GONE);
            holder.dropLayout.setVisibility(View.VISIBLE);
        }

        private void  setSpinnerValue(InformationViewHolder holder,String[] value,InfoModel infoModel)
        {
            SpinnerAdapter spinnerAdapter=  new SpinnerAdapter(context,R.layout.row_spinner,value);
            holder.spinner.setAdapter(spinnerAdapter);
            if (!TextUtils.isEmpty(infoModel.getValue()))
            {
                int pos=spinnerAdapter.getPosition(infoModel.getValue());
                holder.spinner.setSelection(pos);
            }
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        class InformationViewHolder extends RecyclerView.ViewHolder{
            private TextView tvname;
            private ConstraintLayout dropLayout,editLayout;
            private Spinner spinner;
            private EditText etValue;

            public InformationViewHolder(@NonNull View itemView){
                super(itemView);
                tvname = itemView.findViewById(R.id.textView11);
                dropLayout=itemView.findViewById(R.id.drop_layout);
                editLayout=itemView.findViewById(R.id.constraintLayout5);
                spinner=itemView.findViewById(R.id.spinner2);
                etValue=itemView.findViewById(R.id.textView13);
            }
        }
    }
}

