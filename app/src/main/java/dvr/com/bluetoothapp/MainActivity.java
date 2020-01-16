package dvr.com.bluetoothapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.snatik.storage.Storage;

import net.hockeyapp.android.CrashManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dvr.com.bluetoothapp.Util.CSVWriter;
import dvr.com.bluetoothapp.Util.MyUtil;
import dvr.com.bluetoothapp.contentprovider.CachedFileProvider;
import dvr.com.bluetoothapp.fragment.FragmentDashboard;
import dvr.com.bluetoothapp.fragment.FragmentInfo;
import dvr.com.bluetoothapp.fragment.FragmentInformation;
import dvr.com.bluetoothapp.fragment.FragmentInputs;
import dvr.com.bluetoothapp.fragment.FragmentPictures;
import dvr.com.bluetoothapp.fragment.FragmentStateBluetooth;
import dvr.com.bluetoothapp.local_data_base.GraphDataModelClass;
import dvr.com.bluetoothapp.other_classes.InfoBottomData;
import dvr.com.bluetoothapp.other_classes.InfoModel;
import dvr.com.bluetoothapp.other_classes.InputFinalRecordType;
import dvr.com.bluetoothapp.other_classes.InputModel;
import dvr.com.bluetoothapp.other_classes.InternalStorage;

import static dvr.com.bluetoothapp.Model.Datakeys.A_HL;
import static dvr.com.bluetoothapp.Model.Datakeys.B_HL_WL;
import static dvr.com.bluetoothapp.Model.Datakeys.C_HL_END_RS;
import static dvr.com.bluetoothapp.Model.Datakeys.D_HL_EDGE_MH;
import static dvr.com.bluetoothapp.Model.Datakeys.E_END_MH_RAKE_B;
import static dvr.com.bluetoothapp.Model.Datakeys.FEET_AND_INCHES_DATA;
import static dvr.com.bluetoothapp.Model.Datakeys.INCH_FIFTH;
import static dvr.com.bluetoothapp.Model.Datakeys.INCH_FIRST;
import static dvr.com.bluetoothapp.Model.Datakeys.INCH_FOURTH;
import static dvr.com.bluetoothapp.Model.Datakeys.INCH_SECOND;
import static dvr.com.bluetoothapp.Model.Datakeys.INCH_THIRD;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_FINAL_RECORD;
import static dvr.com.bluetoothapp.Model.Datakeys.INPUT_SELECTED_TYPE;
import static dvr.com.bluetoothapp.Model.Datakeys.MM_DATA;
import static dvr.com.bluetoothapp.Model.Datakeys.REC_INCH_FIFTH;
import static dvr.com.bluetoothapp.Model.Datakeys.UOM;

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

    private List<Uri> imgList = new ArrayList<>();
    private File pdfFile;

    private static String FILE_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setDefaultFragment();
        Hawk.init(getApplicationContext()).build();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
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

    private void chekIFbluetoothIsEnabled() {
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
        fragmentManager.beginTransaction().add(R.id.container, new FragmentInformation()).commit();
    }

    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("WrongConstant")
    @OnClick(R.id.imgHamburger)
    public void changeDrawer() {
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
                if (InternalStorage.isFileContains(MainActivity.this, "infoList")) {
                    if (Hawk.get("height") == null)
                        Hawk.put("height", "0,0");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentInputs()).commit();
                } else
                    Toast.makeText(getApplicationContext(), "Please enter Information first", Toast.LENGTH_SHORT).show();
                changeDrawer();
//                BluetoohFragment("MSP");

                break;

            case R.id.llmss:
                findViewById(R.id.tvline).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentPictures()).commit();
                changeDrawer();
