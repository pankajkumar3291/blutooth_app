package dvr.com.bluetoothapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dvr.com.bluetoothapp.R;
import dvr.com.bluetoothapp.other_classes.InfoModel;
import dvr.com.bluetoothapp.other_classes.InternalStorage;
import dvr.com.bluetoothapp.other_classes.PDFHelper;
/**
 * A simple {@link Fragment} subclass.
 */

public class FragmentPictures extends Fragment {

    private View view;
    private ImageView imageView;
    private List<Uri> imgList = new ArrayList<>();
    private ImgAdapter imgAdapter;
    private RecyclerView recImgList;
    private Bitmap bitmap;
    private PDFHelper pdfHelper;
    private ConstraintLayout mainlayout;
    public static final int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    Context context;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_pictures, container, false);

        context = view.getContext();
        findidOfView();

        TextView tvProjName2 = getActivity().findViewById(R.id.tvpname);





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

            tvProjName2.setText(list.get(2).getValue());
            tvProjName2.setVisibility(View.VISIBLE);
        }
        else
        {
            tvProjName2.setVisibility(View.GONE);
        }


        getPreviousImageList();
        clickEvents();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        return view;
    }

    //Get previous image list from storage
    private void getPreviousImageList() {

        //Folder Name
        String folder_name = "Ds Haritz Pictures";
        // Get File directory
        File directory  = new File(Environment.getExternalStorageDirectory(), folder_name);

        File[] img_file = directory.listFiles();

        if(img_file != null) {
            if (img_file.length > 0) {


                for (int i = 0; i < img_file.length; i++) {
                    if (img_file[i].getAbsolutePath().contains(".jpg")) {

                        imgList.add(Uri.fromFile(img_file[i]));
                    }
                }
            } else {
                Toast.makeText(context, "No File Found", Toast.LENGTH_SHORT).show();
            }
        }

        if(imgList.size()>0){
            setAdapter();
        }

    }

    // Request permissions
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

    // OnClickEvents
    private void clickEvents() {
        view.findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermossion();
            }
        });

        view.findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
            }
        });
    }

    // Bind all views
    private void findidOfView() {
        imageView = view.findViewById(R.id.imageView3);
        recImgList = view.findViewById(R.id.rec_add_img);
        mainlayout = view.findViewById(R.id.reclistparent);
    }
    private void checkPermossion() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "DS HARITZ Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Camera Picture");
                        imageUri = context.getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 101);
                    } else {
                        Toast.makeText(getContext(), "please allow permission froma setting", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Image Result from gallery (102) from camera (101)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // From Camera
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                imgList.add(imageUri);
                setAdapter();
                saveImageToStorage(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // From Gallery
        if (requestCode == 102 && resultCode == Activity.RESULT_OK  &&  data != null) {

            imageUri = data.getData();
            try {

                Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver() , imageUri);
                imgList.add(imageUri);
                setAdapter();
                saveImageToStorage(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Save Image Bitmap To Storage
    private void saveImageToStorage(Bitmap bitmap) {

        //Folder Name
        String folder_name = "Ds Haritz Pictures";
        // Get File directory
        File directory  = new File(Environment.getExternalStorageDirectory(), folder_name);
        //Make Directory if not exist
        if (!directory .exists()) {
            directory .mkdirs();
        }

        //Unique Image Name
        String name = String.valueOf(System.currentTimeMillis());
        File file;
        file = new File(directory, "DH_"+ name + ".jpg");

        // Create a image file if not exist
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                // Compress a image with quality 50 (quality range 0-100)
                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                fos = new FileOutputStream(file);
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    // List of images (previous, from camera and from gallery)
    private void setAdapter() {

        if(imgAdapter!= null){
            imgAdapter.notifyDataSetChanged();
        }else {
            imgAdapter = new ImgAdapter(context, imgList);
            LinearLayoutManager manager = new GridLayoutManager(context, 1);
            recImgList.setLayoutManager(manager);
            recImgList.setHasFixedSize(true);
            recImgList.setItemViewCacheSize(20);
            recImgList.setDrawingCacheEnabled(true);
            recImgList.setAdapter(imgAdapter);
        }
    }



// todo ================================== Adapter class ========================================

    class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {
        private Context context;
        private List<Uri> imgList;

        public ImgAdapter(Context context, List<Uri> imgList) {
            this.context = context;
            this.imgList = imgList;
        }

        @NonNull
        @Override
        public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_add_img, parent, false);
            return new ImgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImgViewHolder holder, int position) {

            Picasso.get()
                    .load(imgList.get(position))
                    .into(holder.img);
            holder.btnCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteImageFile(imgList.get(position));

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
                btnCross = itemView.findViewById(R.id.imageButton);
            }
        }

        // Delete Picture From Storage
        private void deleteImageFile(Uri uri) {

            File file = new File(uri.getPath());

            if(file.exists()){
                file.delete();
                Toast.makeText(context, "Delete image successfully", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
