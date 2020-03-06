package com.smarteist.autoimageslider.InfiniteAdapter;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.smarteist.autoimageslider.SliderViewAdapter;


public class InfinitePagerAdapter extends PagerAdapter {

    // Warning: it should be an even number.
    public static final int INFINITE_SCROLL_LIMIT = 32400;
    private static final String TAG = "InfinitePagerAdapter";

    private SliderViewAdapter adapter;
    private int virtualPosition;

    public InfinitePagerAdapter(SliderViewAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerAdapter getRealAdapter() {
        return this.adapter;
    }

    @Override
    public int getCount() {
        if (getRealCount() < 1) {
            return 0;
        }
        // warning: infinite scroller actually is not infinite!
        // very big number will be cause memory problems.
        return getRealCount() * INFINITE_SCROLL_LIMIT;
    }

    /**
     * @return the {@link #getCount()} result of the wrapped adapter
     */
    public int getRealCount() {
        int count;
        try {
            count = getRealAdapter().getCount();
        } catch (Exception e) {
            count = 0;
        }
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getRealCount() < 1) {
            return null;
        }
        virtualPosition = position % getRealCount();

        //Log.i(TAG, "instantiateItem: real position: " + position);
        //Log.i(TAG, "instantiateItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        return adapter.instantiateItem(container, virtualPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (getRealCount() < 1) {
            return;
        }
        int virtualPosition = position % getRealCount();
        //Log.i(TAG, "destroyItem: real position: " + position);
        //Log.i(TAG, "destroyItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        adapter.destroyItem(container, virtualPosition, object);
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */
    @Override
    public void finishUpdate(ViewGroup container) {
        adapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return adapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        adapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return adapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        adapter.startUpdate(container);
    }

    public int getVirtualPosition() {
        return virtualPosition;
    }

}