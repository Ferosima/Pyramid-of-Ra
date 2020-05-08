package com.example.pyramidofra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    Paint paint;
    GameView gameView;

    int w, h;
    int w_field_max, h_field_max;
    int unitW, unitH;
    int n = 4;
    int round = 1;
    int time_timer = 0;
    int matrix[][];
    int i, j;
    int r_v, r_h;

    // int xStart, yStart;
    float dragX, dragY;
    int leftStart, topStart;

    ArrayList<Integer> i_h;
    ArrayList<Integer> j_h;
    ArrayList<Integer> m_h;
    ArrayList<Integer> x_Start;
    ArrayList<Integer> x_first;

    ArrayList<Integer> i_v;
    ArrayList<Integer> j_v;
    ArrayList<Integer> m_v;
    ArrayList<Integer> y_Start;
    ArrayList<Integer> y_first;


    Boolean first = true;
    Boolean click = true;
    Boolean is_click = false;
    Boolean is_game = false;
    Boolean is_game_run = false;
    Boolean start_game = false;
    Boolean is_timer = true;
    Boolean is_timer_run = true;
    Boolean is_down = false;


    Coord coordField;
    Coord coordBall[][];

    Bitmap mBackgroundField;
    Bitmap ball_bitmap[];

    CountDownTimer timer_start;
    CountDownTimer timer_game;

    TextView text_timer;
    TextView text_round;
    TextView text_count;

    Random random;
    Listener mListener;

    public interface Listener { // create an interface
        void goToMain();
    }

    public GameView(Context context, Listener mListener) {
        super(context);
        // this.mListener=mListener;
        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        gameView = this;
        this.mListener = mListener;
        ball_bitmap = new Bitmap[4];
        coordBall = new Coord[n][n];
        matrix = new int[n][n];
        random = new Random();

        i_h = new ArrayList<>();
        j_h = new ArrayList<>();
        m_h = new ArrayList<>();
        x_Start = new ArrayList<>();
        x_first = new ArrayList<>();

        i_v = new ArrayList<>();
        j_v = new ArrayList<>();
        m_v = new ArrayList<>();
        y_Start = new ArrayList<>();
        y_first = new ArrayList<>();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (first) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            w_field_max = (w / 60) * 58;
            h_field_max = (w / 60) * 58;
            unitW = w_field_max / 6;
            unitH = h_field_max / 6;

            ball_bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ball_1_optimized);
            ball_bitmap[0] = Bitmap.createScaledBitmap(ball_bitmap[0], (int) unitW, (int) unitH, true);

            ball_bitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ball_2_optimized);
            ball_bitmap[1] = Bitmap.createScaledBitmap(ball_bitmap[1], (int) unitW, (int) unitH, true);

            ball_bitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ball_3_optimized);
            ball_bitmap[2] = Bitmap.createScaledBitmap(ball_bitmap[2], (int) unitW, (int) unitH, true);

            ball_bitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ball_4_optimized);
            ball_bitmap[3] = Bitmap.createScaledBitmap(ball_bitmap[3], (int) unitW, (int) unitH, true);

            text_count = (TextView) getRootView().findViewById(R.id.text_count);
            text_timer = (TextView) getRootView().findViewById(R.id.text_timer);
            text_round = (TextView) getRootView().findViewById(R.id.text_round);

