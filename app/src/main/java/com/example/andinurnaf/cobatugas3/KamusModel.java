package com.example.andinurnaf.cobatugas3;

import android.os.Parcel;
import android.os.Parcelable;

public class KamusModel implements Parcelable {

    private int id;
    private String kata;
    private String Terjemah;

    public KamusModel() {
    }

    public KamusModel(String kata, String Terjemah) {
        this.kata = kata;
        this.Terjemah = Terjemah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return kata;
    }

    public void setWord(String kata) {
        this.kata = kata;
    }

    public String getTranslate() {
        return Terjemah;
    }

    public void setTranslate(String Terjemah) {
        this.Terjemah = Terjemah;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.kata);
        dest.writeString(this.Terjemah);
    }

    protected KamusModel(Parcel in) {
        this.id = in.readInt();
        this.kata = in.readString();
        this.Terjemah= in.readString();
    }

    public static final Parcelable.Creator<KamusModel> CREATOR = new Parcelable.Creator<KamusModel>() {
        @Override
        public KamusModel createFromParcel(Parcel source) {
            return new KamusModel(source);
        }

        @Override
        public KamusModel[] newArray(int size) {
            return new KamusModel[size];
        }
    };
}
