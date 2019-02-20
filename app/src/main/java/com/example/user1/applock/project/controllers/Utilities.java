package com.example.user1.applock.project.controllers;

import com.example.user1.applock.patternlockView.PatternLockView;

import java.util.List;

public class Utilities {

    public static boolean compareTwoPatterns(List<PatternLockView.Dot> lstFrstPattern,List<PatternLockView.Dot> lstScndPattern) {
        boolean isPatternMatched =true;
        if (lstFrstPattern == null || lstScndPattern == null || lstFrstPattern.size()== 0 || lstScndPattern.size() ==0)
            isPatternMatched = false;
        else if(lstFrstPattern.size() !=lstScndPattern.size())
            isPatternMatched =false;
        else{
            int itemsSize = lstFrstPattern.size();
            for( int i=0; i<itemsSize; i++){
                if((lstFrstPattern.get(i).getColumn() != lstScndPattern.get(i).getColumn()) || (lstFrstPattern.get(i).getRow() != lstScndPattern.get(i).getRow())) {
                    isPatternMatched = false;
                    break;
                }
            }
        }
        return isPatternMatched;
    }

}
