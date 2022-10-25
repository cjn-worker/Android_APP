package com.example.androidapp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidapp.R;
import com.example.androidapp.music.SoundPlayUtil;


public class HelpFragment extends Fragment
{
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View inflate = inflater.inflate(R.layout.fragment_help, container, false);


        inflate.setOnTouchListener((v, event) -> true);
        inflate.findViewById(R.id.help_known_btn).setOnClickListener(v -> {
            //播放点击音效
            SoundPlayUtil.getInstance(getContext()).play(3);

            if (getActivity() != null)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(HelpFragment.this);
                transaction.commit();
            }
        });

        return inflate;
    }
}
