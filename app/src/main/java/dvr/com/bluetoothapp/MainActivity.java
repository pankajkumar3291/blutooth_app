package dvr.com.bluetoothapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.snatik.storage.Storage;
import net.hockeyapp.android.CrashManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dvr.com.bluetoothapp.Util.MyUtil;
import dvr.com.bluetoothapp.contentprovider.CachedFileProvider;
import dvr.com.bluetoothapp.fragment.FragmentDashboard;
import dvr.com.bluetoothapp.fragment.FragmentInfo;
import dvr.com.bluetoothapp.fragment.FragmentInformation;
import dvr.com.bluetoothapp.fragment.FragmentInputs;
import dvr.com.bluetoothapp.fragment.FragmentPictures;
import dvr.com.bluetoothapp.fragment.FragmentStateBluetooth;
import dvr.com.bluetoothapp.fragment.FragmentStateOfBluetooth;

public class MainActivity extends AppCompatActivity implements FragmentStateBluetooth.OnFragmentInteractionListener {


    private static final int REQUESTPERMISSIONSDCARD = 1;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.imgHamburger)
    ImageView imghamburger;

    @BindView(R.id.btsaveandshare)
    Button btSave;

    @BindView(R.id.btclean)
    Button btClean;
    boolean isPlaying = false;
    private static final int RCSCARDPERMISSION = 10;

    private static String TAG = "MainActivity";
    private Intent mResultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setDefaultFragment();
        Hawk.init(getApplicationContext()).build();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener(){
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                MyUtil.hideKeyBoardActivity(MainActivity.this);
            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                MyUtil.hideKeyBoardActivity(MainActivity.this);
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                MyUtil.hideKeyBoardActivity(MainActivity.this);
            }
            @Override
            public void onDrawerStateChanged(int newState) {
                MyUtil.hideKeyBoardActivity(MainActivity.this);
            }
        });
        chekIFbluetoothIsEnabled();
        checkForCrashes();

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
    }
    private void chekIFbluetoothIsEnabled(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            TastyToast.makeText(getApplicationContext(), "Bluetooth not Support", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                TastyToast.makeText(getApplicationContext(), "Enabling Bluetooth", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                mBluetoothAdapter.enable();
            }
        }
    }
    private void setDefaultFragment() {
        FragmentInfo fragmentInfo = FragmentInfo.newInstance("", "");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, new FragmentDashboard()).commit();
    }
    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @SuppressLint("WrongConstant")
    @OnClick(R.id.imgHamburger)
    public void changeDrawer(){
        if (isPlaying) {
            TastyToast.makeText(getApplicationContext(), "Please Stop Current Trasaction", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            return;
        }
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else
            drawerLayout.openDrawer(Gravity.START);
    }
    @SuppressLint("WrongConstant")
    @OnClick(R.id.imghamsecdrawer)
    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START);
    }

    @OnClick({R.id.llinfo, R.id.llfop, R.id.llfos, R.id.llmsp, R.id.llmss, R.id.llafp, R.id.llafs, R.id.llafm})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.llinfo:

                infoFragment();
                break;


            case R.id.llfop:

                BluetoohFragment("FOP");

                break;


            case R.id.llfos:
                BluetoohFragment("FOS");


                break;


            case R.id.llmsp:
                if (Hawk.get("infoList")!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentInputs()).commit();
                else
                    Toast.makeText(getApplicationContext(),"Please enter Information first",Toast.LENGTH_SHORT).show();
                changeDrawer();
//                BluetoohFragment("MSP");

                break;


            case R.id.llmss:

                getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentPictures()).commit();
                changeDrawer();
//                BluetoohFragment("MSS");
                break;


            case R.id.llafp:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentDashboard()).commit();
                changeDrawer();
//                BluetoohFragment("AFP");
                break;


            case R.id.llafs:
                BluetoohFragment("AFS");
                break;


            case R.id.llafm:
                BluetoohFragment("AFM");
                break;


        }

    }

    private void BluetoohFragment(String type){
//        FragmentStateBluetooth fragmentStateBluetooth = FragmentStateBluetooth.newInstance(type, type);
//        FragmentManager fragmentManager = getSupportFragmentManager();                                   //todo    comment by charan
//        fragmentManager.beginTransaction().replace(R.id.container, fragmentStateBluetooth).commit();
        changeDrawer();
    }

    private void infoFragment(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentInformation()).commit();

