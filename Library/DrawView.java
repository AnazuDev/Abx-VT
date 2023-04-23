package com.anasbex.abxvt;

//Library Eraser Background Auto and Menual Draw

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.Stack;

import static com.anasbex.abxvt.DrawView.DrawViewAction.AUTO_CLEAR;
import static com.anasbex.abxvt.DrawView.DrawViewAction.MANUAL_CLEAR;
import static com.anasbex.abxvt.DrawView.DrawViewAction.ZOOM;

public class DrawView extends View {

    private Path livePath;
    private Paint pathPaint;

    private Bitmap imageBitmap;
    private final Stack<Pair<Pair<Path, Paint>, Bitmap>> cuts = new Stack<>();
    private final Stack<Pair<Pair<Path, Paint>, Bitmap>> undoneCuts = new Stack<>();

    private float pathX, pathY;

    private static float TOUCH_TOLERANCE = 4;
    private static float COLOR_TOLERANCE = 20;

    private Button undoButton;
    private Button redoButton;
    private View loadingModal;

    private DrawViewAction currentAction;

    public static enum DrawViewAction {
        AUTO_CLEAR,
        MANUAL_CLEAR,
        ZOOM
    }

    public DrawView(Context ctx){
        super(ctx);
    }

    public DrawView(Context c, AttributeSet attrs) {

        super(c, attrs);

        livePath = new Path();

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setDither(true);
        pathPaint.setColor(Color.TRANSPARENT);
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
    }
	
	public void setTouchTolerance(float f){
		this.TOUCH_TOLERANCE = f;
	}

    public void setButtons(Button undoButton, Button redoButton) {
        this.undoButton = undoButton;
        this.redoButton = redoButton;
    }


    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        resizeBitmap(newWidth, newHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        if (imageBitmap != null) {

            canvas.drawBitmap(this.imageBitmap, 0, 0, null);

            for (Pair<Pair<Path, Paint>, Bitmap> action : cuts) {
                if (action.first != null) {
                    canvas.drawPath(action.first.first, action.first.second);
                }
            }

            if (currentAction == MANUAL_CLEAR) {
                canvas.drawPath(livePath, pathPaint);
            }
        }

        canvas.restore();
    }

    private void touchStart(float x, float y) {
        pathX = x;
        pathY = y;

        undoneCuts.clear();
        redoButton.setEnabled(false);

        if (currentAction == AUTO_CLEAR) {
            new AutomaticPixelClearingTask(this).execute((int) x, (int) y);
        } else {
            livePath.moveTo(x, y);
        }

        invalidate();
    }

    private void touchMove(float x, float y) {
        if (currentAction == MANUAL_CLEAR) {
            float dx = Math.abs(x - pathX);
            float dy = Math.abs(y - pathY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                livePath.quadTo(pathX, pathY, (x + pathX) / 2, (y + pathY) / 2);
                pathX = x;
                pathY = y;
            }
        }
    }


    private void touchUp() {
        if (currentAction == MANUAL_CLEAR) {
            livePath.lineTo(pathX, pathY);
            cuts.push(new Pair<>(new Pair<>(livePath, pathPaint), null));
            livePath = new Path();
            undoButton.setEnabled(true);
        }
    }
	
	
	public static zoomView zou;

    public void undo() {
        if (cuts.size() > 0) {

            Pair<Pair<Path, Paint>, Bitmap> cut = cuts.pop();

            if (cut.second != null) {
                undoneCuts.push(new Pair<>(null, imageBitmap));
                this.imageBitmap = cut.second;
            } else {
                undoneCuts.push(cut);
            }

            if (cuts.isEmpty()) {
                undoButton.setEnabled(false);
            }

            redoButton.setEnabled(true);

            invalidate();
        }
        //toast the user
    }

    public void redo() {
        if (undoneCuts.size() > 0) {

            Pair<Pair<Path, Paint>, Bitmap> cut = undoneCuts.pop();

            if (cut.second != null) {
                cuts.push(new Pair<>(null, imageBitmap));
                this.imageBitmap = cut.second;
            } else {
                cuts.push(cut);
            }

            if (undoneCuts.isEmpty()) {
                redoButton.setEnabled(false);
            }

            undoButton.setEnabled(true);

            invalidate();
        }
        //toast the user
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (imageBitmap != null && currentAction != ZOOM) {
            
			if (zou != null){
			zou.removeZoom();
			}
			
			
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(ev.getX(), ev.getY());
                    return true;
                case MotionEvent.ACTION_MOVE:
                    touchMove(ev.getX(), ev.getY());
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    return true;
            }
        } else if (imageBitmap != null) {
            // hichem soft

             zou = new zoomView(getContext(), this);

        }

