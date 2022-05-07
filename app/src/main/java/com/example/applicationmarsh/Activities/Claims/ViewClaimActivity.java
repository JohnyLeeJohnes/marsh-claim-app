package com.example.applicationmarsh.Activities.Claims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applicationmarsh.Fragments.BottomMenu.ClaimsFragment;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Entities.CarClaim;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;

public class ViewClaimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claim);

        initItems();
    }

    private void initItems() {
        //Init views
        TextView date = findViewById(R.id.date);
        TextView policyNumber = findViewById(R.id.policy_number);
        TextView place = findViewById(R.id.place);
        TextView spz = findViewById(R.id.car_spz);
        TextView type = findViewById(R.id.car_type);
        TextView firstname = findViewById(R.id.driver_firstname);
        TextView lastname = findViewById(R.id.driver_lastname);
        TextView email = findViewById(R.id.contact_email);
        TextView phone = findViewById(R.id.contact_phone);
        TextView damageRange = findViewById(R.id.other_info);
        ImageView photo1 = findViewById(R.id.damage_photo_1);
        ImageView photo2 = findViewById(R.id.damage_photo_2);
        ImageView photo3 = findViewById(R.id.damage_photo_3);
        ImageView photo4 = findViewById(R.id.damage_photo_4);

        //Load claim
        CarClaim claim = ClaimsFragment.chosenClaim;

        //Asign values
        date.setText(claim.getDatum());
        policyNumber.setText(String.valueOf(claim.getPolicyNumber()));
        place.setText(claim.getPlace());
        spz.setText(claim.getSPZ());
        type.setText(claim.getCarType());
        firstname.setText(claim.getContactFirstname());
        lastname.setText(claim.getContactLastname());
        email.setText(claim.getContactEmail());
        phone.setText(claim.getContactPhone());

        //Damage range
        damageRange.setText("");
        if (claim.isDamageCar())
            damageRange.setText(String.format("%s%s\n", damageRange.getText(), getString(R.string.view_my_car_damaged)));
        if (claim.isDamageOtherCar())
            damageRange.setText(String.format("%s%s\n", damageRange.getText(), getString(R.string.view_other_car_damaged)));
        if (claim.isDamageOther())
            damageRange.setText(String.format("%s%s", damageRange.getText(), getString(R.string.view_other_object_damaged)));

        //Photos
        if (claim.getPhoto1() != null)
            photo1.setImageBitmap(scaleBitmap(claim.getPhoto1()));
        if (claim.getPhoto2() != null)
            photo2.setImageBitmap(scaleBitmap(claim.getPhoto2()));
        if (claim.getPhoto3() != null)
            photo3.setImageBitmap(scaleBitmap(claim.getPhoto3()));
        if (claim.getPhoto4() != null)
            photo4.setImageBitmap(scaleBitmap(claim.getPhoto4()));
    }

    //Scale photo down - lower lags
    private Bitmap scaleBitmap(Bitmap b) {
        int resolution = (int) (b.getHeight() * (512.0 / b.getWidth()));
        return Bitmap.createScaledBitmap(b, 512, resolution, true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }
}
