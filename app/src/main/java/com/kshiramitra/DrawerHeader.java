package com.kshiramitra;

import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {

    public String name = "Kshira Mitra";
    public String phone = "kshiramitra@gmail.com";
    @View(R.id.profileImageView)
    private ImageView profileImage;
    @View(R.id.nameTxt)
    private TextView nameTxt;
    @View(R.id.emailTxt)
    private TextView emailTxt;

    public DrawerHeader(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Resolve
    private void onResolved() {
        nameTxt.setText(this.name);
        emailTxt.setText(this.phone);
    }
}