package com.example.mongrammaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mongrammaire.Utils.ToastHelper;

public class TcfFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tcf, container, false);

        View btnSimulate = v.findViewById(R.id.btn_start_full_simulation);
        if (btnSimulate != null) {
            btnSimulate.setOnClickListener(view -> 
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Simulation Réelle des Cours")
                    .setMessage("Vous allez démarrer une simulation basée sur les leçons de l'application.\n\n• Questions extraites des exemples\n• Temps limité : 45 minutes\n\nVoulez-vous commencer ?")
                    .setCancelable(false)
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Commencer", (dialog, which) -> {
                        android.content.Intent intent = new android.content.Intent(getActivity(), LessonSimulationActivity.class);
                        intent.putExtra("IS_GLOBAL_SIMULATION", true);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                    })
                    .show()
            );
        }

        setupModuleClicks(v);

        View btnBack = v.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(view -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });
        }

        return v;
    }

    private void setupModuleClicks(View v) {
        View cardListening = v.findViewById(R.id.card_listening);
        if (cardListening != null) {
            cardListening.setOnClickListener(view -> startModuleTraining("Compréhension orale"));
        }

        View cardStructure = v.findViewById(R.id.card_structure);
        if (cardStructure != null) {
            cardStructure.setOnClickListener(view -> startModuleTraining("Structure de la langue"));
        }

        View cardReading = v.findViewById(R.id.card_reading);
        if (cardReading != null) {
            cardReading.setOnClickListener(view -> startModuleTraining("Compréhension écrite"));
        }

        View cardWriting = v.findViewById(R.id.card_writing);
        if (cardWriting != null) {
            cardWriting.setOnClickListener(view -> startModuleTraining("Expression écrite"));
        }
    }

    private void startModuleTraining(String moduleName) {
        android.content.Intent intent = new android.content.Intent(getActivity(), TcfModuleActivity.class);
        intent.putExtra("MODULE_NAME", moduleName);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
