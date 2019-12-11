package dvr.com.bluetoothapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import dvr.com.bluetoothapp.Model.DataBluetooth;
import dvr.com.bluetoothapp.R;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothHolder>{
    List<DataBluetooth > dataBluetooths;

    public BluetoothAdapter(List<DataBluetooth> dataBluetooths){
        this.dataBluetooths = dataBluetooths;
    }
    @NonNull
    @Override
    public BluetoothHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bluetooth,parent,false);
        return new BluetoothHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BluetoothHolder holder, int position){
        holder.tvData.setText(dataBluetooths.get(position).getNum1()+";"+dataBluetooths.get(position).getNum2()+";"+dataBluetooths.get(position).getNum3());
    }
    @Override
    public int getItemCount() {
        return dataBluetooths.size();
    }
    static class BluetoothHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvdta)
        TextView tvData;
        public BluetoothHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this.itemView);
            tvData = itemView.findViewById(R.id.tvdta);
        }
    }
    public void setItems(List<DataBluetooth> dataBluetooths){
        this.dataBluetooths = dataBluetooths;
    }
}