//        if (!(f instanceof FragmentInfo))
//            setDefaultFragment();           // todo comment by charan
        changeDrawer();
    }

    @OnClick(R.id.btsaveandshare)
    public void setBtSave() {
        createFileandSaveData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @OnClick(R.id.btclean)
    public void setBtClean() {

        new MaterialDialog.Builder(this)
                .title(R.string.title)
                .content(R.string.content)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        Storage storage = new Storage(getApplicationContext());
                        String path = storage.getInternalCacheDirectory();
                        String file = path + File.separator + Hawk.get("pobject", "") + "Data" + fmt.format(calendar.getTime()) + ".txt";
                        if (storage.isFileExist(file)) {
                            storage.deleteFile(file);
                            Log.i(TAG, "onClick: " + "file deleted");
                        }
                        Hawk.deleteAll();
                        changeDrawer();
                        Intent intent = getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void createFileandSaveData() {

//        Hawk.put("pobject",edPobj.getText().toString());
//        Hawk.put("refrence",edrefrence.getText().toString());
//        Hawk.put("location",edloc.getText().toString());
//        Hawk.put("place",edplace.getText().toString());
//        Hawk.put("by",edby.getText().toString());
//        Hawk.put("extra",edextra.getText().toString());

        String top = "";
        String pObj = Hawk.get("pobject");
        String ref = Hawk.get("refrence");
        String loc = Hawk.get("location");
        String place = Hawk.get("place");
        String by = Hawk.get("by");
        String extra = Hawk.get("extra");
        String correction = Hawk.get("correction");
        top = top.concat("P.OBJECT:" + pObj);
        top = top.concat("\n" + "REFERENCE" + ":" + ref);
        top = top.concat("\n" + "LOCATION" + ":" + loc);
        top = top.concat("\n" + "PLACE" + ":" + place);
        top = top.concat("\n" + "BY" + ":" + by);
        top = top.concat("\n" + "CORRECTION" + ":" + correction + "mm \n");


        // Hawk.put(mParam1+"last",dataLastLine);

        if (null != Hawk.get("FOPlast", null)) {

            top = top.concat("FOP; ");  // is it fine
            top = top.concat(String.valueOf(Hawk.get("FOPlast")));

            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
            Log.i(TAG, "foplast" + top);

        }
        if (null != Hawk.get("FOSlast", null)) {
            top = top.concat("FOS; ");
            top = top.concat(String.valueOf(Hawk.get("FOSlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");

        }
        if (null != Hawk.get("MSPlast", null)) {
            top = top.concat("MSP; ");
            top = top.concat(String.valueOf(Hawk.get("MSPlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("MSSlast", null)) {
            top = top.concat("MSS; ");
            top = top.concat(String.valueOf(Hawk.get("MSSlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFPlast", null)) {
            top = top.concat("AFP; ");
            top = top.concat(String.valueOf(Hawk.get("AFPlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFSlast", null)) {
            top = top.concat("AFS; ");
            top = top.concat(String.valueOf(Hawk.get("AFSlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFMlast", null)) {
            top = top.concat("AFM; ");
            top = top.concat(String.valueOf(Hawk.get("AFMlast")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }

        /* ////////////////////////////////////////////////////////////////////////////////////////////////// */


        if (null != Hawk.get("FOP", null)) {

            top = top.concat("\n" + String.valueOf(Hawk.get("FOP")));

            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            Log.i(TAG, "Full fop data" + top);

            top = top.concat("\n");


        }
        if (null != Hawk.get("FOS", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("FOS")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");

        }
        if (null != Hawk.get("MSP", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("MSP")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("MSS", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("MSS")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFP", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("AFP")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFS", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("AFS")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFM", null)) {
            top = top.concat("\n" + String.valueOf(Hawk.get("AFM")));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();


//        generateNoteOnSD(getApplicationContext( ), Hawk.get("pobject", "") + "Data" + fmt.format(calendar.getTime( )) + ".txt", top);

        createFile(Hawk.get("pobject", "") + "." + fmt.format(calendar.getTime()) + ".txt", top);
    }

    String h;

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {

        Storage storage = new Storage(getApplicationContext());
        String path = storage.getInternalCacheDirectory();
        String folder = path + File.separator + "DS HARITZ";

        if (!storage.isDirectoryExists(folder)) {
            storage.createDirectory(folder);
        }
        String file = path + File.separator + "DS HARITZ" + File.separator + sFileName;
        if (!storage.isFileExist(file) && storage.isDirectoryExists(folder)) {
            storage.createFile(file, sBody);
        }
        TastyToast.makeText(context, "Saved Succesfully " + path, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("*/*");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "blah@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/" + file));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Hawk.get("pobject", ""));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public void createFile(final String fileName, String content) {
        File folder = new File(getExternalFilesDir(null), "DS HARITZ");
        boolean result = folder.mkdirs();

        if (folder.exists()) {
            final File file = new File(folder, fileName);
////////////////////////////////////////////////////////
            if (file.exists()) {
                file.delete();
                try {
                    if (file.createNewFile()) {
                        FileWriter writer = new FileWriter(file);
                        writer.append(content);
                        writer.flush();
                        writer.close();
                        new MaterialDialog.Builder(this)
                                .title(R.string.fiepath)
                                .cancelable(false)
                                .content(file.getAbsolutePath())
                                .positiveText(R.string.gotIt)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.setType("application/xml");
                                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, file);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
                                        startActivity(Intent.createChooser(shareIntent, "share"));
                                    }
                                })
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                try {
                    if (file.createNewFile()) {
                        FileWriter writer = new FileWriter(file);
                        writer.append(content);
                        writer.flush();
                        writer.close();

                        new MaterialDialog.Builder(this)
                                .title(R.string.fiepath)
                                .cancelable(false)
                                .content(file.getAbsolutePath())
                                .positiveText(R.string.gotIt)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();

                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.setType("application/xml");

                                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, file);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
                                        startActivity(Intent.createChooser(shareIntent, "share"));


                                    }
                                })
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


//
//                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//
//            File fileWithinMyDir = new File(file);
//
//            if(fileWithinMyDir.exists()) {
//                intentShareFile.setType("application/pdf");
//                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
//
//                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                        "DS HARITZ "+Hawk.get("pobject")+" Data");
//                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//
//                startActivity(Intent.createChooser(intentShareFile, "Share File"));
//            }

//
//        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/" + "Test"));
//        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                "Sharing File...");
//        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//
//
//
//        startActivity(Intent.createChooser(intentShareFile, "Share File"));
//
//
//
//


    @Override
    public void onFragmentInteraction(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }


    private void checkForCrashes() {
        CrashManager.register(this);
    }


    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }

    public static Intent getSendEmailIntent(Context context, String email, String subject, String body, String fileName) {

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //Explicitly only use Gmail to send
        emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

        emailIntent.setType("plain/text");

        //Add the recipients
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        //Add the attachment by specifying a reference to our custom ContentProvider
        //and the specific file of interest
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/" + fileName));

        return emailIntent;
    }
}




