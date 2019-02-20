package com.example.user1.applock.patternview_reactive.events;

import android.support.annotation.IntDef;


import com.example.user1.applock.patternlockView.PatternLockView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static com.example.user1.applock.patternview_reactive.events.PatternLockCompoundEvent.EventType.PATTERN_CLEARED;
import static com.example.user1.applock.patternview_reactive.events.PatternLockCompoundEvent.EventType.PATTERN_COMPLETE;
import static com.example.user1.applock.patternview_reactive.events.PatternLockCompoundEvent.EventType.PATTERN_PROGRESS;
import static com.example.user1.applock.patternview_reactive.events.PatternLockCompoundEvent.EventType.PATTERN_STARTED;


/**
 * Created by aritraroy on 27/03/17.
 */

public final class PatternLockCompoundEvent extends BasePatternLockEvent {

    @IntDef({PATTERN_STARTED, PATTERN_PROGRESS, PATTERN_COMPLETE, PATTERN_CLEARED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
        int PATTERN_STARTED = 0;
        int PATTERN_PROGRESS = 1;
        int PATTERN_COMPLETE = 2;
        int PATTERN_CLEARED = 3;
    }

    private final int mEventType;

    public PatternLockCompoundEvent(@EventType int eventType, List<PatternLockView.Dot> pattern) {
        super(pattern);
        mEventType = eventType;
    }

    @EventType
    public int getEventType() {
        return mEventType;
    }
}
