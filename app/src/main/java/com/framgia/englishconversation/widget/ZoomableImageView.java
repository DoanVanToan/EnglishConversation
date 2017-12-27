package com.framgia.englishconversation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * ZoomableImageView
 */

public class ZoomableImageView extends AppCompatImageView {
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final float MIN_SCALE = 1f;
    private static final float MAX_SCALE = 4f;
    private int mMode = NONE;
    private Matrix mMatrix = new Matrix();
    private PointF mLast = new PointF();
    private PointF mStart = new PointF();
    private float[] mValues = new float[9];
    private float mRedundantXSpace, mRedundantYSpace;
    private float mWidth, mHeight, mOrigWidth, mOrigHeight, mBmWidth, mBmHeight, mRight, mBottom;
    private float mSaveScale = 1f;
    private ScaleGestureDetector mScaleDetector;

    public ZoomableImageView(Context context, AttributeSet attr) {
        super(context, attr);
        super.setClickable(false);
        init(context);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                v.performClick();
                mScaleDetector.onTouchEvent(e);
                mMatrix.getValues(mValues);
                float x = mValues[Matrix.MTRANS_X];
                float y = mValues[Matrix.MTRANS_Y];
                PointF curr = new PointF(e.getX(), e.getY());
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLast.set(e.getX(), e.getY());
                        mStart.set(mLast);
                        mMode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mLast.set(e.getX(), e.getY());
                        mStart.set(mLast);
                        mMode = ZOOM;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mMode == ZOOM || (mMode == DRAG && mSaveScale > MIN_SCALE)) {
                            float deltaX = curr.x - mLast.x;
                            float deltaY = curr.y - mLast.y;
                            if (Math.round(mOrigWidth * mSaveScale) < mWidth) {
                                deltaX = 0;
                                deltaY = setDeltaY(y, deltaY);
                            } else if (Math.round(mOrigHeight * mSaveScale) < mHeight) {
                                deltaY = 0;
                                deltaX = setDeltaX(x, deltaX);
                            } else {
                                deltaX = setDeltaX(x, deltaX);
                                deltaY = setDeltaY(y, deltaY);
                            }
                            mMatrix.postTranslate(deltaX, deltaY);
                            mLast.set(curr.x, curr.y);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mMode = NONE;
                        break;
                }
                setImageMatrix(mMatrix);
                invalidate();
                return true;
            }
        });
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBmWidth = bm.getWidth();
        mBmHeight = bm.getHeight();
    }

    /**
     * ScaleListener
     */

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mMode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = mSaveScale;
            mSaveScale *= mScaleFactor;
            if (mSaveScale > MAX_SCALE) {
                mSaveScale = MAX_SCALE;
                mScaleFactor = MAX_SCALE / origScale;
            } else if (mSaveScale < MIN_SCALE) {
                mSaveScale = MIN_SCALE;
                mScaleFactor = MIN_SCALE / origScale;
            }
            mRight = mWidth * mSaveScale - mWidth - (2 * mRedundantXSpace * mSaveScale);
            mBottom = mHeight * mSaveScale - mHeight - (2 * mRedundantYSpace * mSaveScale);
            if (mOrigWidth * mSaveScale <= mWidth || mOrigHeight * mSaveScale <= mHeight) {
                mMatrix.postScale(mScaleFactor, mScaleFactor, mWidth / 2, mHeight / 2);
                if (mScaleFactor < 1) {
                    mMatrix.getValues(mValues);
                    float x = mValues[Matrix.MTRANS_X];
                    float y = mValues[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1) {
                        if (Math.round(mOrigWidth * mSaveScale) < mWidth) {
                            if (y < -mBottom) {
                                mMatrix.postTranslate(0, -(y + mBottom));
                            } else if (y > 0) {
                                mMatrix.postTranslate(0, -y);
                            }
                        } else {
                            if (x < -mRight) {
                                mMatrix.postTranslate(-(x + mRight), 0);
                            } else if (x > 0) {
                                mMatrix.postTranslate(-x, 0);
                            }
                        }
                    }
                }
            } else {
                mMatrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(),
                        detector.getFocusY());
                mMatrix.getValues(mValues);
                float x = mValues[Matrix.MTRANS_X];
                float y = mValues[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (x < -mRight) {
                        mMatrix.postTranslate(-(x + mRight), 0);
                    } else if (x > 0) {
                        mMatrix.postTranslate(-x, 0);
                    }
                    if (y < -mBottom) {
                        mMatrix.postTranslate(0, -(y + mBottom));
                    } else if (y > 0) {
                        mMatrix.postTranslate(0, -y);
                    }
                }
            }
            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        float scale;
        float scaleX = mWidth / mBmWidth;
        float scaleY = mHeight / mBmHeight;
        scale = Math.min(scaleX, scaleY);
        mMatrix.setScale(scale, scale);
        setImageMatrix(mMatrix);
        mSaveScale = 1f;
        mRedundantYSpace = (mHeight - (scale * mBmHeight)) / 2;
        mRedundantXSpace = (mWidth - (scale * mBmWidth)) / 2;
        mMatrix.postTranslate(mRedundantXSpace, mRedundantYSpace);
        mOrigWidth = mWidth - 2 * mRedundantXSpace;
        mOrigHeight = mHeight - 2 * mRedundantYSpace;
        mRight = mWidth * mSaveScale - mWidth - (2 * mRedundantXSpace * mSaveScale);
        mBottom = mHeight * mSaveScale - mHeight - (2 * mRedundantYSpace * mSaveScale);
        setImageMatrix(mMatrix);
    }

    public void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mMatrix.setTranslate(1f, 1f);
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
    }

    public float setDeltaY(float y, float deltaY) {
        if (y + deltaY > 0) {
            deltaY = -y;
        } else if (y + deltaY < -mBottom) {
            deltaY = -(y + mBottom);
        }
        return deltaY;
    }

    public float setDeltaX(float x, float deltaX) {
        if (x + deltaX > 0) {
            deltaX = -x;
        } else if (x + deltaX < -mRight) {
            deltaX = -(x + mRight);
        }
        return deltaX;
    }
}