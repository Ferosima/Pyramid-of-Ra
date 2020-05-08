package com.example.pyramidofra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Coord {
    public int bottom;
    public int left;
    public int right;
    public int top;
    public int h;
    public int w;
    public int centerX;
    public int centerY;

    public Coord( float left,float top, float right,float bottom ) {
        this.bottom = (int) bottom;
        this.left = (int) left;
        this.right = (int) right;
        this.top = (int) top;
        h = (int) ( bottom-top);
        w = (int) (right-left);
        centerX = (int) (w / 2 + left);
        centerY = (int) (h / 2 + top);
    }
    public Coord(Coord coord)
    {this.bottom = coord.bottom;
        this.left =coord.left;
        this.right = coord. right;
        this.top = coord. top;
        h = (int) ( bottom-top);
        w = (int) (right-left);
        centerX = (int) (w / 2 + left);
        centerY = (int) (h / 2 + top);

    }
    public void update(float x, float y)
    {
        top=(int)y;
        bottom=(int)y+h;
        left=(int)x;
        right=(int)x+w;
    }
    public void CoordDraw(Canvas canvas, Paint paint)//hitbox
    {
       int r=w/10;
       Paint p=new Paint();
       p.setColor(Color.GRAY);
       canvas.drawRect(left,top,right,bottom,p);
       canvas.drawRect(left+r,top+r,right-r,bottom-r,paint);
    }

}
