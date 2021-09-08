package co.za.jaspasystems.nonameapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.za.jaspasystems.nonameapp.R;
import co.za.jaspasystems.nonameapp.models.Hamper;
import co.za.jaspasystems.nonameapp.retrofit.ApiClient;
import co.za.jaspasystems.nonameapp.retrofit.ApiInterface;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadHamperActivity extends AppCompatActivity {

    private Button btnSubmit;
    private TextInputLayout tilName, tilContents, tilPrice;
    private ImageView ivHamperImage;
    private String hamperName, hamperContents, hamperPrice, hamperImage;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    private Uri imageUri;
    private StorageReference storageReference, tempStorageReference;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_hamper);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        initViews();
    }



    private void initViews(){
        btnSubmit = findViewById(R.id.activity_load_hamper_btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserFields();
            }
        });
        
        tilName = findViewById(R.id.activity_load_hamper_tilName);
        tilContents = findViewById(R.id.activity_load_hamper_tilContents);
        tilPrice = findViewById(R.id.activity_load_hamper_tilPrice);
        ivHamperImage = findViewById(R.id.activity_load_hamper_ivHamperImage);
        ivHamperImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromDevice();
                // uploadHamperImage();
            }
        });
    }

    private void selectImageFromDevice(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }
    private void uploadHamperImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/hampers/"+fileName);

        tempStorageReference = FirebaseStorage.getInstance().getReference();
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                hamperImage = uri.toString();
                            }
                        });
                        Toast.makeText(LoadHamperActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(LoadHamperActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void checkUserFields(){
        if (TextUtils.isEmpty(tilName.getEditText().getText())){
            tilName.getEditText().setError("Hamper name is required!");
            return;
        }else{
            hamperName = tilName.getEditText().getText().toString().trim();
        }

        if (TextUtils.isEmpty(tilContents.getEditText().getText())){
            tilContents.getEditText().setError("Hamper contents are required!");
            return;
        }else{
            hamperContents = tilContents.getEditText().getText().toString().trim();
        }

        if (TextUtils.isEmpty(tilPrice.getEditText().getText())){
            tilPrice.getEditText().setError("Hamper price is required!");
            return;
        }else{
            hamperPrice = tilPrice.getEditText().getText().toString().trim();
        }

        saveHamperToDatabase();
    }
    private void saveHamperToDatabase() {
        progressDialog.show();

        Hamper hamper = new Hamper(10,hamperName,hamperContents,Double.parseDouble(hamperPrice),hamperImage);

        firebaseFirestore.collection("hampers")
                .add(hamper)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Toast.makeText(LoadHamperActivity.this, "Hamper added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoadHamperActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data !=null && data.getData() !=null){
            imageUri = data.getData();
            ivHamperImage.setImageURI(imageUri);
            uploadHamperImage();
        }
    }
}