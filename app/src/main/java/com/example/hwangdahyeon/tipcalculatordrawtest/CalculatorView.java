package com.example.hwangdahyeon.tipcalculatordrawtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.os.*;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class CalculatorView extends View {

    private Bitmap imgBack, HOWMUCH, TIP, TOTAL;         // 배경
    private Bitmap topWrite , bottomWirte;

    private int width, height;      // 디바이스 해상도의 가로,세로길이

    private int alphaHalf = 255 / 2;

    private Paint pStar;
    private Paint perButton;
    private Paint $, tipValueText, totalValueText , percent;

    private EditText moneyValue;
    private EditText perValue;
    private boolean oneRoop_Check;

    private Typeface t , t_$;
    private Typeface tiptotal;
    private Typeface perWirte;
    private Display display;
    private RelativeLayout.LayoutParams lp;
    private MainActivity cnxt;

    private BitmapFactory.Options options;

    private ArrayList<Star> star;              //별 ArrayList 선언
    private ArrayList<PerButton> perButtons;              //고유어 ArrayList 선언

    private double tipValue = 0;
    private double totalValue = 0;

    private int tipValueTextWidth = 0;
    private int totalValueTextWidth = 0;

    private int moneyValueLength = 0;
    //------------------------------------
    //생성자
    //------------------------------------
    public CalculatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cnxt = (MainActivity) context;

        $ = new Paint();
        pStar = new Paint();
        perButton = new Paint();
        tipValueText = new Paint();
        totalValueText = new Paint();
        percent = new Paint();

        oneRoop_Check = true;

        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))        //display값 받아오는 부분.
                .getDefaultDisplay();

        width = display.getWidth(); // View의 가로 폭
        height = display.getHeight(); // View의 세로 높이

        options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.main  , options);    //배경 이미지 경로설정
        imgBack = Bitmap.createScaledBitmap(imgBack, width, height + 100, false);               //배경 이미지 사이즈 조정

        HOWMUCH = BitmapFactory.decodeResource(context.getResources(), R.drawable.howmuchre , options);    //배경 이미지 경로설정
        HOWMUCH = Bitmap.createScaledBitmap(HOWMUCH, width / 5, width / 30, false);               //배경 이미지 사이즈 조정

        TIP = BitmapFactory.decodeResource(context.getResources(), R.drawable.tip , options);    //배경 이미지 경로설정
        TIP = Bitmap.createScaledBitmap(TIP, width / 15, width / 23, false);               //배경 이미지 사이즈 조정

        TOTAL = BitmapFactory.decodeResource(context.getResources(), R.drawable.total , options);    //배경 이미지 경로설정
        TOTAL = Bitmap.createScaledBitmap(TOTAL, width / 8, width / 23, false);               //배경 이미지 사이즈 조정

        topWrite = BitmapFactory.decodeResource(context.getResources(), R.drawable.topwirte , options);    //배경 이미지 경로설정
        topWrite = Bitmap.createScaledBitmap(topWrite, (int)(width / 2.7), width / 21, false);               //배경 이미지 사이즈 조정

        bottomWirte = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottomwrite , options);    //배경 이미지 경로설정
        bottomWirte = Bitmap.createScaledBitmap(bottomWirte, (int)(width / 2.2), width / 22, false);               //배경 이미지 사이즈 조정


        t = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        //t_$ = getResources().getFont(R.font.aritasemi);


        //tiptotal = getResources().getFont(R.font.aritasemi);
        //perWirte = getResources().getFont(R.font.aritalight);

        star = new ArrayList<>(); // 별 ArrayList 생성
        perButtons = new ArrayList<>(); // 버튼 ArrayList 생성

        //----------------------------------
        // 버튼객체 생성 및 위치조정
        //----------------------------------
        perButtons.add(new PerButton((int) ((width / 20)) * 2, (int) (height / 2.7), 0, getContext()));
        perButtons.add(new PerButton((int) ((width / 20) * 6.2), (int) (height / 2.7), 1, getContext()));
        perButtons.add(new PerButton((int) ((width / 20) * 10.4), (int) (height / 2.7), 2, getContext()));
        perButtons.add(new PerButton((int) ((width / 20) * 14.6), (int) (height / 2.7), 3, getContext()));

        mHandler.sendEmptyMessageDelayed(0, 10);
        Word_Time_Handler.sendEmptyMessageDelayed(0, 100);
    }

    //----------------------------------
    // 그려주는부분
    //----------------------------------
    @SuppressLint("ResourceAsColor")
    public void onDraw(final Canvas canvas) {
        Melt_Object();

        //------------------------------------
        //배경 ,HOWMUCH , TIP , TOTAL 그리기
        //------------------------------------
        canvas.drawBitmap(imgBack, 0, 0, null);     //배경을 그려줌.

        canvas.drawBitmap(HOWMUCH, width / 2 - (HOWMUCH.getWidth() / 2), height / 15, null);     //HOW MUCH?
        canvas.drawBitmap(TIP, width / 2 - (TIP.getWidth() / 2), (int) ((height / 10) * 7.6), null);     //Tip
        canvas.drawBitmap(TOTAL, width / 2 - (TOTAL.getWidth() / 2), (int) ((height / 10) * 8.5), null);     //HOW MUCH?
        canvas.drawBitmap(topWrite, width / 2 - (int)(topWrite.getWidth() / 2.2), (int) ((height / 4) * 1.35 ), null);     //topWrite
        canvas.drawBitmap(bottomWirte, width / 5 , (int) ((height / 4) * 2 ), null);     //topWrite

        //----------------------------------
        // EditText 그리기
        //----------------------------------
        if (oneRoop_Check) {

            //----------------------------------
            // moneyValue EditText 그리기
            //----------------------------------
            moneyValue = new EditText(cnxt);
            lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            moneyValue.setLayoutParams(lp);
            moneyValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            moneyValue.setY(height / 12);
            moneyValue.setTextSize(width / 15);
            moneyValue.setHint("00");
            moneyValue.setHintTextColor(Color.parseColor("#273982"));
            moneyValue.setTextColor(Color.parseColor("#273982"));
            moneyValue.setTypeface(t_$);
            moneyValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            moneyValue.setBackground(null);
            ((RelativeLayout) this.getParent()).addView(moneyValue);

            //----------------------------------
            // perValue EditText 그리기
            //----------------------------------
            perValue = new EditText(cnxt);
            lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            perValue.setLayoutParams(lp);
            perValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            perValue.setX((int)(width / 10 * 6.6)) ;
            perValue.setAlpha(0.4f);
            perValue.setY((int) (height / 2.1));
            perValue.setTextSize(width / 25);
            perValue.setTextColor(Color.WHITE);
            perValue.setHintTextColor(Color.WHITE);
            perValue.setHint("00");

            perValue.setTypeface(perWirte);
            perValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            perValue.setBackground(null);
            ((RelativeLayout) this.getParent()).addView(perValue);

            //----------------------------------
            // $ 세팅
            //----------------------------------
            $.setTypeface(t_$);
            $.setTextSize((int) (width / 6));
            $.setColor(Color.parseColor("#273982"));

            //----------------------------------
            // tipValueText 세팅
            //----------------------------------
            tipValueText.setTypeface(tiptotal);
            tipValueText.setTextSize((int) (width / 12));
            tipValueText.setColor(Color.parseColor("#273982"));

            //----------------------------------
            // totalValueText 세팅
            //----------------------------------
            totalValueText.setTypeface(tiptotal);
            totalValueText.setTextSize((int) (width / 12));
            totalValueText.setColor(Color.parseColor("#273982"));


            //----------------------------------
            // percent 세팅
            //----------------------------------
            percent.setTypeface(perWirte);
            percent.setTextSize(width / 10);
            percent.setColor(Color.WHITE);
            percent.setAntiAlias(true);
            percent.setAlpha(102);

            oneRoop_Check = false;
        }
        //moneyValue의 x좌표 이동
        moneyValueLength = (int) Math.ceil(tipValueText.measureText("$"));
        moneyValue.setX((width / 2) - (moneyValue.getWidth() / 2) - moneyValueLength );

        canvas.drawText("%", perValue.getX() + perValue.getWidth() , (int)(height/10 * 5.35) , percent);

        //----------------------------------
        // moneyValue TextWatcher
        //----------------------------------
        moneyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //perValue에 값이 들어있을경우
                if (perValue.getText().length() > 0 ) {
                    //moneyValue에 값이 들어있을경우
                    if (moneyValue.getText().length() > 0) {
                        //tipValue와 totalValue를 변경
                        tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * Integer.parseInt(perValue.getText() + "") / 100.0) * 100) / 100.0;
                        totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + (Integer.parseInt(moneyValue.getText() + "") * Integer.parseInt(perValue.getText() + "") / 100.0)) * 100) / 100.0;
                    }

                } else {
                    for (int a = 0; a < perButtons.size(); a++) {
                        if (perButtons.get(a).alpha == alphaHalf)
                            continue;
                        switch (a) {
                            case 0:
                                if (moneyValue.getText().length() > 0) {
                                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.12) * 100) / 100.0;
                                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.12) * 100) / 100.0;
                                }
                                break;
                            case 1:
                                if (moneyValue.getText().length() > 0) {
                                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.15) * 100) / 100.0;
                                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.15) * 100) / 100.0;
                                }
                                break;
                            case 2:
                                if (moneyValue.getText().length() > 0) {
                                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.18) * 100) / 100.0;
                                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.18) * 100) / 100.0;
                                }
                                break;
                            case 3:
                                if (moneyValue.getText().length() > 0) {
                                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.2) * 100) / 100.0;
                                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.2) * 100) / 100.0;
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //----------------------------------
        // perValue TextWatcher
        //----------------------------------
        perValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                perValue.setAlpha(0.7f);
                percent.setAlpha(178);

                for (int a = 0; a < perButtons.size(); a++) {
                    if (perButtons.get(a).alpha == alphaHalf)
                        continue;

                    perButtons.get(a).alpha = alphaHalf;

                }

                if (moneyValue.getText().length() > 0) {

                    if (perValue.getText().length() > 0) {
                        tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * Integer.parseInt(perValue.getText() + "") / 100.0) * 100) / 100.0;
                        totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + (Integer.parseInt(moneyValue.getText() + "") * Integer.parseInt(perValue.getText() + "") / 100.0)) * 100) / 100.0;

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //----------------------------------
        // moneyValue뒤에 $ 그리기
        //----------------------------------
        canvas.drawText("$", (width / 2) + moneyValue.getWidth() / 2 - moneyValueLength, (int)(height / 5.8), $);

        if (moneyValue.getText().length() == 0) {
            tipValue = 0;
            totalValue = 0;
        }

        //----------------------------------
        // tipValueText 그리기
        //----------------------------------

        tipValueTextWidth = (int) Math.ceil(tipValueText.measureText(tipValue + "$"));

        if (tipValue > 0) {
            canvas.drawText(tipValue + " $", (width / 2) - tipValueTextWidth / 2, (int) ((height / 10) *8.2), tipValueText);
        } else {
            tipValue = 0;
            tipValueTextWidth = (int) Math.ceil(tipValueText.measureText(tipValue + "$"));
            canvas.drawText(tipValue + " $", (width / 2) - tipValueTextWidth / 2, (int) ((height / 10) * 8.2), tipValueText);
        }

        //----------------------------------
        // totalValueText 그리기
        //----------------------------------
        totalValueTextWidth = (int) Math.ceil(totalValueText.measureText(totalValue + "$"));
        if (tipValue > 0) {
            canvas.drawText(totalValue + " $", (width / 2) - totalValueTextWidth / 2, (int) ((height / 10) * 9.1), totalValueText);
        } else {
            totalValue = 0;
            totalValueTextWidth = (int) Math.ceil(totalValueText.measureText(totalValue + "$"));
            canvas.drawText(totalValue + " $", (width / 2) - totalValueTextWidth / 2, (int) ((height / 10) * 9.1), totalValueText);
        }

        //----------------------------------
        // 별 그리기
        //----------------------------------
        for (Star tStar : star) {                //단어그리기
            pStar.setAlpha(tStar.alpha);
            canvas.drawBitmap(tStar.star, tStar.nW_x, tStar.nW_y, pStar);
        }

        //----------------------------------
        // 버튼 그리기
        //----------------------------------
        for (PerButton tPerButton : perButtons) {                //단어그리기
            perButton.setAlpha(tPerButton.alpha);
            canvas.drawBitmap(tPerButton.PerButton, tPerButton.nW_x, tPerButton.nW_y, perButton);
        }

        System.gc();
    }

    private void Delete_Object() {
        for (int i = star.size() - 1; i >= 0; i--) {
            if (star.get(i).dead == true) {
                star.get(i).star.recycle();
                star.get(i).star = null;
                star.remove(i);
            }
        }
    }

    private void Melt_Object() {
        Delete_Object();
        for (int i = star.size() - 1; i >= 0; i--) {
            if (star.get(i).deadStart == true) {
                if (star.get(i).Star_Melt() == true) {
                    star.get(i).dead = true;
                }
            } else {
                star.get(i).Star_Create();
            }
        }
    }

    //------------------------------------
    // onTouch Event
    //------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {                     //터치했을때

            if (x > perButtons.get(0).nW_x
                    && x < perButtons.get(0).nW_x + (perButtons.get(0).PerButton.getWidth())
                    && y > perButtons.get(0).nW_y
                    && y < perButtons.get(0).nW_y + (perButtons.get(0).PerButton.getHeight())) {

                perValue.setText("");

                perButtons.get(0).alpha = 255;
                for (int i = 1; i < perButtons.size(); i++) {
                    perButtons.get(i).alpha = alphaHalf;
                }

                if (moneyValue.getText().length() > 0) {
                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.12) * 100) / 100.0;
                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.12) * 100) / 100.0;
                }

                perValue.setAlpha(0.4f);
                percent.setAlpha(102);


            }

            if (x > perButtons.get(1).nW_x &&
                    x < perButtons.get(1).nW_x + (perButtons.get(0).PerButton.getWidth()) &&
                    y > perButtons.get(1).nW_y &&
                    y < perButtons.get(1).nW_y + (perButtons.get(1).PerButton.getHeight())) {

                perValue.setText("");

                perButtons.get(1).alpha = 255;
                for (int i = 0; i < perButtons.size(); i++) {
                    if (i == 1) {
                        continue;
                    }
                    perButtons.get(i).alpha = alphaHalf;
                }

                if (moneyValue.getText().length() > 0) {
                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.15) * 100) / 100.0;
                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.15) * 100) / 100.0;
                }
                perValue.setAlpha(0.4f);
                percent.setAlpha(102);
            }

            if (x > perButtons.get(2).nW_x &&
                    x < perButtons.get(2).nW_x + (perButtons.get(0).PerButton.getWidth()) &&
                    y > perButtons.get(2).nW_y &&
                    y < perButtons.get(2).nW_y + (perButtons.get(2).PerButton.getHeight())) {

                perValue.setText("");

                perButtons.get(2).alpha = 255;
                for (int i = 0; i < perButtons.size(); i++) {
                    if (i == 2) {
                        continue;
                    }
                    perButtons.get(i).alpha = alphaHalf;
                }

                if (moneyValue.getText().length() > 0) {
                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.18) * 100) / 100.0;
                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.18) * 100) / 100.0;
                }
                perValue.setAlpha(0.4f);
                percent.setAlpha(102);

            }
            if (x > perButtons.get(3).nW_x &&
                    x < perButtons.get(3).nW_x + (perButtons.get(0).PerButton.getWidth()) &&
                    y > perButtons.get(3).nW_y &&
                    y < perButtons.get(3).nW_y + (perButtons.get(3).PerButton.getHeight())) {

                perValue.setText("");

                perButtons.get(3).alpha = 255;
                for (int i = 0; i < perButtons.size(); i++) {
                    if (i == 3) {
                        continue;
                    }
                    perButtons.get(i).alpha = alphaHalf;
                }

                if (moneyValue.getText().length() > 0) {
                    tipValue = Math.round((Integer.parseInt(moneyValue.getText() + "") * 0.2) * 100) / 100.0;
                    totalValue = Math.round((Integer.parseInt(moneyValue.getText() + "") + Integer.parseInt(moneyValue.getText() + "") * 0.2) * 100) / 100.0;
                }
                perValue.setAlpha(0.4f);
                percent.setAlpha(102);
            }

        }
        return true;
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate(); // View를 다시 그림
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    }; // Handler

    //별 생성 핸들러
    Handler Word_Time_Handler = new Handler() {
        public void handleMessage(Message msg) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * ((height / 3) * 2));

            while (x > width / 5 &&
                    y < height / 3 &&
                    x < (width / 5) * 4) {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * ((height / 3) * 2));
            }

            star.add(new Star(x, y, getContext()));

            Word_Time_Handler.sendEmptyMessageDelayed(0, 200);
        }
    };


}
