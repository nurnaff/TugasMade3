package com.example.andinurnaf.cobatugas3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String ITEM_WORD = "ITEM_WORD";
    public static final String ITEM_TRANSLATE = "ITEM_TRANSLATE";

    TextView t_kata;
    TextView t_terjemah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        t_kata=(TextView)findViewById(R.id.tv_word);
        t_terjemah=(TextView)findViewById(R.id.tv_translate);

        ButterKnife.bind(this);

        t_kata.setText(getIntent().getStringExtra(ITEM_WORD));
        t_terjemah.setText(getIntent().getStringExtra(ITEM_TRANSLATE));
    }
}
