package co.za.jaspasystems.nonameapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import co.za.jaspasystems.nonameapp.R;
import co.za.jaspasystems.nonameapp.adapters.HamperAdapter;
import co.za.jaspasystems.nonameapp.models.Hamper;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Hamper> hamperArrayList;
    private HamperAdapter hamperAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        // set recycler view
        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialise views
        progressBar = findViewById(R.id.main_progressBar);
        textView = findViewById(R.id.main_textView);

        // initialise arraylist and adapter
        hamperArrayList = new ArrayList<>();
        hamperAdapter = new HamperAdapter(this,hamperArrayList);
        
        // get data from CloudFirestore
        getListOfHampersFromCloudFirestore();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hamperArrayList.clear();
        getListOfHampersFromCloudFirestore();
    }

    private void getListOfHampersFromCloudFirestore() {
        firebaseFirestore.collection("hampers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() == 0) {
                            progressBar.setVisibility(GONE);
                            textView.setText("No hampers available. Add hampers");
                            textView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(GONE);
                        }else{

                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                                hamperArrayList.add(doc.getDocument().toObject(Hamper.class));
                            }

                            hamperAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(hamperAdapter);
                            progressBar.setVisibility(GONE);
                            textView.setVisibility(GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressBar.setVisibility(GONE);
                        textView.setText(e.getMessage());
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }
                });

    }

    public void openLoadHamperScreen(View view) {
        Intent intent = new Intent(MainActivity.this,LoadHamperActivity.class);
        startActivity(intent);
    }
}