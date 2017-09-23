package net.steamcrafted.lineartimepicker.svg;// TODO Include your package name here

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class SvgHand {
    private static final Paint  p  = new Paint();
    private static final Paint  ps = new Paint();
    private static final Path   t  = new Path();
    private static final Matrix m  = new Matrix();
    private static float od;
    protected static ColorFilter cf = null;

    /**
     *  IMPORTANT: Due to the static usage of this class this
     *  method sets the tint color statically. So it is highly
     *  recommended to call the clearColorTint method when you
     *  have finished drawing.
     *
     *  Sets the color to use when drawing the SVG. This replaces
     *  all parts of the drawable which are not completely
     *  transparent with this color.
     */
    public static void setColorTint(int color){
        cf = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void clearColorTint(int color){
        cf = null;
    }

    public static void draw(Canvas c, int w, int h){
        draw(c, w, h, 0, 0);
    }

    public static void draw(Canvas c, int w, int h, int dx, int dy){
        float ow = 550f;
        float oh = 450f;

        od = (w / ow < h / oh) ? w / ow : h / oh;

        r();
        c.save();
        c.translate((w - od * ow) / 2f + dx, (h - od * oh) / 2f + dy);

        m.reset();
        m.setScale(od, od);

        c.save();
        ps.setColor(Color.argb(0,0,0,0));
        ps.setStrokeCap(Paint.Cap.BUTT);
        ps.setStrokeJoin(Paint.Join.MITER);
        ps.setStrokeMiter(4.0f*od);
        c.scale(1.0f,1.0f);
        c.save();
        c.save();
        p.setColor(Color.argb(0,0,0,0));
        ps.setColor(Color.parseColor("#FFFFFF"));
        ps.setStrokeWidth(6.8f*od);
        ps.setStrokeCap(Paint.Cap.ROUND);
        ps.setStrokeJoin(Paint.Join.ROUND);
        t.reset();
        t.moveTo(543.85f,401.95f);
        t.lineTo(418.05f,328.95f);
        t.lineTo(369.2f,299.95f);
        t.quadTo(360.95f,294.45f,351.7f,285.05f);
        t.quadTo(336.15f,269.25f,330.15f,253.0f);
        t.quadTo(329.05f,250.0f,328.25f,246.95f);
        t.quadTo(325.65f,236.95f,325.25f,227.85f);
        t.lineTo(325.1f,209.5f);
        t.quadTo(325.0f,196.65f,323.1f,183.25f);
        t.lineTo(320.5f,168.25f);
        t.quadTo(315.95f,144.55f,313.5f,136.0f);
        t.lineTo(309.25f,116.75f);
        t.quadTo(302.55f,96.3f,286.95f,97.25f);
        t.quadTo(273.65f,98.05f,261.95f,112.0f);
        t.quadTo(237.95f,99.1f,213.95f,122.0f);
        t.quadTo(185.8f,110.9f,165.05f,132.5f);
        t.quadTo(161.7f,134.35f,160.0f,133.15f);
        t.quadTo(156.2f,130.4f,152.8f,125.65f);
        t.lineTo(145.5f,115.5f);
        t.lineTo(72.7f,10.0f);
        t.lineTo(67.75f,6.9f);
        t.quadTo(61.9f,4.4f,57.2f,7.5f);
        t.quadTo(51.85f,11.1f,50.2f,16.95f);
        t.quadTo(48.65f,22.25f,50.25f,29.0f);
        t.lineTo(52.85f,37.6f);
        t.quadTo(56.95f,49.55f,64.5f,66.1f);
        t.quadTo(86.35f,114.4f,129.55f,186.0f);
        t.lineTo(130.75f,189.35f);
        t.quadTo(132.05f,193.85f,132.75f,199.45f);
        t.quadTo(134.85f,217.3f,129.55f,239.5f);
        t.lineTo(128.5f,240.05f);
        t.quadTo(127.1f,240.65f,125.3f,240.65f);
        t.quadTo(119.5f,240.65f,112.05f,235.0f);
        t.quadTo(101.55f,227.05f,79.55f,218.05f);
        t.quadTo(54.8f,207.95f,43.2f,209.0f);
        t.quadTo(28.75f,210.3f,22.7f,212.75f);
        t.quadTo(14.5f,216.15f,8.25f,225.5f);
        t.lineTo(7.3f,227.15f);
        t.quadTo(6.3f,229.25f,6.0f,231.4f);
        t.quadTo(5.0f,238.15f,11.25f,242.5f);
        t.quadTo(18.75f,243.5f,28.9f,246.55f);
        t.quadTo(38.15f,249.35f,45.95f,253.0f);
        t.quadTo(55.3f,257.4f,62.65f,263.0f);
        t.quadTo(69.0f,265.15f,76.95f,268.75f);
        t.quadTo(92.8f,276.0f,100.6f,283.5f);
        t.quadTo(113.05f,295.5f,147.45f,317.4f);
        t.quadTo(188.8f,343.65f,219.4f,355.0f);
        t.quadTo(233.1f,360.65f,255.1f,374.2f);
        t.quadTo(299.0f,401.3f,340.2f,441.0f);
        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(3,2,0,1);
        p.setColor(Color.argb(0,0,0,0));
        ps.setColor(Color.parseColor("#FFFFFF"));
        ps.setStrokeWidth(6.8f*od);
        ps.setStrokeCap(Paint.Cap.ROUND);
        ps.setStrokeJoin(Paint.Join.ROUND);
        c.restore();
        r(3,2,0,1);
        p.setColor(Color.argb(0,0,0,0));
        ps.setColor(Color.parseColor("#FFFFFF"));
        ps.setStrokeWidth(6.8f*od);
        ps.setStrokeCap(Paint.Cap.ROUND);
        ps.setStrokeJoin(Paint.Join.ROUND);
        c.restore();
        r();

        c.restore();
    }

    public static Drawable getDrawable(int size){
        return new SvgHandDrawable(size);
    }

    public static Drawable getTintedDrawable(int size, int color){
        return new SvgHandDrawable(size, color);
    }

    private static class SvgHandDrawable extends Drawable {
        private int s = 0;
        private ColorFilter cf = null;

        public SvgHandDrawable(int s) {
            this.s = s;
            setBounds(0, 0, s, s);
            invalidateSelf();
        }

        public SvgHandDrawable(int s, int c) {
            this(s);
            cf = new PorterDuffColorFilter(c, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public int getIntrinsicHeight() {
            return s;
        }

        @Override
        public int getIntrinsicWidth() {
            return s;
        }

        @Override
        public void draw(Canvas c) {
            Rect b = getBounds();
            SvgHand.cf = cf;
            SvgHand.draw(c, b.width(), b.height(), b.left, b.top);
            SvgHand.cf = null;
        }

        @Override
        public void setAlpha(int i) {}

        @Override
        public void setColorFilter(ColorFilter c) { cf = c; invalidateSelf(); }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    private static void r(Integer... o){
        p.reset();
        ps.reset();
        if(cf != null){
            p.setColorFilter(cf);
            ps.setColorFilter(cf);
        }
        p.setAntiAlias(true);
        ps.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        ps.setStyle(Paint.Style.STROKE);
        for(Integer i : o){
            switch (i){
                case 0: ps.setStrokeJoin(Paint.Join.MITER); break;
                case 1: ps.setStrokeMiter(4.0f*od); break;
                case 2: ps.setStrokeCap(Paint.Cap.BUTT); break;
                case 3: ps.setColor(Color.argb(0,0,0,0)); break;
            }
        }
    }
};