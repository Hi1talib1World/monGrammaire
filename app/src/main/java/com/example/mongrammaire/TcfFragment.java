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
                    .setTitle("Simulation d'examen TCF")
                    .setMessage("Vous allez démarrer un examen blanc complet contenant les 4 modules.\n\n• Temps limité : 90 minutes\n• Aucun retour en arrière possible\n\nVoulez-vous commencer ?")
                    .setCancelable(false)
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Commencer", (dialog, which) -> {
                        android.content.Intent intent = new android.content.Intent(getActivity(), FullSimulationActivity.class);
                        intent.putExtra("IS_GLOBAL_SIMULATION", true);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                    })
                    .show()
            );
        }

        return v;
    }
}
