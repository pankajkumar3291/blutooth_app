package dvr.com.bluetoothapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.other_classes.PDFHelper;
/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPictures extends Fragment{

    private View view;
    private ImageView imageView;
    private List<Bitmap> imgList = new ArrayList<>();
    private ImgAdapter imgAdapter;
    private RecyclerView recImgList;
    private Bitmap bitmap;
    private PDFHelper pdfHelper;
    private ConstraintLayout mainlayout;
    public static final int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_pictures, container, false);
        pdfHelper =  new PDFHelper(new File(Environment.getExternalStorageDirectory().getPath()),getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        fn_permission();
        mainlayout=view.findViewById(R.id.reclistparent);
        findidOfView();
        clickEvents();
        view.findViewById(R.id.imageView66).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pdfHelper.saveImageToPDF(recImgList,getRecyclerViewScreenshot(recImgList),"pdfFile");
            }
        });
        return view;
    }

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view){
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }

    private void fn_permission(){
        if ((ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
            }
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
    private void clickEvents() {
        view.findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermossion();
            }
        });
        view.findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
            }
        });
    }
    private void findidOfView() {
        imageView = view.findViewById(R.id.imageView3);
        recImgList = view.findViewById(R.id.rec_add_img);
    }
    private void checkPermossion() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // start the image capture Intent
                        startActivityForResult(intent, 101);
                    } else {
                        Toast.makeText(getContext(), "please allow permission froma setting", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK){
            if (imgAdapter!=null)
            {
                imgList.add((Bitmap) data.getExtras().get("data"));
                imgAdapter.notifyDataSetChanged();
            }
            else
            {
                imgAdapter = new ImgAdapter(getActivity(), imgList);
                imgList.add((Bitmap) data.getExtras().get("data"));
                recImgList.setHasFixedSize(true);
                recImgList.setAdapter(imgAdapter);
                imgAdapter.notifyDataSetChanged();
            }
//            Bitmap image = (Bitmap) data.getExtras().get("data");
        }
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                try {

                    if (imgAdapter!=null)
                    {
                        imgList.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()));
                        imgAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        imgAdapter = new ImgAdapter(getActivity(), imgList);
                        imgList.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()));
                        recImgList.setHasFixedSize(true);
                        recImgList.setAdapter(imgAdapter);
                        imgAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// todo ================================== Adapter class ========================================


    class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {
        private Context context;
        private List<Bitmap> imgList;

        public ImgAdapter(Context context, List<Bitmap> imgList) {
            this.context = context;
            this.imgList=imgList;
        }
        @NonNull
        @Override
        public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.row_add_img, parent, false);
            return new ImgViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ImgViewHolder holder, int position) {

            Bitmap bitmap = Bitmap.createScaledBitmap(imgList.get(position), 300, 200, true);
            holder.img.setImageBitmap(bitmap);
            holder.btnCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        @Override
        public int getItemCount() {
            return imgList.size();
        }

        class ImgViewHolder extends RecyclerView.ViewHolder {
            private ImageView img;
            private ImageButton btnCross;

            public ImgViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.imageView3);
                btnCross=itemView.findViewById(R.id.imageButton);
            }
        }
    }
}