//need in func

            first = false;
        }

        if (is_timer_run) {
            is_timer_run = false;
            text_round.setVisibility(VISIBLE);
            timer_start = new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    is_timer = false;
                    is_game_run = true;
                    is_game = true;
                    text_count.setText("0");
                    text_round.setVisibility(INVISIBLE);
                    start_game = false;
                    invalidate();
                }
            }.start();
        }

        if (is_timer) {
            text_round.setText("Round:" + round);
        }

        if (is_game_run) {
            is_game_run = false;
            text_round.setVisibility(VISIBLE);
            coordBall = new Coord[n][n];
            matrix = new int[n][n];
            mBackgroundField = BitmapFactory.decodeResource(getResources(), R.drawable.gamebox_optimized);
            mBackgroundField = Bitmap.createScaledBitmap(mBackgroundField, (int) unitW * n, unitH * n, true);

            coordField = new Coord((w - unitW * n) / 2, h / 10, ((w - unitW * n) / 2) + unitW * 4, h / 10 + unitH * n);
            //Log.d("left", ((w - (w_field_max / 6) * 4) / 2) + " ");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    float r, l, t, b;
                    r = coordField.left + (j + 1) * unitW;
                    l = coordField.left + (j) * unitW;
                    t = coordField.top - unitH;
                    ;// + (i) * unitH;
                    b = coordField.top; //+ (i + 1) * unitH;
                    coordBall[i][j] = new Coord(l, t, r, b);
                    matrix[i][j] = random.nextInt(4) + 1;
                }
            }
            is_click = true;
            timer_start = new CountDownTimer(62000, 1000) {
                public void onTick(long millisUntilFinished) {
                    time_timer = (int) millisUntilFinished;
                }

                public void onFinish() {
                    is_timer_run = true;
                    is_timer = true;
                    is_game = false;
                    text_round.setVisibility(VISIBLE);
                    round++;
                    n++;
                    if (round == 4)
                        mListener.goToMain();
                    is_click = false;
                    invalidate();
                }
            }.start();
        }

        if (is_game) {
            text_round.setText("Round:" + round);
            if (time_timer / 1000 % 60 < 10)
                text_timer.setText(time_timer / 1000 / 60 + " : 0" + time_timer / 1000 % 60);
            else
                text_timer.setText(time_timer / 1000 / 60 + " : " + time_timer / 1000 % 60);
            canvas.drawBitmap(mBackgroundField, coordField.left, coordField.top, paint);
            if (start_game) {

                MoveElementRight();
                MoveElementUp();
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++) {
                        canvas.drawBitmap(ball_bitmap[matrix[i][j] - 1], coordBall[i][j].left, coordBall[i][j].top, paint);
                    }
                //check 3


                        if (is_down) {
                    for (int m = n - 1; m >= 0; m--) {
                        for (int k = 0; k < n; k++) {
                            Log.d("done_down", "d" + m + " " + coordBall[m][k].top + " " + coordField.top);
                            if (m != n - 1) {
                                if (coordBall[m][k].top >= coordField.top + unitH * m) {
                                    if (m == 0) {
                                        int stop = 0;
                                        for (int q = 0; q < n; q++)
                                            if (coordBall[m][q].top >= coordField.top + unitH * m)
                                                stop++;
                                        Log.d("done_down ", stop + "");
                                        if (stop == n) {
                                            for (int i = 0; i < n; i++)
                                                for (int j = 0; j < n; j++)
                                                    coordBall[i][j].update(coordBall[i][j].left, coordField.top + unitH * (i));
                                            Log.d("done_down", "downіііі" + coordBall[m][k].top + " " + coordField.top);
                                            is_down = false;
                                            is_click = true;
                                            break;
                                        }
                                    }
                                    coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
                                    continue;
                                }
                                if (coordBall[m + 1][k].top >= coordField.top + unitH * (m + 1))
                                    coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
                            } else {
                                if (coordBall[m][k].bottom <= coordField.top + unitH * m) {
                                    coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
                                } else
                                    coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
                            }
                        }
                    }

                }
                        else
                            for (int i = 0; i < n; i++)
                                for (int j = 0; j < n; j++)
                                    Check(i,j);
                String log = "";
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        log += matrix[i][j] + "|";
                    }
                    log += "\n";
                }
                //Log.d("done_matrix", "log" + "\n" + log);


            }
            else {
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++) {
                        canvas.drawBitmap(ball_bitmap[matrix[i][j] - 1], coordBall[i][j].left, coordBall[i][j].top, paint);
                    }
