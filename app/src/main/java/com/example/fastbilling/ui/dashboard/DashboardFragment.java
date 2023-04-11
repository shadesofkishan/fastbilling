package com.example.fastbilling.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.fastbilling.R;
import com.example.fastbilling.adapter.RecyclerAdapter;
import com.example.fastbilling.adapter.ScanBillingAdapter;
import com.example.fastbilling.databinding.FragmentDashboardBinding;
import com.example.fastbilling.databinding.FragmentHomeBinding;
import com.example.fastbilling.db.DBHandler;
import com.example.fastbilling.db.roomdb.AppDatabase;
import com.example.fastbilling.db.roomdb.UserDao;
import com.example.fastbilling.db.roomdb.UserDao_Impl;
import com.example.fastbilling.db.roomdbmodule.Item_list;
import com.example.fastbilling.model.BillingItemModel;
import com.example.fastbilling.model.ItemsModel;
import com.example.fastbilling.ui.BillingCheckoutActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private CodeScanner mCodeScanner;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private CodeScanner cs;
    boolean DOES_DEVICE_HAS_BARCODE_SCANNER = false;

    public CodeScannerView scannerView;
    Handler mHandler;

    RecyclerAdapter rAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ItemsModel> dataHolder=new ArrayList<>();
    protected UserDao userDao;
    private RelativeLayout submitBillingBtn;
    private TextView countItem;
    private ArrayList<String> scanList = new ArrayList<>();
    private ArrayList<String> sTitleList = new ArrayList<>();
    private ArrayList<String> sPriceList = new ArrayList<>();




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        final Activity activity = getActivity();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        AppDatabase db = Room.databaseBuilder(activity.getApplicationContext(),
//                AppDatabase.class, "item_list").build();
//        userDao = db.userDao();
        mHandler = new Handler();
        submitBillingBtn = root.findViewById(R.id.submitBillingBtn);
        countItem = root.findViewById(R.id.countItem);
        scannerView = root.findViewById(R.id.scanner_view);
        recyclerView = root.findViewById(R.id.rvScanResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (checkPermission()) {
            new Handler(Looper.getMainLooper())
                    .postDelayed(() -> {
                        try{
                            mCodeScanner.startPreview();
                        } catch (Exception e){

                        } }, 200);
        }
        else {
            requestPermission();
        }
getScan();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                ItemsModel deletedItem = dataHolder.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();

                dataHolder.remove(viewHolder.getAdapterPosition());

                String count = "("+rAdapter.getItemCount()+")";
                countItem.setText(count);

                rAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar.make(recyclerView, deletedItem.getTitle(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataHolder.add(position, deletedItem);
                        rAdapter.notifyItemInserted(position);
                        String count = "("+rAdapter.getItemCount()+")";
                        countItem.setText(count);
                    }
                }).show();
                rAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);


        submitBillingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataHolder.isEmpty())
                {
                    for (int j=0;j<dataHolder.size();j++)
                    {

                        sTitleList.add(dataHolder.get(j).getTitle());
                        sPriceList.add(dataHolder.get(j).getPrice());

                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("title_list", sTitleList);
                    bundle.putStringArrayList("price_list", sPriceList);

                   // InsertBillingData(dataHolder);
                    Intent intent = new Intent(getActivity(), BillingCheckoutActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dataHolder.clear();
                    sTitleList.clear();
                    sPriceList.clear();
                    countItem.setText("(0)");
                    rAdapter.notifyDataSetChanged();
                }

            }
        });

        return root;
    }


    private void getScan(){
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setFlashEnabled(true);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                scanResult(result.getText());
                scanList.add(result.getText().toString());

                Log.d("seperator","\n\n\n========= result  =============\n\n\n");
                Log.d("result","result:"+ result.getText());
                Log.d("seperator","\n\n\n========= result  =============\n\n\n");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String imeisearch = ""+result.getText();
                        Cursor cursor=new DBHandler(getActivity()).getData(imeisearch);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                ItemsModel model = new ItemsModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                                dataHolder.add(model);
                            }
                        }
                        else
                        {
                            Log.e("QuickScan","Cursor is null");
                        }
                        rAdapter=new RecyclerAdapter(dataHolder);
                        recyclerView.setAdapter(rAdapter);
                        String count = "("+rAdapter.getItemCount()+")";
                        countItem.setText(count);
                        rAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private void InsertBillingData(ArrayList<ItemsModel> model) {
        String bTitle="",bDesc="",bPrice="",bImei="",res="";
        for (int i=0;i<model.size();i++)
        {
            bTitle = model.get(i).getTitle();
            bDesc = model.get(i).getDescription();
            bPrice = model.get(i).getPrice();
            bImei = model.get(i).getImei();

        }
        if (!bTitle.equals("") && !bDesc.equals("") && !bPrice.equals("") && !bImei.equals(""))
        {
            //res=new DBHandler(getActivity()).addBillingitems(bTitle,bDesc,bPrice,bImei);
        }
         if (!res.equals(""))
         {
             Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
         }
    }

    private void scanResult(String result)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                Log.d("QuickScanFragment","scan items"+scanList);
                Beep();
                getVibration();
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                try{
                    mCodeScanner.startPreview();
                } catch (Exception e){

                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private void sound(){
        RingtoneManager.getRingtone(getActivity(),
                        Uri.parse("android.resource://"+getActivity().getPackageName()+"/raw/bar_code_beep"))
                .play();
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(200);
    }

    private void soundWrong(){
        RingtoneManager.getRingtone(getActivity(),
                        Uri.parse("android.resource://"+getActivity().getPackageName()+"/R.raw/wrong_scan_bar_code"))
                .play();
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1000);
    }

    public static void Beep() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    public void getVibration(){
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1000);
    }


}