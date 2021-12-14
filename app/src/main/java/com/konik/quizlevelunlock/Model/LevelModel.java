package com.konik.quizlevelunlock.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;



public class LevelModel {


    private String LevelUID= "NO";
    private String LevelName = "NO";
    private String LevelPhotoUrl = "NO";
    private String LevelSyllabus = "NO";
    private String LevelExtra = "NO";
    private String LevelCreator = "NO";
    private long LeveliViewCount = 0;
    private long LeveliTotalQues = 0;
    private long LeveliPriority = 0;
    private long LeveliCoins = 0;
    private long LeveliTotalParticipant = 0;
    private long LeveliDuration = 0;
    private @ServerTimestamp Date LeveliDate;

    public LevelModel() {
    }

    public LevelModel(String levelUID, String levelName, String levelPhotoUrl, String levelSyllabus, String levelExtra, String levelCreator, long leveliViewCount, long leveliTotalQues, long leveliPriority, long leveliCoins, long leveliTotalParticipant, long leveliDuration, Date leveliDate) {
        LevelUID = levelUID;
        LevelName = levelName;
        LevelPhotoUrl = levelPhotoUrl;
        LevelSyllabus = levelSyllabus;
        LevelExtra = levelExtra;
        LevelCreator = levelCreator;
        LeveliViewCount = leveliViewCount;
        LeveliTotalQues = leveliTotalQues;
        LeveliPriority = leveliPriority;
        LeveliCoins = leveliCoins;
        LeveliTotalParticipant = leveliTotalParticipant;
        LeveliDuration = leveliDuration;
        LeveliDate = leveliDate;
    }

    public String getLevelUID() {
        return LevelUID;
    }

    public void setLevelUID(String levelUID) {
        LevelUID = levelUID;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public String getLevelPhotoUrl() {
        return LevelPhotoUrl;
    }

    public void setLevelPhotoUrl(String levelPhotoUrl) {
        LevelPhotoUrl = levelPhotoUrl;
    }

    public String getLevelSyllabus() {
        return LevelSyllabus;
    }

    public void setLevelSyllabus(String levelSyllabus) {
        LevelSyllabus = levelSyllabus;
    }

    public String getLevelExtra() {
        return LevelExtra;
    }

    public void setLevelExtra(String levelExtra) {
        LevelExtra = levelExtra;
    }

    public String getLevelCreator() {
        return LevelCreator;
    }

    public void setLevelCreator(String levelCreator) {
        LevelCreator = levelCreator;
    }

    public long getLeveliViewCount() {
        return LeveliViewCount;
    }

    public void setLeveliViewCount(long leveliViewCount) {
        LeveliViewCount = leveliViewCount;
    }

    public long getLeveliTotalQues() {
        return LeveliTotalQues;
    }

    public void setLeveliTotalQues(long leveliTotalQues) {
        LeveliTotalQues = leveliTotalQues;
    }

    public long getLeveliPriority() {
        return LeveliPriority;
    }

    public void setLeveliPriority(long leveliPriority) {
        LeveliPriority = leveliPriority;
    }

    public long getLeveliCoins() {
        return LeveliCoins;
    }

    public void setLeveliCoins(long leveliCoins) {
        LeveliCoins = leveliCoins;
    }

    public long getLeveliTotalParticipant() {
        return LeveliTotalParticipant;
    }

    public void setLeveliTotalParticipant(long leveliTotalParticipant) {
        LeveliTotalParticipant = leveliTotalParticipant;
    }

    public long getLeveliDuration() {
        return LeveliDuration;
    }

    public void setLeveliDuration(long leveliDuration) {
        LeveliDuration = leveliDuration;
    }

    public Date getLeveliDate() {
        return LeveliDate;
    }

    public void setLeveliDate(Date leveliDate) {
        LeveliDate = leveliDate;
    }
}