        return super.onTouchEvent(ev);
    }

    private void resizeBitmap(int width, int height) {
        if (width > 0 && height > 0 && imageBitmap != null) {
            imageBitmap = BitmapUtility.getResizedBitmap(this.imageBitmap, width, height);
            imageBitmap.setHasAlpha(true);
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
        resizeBitmap(getWidth(), getHeight());
    }

    public Bitmap getCurrentBitmap() {
        return this.imageBitmap;
    }

    public void setAction(DrawViewAction newAction) {
        this.currentAction = newAction;
    }

    public void setStrokeWidth(int strokeWidth) {
        pathPaint = new Paint(pathPaint);
        pathPaint.setStrokeWidth(strokeWidth);
    }

    public void setLoadingModal(View loadingModal) {
        this.loadingModal = loadingModal;
    }

    private static class AutomaticPixelClearingTask extends AsyncTask<Integer, Void, Bitmap> {

        private WeakReference<DrawView> drawViewWeakReference;

        AutomaticPixelClearingTask(DrawView drawView) {
            this.drawViewWeakReference = new WeakReference<>(drawView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            drawViewWeakReference.get().cuts.push(new Pair<>(null, drawViewWeakReference.get().imageBitmap));
        }

        @Override
        protected Bitmap doInBackground(Integer... points) {
            Bitmap oldBitmap = drawViewWeakReference.get().imageBitmap;

            int colorToReplace = oldBitmap.getPixel(points[0], points[1]);

            int width = oldBitmap.getWidth();
            int height = oldBitmap.getHeight();
            int[] pixels = new int[width * height];
            oldBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            int rA = Color.alpha(colorToReplace);
            int rR = Color.red(colorToReplace);
            int rG = Color.green(colorToReplace);
            int rB = Color.blue(colorToReplace);

            int pixel;

            // iteration through pixels
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    // get current index in 2D-matrix
                    int index = y * width + x;
                    pixel = pixels[index];
                    int rrA = Color.alpha(pixel);
                    int rrR = Color.red(pixel);
                    int rrG = Color.green(pixel);
                    int rrB = Color.blue(pixel);

                    if (rA - COLOR_TOLERANCE < rrA && rrA < rA + COLOR_TOLERANCE && rR - COLOR_TOLERANCE < rrR && rrR < rR + COLOR_TOLERANCE &&
                            rG - COLOR_TOLERANCE < rrG && rrG < rG + COLOR_TOLERANCE && rB - COLOR_TOLERANCE < rrB && rrB < rB + COLOR_TOLERANCE) {
                        pixels[index] = Color.TRANSPARENT;
                    }
                }
            }

            Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            return newBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            drawViewWeakReference.get().imageBitmap = result;
            drawViewWeakReference.get().undoButton.setEnabled(true);
            drawViewWeakReference.get().invalidate();
        }
    }

   public static class BitmapUtility {

        public static Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
            Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            float originalWidth = bitmap.getWidth();
            float originalHeight = bitmap.getHeight();

            Canvas canvas = new Canvas(background);

            float scale = width / originalWidth;

            float xTranslation = 0.0f;
            float yTranslation = (height - originalHeight * scale) / 2.0f;

            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);

            Paint paint = new Paint();
            paint.setFilterBitmap(true);

            canvas.drawBitmap(bitmap, transformation, paint);

            return background;
        }

        public static Bitmap getBorderedBitmap(Bitmap image, int borderColor, int borderSize) {

            // Creating a canvas with an empty bitmap, this is the bitmap that gonna store the final canvas changes
            Bitmap finalImage = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(finalImage);

            // Make a smaller copy of the image to draw on top of original
            Bitmap imageCopy = Bitmap.createScaledBitmap(image, image.getWidth() - borderSize, image.getHeight() - borderSize, true);

            // Let's draw the bigger image using a white paint brush
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(borderColor, PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(image, 0, 0, paint);

            int width = image.getWidth();
            int height = image.getHeight();
            float centerX = (width - imageCopy.getWidth()) * 0.5f;
            float centerY = (height - imageCopy.getHeight()) * 0.5f;
            // Now let's draw the original image on top of the white image, passing a null paint because we want to keep it original
            canvas.drawBitmap(imageCopy, centerX, centerY, null);

            // Returning the image with the final results
            return finalImage;
        }
    }
	
	
	
	

    public static class zoomView implements ScaleGestureDetector.OnScaleGestureListener {

        private Context ctx;
        private View myView;

        static enum Mode {
            NONE,
            DRAG,
            ZOOM
        }

        private static final float MIN_ZOOM = 1.0f;
        private static final float MAX_ZOOM = 4.0f;

        private Mode mode = Mode.NONE;
        private float scale = 1.0f;
        private float lastScaleFactor = 0f;

        private float startX = 0f;
        private float startY = 0f;

        private float dx = 0f;
        private float dy = 0f;
        private float prevDx = 0f;
        private float prevDy = 0f;

        public zoomView(Context ctx, View v){
            this.myView = v;
            init(ctx, v);
        }

        public void init(Context context, View view) {
            final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            if (scale > MIN_ZOOM) {
                                mode = Mode.DRAG;
                                startX = motionEvent.getX() - prevDx;
                                startY = motionEvent.getY() - prevDy;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == Mode.DRAG) {
                                dx = motionEvent.getX() - startX;
                                dy = motionEvent.getY() - startY;
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            mode = Mode.ZOOM;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            mode = Mode.DRAG;
                            break;
                        case MotionEvent.ACTION_UP:
                            mode = Mode.NONE;
                            prevDx = dx;
                            prevDy = dy;
                            break;
                    }
                    scaleDetector.onTouchEvent(motionEvent);

                    if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        float maxDx = (myView.getWidth() - (myView.getWidth() / scale)) / 2 * scale;
                        float maxDy = (myView.getHeight() - (myView.getHeight() / scale)) / 2 * scale;
                        dx = Math.min(Math.max(dx, -maxDx), maxDx);
                        dy = Math.min(Math.max(dy, -maxDy), maxDy);
                        applyScaleAndTranslation();
                    }

                    return true;
                }
            });
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleDetector) {
            float scaleFactor = scaleDetector.getScaleFactor();
            if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
                scale *= scaleFactor;
                scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
                lastScaleFactor = scaleFactor;
            } else {
                lastScaleFactor = 0;
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        }

        private void applyScaleAndTranslation() {
            this.myView.setScaleX(scale);
            this.myView.setScaleY(scale);
            this.myView.setTranslationX(dx);
            this.myView.setTranslationY(dy);
        }

        public void removeZoom(){
            myView.setOnTouchListener(null);
        }
    }

}



