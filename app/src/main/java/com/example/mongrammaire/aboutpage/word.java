package com.example.mongrammaire.aboutpage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.mongrammaire.Element;
import com.example.mongrammaire.R;
import com.example.mongrammaire.aboutpage.AboutPage;

import java.util.Calendar;

public class word extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_word, container, false);
        Element versionElement = new Element();
        versionElement.setTitle(getString(R.string.version_format, "6.2"));

        Element adsElement = new Element();
        adsElement.setTitle(getString(R.string.advertise_with_us));

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .addItem(versionElement)
                .addItem(adsElement)
                .addGroup(getString(R.string.connect_with_us))
                .addEmail("hichamoutaleb7@gmail.com")
                .addFacebook("hichamtalib8")
                .addTwitter("hicham_talib")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("DENZO")
                .addInstagram("hicham_talib1")
                .addGitHub("Hi1talib1World")
                .create();

        viewGroup.addView(aboutPage);
        return viewGroup;
    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.icon);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

}
