package com.lxj.androidktx.livedata;

import android.annotation.SuppressLint;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.arch.lifecycle.Observer;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Created by cxzheng on 2018/8/27.
 * <p>
 * Customize a LiveData that controls whether you need sticky.Default is not sticky.
 * <p>
 * LiveData's own sticky feature makes it easy to cause many problems when LiveData is static global, which is annoying.
 */

public class NoStickyLiveData<T> {

    private static final String TAG = NoStickyLiveData.class.getSimpleName();

    static final int START_VERSION = -1;
    private static final Object NOT_SET = new Object();
    private Handler handler = new Handler(Looper.getMainLooper());

    private Map<Observer<T>, ObserverWrapper> mObservers = new ConcurrentHashMap<>();

    private volatile Object mData = NOT_SET;

    private int mVersion = START_VERSION;

    private boolean mDispatchingValue;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean mDispatchInvalidated;


    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        this.observe(owner, observer, false);
    }

    @MainThread
    public void observeForever(@NonNull Observer<T> observer) {
        this.observeForever(observer, false);
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer, boolean isSticky) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            return;
        }
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer, isSticky);
        ObserverWrapper existing = mObservers.get(observer);
        if (existing == null) {
            existing = mObservers.put(observer, wrapper);
        }
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        owner.getLifecycle().addObserver(wrapper);
    }

    @MainThread
    public void observeForever(@NonNull Observer<T> observer, boolean isSticky) {
        AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer, isSticky);
        ObserverWrapper existing = mObservers.get(observer);
        if (existing == null) {
            existing = mObservers.put(observer, wrapper);
        }
        if (existing != null && existing instanceof NoStickyLiveData.LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        wrapper.activeStateChanged(true);
    }

    @MainThread
    public void setValue(T value) {
        assertMainThread("setValue");
        mVersion++;
        mData = value;
        dispatchingValue(null);
    }

    public void postValue(T value){
        mVersion++;
        mData = value;
        handler.post(new Runnable() {
            @Override
            public void run() {
                dispatchingValue(null);
            }
        });
    }

    @Nullable
    public T getValue() {
        Object data = mData;
        if (data != NOT_SET) {
            //noinspection unchecked
            return (T) data;
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver {
        @NonNull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<T> observer, boolean isSticky) {
            super(observer, isSticky);
            mOwner = owner;
        }

        @Override
        boolean shouldBeActive() {
            return mOwner.getLifecycle().getCurrentState().isAtLeast(STARTED);
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            if (mOwner.getLifecycle().getCurrentState() == DESTROYED) {
                removeObserver(mObserver);
                return;
            }
            activeStateChanged(shouldBeActive());
        }

        @Override
        boolean isAttachedTo(LifecycleOwner owner) {
            return mOwner == owner;
        }

        @Override
        void detachObserver() {
            mOwner.getLifecycle().removeObserver(this);
        }
    }

    private class AlwaysActiveObserver extends ObserverWrapper {

        AlwaysActiveObserver(Observer<T> observer, boolean isSticky) {
            super(observer, isSticky);
        }

        @Override
        boolean shouldBeActive() {
            return true;
        }
    }

    private abstract class ObserverWrapper {
        final Observer<T> mObserver;
        boolean mActive;
        int mLastVersion;
        boolean mIsSticky;

        ObserverWrapper(Observer<T> observer, boolean isSticky) {
            mObserver = observer;
            this.mIsSticky = isSticky;
            mLastVersion = isSticky ? START_VERSION : mVersion;
        }

        abstract boolean shouldBeActive();

        boolean isAttachedTo(LifecycleOwner owner) {
            return false;
        }

        void detachObserver() {
        }

        void activeStateChanged(boolean newActive) {
            if (newActive == mActive) {
                return;
            }
            mActive = newActive;

            if (mActive) {
                dispatchingValue(this);
            }
        }
    }

    private void dispatchingValue(@Nullable ObserverWrapper initiator) {
        if (mDispatchingValue) {
            mDispatchInvalidated = true;
            return;
        }
        mDispatchingValue = true;
        do {
            mDispatchInvalidated = false;
            if (initiator != null) {
                considerNotify(initiator);
                initiator = null;
            } else {
                for (Map.Entry<Observer<T>, ObserverWrapper> entry : mObservers.entrySet()) {
                    considerNotify(entry.getValue());
                    if (mDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (mDispatchInvalidated);
        mDispatchingValue = false;
    }


    private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
            return;
        }
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
        if (observer.mLastVersion >= mVersion) {
            return;
        }
        observer.mLastVersion = mVersion;
        //noinspection unchecked
        observer.mObserver.onChanged((T) mData);
    }


    @MainThread
    public void removeObserver(@NonNull final Observer<T> observer) {
        assertMainThread("removeObserver");
        ObserverWrapper removed = mObservers.remove(observer);
        if (removed == null) {
            return;
        }
        removed.detachObserver();
        removed.activeStateChanged(false);
    }

    @SuppressWarnings("WeakerAccess")
    @MainThread
    public void removeObservers(@NonNull final LifecycleOwner owner) {
        assertMainThread("removeObservers");
        for (Map.Entry<Observer<T>, ObserverWrapper> entry : mObservers.entrySet()) {
            if (entry.getValue().isAttachedTo(owner)) {
                removeObserver(entry.getKey());
            }
        }
    }


    private static void assertMainThread(String methodName) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot invoke " + methodName + " on a background"
                    + " thread");
        }
    }
}