//                for (int m = n - 1; m >= 0; m--) {
//                    Log.d("m", m + "");
//                    for (int k = 0; k < n; k++) {
//                        if (m != n - 1) {
//                            if (coordBall[m][k].top >= coordField.top + unitH * m) {
//                                if (m == 0) {
//                                    start_game = true;
//                                    is_click = true;
//                                }
//                                coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
//
//                                break;
//                            }
//                            if (coordBall[m + 1][k].top == coordField.top + unitH * (m + 1))
//                                coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
//                        } else {
//                            if (coordBall[m][k].bottom <= coordField.top + unitH * m) {
//                                coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
//                            } else
//                                coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
//                        }
//                    }
//                }\
                for (int m = n - 1; m >= 0; m--) {
                    for (int k = 0; k < n; k++) {
                        Log.d("done_down", "d" + m + " " + coordBall[m][k].top + " " + coordField.top);
                        if (m != n - 1) {
                            if (coordBall[m][k].top >= coordField.top + unitH * m) {
                                if (m == 0) {
                                    int stop = 0;
                                    for (int q = 0; q < n; q++)
                                        if (coordBall[m][q].top >= coordField.top + unitH * m)
                                            stop++;
                                    Log.d("done_down ", stop + "");
                                    if (stop == n) {
                                        for (int i = 0; i < n; i++)
                                            for (int j = 0; j < n; j++)
                                                coordBall[i][j].update(coordBall[i][j].left, coordField.top + unitH * (i));
                                        Log.d("done_down", "downіііі" + coordBall[m][k].top + " " + coordField.top);
                                        start_game = true;
                                        is_click = true;
                                        break;
                                    }
                                }
                                coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
                                continue;
                            }
                            if (coordBall[m + 1][k].top >= coordField.top + unitH * (m + 1))
                                coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
                        } else {
                            if (coordBall[m][k].bottom <= coordField.top + unitH * m) {
                                coordBall[m][k].update(coordBall[m][k].left, coordBall[m][k].top + unitH / 4);
                            } else
                                coordBall[m][k].update(coordBall[m][k].left, coordField.top + unitH * (m));
                        }
                    }
                }
            }

        }
//        String log = "";
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                log += matrix[i][j] +"|";
//            }
//            log+="\n";
//        }
//        Log.d("matrix", "log"+"\n"+log);
        invalidate();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN: {
                if (is_click) {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (evX >= coordBall[i][j].left && evX <= coordBall[i][j].right &&
                                    evY >= coordBall[i][j].top && evY <= coordBall[i][j].bottom) {
                                this.i = i;
                                this.j = j;
                                dragX = evX - coordBall[i][j].left;
                                dragY = evY - coordBall[i][j].top;
                                leftStart = coordBall[i][j].left;
                                topStart = coordBall[i][j].top;
                                click = true;
                                break;
                            }
                        }
                        invalidate();
                    }
                }
            }
            case MotionEvent.ACTION_MOVE:
                // Log.d("left", i + "");
                if (is_click)
                    if (click) {
                        if (i <= n - 2)
                            if (evY >= coordBall[i + 1][j].top + coordBall[i + 1][j].h / 4) {
//                                xStart = coordBall[i][j].left;
//                                yStart = coordBall[i][j].top;
//                                int n = matrix[i][j];
//                                matrix[i][j] = matrix[i + 1][j];
//                                matrix[i + 1][j] = n;
                                if (!i_v.contains(i) && !j_v.contains(i + 1) && !m_v.contains(j))
                                    AddToMoveElementUp(i, i + 1, j, coordBall[i][j].top);
                                click = false;
                                break;
                            }
//                    if (i <= n - 1)
//                        if (coordBall[i][j].top > coordBall[i - 1][j].top)
//                            coordBall[i][j].update(evX - dragX, evY - dragY);
//                        else {
//                            back = true;
//                            break;
//                        }
//                    if (i == n - 1)
//                        if (coordBall[i][j].bottom > coordField.bottom) {
//                            coordBall[i][j].update(leftStart, topStart);
//                            invalidate();
//                            break;
//                        }
//                    if (j <= n - 2)
//                        if (evX - dragX >= coordBall[i][j + 1].left+coordBall[i][j + 1].w/4)
//                            coordBall[i][j].update(evX - dragX, evY - dragY);
//                    if (j <= n - 1) {
//                        if (coordBall[i][j].left > coordBall[i][j - 1].left)
//                            coordBall[i][j].update(evX - dragX, evY - dragY);
//                        else {
//                            back = true;
//                            break;
//                        }
//                    }
//                    if (j == n - 1)
//                        if (coordBall[i][j].right > coordField.right) {
//                            coordBall[i][j].update(leftStart, topStart);
//                            invalidate();
//                            break;
//                        }
                        if (i >= 1)
                            if (evY <= coordBall[i - 1][j].bottom - coordBall[i - 1][j].h / 4) {
//                                int n = matrix[i][j];
//                                matrix[i][j] = matrix[i - 1][j];
//                                matrix[i - 1][j] = n;
                                if (!i_v.contains(i - 1) && !j_v.contains(i) && !m_v.contains(j))
                                    AddToMoveElementUp(i - 1, i, j, coordBall[i - 1][j].top);
                                click = false;
                                break;
                            }
                        if (j <= n - 2)
                            if (evX >= coordBall[i][j + 1].left + coordBall[i][j + 1].w / 4) {
                                //int n = matrix[i][j];
//                            matrix[i][j] = matrix[i][j + 1];
//                            matrix[i][j + 1] = n;
                                // xStart=coordBall[i][j].left;
                                if (!i_h.contains(i) && !j_h.contains(j) && !m_h.contains(j + 1))
                                    AddToMoveElementRight(i, j, j + 1, coordBall[i][j].left);
                                click = false;
                                break;
                            }
                        if (j >= 1)
                            if (evX <= coordBall[i][j - 1].right - coordBall[i][j - 1].h / 4) {
                                // int n = matrix[i][j];
//                            matrix[i][j] = matrix[i][j - 1];
//                            matrix[i][j - 1] = n;
                                if (!i_h.contains(i) && !j_h.contains(j - 1) && !m_h.contains(j))
                                    AddToMoveElementRight(i, j - 1, j, coordBall[i][j - 1].left);
                                click = false;
                                break;
                            }
                    }

            case MotionEvent.ACTION_UP:
                invalidate();
        }
        return true;
    }

    //    public void MoveElementRight(final int i, final int j, final int k, final int xStart) {
