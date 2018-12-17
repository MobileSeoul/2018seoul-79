package com.hojong.meokgol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

public class MyNMapResourceProvider extends NMapResourceProvider {
    private static final String LOG_TAG = "NMapViewerResourceProvider";
    private static final boolean DEBUG = false;

    private static final Bitmap.Config BITMAP_CONFIG_DEFAULT = Bitmap.Config.ARGB_8888;

    private static final int POI_FONT_COLOR_NUMBER = 0xFF909090;
    private static final float POI_FONT_SIZE_NUMBER = 10.0F;

    private static final int POI_FONT_COLOR_ALPHABET = 0xFFFFFFFF;
    private static final float POI_FONT_OFFSET_ALPHABET = 6.0F;
    private static final Typeface POI_FONT_TYPEFACE = null;//Typeface.DEFAULT_BOLD;

    private static final int CALLOUT_TEXT_COLOR_NORMAL = 0xFFFFFFFF;
    private static final int CALLOUT_TEXT_COLOR_PRESSED = 0xFF9CA1AA;
    private static final int CALLOUT_TEXT_COLOR_SELECTED = 0xFFFFFFFF;
    private static final int CALLOUT_TEXT_COLOR_FOCUSED = 0xFFFFFFFF;

    private final Rect mTempRect = new Rect();
    private final Paint mTextPaint = new Paint();

    public MyNMapResourceProvider(Context context) {
        super(context);

        mTextPaint.setAntiAlias(true);
    }

    /**
     * Get drawable for markerId at focused state
     *
     * @param markerId unique id for POI or Number icons.
     * @param focused true for focused state, false otherwise.
     * @return
     */
    public Drawable getDrawable(int markerId, boolean focused, NMapOverlayItem item) {
        Drawable marker = null;

        int resourceId = findResourceIdForMarker(markerId, focused);
        if (resourceId > 0) {
            marker = mContext.getResources().getDrawable(resourceId);
        } else {
            resourceId = 4 * markerId;
            if (focused) {
                resourceId += 1;
            }

            marker = getDrawableForMarker(markerId, focused, item);
        }

        // set bounds
        if (marker != null) {
            setBounds(marker, markerId, item);
        }

        return marker;
    }

    public Bitmap getBitmap(int markerId, boolean focused, NMapOverlayItem item) {
        Bitmap bitmap = null;

        Drawable marker = getDrawable(markerId, focused, item);
        if (marker != null) {
            bitmap = getBitmap(marker);
        }

        return bitmap;
    }

    public Bitmap getBitmap(Drawable marker) {
        Bitmap bitmap = null;

        if (marker != null) {
            int width = marker.getIntrinsicWidth();
            int height = marker.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG_DEFAULT);

            marker.setBounds(0, 0, width, height);

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(0x00000000);

            marker.draw(canvas);
        }

