package com.example.user1.applock.patternview_reactive.events;

import com.example.user1.applock.patternlockView.PatternLockView;

import java.util.List;

/**
 * Created by aritraroy on 01/04/17.
 */

public class PatternLockCompleteEvent extends BasePatternLockEvent {

    public PatternLockCompleteEvent(List<PatternLockView.Dot> pattern) {
        super(pattern);
    }
}
