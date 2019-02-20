package com.example.user1.applock.patternview_reactive.observables;


import io.reactivex.Observable;
import io.reactivex.Observer;


import com.example.user1.applock.patternlockView.PatternLockView;


/**
 * Created by aritraroy on 01/04/17.
 */

public abstract class BasePatternLockViewObservable<BasePatternLockEvent>
        extends Observable<BasePatternLockEvent> {
    protected PatternLockView mPatternLockView;
    protected boolean mEmitInitialValue;

    protected BasePatternLockViewObservable(PatternLockView patternLockView, boolean emitInitialValue) {
        mPatternLockView = patternLockView;
        mEmitInitialValue = emitInitialValue;
    }

    protected abstract void subscribeListener(Observer<? super BasePatternLockEvent> observer);
}
