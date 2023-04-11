package com.example.fastbilling.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastbilling.MainActivity;
import com.example.fastbilling.R;
import com.example.fastbilling.adapter.RecyclerAdapter;
import com.example.fastbilling.databinding.FragmentHomeBinding;
import com.example.fastbilling.db.DBHandler;
import com.example.fastbilling.model.ItemsModel;
import com.example.fastbilling.ui.AddItemsActivity;
import com.example.fastbilling.ui.BillingCheckoutActivity;
import com.example.fastbilling.util.services;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<ItemsModel> dataHolder=new ArrayList<>();
    private FloatingActionButton floatingAddButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        dataHolder.clear();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        floatingAddButton = root.findViewById(R.id.floatingAddButton);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter rAdapter=new RecyclerAdapter(dataHolder);
        recyclerView.setAdapter(rAdapter);


        Cursor cursor=new DBHandler(getActivity()).readAllData();
        while(cursor.moveToNext())
        {
            ItemsModel model=new ItemsModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));

                dataHolder.add(model);

        }



        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

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

                getAlertDialog(deletedItem);

                rAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());


            }
        }).attachToRecyclerView(recyclerView);


        return root;
    }

    private void getAlertDialog(ItemsModel deleteItem){
        Dialog dialog = services.getAlertDialog(getActivity());
        TextView title = dialog.findViewById(R.id.alert_title);
        RelativeLayout postive = dialog.findViewById(R.id.positiveBtn);
        RelativeLayout nagetive = dialog.findViewById(R.id.nagetiveBtn);
        title.setText("Are you sure to delete this item ?");

        postive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor=new DBHandler(getActivity()).deleteItem(deleteItem.getImei());
                while(cursor.moveToNext())
                {
                    ItemsModel model=new ItemsModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                    dataHolder.remove(model);
                }
                dialog.dismiss();
            }
        });
        nagetive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}