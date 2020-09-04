package com.haodong.practice.ppjoke.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.haodong.practice.libnavannotation.FragmentDestination;
import com.haodong.practice.ppjoke.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@FragmentDestination(pageUrl = "main/tabs/my")
public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        final TextView textView = root.findViewById(R.id.text_my);
        textView.setText("this is MyFragment");
        return root;
    }
}