        return bitmap;
    }

    public Bitmap getBitmap(int resourceId) {
        Bitmap bitmap = null;

        Drawable marker = null;
        if (resourceId > 0) {
            marker = mContext.getResources().getDrawable(resourceId);
        }

        if (marker != null) {
            bitmap = getBitmap(marker);
        }

        return bitmap;
    }

    public Bitmap getBitmapWithNumber(int resourceId, String strNumber, float offsetY, int fontColor, float fontSize) {
        Bitmap bitmap = null;

        Drawable marker = getDrawableWithNumber(resourceId, strNumber, offsetY, fontColor, fontSize);

        if (marker != null) {
            bitmap = getBitmap(marker);
        }

        return bitmap;
    }

    @Override
    public Drawable getDrawableForInfoLayer(NMapOverlayItem item) {
        return null;
    }

    /**
     * Class to find resource Ids on map view
     */
    private class ResourceIdsOnMap {

        int markerId;
        int resourceId;
        int resourceIdFocused;

        ResourceIdsOnMap(int markerId, int resourceId, int resourceIdFocused) {
            this.markerId = markerId;
            this.resourceId = resourceId;
            this.resourceIdFocused = resourceIdFocused;
        }
    }

    @Override
    protected int findResourceIdForMarker(int markerId, boolean focused) {
        return markerId;
    }

    /**
     * 	Set bounds for this marker depending on its shape.
     *
     */
    @Override
    protected void setBounds(Drawable marker, int markerId, NMapOverlayItem item) {
    }

    @Override
    public Drawable[] getLocationDot() {
        return null;
    }

    @Override
    public Drawable getDirectionArrow() {
        return null;
    }

    public Drawable getDrawableWithNumber(int resourceId, String strNumber, float offsetY, int fontColor, float fontSize) {

        Bitmap textBitmap = getBitmapWithText(resourceId, strNumber, fontColor, fontSize, offsetY);

        //Log.i(LOG_TAG, "getDrawableWithNumber: width=" + textBitmap.getWidth() + ", height=" + textBitmap.getHeight() + ", density=" + textBitmap.getDensity());

        // set bounds
        Drawable marker = new BitmapDrawable(mContext.getResources(), textBitmap);
        if (marker != null) {
            NMapOverlayItem.boundCenter(marker);
        }

        //Log.i(LOG_TAG, "getDrawableWithNumber: width=" + marker.getIntrinsicWidth() + ", height=" + marker.getIntrinsicHeight());

        return marker;
    }

    public Drawable getDrawableWithAlphabet(int resourceId, String strAlphabet, int fontColor, float fontSize) {

        Bitmap textBitmap = getBitmapWithText(resourceId, strAlphabet, fontColor, fontSize, POI_FONT_OFFSET_ALPHABET);

        // set bounds
        Drawable marker = new BitmapDrawable(mContext.getResources(), textBitmap);
        if (marker != null) {
            NMapOverlayItem.boundCenterBottom(marker);
        }

        return marker;
    }

    @Override
    protected Drawable getDrawableForMarker(int markerId, boolean focused, NMapOverlayItem item) {
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_place);

        return drawable;
    }

    private Bitmap getBitmapWithText(int resourceId, String strNumber, int fontColor, float fontSize, float offsetY) {
        Bitmap bitmapBackground = BitmapFactory.decodeResource(mContext.getResources(), resourceId);

        int width = bitmapBackground.getWidth();
        int height = bitmapBackground.getHeight();
        //Log.i(LOG_TAG, "getBitmapWithText: width=" + width + ", height=" + height + ", density=" + bitmapBackground.getDensity());

        Bitmap textBitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG_DEFAULT);

        Canvas canvas = new Canvas(textBitmap);

        canvas.drawBitmap(bitmapBackground, 0, 0, null);

        // set font style
        mTextPaint.setColor(fontColor);
        // set font size
        mTextPaint.setTextSize(fontSize * mScaleFactor);
        // set font type
        if (POI_FONT_TYPEFACE != null) {
            mTextPaint.setTypeface(POI_FONT_TYPEFACE);
        }

        // get text offset
        mTextPaint.getTextBounds(strNumber, 0, strNumber.length(), mTempRect);
        float offsetX = (width - mTempRect.width()) / 2 - mTempRect.left;
        if (offsetY == 0.0F) {
            offsetY = (height - mTempRect.height()) / 2 + mTempRect.height();
        } else {
            offsetY = offsetY * mScaleFactor + mTempRect.height();
        }

        //Log.i(LOG_TAG, "getBitmapWithText: number=" + number + ", focused=" + focused);
        //Log.i(LOG_TAG, "getBitmapWithText: offsetX=" + offsetX + ", offsetY=" + offsetY + ", boundsWidth=" + mTempRect.width() + ", boundsHeight=" + mTempRect.height());

        // draw text
        canvas.drawText(strNumber, offsetX, offsetY, mTextPaint);

        return textBitmap;
    }

    @Override
    public Drawable getCalloutBackground(NMapOverlayItem item) {
        return null;
    }

    @Override
    public String getCalloutRightButtonText(NMapOverlayItem item) {
        return null;
    }

    @Override
    public Drawable[] getCalloutRightButton(NMapOverlayItem item) {
        return null;
    }

    @Override
    public Drawable[] getCalloutRightAccessory(NMapOverlayItem item) {
        return null;
    }

    /**
     * 말풍선의 텍스트 색상을 customize한다.
     *
     * @param item
     * @return
     * @see com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay.ResourceProvider#getCalloutTextColors(com.nhn.android.maps.NMapOverlayItem)
     */
    @Override
    public int[] getCalloutTextColors(NMapOverlayItem item) {
        int[] colors = new int[4];
        colors[0] = CALLOUT_TEXT_COLOR_NORMAL;
        colors[1] = CALLOUT_TEXT_COLOR_PRESSED;
        colors[2] = CALLOUT_TEXT_COLOR_SELECTED;
        colors[3] = CALLOUT_TEXT_COLOR_FOCUSED;
        return colors;
    }

    @Override
    public int getParentLayoutIdForOverlappedListView() {
        // not supported
        return 0;
    }

    @Override
    public int getOverlappedListViewId() {
        // not supported
        return 0;
    }

    @Override
    public int getLayoutIdForOverlappedListView() {
        // not supported
        return 0;
    }

    @Override
    public int getListItemLayoutIdForOverlappedListView() {
        // not supported
        return 0;
    }

    @Override
    public int getListItemTextViewId() {
        // not supported
        return 0;
    }

    @Override
    public int getListItemTailTextViewId() {
        // not supported
        return 0;
    }

    @Override
    public int getListItemImageViewId() {
        // not supported
        return 0;
    }

    @Override
    public int getListItemDividerId() {
        // not supported
        return 0;
    }

    @Override
    public void setOverlappedListViewLayout(ListView listView, int itemCount, int width, int height) {
        // not supported
    }

    @Override
    public void setOverlappedItemResource(NMapPOIitem poiItem, ImageView imageView) {
        // not supported
    }

    private int getResourceIdOnMapView(int markerId, boolean focused, ResourceIdsOnMap resourceIdsArray[]) {
        int resourceId = 0;

        for (ResourceIdsOnMap resourceIds : resourceIdsArray) {
            if (resourceIds.markerId == markerId) {
                resourceId = (focused) ? resourceIds.resourceIdFocused : resourceIds.resourceId;
                break;
            }
        }

        return resourceId;
    }
}