package hl.iss.whu.edu.laboratoryproject.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by fate on 2016/11/13.
 */

public class GlideRoundTransform extends BitmapTransformation {
    private int radius;
    public GlideRoundTransform(Context context) {
        this(context,50);
    }
    public GlideRoundTransform(Context context,int radius){
        super(context);
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool,toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source){
        if (source == null)return null;
        Bitmap result = pool.get(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null){
            result = Bitmap.createBitmap(2*radius,2*radius, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawCircle(radius,radius,radius,paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName()+radius;
    }
}
