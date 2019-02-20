package com.example.user1.applock.project.DbHelper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences mSPMember;

    private SharedPreferences.Editor mEditorMember;

    private static final String KEY_MEMBER_SHARED_PREF_NAME = "SP_APP_LOCK_MEMBER_INFO";

    private static final String KEY_MEMBER_EMAIL = "memberEmail";
    private static final String KEY_MEMBER_LOCK_PATTERN = "lockPattern";

    // Constructor
    public SessionManager(Context context) {
        int PRIVATE_MODE = 0;
        mSPMember = context.getSharedPreferences(KEY_MEMBER_SHARED_PREF_NAME, PRIVATE_MODE);
    }

    private boolean commitEditor() {
        try {
            mEditorMember.apply();
            return mEditorMember.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setMemberEmail(String firstName) {
        mEditorMember = mSPMember.edit();
        mEditorMember.putString(KEY_MEMBER_EMAIL, firstName);
        mEditorMember.apply();
    }

    public void setMemberLockPattern(String firstName) {
        mEditorMember = mSPMember.edit();
        mEditorMember.putString(KEY_MEMBER_LOCK_PATTERN, firstName);
        mEditorMember.apply();
    }


    public String getMemberEmail() {
        return mSPMember.getString(KEY_MEMBER_EMAIL, null);
    }

    public String getMemberLockPattern() {
        return mSPMember.getString(KEY_MEMBER_LOCK_PATTERN, null);
    }

}
