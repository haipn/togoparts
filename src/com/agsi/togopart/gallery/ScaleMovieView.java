package com.agsi.togopart.gallery;

import android.content.Context;
import android.widget.ImageView;

public class ScaleMovieView extends ImageView {

	public ScaleMovieView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

//	static final int ANIM_DURATION = 200;// 200 Millisecond
//
//	static enum Type {
//		SCALE, TRANSLATE_X, TRANSLATE_Y
//	}
//
//	static final float MAX_SCALE = 3;
//
//	// We can be in one of these 3 states
//	static final int NONE = 0;
//	static final int DRAG = 1;
//	static final int ZOOM = 2;
//
//	private static final String TAG = null;
//	int mode = NONE;
//
//	private ViewPager mPictureViewPager;
//
//	Matrix mMatrix = new Matrix();
//	Matrix mSavedMatrix = new Matrix();
//	PointF start = new PointF();
//	float oldDist = 1f;
//	private int mCenterX;
//	private int mCenterY;
//	private float[] mValues;
//	private Vector<ValueAnimator> mValueAnimators = new Vector<ValueAnimator>();
//	private GestureDetector mDetector;
//
//	private float mKeyScale;
//	private float mKeyTranslateX;
//	private float mKeyTranslateY;
//	private float mKeyWidth;
//	private float mKeyHeight;
//
//	private boolean mFlag = true;
//	private boolean mIsDraging = false;
//
//	private int mWidth;
//	private int mHeight;
//
//	private int mWidthImage = 0;
//	private int mHeightImage = 0;
//
//	public ScaleMovieView(Context context) {
//		super(context);
//		setScaleType(ScaleType.MATRIX);
//		init();
//
//	}
//
//	public ScaleMovieView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		setScaleType(ScaleType.MATRIX);
//		init();
//	}
//
//	public ScaleMovieView(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//		setScaleType(ScaleType.MATRIX);
//		init();
//	}
//
//	private void init() {
//		mIsDraging = false;
//		mValues = new float[9];
//		mValues[0] = 1;
//		mValues[1] = 0;
//		mValues[2] = 0;
//
//		mValues[3] = 0;
//		mValues[4] = 1;
//		mValues[5] = 0;
//
//		mValues[6] = 0;
//		mValues[7] = 0;
//		mValues[8] = 1;
//		mMatrix.setValues(mValues);
//		setImageMatrix(mMatrix);
//	}
//
//	public void setDrag(boolean drag) {
//		mIsDraging = drag;
//	}
//
//	@Override
//	protected void onRestoreInstanceState(Parcelable state) {
//		super.onRestoreInstanceState(state);
//		mIsDraging = false;
//	}
//
//	@Override
//	protected Parcelable onSaveInstanceState() {
//		return super.onSaveInstanceState();
//	}
//
//	public void setPager(ViewPager parent) {
//		mPictureViewPager = parent;
//
//	}
//
//	private PointF f1 = new PointF();
//	private PointF f2 = new PointF();
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// Handle touch events here...
//		Log.d(TAG, "draging= " + mIsDraging);
//		if (mPictureViewPager != null && !mIsDraging) {
//			mPictureViewPager.requestDisallowInterceptTouchEvent(true);
//		}
//		if (mDetector == null) {
//			mDetector = new GestureDetector(getContext(), new SimpleGesture());
//		}
//		mDetector.onTouchEvent(event);
//		switch (event.getAction() & MotionEvent.ACTION_MASK) {
//		case MotionEvent.ACTION_DOWN:
//			mIsDraging = false;
//			f1.x = event.getX();
//			f1.y = event.getY();
//			mSavedMatrix.set(mMatrix);
//			mode = DRAG;
//			break;
//		case MotionEvent.ACTION_POINTER_DOWN:
//			mIsDraging = false;
//			oldDist = spacing(event);
//			if (oldDist > 10f) {
//				f1.x = event.getX(0);
//				f1.y = event.getY(0);
//				f2.x = event.getX(1);
//				f2.y = event.getY(1);
//				mSavedMatrix.set(mMatrix);
//				mode = ZOOM;
//				mMatrix.getValues(mValues);
//				if (mValues[Matrix.MSCALE_X] > mKeyScale) {
//					mFlag = true;
//				} else {
//					mFlag = false;
//				}
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//			mode = NONE;
//			mIsDraging = false;
//			break;
//		case MotionEvent.ACTION_POINTER_UP:
//			mode = NONE;
//			mIsDraging = false;
//			animateRestoreState(false);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if (mode == ZOOM) {
//				mIsDraging = false;
//				float newDist = spacing(event);
//				if (newDist > 10f) {
//					mMatrix.set(mSavedMatrix);
//					float scale = newDist / oldDist;
//					mMatrix.postScale(scale, scale, mCenterX, mCenterY);
//					mMatrix.getValues(mValues);
//					if (mValues[Matrix.MSCALE_X] / mKeyScale > MAX_SCALE) {
//						scale = MAX_SCALE * mKeyScale
//								/ mValues[Matrix.MSCALE_X];
//						mMatrix.postScale(scale, scale, mCenterX, mCenterY);
//					}
//					if (mFlag && mValues[Matrix.MSCALE_X] > mKeyScale) {
//						mMatrix.getValues(mValues);
//						boolean change = false;
//						if (mValues[Matrix.MTRANS_X] > mKeyTranslateX) {
//							mValues[Matrix.MTRANS_X] = mKeyTranslateX;
//							change = true;
//						}
//						RectF r = new RectF();
//						mMatrix.mapRect(r);
//						// getWidthSize
//						float right = r.left + getWidth()
//								* mValues[Matrix.MSCALE_X];
//						if (right < mKeyTranslateX + mKeyWidth) {
//							mValues[Matrix.MTRANS_X] += mKeyTranslateX
//									+ mKeyWidth - right;
//							change = true;
//						}
//						if (mValues[Matrix.MTRANS_Y] > mKeyTranslateY) {
//							mValues[Matrix.MTRANS_Y] = mKeyTranslateY;
//							change = true;
//						}
//						float bottom = r.top + getHeight()
//								* mValues[Matrix.MSCALE_Y];
//						if (bottom < mKeyHeight + mKeyTranslateY) {
//							mValues[Matrix.MTRANS_Y] += mKeyHeight
//									+ mKeyTranslateY - bottom;
//							change = true;
//						}
//						if (change) {
//							mMatrix.setValues(mValues);
//						}
//					}
//					f1.x = event.getX(0);
//					f1.y = event.getY(0);
//					f2.x = event.getX(1);
//					f2.y = event.getY(1);
//					oldDist = newDist;
//				}
//			} else if (mode == DRAG) {
//				mMatrix.getValues(mValues);
//				float dx = event.getX() - f1.x;
//				float dy = event.getY() - f1.y;
//				RectF r = new RectF();
//				mMatrix.mapRect(r);
//				if (dx < 0) {
//					// move to left
//					float right = r.left + getWidth()
//							* mValues[Matrix.MSCALE_X];
//					if (right <= mWidth) {
//						// enabled ViewPager Touch event
//						Log.d(TAG, "pager enable touch....");
//						if (mPictureViewPager != null && !mIsDraging) {
//							mPictureViewPager
//									.requestDisallowInterceptTouchEvent(false);
//							mIsDraging = true;
//						}
//						dx = 0;
//					} else {
//						if (right + dx < mWidth) {
//							dx = mWidth - right;
//						}
//					}
//				} else if (dx > 0) {
//					// move to right
//					if (r.left >= 0) {
//						// enable ViewPager Touch event
//						Log.d(TAG, "pager enable touch....");
//						if (mPictureViewPager != null && !mIsDraging) {
//							mPictureViewPager
//									.requestDisallowInterceptTouchEvent(false);
//							mIsDraging = true;
//						}
//						dx = 0;
//					} else {
//						if (r.left + dx > 0) {
//							dx = -r.left;
//						}
//					}
//				}
//
//				if (dy > 0) {
//					// move to bottom
//					if (r.top >= 0) {
//						dy = 0;
//					} else {
//						if (r.top + dy > 0) {
//							dy = -r.top;
//						}
//					}
//				} else if (dy < 0) {
//					float bottom = r.top + getHeight()
//							* mValues[Matrix.MSCALE_Y];
//					if (bottom < mHeight) {
//						dy = 0;
//					} else {
//						if (bottom + dy < mHeight) {
//							dy = mHeight - bottom;
//						}
//					}
//
//				}
//				mValues[Matrix.MTRANS_X] += dx;
//				mValues[Matrix.MTRANS_Y] += dy;
//				mMatrix.setValues(mValues);
//				f1.x = event.getX();
//				f1.y = event.getY();
//			}
//			mSavedMatrix.set(mMatrix);
//			break;
//		}
//		setImageMatrix(mMatrix);
//		invalidate();
//		return true; // indicate event was handled
//	}
//
//	private void animateRestoreState(boolean condition) {
//		for (ValueAnimator animator : mValueAnimators) {
//			animator.cancel();
//		}
//		mValueAnimators.removeAllElements();
//		mMatrix.getValues(mValues);
//		if (mValues[Matrix.MSCALE_X] < mKeyScale || condition) {
//			animate(mValues[Matrix.MSCALE_X], mKeyScale, Type.SCALE, null);
//			animate(mValues[Matrix.MTRANS_X], mKeyTranslateX, Type.TRANSLATE_X,
//					null);
//			animate(mValues[Matrix.MTRANS_Y], mKeyTranslateY, Type.TRANSLATE_Y,
//					null);
//		}
//	}
//
//	// @Override
//	// public void setImageDrawable(Drawable drawable) {
//	// if (drawable instanceof AsyncDrawable) {
//	// drawable.setBounds(0, 0, getWidthSize(), getHeightSize());
//	// }
//	// super.setImageDrawable(drawable);
//	// }
//
//	@Override
//	protected void onConfigurationChanged(Configuration newConfig) {
//		Log.d(TAG, "config change...");
//		super.onConfigurationChanged(newConfig);
//		mIsDraging = false;
//		this.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				setSize(mWidthImage, mHeightImage, getWidth(), getHeight());
//			}
//		}, 50);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int width = MeasureSpec.getSize(widthMeasureSpec);
//		int height = MeasureSpec.getSize(heightMeasureSpec);
//		setMeasuredDimension(width, height);
//	}
//
//	/** Determine the space between the first two fingers */
//	private float spacing(MotionEvent event) {
//		// ...
//		float x = event.getX(0) - event.getX(1);
//		float y = event.getY(0) - event.getY(1);
//		return (float) Math.sqrt(x * x + y * y);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		mWidth = canvas.getWidth();
//		mHeight = canvas.getHeight();
//		mCenterX = mWidth / 2;
//		mCenterY = mHeight / 2;
//
//	}
//
//	public void setSizeImage(int w, int h) {
//		mWidthImage = w;
//		mHeightImage = h;
//	}
//
//	public void setSize(int width, int height, int widthView, int heightView) {
//		int rw = width;
//		int rh = height;
//		int sample = calculateSampleSize(width, height, widthView, heightView);
//		if (sample > 1) {
//			rw = width / sample;
//			rh = height / sample;
//		}
//		float scale = (widthView * 1.0f) / rw;
//		float rhs = rh * scale;
//		if (rhs > heightView) {
//			scale = scale * heightView / rhs;
//		}
//		mKeyWidth = scale * rw;
//		mKeyHeight = scale * rh;
//		float translateX = (widthView - mKeyWidth) / 2;
//		float translateY = (heightView - mKeyHeight) / 2;
//		mKeyScale = scale;
//		mKeyTranslateX = translateX;
//		mKeyTranslateY = translateY;
//		mValues[Matrix.MSCALE_X] = scale;
//		mValues[Matrix.MSCALE_Y] = scale;
//		mValues[Matrix.MTRANS_X] = translateX;
//		mValues[Matrix.MTRANS_Y] = translateY;
//		mMatrix.setValues(mValues);
//		setImageMatrix(mMatrix);
//		invalidate();
//		//TODO: setSIze
////		setSize(rw, rh);
//	}
//	public static int calculateSampleSize(int width, int height, int wView, int hView) {
//		int scale = 1;
//		while (true) {
//			if (width / 2 < wView
//					|| height / 2 < hView)
//				break;
//			width /= 2;
//			height /= 2;
//			scale *= 2;
//		}
//		return scale;
//	}
//
//	private void animate(float first, float target, final Type type,
//			Animator.AnimatorListener listener) {
//		ValueAnimator evaluator = ValueAnimator.ofFloat(first, target);
//		evaluator.setDuration(ANIM_DURATION);
//		evaluator.setInterpolator(new DecelerateInterpolator());
//		evaluator.addUpdateListener(new AnimatorUpdateListener() {
//
//			@Override
//			public void onAnimationUpdate(ValueAnimator animation) {
//				float value = (Float) animation.getAnimatedValue();
//				// Log.d(TAG, "value =" + value);
//				mMatrix.getValues(mValues);
//				switch (type) {
//				case TRANSLATE_X:
//					mValues[Matrix.MTRANS_X] = value;
//					break;
//				case TRANSLATE_Y:
//					mValues[Matrix.MTRANS_Y] = value;
//					break;
//				case SCALE:
//					mValues[Matrix.MSCALE_X] = value;
//					mValues[Matrix.MSCALE_Y] = value;
//				}
//				mMatrix.setValues(mValues);
//				setImageMatrix(mMatrix);
//			}
//		});
//		if (listener != null) {
//			evaluator.addListener(listener);
//		}
//		mValueAnimators.add(evaluator);
//		evaluator.start();
//	}
//
//	private void animateZoom(float start, float end) {
//		final ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end)
//				.setDuration(ANIM_DURATION);
//		valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
//
//			@Override
//			public void onAnimationUpdate(ValueAnimator animation) {
//				float value = (Float) animation.getAnimatedValue();
//				mMatrix.getValues(mValues);
//				if (mValues[Matrix.MSCALE_X] < mKeyScale * MAX_SCALE) {
//					mMatrix.postScale(value, value, mCenterX, mCenterY);
//
//				} else {
//					valueAnimator.cancel();
//					float scale = mKeyScale * MAX_SCALE
//							/ mValues[Matrix.MSCALE_X];
//					mMatrix.postScale(scale, scale, mCenterX, mCenterY);
//				}
//				setImageMatrix(mMatrix);
//			}
//		});
//		mValueAnimators.add(valueAnimator);
//		valueAnimator.start();
//	}
//
//	public class SimpleGesture extends SimpleOnGestureListener {
//
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}
//
//		@Override
//		public boolean onDoubleTap(MotionEvent e) {
//			mMatrix.getValues(mValues);
//			if (mValues[Matrix.MSCALE_X] / mKeyScale >= MAX_SCALE) {
//				animateRestoreState(true);
//				return true;
//			}
//			for (ValueAnimator animator : mValueAnimators) {
//				animator.cancel();
//			}
//			mValueAnimators.removeAllElements();
//			animateZoom(1, 1.1f);
//			return true;
//		}
//
//		@Override
//		public boolean onSingleTapConfirmed(MotionEvent e) {
//			return true;
//		}
//
//	}

}
