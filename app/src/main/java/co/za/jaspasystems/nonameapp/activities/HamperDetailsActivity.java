package co.za.jaspasystems.nonameapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import co.za.jaspasystems.nonameapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class HamperDetailsActivity extends AppCompatActivity {

    private EditText etName, etContents, etPrice;
    private String imageUrl;
    private ImageView ivImage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamper_details);

        initViews();
    }

    private void initViews() {
        etName = findViewById(R.id.activity_hamper_details_etHamperName);
        etContents = findViewById(R.id.activity_hamper_details_etHamperContents);
        etPrice = findViewById(R.id.activity_hamper_details_etHamperPrice);
        ivImage = findViewById(R.id.activity_hamper_details_ivHamperImage);

        // set data
        etName.setText(getIntent().getStringExtra("name"));
        etContents.setText(getIntent().getStringExtra("contents"));
        // etPrice.setText(getIntent().getData("price"));
    }
}