//
//        new CountDownTimer(400, 10) {
//            public void onTick(long millisUntilFinished) {
//                if (coordBall[i][k].left > xStart) {
//                    coordBall[i][k].update(coordBall[i][k].left - unitW / 10, coordBall[i][k].top);
//                    coordBall[i][j].update(coordBall[i][j].left + unitW / 10, coordBall[i][j].top);
//                    Log.d("time" + j, millisUntilFinished + " ");
//                    invalidate();
//                } else {
//                    coordBall[i][k].update(xStart, coordBall[i][k].top);
//                    coordBall[i][j].update(xStart + unitW, coordBall[i][k].top);
//                }
//            }
//
//            public void onFinish() {
//
//                Coord coord = coordBall[i][k];
//                coordBall[i][k] = coordBall[i][j];
//                coordBall[i][j] = coord;
//                int n = matrix[i][j];
//                matrix[i][j] = matrix[i][j + 1];
//                matrix[i][j + 1] = n;
//                invalidate();
//            }
//        }.start();
//    }
//
//    public void MoveElementLeft(final int i, final int j, final int k, final int xStart) {
//
//        new CountDownTimer(400, 10) {
//            public void onTick(long millisUntilFinished) {
//                if (coordBall[i][k].left < xStart) {
//                    coordBall[i][k].update(coordBall[i][k].left + unitW / 10, coordBall[i][k].top);
//                    coordBall[i][j].update(coordBall[i][j].left - unitW / 10, coordBall[i][j].top);
//                    Log.d("time" + j, millisUntilFinished + " ");
//                    invalidate();
//                } else {
//                    coordBall[i][k].update(xStart, coordBall[i][k].top);
//                    coordBall[i][j].update(xStart - unitW, coordBall[i][k].top);
//                }
//            }
//
//            public void onFinish() {
//
//                Coord coord = coordBall[i][k];
//                coordBall[i][k] = coordBall[i][j];
//                coordBall[i][j] = coord;
//                int n = matrix[i][j];
//                matrix[i][j] = matrix[i][j - 1];
//                matrix[i][j - 1] = n;
//                invalidate();
//            }
//        }.start();
//    }
    public void AddToMoveElementRight(final int i, final int j, final int k, final int xStart) {
        i_h.add(i);
        j_h.add(j);
        m_h.add(k);
        x_Start.add(xStart);
        x_first.add(1);
        is_click = false;
    }


    public void AddToMoveElementUp(final int v_1, final int v_2, final int h, final int yStart) {
        i_v.add(v_1);
        j_v.add(v_2);
        m_v.add(h);
        y_Start.add(yStart);
        y_first.add(1);
        is_click = false;
    }

    public void MoveElementRight() {
        for (int i = 0; i < i_h.size(); i++) {
            int v = i_h.get(i);
            int h_1 = j_h.get(i);
            int h_2 = m_h.get(i);
            if (x_first.get(i) < 3) {
                if (coordBall[v][h_2].left > x_Start.get(i)) {
                    coordBall[v][h_2].update(coordBall[v][h_2].left - unitW / 10, coordBall[v][h_2].top);
                    coordBall[v][j_h.get(i)].update(coordBall[v][j_h.get(i)].left + unitW / 10, coordBall[v][j_h.get(i)].top);
                } else {
                    coordBall[v][h_2].update(x_Start.get(i), coordBall[v][h_2].top);
                    coordBall[v][j_h.get(i)].update(x_Start.get(i) + unitW, coordBall[v][j_h.get(i)].top);
                    Coord coord = coordBall[v][h_2];
                    coordBall[v][h_2] = coordBall[v][h_1];
                    coordBall[v][h_1] = coord;
                    int n = matrix[v][h_2];
                    matrix[v][h_2] = matrix[v][h_1];
                    matrix[v][h_1] = n;
                    //AddToMoveElementRight(i_h.get(i),m_h.get(i),j_h.get(i),x_Start.get(i));
                    if (Check(v, h_1) || Check(v, h_2)) {
                        Log.d("done_checkRight", "yes");
                        i_h.remove(i);
                        j_h.remove(i);
                        m_h.remove(i);
                        x_Start.remove(i);
                        x_first.remove(i);
                        // is_click=true;
                    } else {
                        x_first.set(i, x_first.get(i) + 1);
                        // Log.d("done_false", y_first.get(i) + " ");
                    }

                }
            } else {
                i_h.remove(i);
                j_h.remove(i);
                m_h.remove(i);
                x_Start.remove(i);
                x_first.remove(i);
                is_click = true;
            }
        }
    }

    public void MoveElementUp() {
        for (int i = 0; i < i_v.size(); i++) {
            int h = m_v.get(i);
            int v_1 = i_v.get(i);
            int v_2 = j_v.get(i);
            if (y_first.get(i) < 3) {
                if (coordBall[v_2][h].top > y_Start.get(i)) {
                    coordBall[v_2][h].update(coordBall[v_2][h].left, coordBall[v_2][h].top - unitH / 10);
                    coordBall[v_1][h].update(coordBall[v_1][h].left, coordBall[v_1][h].top + unitW / 10);
                } else {
                    coordBall[v_2][h].update(coordBall[v_2][h].left, y_Start.get(i));
                    coordBall[v_1][h].update(coordBall[v_1][h].left, y_Start.get(i) + unitH);
//                Coord coord = coordBall[v_2][h];
//                coordBall[v_2][h] = coordBall[v_1][h];
//                coordBall[v_1][h] = coord;
                    swapUp(v_1, v_2, h);
                    int n = matrix[v_2][h];
                    matrix[v_2][h] = matrix[v_1][h];
                    matrix[v_1][h] = n;

                    if (Check(v_1, h) || Check(v_2, h)) {
                        Log.d("done_checkUp", "yes");
                        i_v.remove(i);
                        j_v.remove(i);
                        m_v.remove(i);
                        y_Start.remove(i);
                        y_first.remove(i);
                        //is_click=true;
                    } else {
                        y_first.set(i, y_first.get(i) + 1);
                        Log.d("done_false", y_first.get(i) + " ");
                    }

                }
            } else {
                i_v.remove(i);
                j_v.remove(i);
                m_v.remove(i);
                y_Start.remove(i);
                y_first.remove(i);
                is_click = true;
            }
        }
    }


    public Boolean CheckVertical() {
        int elem = matrix[0][0];
        int v = 0;
        for (int j = 0; j < n; j++)
            for (int i = 0; i < n; i++) {
                if (elem == matrix[i][j]) {
                    v++;
                    if (v == 3) {
                        //всунуть обработку
                        return true;

                    }
                } else
                    elem = matrix[i][j];
            }
        elem = matrix[0][0];
        int h = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (elem == matrix[i][j]) {
                    h++;
                    if (h == 3)
                        return true;
                    //всунуть обработку
                } else
                    elem = matrix[i][j];
            }
        return false;
    }

    public Boolean Check(int i, int j) {
        int h = 0;
        String log = "";
        for (int k = 0; k < n; k++) {
            for (int o = 0; o < n; o++) {
                log += matrix[k][o] + "|";
            }
            log += "\n";
        }
        Log.d("done_mmatrix", "log" + "\n" + log + "\n" + i);


//        if (j - 1 >= 0) {
//            if (matrix[i][j - 1] == matrix[i][j])
//                h++;
//            if (j + 1 <= n - 1) {
//                if (matrix[i][j + 1] == matrix[i][j])
//                    h++;
//                if (h == 2) {
        int start = j;
        int finish = j;
        for (int m = j; m >= 0; m--)
            if (matrix[i][m] == matrix[i][j])
                start = m;
            else
                break;
        for (int m = j; m < n; m++)
            if (matrix[i][m] == matrix[i][j])
                finish = m;
            else
                break;
        if (finish - start >= 2) {
            for (int u = i; u >= 0; u--) {
                for (int k = finish; k >= start; k--) {
                    if (u == i)
                        if (u!=0)
                        matrix[u][k] = matrix[u-1][k];
                        else
                            matrix[u][k] = random.nextInt(4) + 1;
                    coordBall[u][k].update(coordBall[u][k].left, (coordField.top - unitH) + (unitH * u));
                    //Log.d("done_down", "ball" + u + " " + coordBall[u][k].top + " " + coordField.top);
                    if (u == 0 && k == start) {
                        text_count.setText(Integer.toString(Integer.parseInt(text_count.getText().toString()) + 1));
                        is_down = true;
                        return true;
                    }
                }
            }
        }

//            h = 0;
//            for (int m = j; m < n && m <= j + 2; m++) {
//                if (matrix[i][m] == matrix[i][j]) {
//                    h += 1;
//                    if (h == 3) {
//                        int finish = m;
//                        for (int k = m; k < n; k++) {
//                            if (matrix[i][k] == matrix[i][j])
//                                finish++;
//                            else
//                                break;
//                        }
//                        for (int u = i; u >= 0; u--) {
//                            Log.d("done_down+", u + "");
//                            for (int k = j; k < n && k <= finish; k++) {
//                                if (u == i)
//                                    matrix[u][k] = random.nextInt(4) + 1;
//                                coordBall[u][k].update(coordBall[u][k].left, (coordField.top - unitH) + (unitH * u));
//                                Log.d("done_down", "ball+" + u + " " + coordBall[u][k].top + " " + coordField.top);
//                                if (u == 0 && k == j + 2) {
//                                    text_count.setText(Integer.toString(Integer.parseInt(text_count.getText().toString()) + 1));
//                                    is_down = true;
//                                    return true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            h = 0;
//            for (int m = j; m >= 0 && m >= j - 2; m--) {
//                if (matrix[i][m] == matrix[i][j]) {
//                    h += 1;
//                    if (h == 3) {
//
//
//                        int start = m;
//                        for (int k = m; k >= 0; k--)
//                            if (matrix[i][k] == matrix[i][j])
//                                start = k;
//                            else
//                                break;
//
//                        for (int u = i; u >= 0; u--) {
//                            Log.d("done_down", u + "");
//                            for (int k = j; k < n && k >= start; k--) {
//                                if (u == i)
//                                    matrix[u][k] = random.nextInt(4) + 1;
//                                coordBall[u][k].update(coordBall[u][k].left, (coordField.top - unitH) + (unitH * u));
//                                Log.d("done_down", "ball" + u + " " + coordBall[u][k].top + " " + coordField.top);
//                                if (u == 0 && k == start) {
//                                    text_count.setText(Integer.toString(Integer.parseInt(text_count.getText().toString()) + 1));
//                                    is_down = true;
//                                    return true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }



                start = i ;
                finish = i;
                for (int m = i; m >= 0; m--)
                    if (matrix[m][j] == matrix[i][j])
                        start = m;
                    else
                        break;
                for (int m = i; m < n; m++)
                    if (matrix[m][j] == matrix[i][j])
                        finish = m;
                    else
                        break;
                    if (finish-start>=2){
                for (int k = finish; k >= 0; k--) {
                    if (k<start) {
                        Log.d("done_fin",k+" "+finish+" "+start);
                        matrix[finish - start + k][j] = matrix[k][j];
                    }
                    if (k <= finish - start) {
                        matrix[k][j]=random.nextInt(4)+1;
                        coordBall[k][j].update(coordBall[k][j].left, (coordField.top - unitH));
                    }
                    if (k > finish - start)
                        coordBall[k][j].update(coordBall[k][j].left, (coordField.top - unitH) + (unitH * n - finish));

                    if (k == 0) {
                        text_count.setText(Integer.toString(Integer.parseInt(text_count.getText().toString()) + 1));
                        if (k == 0) {
                            is_down = true;
                            return true;
                        }
                    }
                }
            }



            return false;
        }

        public void swapUp ( int v_1, int v_2, int h){
            Coord coord = coordBall[v_2][h];
            coordBall[v_2][h] = coordBall[v_1][h];
            coordBall[v_1][h] = coord;
        }

        public void MoveDown ( int i){

        }

    }

