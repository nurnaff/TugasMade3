package com.example.andinurnaf.cobatugas3;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreloadActivity extends AppCompatActivity {

    ProgressBar progress_bar;
    TextView t_muat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        t_muat=(TextView)findViewById(R.id.tv_load);

        ButterKnife.bind(this);

        new LoadData().execute();
    }

    private void loadDummyProcess() {
        final int countDown = 1000;
        progress_bar.setMax(countDown);
        CountDownTimer countDownTimer = new CountDownTimer(countDown, (countDown / 100)) {
            @Override
            public void onTick(long l) {
                progress_bar.setProgress((int) (countDown - l));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {
        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(PreloadActivity.this);
            appPreference = new AppPreference(PreloadActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KamusModel> kamusEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<KamusModel> kamusIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kamusHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                t_muat.setVisibility(View.INVISIBLE);
                try {
                    synchronized (this) {
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress_bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreloadActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }
    }

    public ArrayList<KamusModel> preLoadRaw(int data) {
        ArrayList<KamusModel> kamusModels = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                KamusModel kamusModel;
                kamusModel = new KamusModel(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kamusModels;
    }
}