//                BluetoohFragment("MSS");
                break;
            case R.id.llafp:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashboard()).commit();
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

    private void BluetoohFragment(String type) {
//        FragmentStateBluetooth fragmentStateBluetooth = FragmentStateBluetooth.newInstance(type, type);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.container, fragmentStateBluetooth).commit();
        changeDrawer();
    }

    private void infoFragment() {
        changeDrawer();

        Handler mHandler = new Handler();

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentInformation()).commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    @OnClick(R.id.btsaveandshare)
    public void setBtSave() {
        createFileandSaveData(102);
    }

    @OnClick(R.id.btsav)
    public void setBtSaveOnly() {
        createFileandSaveData(101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
//        Objects.requireNonNull(fragment).onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

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
//                        Hawk.put(UOM,Hawk.get(UOM));
                        cleardata();

                        if (InternalStorage.isFileContains(MainActivity.this, "infoList")) {
                            try {
                                List<InfoModel> list = (List<InfoModel>) InternalStorage.readObject(MainActivity.this, "infoList");
                                for (int i = 0; i <= list.size(); i++) {
                                    switch (i) {
                                        case 2:
                                        case 10:
                                        case 11:
                                        case 12:
                                            list.get(i).setValue("");
                                            break;
                                    }
                                }
                                InternalStorage.deleteFolder(MainActivity.this, "infoList");
                                InternalStorage.writeObject(MainActivity.this, "infoList", list);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        String folder_name = "Ds Haritz Pictures";
                        // Get File directory
                        File directory = new File(Environment.getExternalStorageDirectory(), folder_name);

                        deleteRecursive(directory);
                        if (InternalStorage.isFileContains(MainActivity.this, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(MainActivity.this, MM_DATA) && Hawk.get(INPUT_FINAL_RECORD) != null) {
                            try {
                                InternalStorage.deleteFolder(MainActivity.this, FEET_AND_INCHES_DATA);
                                InternalStorage.deleteFolder(MainActivity.this, MM_DATA);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }


                        if (InternalStorage.isFileContains(MainActivity.this, REC_INCH_FIFTH)) {
                            try {
                                InternalStorage.deleteFolder(MainActivity.this, REC_INCH_FIFTH);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (InternalStorage.isFileContains(MainActivity.this, MM_DATA)) {

                            try {
                                InternalStorage.deleteFolder(MainActivity.this, MM_DATA);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        Hawk.put(REC_INCH_FIFTH, "");
                        Hawk.deleteAll();
                        Hawk.put(REC_INCH_FIFTH, "");
                        changeDrawer();
                        Intent intent = getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private void cleardata() {
        try {
            InternalStorage.deleteFolder(MainActivity.this, REC_INCH_FIFTH);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    String currentdate = "";

    private void createFileandSaveData(int code) {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentdate = df.format(c);

        InputFinalRecordType inputFinalRecordType = null;


        String top = "";
        GraphDataModelClass feetAndInches = null;
        GraphDataModelClass mm_data = null;
        List<InfoModel> list = null;


        if (InternalStorage.isFileContains(this, "infoList")) {

            try {
                list = (List<InfoModel>) InternalStorage.readObject(this, "infoList");
                if (InternalStorage.isFileContains(MainActivity.this, FEET_AND_INCHES_DATA) && InternalStorage.isFileContains(MainActivity.this, MM_DATA)) {
                    feetAndInches = (GraphDataModelClass) InternalStorage.readObject(MainActivity.this, MM_DATA);
                    mm_data = (GraphDataModelClass) InternalStorage.readObject(MainActivity.this, MM_DATA);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (list != null) {
                top = top.concat("Date:; " + currentdate + "\n");
                //       top = top.concat("\n\n" + "         " + "   INFO SECTION    " + "             ");
                //       top = top.concat("\n" + "           " + " ============    " + "              \n\n");


                if (TextUtils.isEmpty(list.get(10).getValue()))
                    list.get(10).setValue("0");
                top = top.concat("By; " + list.get(0).getValue());
                top = top.concat("\n" + "CORRECTION" + ":; " + list.get(1).getValue() + "mm");
                top = top.concat("\n" + "BARGE N" + ":; " + list.get(2).getValue());
                top = top.concat("\n" + "CONDTION" + ":; " + list.get(3).getValue());
                top = top.concat("\n" + "VESSEL" + ":; " + list.get(4).getValue());
                top = top.concat("\n" + "COMODITY" + ":; " + list.get(5).getValue());

                top = top.concat("\n" + "FILE NR:; " + list.get(6).getValue());
                top = top.concat("\n" + "FLEET:; " + list.get(7).getValue());
                top = top.concat("\n" + "LAUNCH" + ":; " + list.get(8).getValue());
                top = top.concat("\n" + "UOM" + ":; " + list.get(9).getValue());

                if (list.get(9).getValue().equalsIgnoreCase("METRIC"))
                    top = top.concat("\n" + "HEIGHT" + ":; " + list.get(10).getValue() + " mm");
                else if (list.get(9).getValue().equalsIgnoreCase("IMPERIAL"))

                    if (Hawk.get(REC_INCH_FIFTH) == null || TextUtils.isEmpty(Hawk.get(REC_INCH_FIFTH).toString()))
                        top = top.concat("\n" + "HEIGHT" + ":; " + list.get(10).getValue() + " Ft " + "0 " + "In");
                    else
                        top = top.concat("\n" + "HEIGHT" + ":; " + list.get(10).getValue() + " Ft " + Hawk.get(REC_INCH_FIFTH).toString() + "In");


                top = top.concat("\n" + "TYPE" + ":; " + list.get(11).getValue());
                top = top.concat("\n" + "BARGE TYPE" + ":; " + list.get(12).getValue() + "\n");

                FILE_NAME = "_" + list.get(6).getValue() + "_" + list.get(2).getValue() + "_" + list.get(0).getValue() + "_" + list.get(3).getValue();

                if (list.get(12).getValue().equalsIgnoreCase("Rake") && list.get(9).getValue().equalsIgnoreCase("METRIC")) {
                    try {
                        InfoBottomData infoBottomData = (InfoBottomData) InternalStorage.readObject(MainActivity.this, REC_INCH_FIFTH);
                        top = top.concat("\n" + "A - HL:; " + infoBottomData.getA_hl() + " mm");
                        top = top.concat("\n" + "B - HL - WL:; " + infoBottomData.getB_hl_wl() + " mm");
                        top = top.concat("\n" + "C - HL - END RS" + ":; " + infoBottomData.getC_hl_end_rs() + " mm");
                        top = top.concat("\n" + "D - HL - EDGE MH" + ":; " + infoBottomData.getD_hl_edge_mh() + " mm");
                        top = top.concat("\n" + "E - END MH - RAKE B" + ":; " + infoBottomData.getE_end_mh_rake_b() + " mm\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (list.get(12).getValue().equalsIgnoreCase("Rake") && list.get(9).getValue().equalsIgnoreCase("IMPERIAL")) {

                    try {
                        InfoBottomData infoBottomData = (InfoBottomData) InternalStorage.readObject(MainActivity.this, REC_INCH_FIFTH);
                        top = top.concat("\n" + "A - HL:; " + infoBottomData.getA_hl() + " Ft " + infoBottomData.getInch_first() + " In");
                        top = top.concat("\n" + "B - HL - WL:; " + infoBottomData.getB_hl_wl() + " Ft " + infoBottomData.getInch_second() + " In");
                        top = top.concat("\n" + "C - HL - END RS" + ":; " + infoBottomData.getC_hl_end_rs() + " Ft " + infoBottomData.getInch_third() + " In");
                        top = top.concat("\n" + "D - HL - EDGE MH" + ":; " + infoBottomData.getD_hl_edge_mh() + " Ft " + infoBottomData.getInch_fourth() + " In");
                        top = top.concat("\n" + "E - END MH - RAKE B" + ":; " + infoBottomData.getE_end_mh_rake_b() + " Ft " + infoBottomData.getInch_fifth() + " In\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "please fill information first", Toast.LENGTH_LONG).show();
            return;
        }


        if (feetAndInches != null && mm_data != null) {
            //      top = top.concat("\n\n" + "         " + "   INPUT SECTION    " + "             ");
            //      top = top.concat("\n" + "           " + " =============    " + "              \n");

            if (Hawk.get(INPUT_SELECTED_TYPE) != null) {


                if (Hawk.get(INPUT_SELECTED_TYPE).toString().equalsIgnoreCase("Feet and Inches")) {
                    //top = top.concat("\n FINAL READINGSUOM:; IMPERIAL");
                    top = top.concat("\nFOP:; " + feetAndInches.getFop().getValue_first());
                    top = top.concat("\nFOS:; " + feetAndInches.getFos().getValue_first());
                    top = top.concat("\nMSP:; " + feetAndInches.getMsp().getValue_first());
                    top = top.concat("\nMSS:; " + feetAndInches.getMss().getValue_first());
                    top = top.concat("\nAFP:; " + feetAndInches.getAfp().getValue_first());
                    top = top.concat("\nAFS:; " + feetAndInches.getAfs().getValue_first());

                    top = top.concat("\n\nFORE:; " + feetAndInches.getFere());
                    top = top.concat("\n1P:; " + feetAndInches.getOneP());
                    top = top.concat("\n1S:; " + feetAndInches.getOneS());
                    top = top.concat("\n2P:; " + feetAndInches.getTwoP());
                    top = top.concat("\n2S:; " + feetAndInches.getTwoS());
                    top = top.concat("\n3P:; " + feetAndInches.getThreeP());
                    top = top.concat("\n3S:; " + feetAndInches.getThreeS());
                    top = top.concat("\n4P:; " + feetAndInches.getFourP());
                    top = top.concat("\n4S:; " + feetAndInches.getFourS());
                    top = top.concat("\n5P:; " + feetAndInches.getFiveP());
                    top = top.concat("\n5S:; " + feetAndInches.getFiveS());
                    top = top.concat("\nAFT:; " + feetAndInches.getAft() + "\n");
                    top = top.concat("\nNote:; ");
                    top = top.concat("\n" + Hawk.get("note") != null ? Hawk.get("note").toString() : "" + "\n");
                    top = top.concat("\n" + "\nHeight:; " + Hawk.get("height") + " Ft");

                    if (Hawk.get(INPUT_FINAL_RECORD) != null) {
                        inputFinalRecordType = Hawk.get(INPUT_FINAL_RECORD);
                        top = top.concat("\nA_QTR:; " + inputFinalRecordType.getFeet_and_inches().getQtm() + " Ft");
                        top = top.concat("\nQTR:; " + inputFinalRecordType.getFeet_and_inches().getQtr() + " Ft\n\n");
                    }

                } else {
                    //      top = top.concat("UOM:; METRIC \n");
                    top = top.concat("\nFOP:; " + mm_data.getFop().getValue_third());
                    top = top.concat("\nFOS:; " + mm_data.getFos().getValue_third());
                    top = top.concat("\nMSP:; " + mm_data.getMsp().getValue_third());
                    top = top.concat("\nMSS:; " + mm_data.getMss().getValue_third());
                    top = top.concat("\nAFP:; " + mm_data.getAfp().getValue_third());
                    top = top.concat("\nAFS:; " + mm_data.getAfs().getValue_third());
                    top = top.concat("\n\nFORE:; " + mm_data.getFere());
                    top = top.concat("\n1P:; " + mm_data.getOneP());
                    top = top.concat("\n1S:; " + mm_data.getOneS());
                    top = top.concat("\n2P:; " + mm_data.getTwoP());
                    top = top.concat("\n2S:; " + mm_data.getTwoS());
                    top = top.concat("\n3P:; " + mm_data.getThreeP());
                    top = top.concat("\n3S:; " + mm_data.getThreeS());
                    top = top.concat("\n4P:; " + mm_data.getFourP());
                    top = top.concat("\n4S:; " + mm_data.getFourS());
                    top = top.concat("\n5P:; " + mm_data.getFiveP());
                    top = top.concat("\n5S:; " + mm_data.getFiveS());
                    top = top.concat("\nAFT:; " + mm_data.getAft() + "\n");
                    top = top.concat("\nNote:; ");
                    top = top.concat("\n" + Hawk.get("note") != null ? Hawk.get("note").toString() : "" + "\n");
                    top = top.concat("\n" + "\nHeight:; " + list.get(10).getValue() + " mm");

                    if (Hawk.get(INPUT_FINAL_RECORD) != null) {
                        inputFinalRecordType = Hawk.get(INPUT_FINAL_RECORD);
                        top = top.concat("\nA_QTR:; " + inputFinalRecordType.getFeet_and_inches().getQtm() + " mm");
                        top = top.concat("\nQTR:; " + inputFinalRecordType.getFeet_and_inches().getQtr() + " mm\n\n");
                    }
                }
            }

            //                feetAndInches = (GraphDataModelClass) InternalStorage.readObject(context, FEET_AND_INCHES_DATA);
//                mm = (GraphDataModelClass) InternalStorage.readObject(context, MM_DATA);
//                feetAndInchesFinal = inputFinalRecordType.getFeet_and_inches();
//                mmFinal=inputFinalRecordType.getMm();


        }

        // Hawk.put(mParam1+"last",dataLastLine);

        if (null != Hawk.get("FOPlast", null)) {

            top = top.concat("FOP;");  // is it fine
            top = top.concat("" + Hawk.get("FOPlast"));

            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
            Log.i(TAG, "foplast" + top);

        }
        if (null != Hawk.get("FOSlast", null)) {
            top = top.concat("FOS;");
            top = top.concat("" + Hawk.get("FOSlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");

        }
        if (null != Hawk.get("MSPlast", null)) {
            top = top.concat("MSP;");
            top = top.concat("" + Hawk.get("MSPlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("MSSlast", null)) {
            top = top.concat("MSS;");
            top = top.concat("" + Hawk.get("MSSlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFPlast", null)) {
            top = top.concat("AFP;");
            top = top.concat("" + Hawk.get("AFPlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFSlast", null)) {
            top = top.concat("AFS;");
            top = top.concat("" + Hawk.get("AFSlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        if (null != Hawk.get("AFMlast", null)) {
            top = top.concat("AFM;");
            top = top.concat("" + Hawk.get("AFMlast"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            top = top.concat("\n");
        }
        top = top.concat("\nRAW DATA:");

        /* ////////////////////////////////////////////////////////////////////////////////////////////////// */

        if (null != Hawk.get("FOP", null)) {

            top = top.concat("\n" + Hawk.get("FOP"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            Log.i(TAG, "Full fop data" + top);

            //top = top.concat("\n");


        }
        if (null != Hawk.get("FOS", null)) {
            top = top.concat("" + Hawk.get("FOS"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //top = top.concat("\n");

        }
        if (null != Hawk.get("MSP", null)) {
            top = top.concat("" + Hawk.get("MSP"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //    top = top.concat("\n");
        }
        if (null != Hawk.get("MSS", null)) {
            top = top.concat("" + Hawk.get("MSS"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //    top = top.concat("\n");
        }
        if (null != Hawk.get("AFP", null)) {
            top = top.concat("" + Hawk.get("AFP"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //    top = top.concat("\n");
        }
        if (null != Hawk.get("AFS", null)) {
            top = top.concat("" + Hawk.get("AFS"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //    top = top.concat("\n");
        }
        if (null != Hawk.get("AFM", null)) {
            top = top.concat("" + Hawk.get("AFM"));
            if (top.contains("\r")) {
                top = top.replace("\r", "");
            }
            //   top = top.concat("\n");
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();

        createFile(Hawk.get("pobject", "") + "." + fmt.format(calendar.getTime()) + ".txt", top, code);
    }


    public void createFile(final String fileName, String content, int code) {

        File folder = new File(getExternalFilesDir(null), "DS HARITZ");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // File Name
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat f = new SimpleDateFormat("ddMMyy");

        File sub_folder = new File(folder, f.format(c));
        if (!sub_folder.exists()) {
            sub_folder.mkdirs();
        }
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy_hh-mm-ss");
        String file_name = df.format(c).replace("-", "");

        file_name = file_name + FILE_NAME + ".txt";

        File txtFile = new File(sub_folder, file_name);

////////////////////////////////////////////////////////

        if (code == 102) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle(R.string.share_report);
            // Setting Dialog Message
            alertDialog.setMessage(R.string.select_report);
            // Setting Icon to Dialog
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(R.string.save_graph, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed YES button. Write Logic Here\
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(R.string.fiepath)
                            .content(txtFile.getAbsolutePath())
                            .cancelable(false)
                            .positiveText(R.string.gotIt)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialogs, @NonNull DialogAction which) {

                                    FileWriter writer = null;
                                    try {
                                        writer = new FileWriter(txtFile);
                                        writer.append(content);
                                        writer.flush();
                                        writer.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (txtFile.exists()) {
                                        dialogs.dismiss();
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.setType("application/xml");
                                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, txtFile);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
                                        startActivity(Intent.createChooser(shareIntent, "share"));
                                    }
                                }
                            })
                            .show();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(R.string.save_picture, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed No button. Write Logic Here
                    getImagesFromStorage();
                    if (imgList.size() > 0) {
                        if (createPdfFromImages()) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setType("application/pdf");
                            Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, pdfFile);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, pdfFile.getName());
                            startActivity(Intent.createChooser(shareIntent, "share"));
                        }
                    } else {

                    }
                }
            });

            // Setting Netural "Cancel" Button
            alertDialog.setNeutralButton("Both", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed Cancel button. Write Logic Here
                    saveBothFiles(txtFile, content, 1, fileName);
                }
            });
            // Showing Alert Message
            alertDialog.show();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle(R.string.save_report);
            // Setting Dialog Message
            alertDialog.setMessage(R.string.select_save_report);
            // Setting Icon to Dialog
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(R.string.save_graph, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(R.string.fiepath)
                            .content(txtFile.getAbsolutePath())
                            .cancelable(false)
                            .positiveText(R.string.save)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialogs, @NonNull DialogAction which) {
                                    FileWriter writer = null;
                                    try {
                                        writer = new FileWriter(txtFile);
                                        writer.append(content);
                                        writer.flush();
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(R.string.save_picture, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    getImagesFromStorage();
                    if (imgList.size() > 0) {
                        if (createPdfFromImages()) {
                            TastyToast.makeText(MainActivity.this, "Picture Report Save Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }
                    }
                }
            });
            // Setting Netural "Cancel" Button
            alertDialog.setNeutralButton("Both", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    saveBothFiles(txtFile, content, 0, fileName);
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    private void saveBothFiles(File txtFile, String content, int from, String txtFileName) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(txtFile);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getImagesFromStorage();
        if (imgList.size() > 0) {
            if (createPdfFromImages()) {
                TastyToast.makeText(MainActivity.this, " Both Reports Saved Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        } else
            TastyToast.makeText(MainActivity.this, "Graph r Saved Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        if (from == 1) {
            if (imgList.size() > 0) {
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, pdfFile);
                Uri uritxt = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, txtFile);
                Uris.clear();
                Uris.add(uri);
                Uris.add(uritxt);
                Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uris);
                shareIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivity(Intent.createChooser(shareIntent, "share"));
            }
        }
    }

    ArrayList<Uri> Uris = new ArrayList<>();

    private void getImagesFromStorage() {
        String folder_name = "Ds Haritz Pictures";
        // Get File directory
        File directory = new File(Environment.getExternalStorageDirectory(), folder_name);
        File[] img_file = directory.listFiles();

        if (img_file != null) {
            if (img_file.length > 0) {

                for (int i = 0; i < img_file.length; i++) {
                    if (img_file[i].getAbsolutePath().contains(".jpg")) {

                        imgList.add(Uri.fromFile(img_file[i]));
                    }
                }
            } else {
                TastyToast.makeText(MainActivity.this, "No pictures found to generate report!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }
    }

    private boolean createPdfFromImages() {

        List<InfoModel> list = null;
        try {
            list = (List<InfoModel>) InternalStorage.readObject(this, "infoList");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        File folder = new File(getExternalFilesDir(null), "DS HARITZ");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // File Name
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat f = new SimpleDateFormat("ddMMyy");

        File sub_folder = new File(folder, f.format(c));
        if (!sub_folder.exists()) {
            sub_folder.mkdirs();
        }

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
        String file_name = df.format(c).replace("-", "");

        file_name = file_name + "_" + list.get(6).getValue() + "_" + list.get(2).getValue() + "_" + list.get(0).getValue() + "_" + list.get(3).getValue() + ".PDF";

        pdfFile = new File(sub_folder, file_name);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile)); //  Change pdf's name.
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        document.setPageSize(PageSize.A4);
        PdfPTable table = new PdfPTable(4);

        table.addCell("DATE");
        table.addCell(currentdate);
        table.addCell("CONDITION");
        table.addCell(list.get(3).getValue().toString());


        table.addCell("BARGE N");
        table.addCell(list.get(2).getValue().toString());
        table.addCell("COMODITY");
        table.addCell(list.get(5).getValue().toString());

        table.addCell("VESSEL");
        table.addCell(list.get(4).getValue().toString());
        table.addCell("FILE NR");
        table.addCell(list.get(6).getValue().toString());

        table.setSpacingAfter(10f);
        table.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);

        for (int i = 0; i < imgList.size(); i++) {

            if (i % 2 == 0) {
                if (i != 0) {
                    document.newPage();
                }
            }

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgList.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (i % 2 == 0) {
                    document.add(table);
                }
                Bitmap newBitmap = Bitmap.createBitmap(Objects.requireNonNull(bitmap).getWidth(), bitmap.getHeight(), bitmap.getConfig());
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

                Image image = Image.getInstance(outputStream.toByteArray());

                image.setAlignment(Image.ALIGN_CENTER);

                float docW = PageSize.A4.getWidth();
                float docH = ((PageSize.A4.getHeight()) * 4) / 11;
                image.scaleToFit(docW, docH);

                if (!(i % 2 == 0)) {
                    document.add(new Paragraph("   "));
                }

                document.add(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        document.close();

        return true;
    }

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
}