package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle("Version " + getString(R.string.versionName));

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .addItem(versionElement)
                .addGroup(getString(R.string.contact_developer))
                .addEmail("duartemr.pinto@gmail.com", getString(R.string.about_email_label))
                .addGitHub("444Duarte/basic-typing-benchmark-android", getString(R.string.about_github_label))
                .create();
        setContentView(aboutPage);
    }
}
