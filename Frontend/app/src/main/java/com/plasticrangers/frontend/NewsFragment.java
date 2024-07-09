package com.plasticrangers.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plasticrangers.frontend.databinding.*;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private ListAdapter listAdapter;
    private ArrayList<NewsData> dataArrayList = new ArrayList<>();
    private NewsData listData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Obtener la altura del ActionBar/Toolbar
        int actionBarHeight = 0;
        if (getActivity() != null && getActivity().getActionBar() != null) {
            actionBarHeight = getActivity().getActionBar().getHeight();
        }

        // Configurar las reglas de posicionamiento para el ListView
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(10, actionBarHeight + 240, 10, 10);
        binding.listview.setLayoutParams(layoutParams);

        int[] imageList = {R.drawable.andalucia, R.drawable.galicia_1_removebg_preview, R.drawable.bandera_de_cordoba, R.drawable.spain};
        String[] linkList = {getResources().getString(R.string.linkSanJuan), getResources().getString(R.string.linkMarea), getResources().getString(R.string.linkGranjas), getResources().getString(R.string.linkTop5)};

        String[] nameList = {getResources().getString(R.string.sanJuan), getResources().getString(R.string.mareaBlanca), getResources().getString(R.string.granjas), getResources().getString(R.string.top5)};
        String[] descList = {getResources().getString(R.string.descsanJuan), getResources().getString(R.string.descMarea), getResources().getString(R.string.descGranjas), getResources().getString(R.string.desctop5)};
        for (int i = 0; i < imageList.length; i++) {
            listData = new NewsData(nameList[i], linkList[i], imageList[i], descList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(requireContext(), dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(requireContext(), DetailedActivity.class);
            intent.putExtra("name", nameList[i]);
            intent.putExtra("link", linkList[i]);
            intent.putExtra("image", imageList[i]);
            intent.putExtra("desc", descList[i]);
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

