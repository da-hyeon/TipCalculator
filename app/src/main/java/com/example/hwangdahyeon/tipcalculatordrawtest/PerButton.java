package com.example.hwangdahyeon.tipcalculatordrawtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.WindowManager;

public class PerButton {

    public int nW_x, nW_y; // 단어 좌표
    public int sW_x, sW_y; // 단어 크기
    public Bitmap PerButton; // 단어 이미지

    public int alpha = 255/2; // 이미지의 Alpha (투명도)

    public int res[] = {R.drawable.per_12, R.drawable.per_15, R.drawable.per_18, R.drawable.per_20};

    //----------------------------------
    // 생성자
    //----------------------------------
    public PerButton(int _x, int _y, int index, Context context) {

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();

        nW_x = _x;
        nW_y = _y;

        sW_x = (int)(display.getWidth() / 5);          //이미지의 x축 크기
        sW_y = (int)(display.getWidth() / 5);          //이미지의 y축 크기

        PerButton = BitmapFactory.decodeResource(context.getResources(), res[index]);               //이미지생성
        PerButton = Bitmap.createScaledBitmap(PerButton, sW_x, sW_y, true);       //크기조정

    }

}
