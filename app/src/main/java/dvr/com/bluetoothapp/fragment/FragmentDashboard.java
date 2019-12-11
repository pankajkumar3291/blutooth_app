package dvr.com.bluetoothapp.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import dvr.com.bluetoothapp.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class FragmentDashboard extends Fragment{
    private RecyclerView recyclerView;
    private View view;
    private FloatingActionButton btnAdd;
    private List<String> historicList = new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_dashboard, container, false);
        findIdOfView();
//        view.findViewById(R.id.tvfragtype).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//            }
//        });
        return view;
    }
    private void findIdOfView() {
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        historicList.add("123456 - L - 2.0,942'");
        recyclerView = view.findViewById(R.id.historic_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new HistoricAdapter(getActivity(), historicList));
        btnAdd=view.findViewById(R.id.floatingActionButton2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentInformation()).commit();
            }
        });
    }
    // todo ===================================== Adpter ==========================================

    class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.HisoricalViewHolder>{

        private List<String> historicList = new ArrayList<>();
        private Context context;

        public HistoricAdapter(Context context, List<String> historicList) {
            this.context = context;
            this.historicList = historicList;
        }
        @NonNull
        @Override
        public HisoricalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(context).inflate(R.layout.row_dashboard, parent, false);
            return new HisoricalViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull HisoricalViewHolder holder, int position){
            holder.tvhistoriname.setText(historicList.get(position));
        }
        @Override
        public int getItemCount(){
            return historicList.size();
        }
        class HisoricalViewHolder extends RecyclerView.ViewHolder{
            private TextView tvhistoriname;

            public HisoricalViewHolder(@NonNull View itemView){
                super(itemView);
                tvhistoriname = itemView.findViewById(R.id.tvdetail);
            }
        }
    }
}
