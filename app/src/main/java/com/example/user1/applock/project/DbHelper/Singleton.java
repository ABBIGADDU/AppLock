package com.example.user1.applock.project.DbHelper;

import com.example.user1.applock.patternlockView.PatternLockView;
import com.example.user1.applock.patternview_reactive.events.PatternLockCompleteEvent;

import java.util.List;

public class Singleton {
    private static volatile Singleton _singleton = new Singleton();

    public static Singleton getInstance() {
        synchronized (Singleton.class) {
            if (_singleton == null)
                _singleton = new Singleton();
        }
        return _singleton;
    }

    private List<PatternLockView.Dot> lstFirstSavingDots ;

    public List<PatternLockView.Dot> getLstFirstSavingDots() {
        return lstFirstSavingDots;
    }

    public void setLstFirstSavingDots(List<PatternLockView.Dot> lstFirstSavingDots) {
        this.lstFirstSavingDots = lstFirstSavingDots;
    }
}
