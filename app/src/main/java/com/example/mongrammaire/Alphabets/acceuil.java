package com.example.mongrammaire.Alphabets;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mongrammaire.R;
import com.example.mongrammaire.databinding.FragmentAcceuilBinding;

import java.util.HashMap;
import java.util.Map;

public class acceuil extends Fragment {

    private FragmentAcceuilBinding binding;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap = new HashMap<>();

    public acceuil() {
        // Required empty public constructor
    }

    public static acceuil newInstance() {
        return new acceuil();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
                
        // Load sounds
        loadSound(R.id.btn_a, R.raw.a);
        loadSound(R.id.btn_b, R.raw.b);
        loadSound(R.id.btn_c, R.raw.c);
        loadSound(R.id.btn_d, R.raw.d);
        loadSound(R.id.btn_e, R.raw.e);
        loadSound(R.id.btn_f, R.raw.f);
        loadSound(R.id.btn_g, R.raw.g);
        loadSound(R.id.btn_i, R.raw.i);
        loadSound(R.id.btn_j, R.raw.j);
        loadSound(R.id.btn_k, R.raw.k);
        loadSound(R.id.btn_l, R.raw.l);
        loadSound(R.id.btn_m, R.raw.m);
        loadSound(R.id.btn_n, R.raw.n);
        loadSound(R.id.btn_o, R.raw.o);
        loadSound(R.id.btn_p, R.raw.p);
        loadSound(R.id.btn_q, R.raw.q);
        loadSound(R.id.btn_r, R.raw.r);
        loadSound(R.id.btn_s, R.raw.s);
    }

    private void loadSound(int viewId, int rawId) {
        if (getContext() != null) {
            int soundId = soundPool.load(getContext(), rawId, 1);
            soundMap.put(viewId, soundId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcceuilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        View.OnClickListener listener = v -> {
            Integer soundId = soundMap.get(v.getId());
            if (soundId != null) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        };

        // Assign listener to all alphabet buttons in the grid
        for (int i = 0; i < binding.alphabetGrid.getChildCount(); i++) {
            View child = binding.alphabetGrid.getChildAt(i);
            if (child instanceof android.widget.Button) {
                child.setOnClickListener(listener);
            }
        }

        binding.btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
