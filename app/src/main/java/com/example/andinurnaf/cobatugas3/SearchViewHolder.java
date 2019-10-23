package com.example.andinurnaf.cobatugas3;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_word)
    TextView t_kata;

    @BindView(R.id.tv_translate)
    TextView t_Terjemah;

    public SearchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final KamusModel item) {
        t_kata.setText(item.getWord());
        t_Terjemah.setText(item.getTranslate());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.ITEM_WORD, item.getWord());
                intent.putExtra(DetailActivity.ITEM_TRANSLATE, item.getTranslate());
                itemView.getContext().startActivity(intent);
            }
        });
    }
}