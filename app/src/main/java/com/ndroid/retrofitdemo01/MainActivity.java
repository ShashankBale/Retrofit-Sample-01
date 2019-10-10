package com.ndroid.retrofitdemo01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndroid.retrofitdemo01.model.XyzUsers;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btnFetchData;
    ProgressBar progressBar;
    TextView tvOutputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObjects();
        populateView();
        addListeners();
    }

    private void initObjects() {
        btnFetchData = findViewById(R.id.btnFetch);
        progressBar = findViewById(R.id.progressBar);
        tvOutputData = findViewById(R.id.tvOutputData);
    }

    private void populateView() {
        progressBar.setVisibility(View.GONE);
    }

    private void addListeners() {
        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNetworkWork();
            }
        });
    }

    private void doNetworkWork() {
        ApiInterface apiInterface = getRetrofitClient().create(ApiInterface.class);
        Call<List<XyzUsers>> call = apiInterface.getUsers();
        showProgress(true);
        call.enqueue(new XyzUsersCallBack());
    }

    private void showProgress(boolean flag) {
        progressBar.setVisibility(flag ? View.VISIBLE : View.GONE);
        tvOutputData.setVisibility(!flag ? View.VISIBLE : View.GONE);
    }

    public Retrofit getRetrofitClient() {
        //URL : https://jsonplaceholder.typicode.com/users
        Retrofit retrofit
                = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public class XyzUsersCallBack implements Callback<List<XyzUsers>> {
        @Override
        public void onResponse(Call<List<XyzUsers>> call, Response<List<XyzUsers>> response) {
            showProgress(false);
            List<XyzUsers> alUsers = response.body();
            String data = "";
            for (XyzUsers user : alUsers) {
                data = data + "\nName : " + user.getName();
                data = data + "\nPhone : " + user.getPhone();
                data = data + "\n\n\n";
            }
            tvOutputData.setText(data);
        }

        @Override
        public void onFailure(Call<List<XyzUsers>> call, Throwable t) {
            // Log error here since request failed
            Log.e("onFailure", t.toString());
            Toast.makeText(getApplicationContext(), "Sorry, Something went wrong", Toast.LENGTH_LONG).show();
            showProgress(false);
        }
    }
}
