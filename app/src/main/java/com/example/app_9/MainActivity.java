package com.example.app_9;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String NO_DATA = "No data";
    EditText enbpointEditText;
    RecyclerView recyclerView;
    PersonAdapter personAdapter;
    FilmAdapter filmAdapter;
    Button fetchPerson;
    Button fetchPeople;
    Button fetchFilm;
    Button fetchFilms;
    ArrayList<Person> personArrayList = new ArrayList<>();
    ArrayList<Film> filmArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enbpointEditText = findViewById(R.id.etEndPoint);
        recyclerView = findViewById(R.id.recyclerView);
        fetchPerson = findViewById(R.id.btnPerson);
        fetchPeople = findViewById(R.id.btnPeople);
        fetchFilm = findViewById(R.id.btnFilm);
        fetchFilms = findViewById(R.id.btnFilms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter(personArrayList);
        filmAdapter = new FilmAdapter(filmArrayList);

        fetchPerson.setOnClickListener(view -> {
            recyclerView.setAdapter(personAdapter);
            String endpoint = enbpointEditText.getText().toString();

            checkPersonDataEndpoint(endpoint);
        });

        fetchPeople.setOnClickListener(view -> {
            recyclerView.setAdapter(personAdapter);
            fetchPeopleData();
        });

        fetchFilm.setOnClickListener(view -> {
            recyclerView.setAdapter(filmAdapter);
            String endpoint = enbpointEditText.getText().toString();

            checkFilmDataEndpoint(endpoint);
        });

        fetchFilms.setOnClickListener(view -> {
            recyclerView.setAdapter(filmAdapter);
            fetchFilmsData();
        });
    }

    private void checkFilmDataEndpoint(String endpoint) {
        if (!endpoint.isEmpty()) {
            fetchFilmData(endpoint);
        } else {
            Toast.makeText(MainActivity.this, "Please enter film ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPersonDataEndpoint(String endpoint) {
        if (!endpoint.isEmpty()) {
            fetchPersonData(endpoint);
        } else {
            Toast.makeText(MainActivity.this, "Please enter person ID", Toast.LENGTH_SHORT).show();
        }
    }

    private <T> void executeApiCall(Call<T> call, ArrayList<T> dataList, RecyclerView.Adapter<?> adapter) {
        call.enqueue(new Callback<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataList.clear();
                    dataList.add(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, NO_DATA, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private <T, R> void executeApiCallForList(Call<R> call, ArrayList<T> dataList, RecyclerView.Adapter<?> adapter, Function<R, List<T>> extractor) {
        call.enqueue(new Callback<R>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<R> call, @NonNull Response<R> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<T> items = extractor.apply(response.body());
                    dataList.clear();
                    dataList.addAll(items);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, NO_DATA, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<R> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchFilmData(String endpoint) {
        Call<Film> call = RetrofitClient.getInstance().getSwaPiService().getFilms(endpoint);
        executeApiCall(call, filmArrayList, filmAdapter);
    }

    private void fetchFilmsData() {
        Call<FilmResponse> call = RetrofitClient.getInstance().getSwaPiService().getFilms();
        executeApiCallForList(call, filmArrayList, filmAdapter, FilmResponse::getResults);
    }

    private void fetchPersonData(String endpoint) {
        Call<Person> call = RetrofitClient.getInstance().getSwaPiService().getPerson(endpoint);
        executeApiCall(call, personArrayList, personAdapter);
    }

    private void fetchPeopleData() {
        Call<PersonResponse> call = RetrofitClient.getInstance().getSwaPiService().getPeople();
        executeApiCallForList(call, personArrayList, personAdapter, PersonResponse::getResults);
    }
}