package com.konik.quizlevelunlock.Model;

public class RankModel {

    private String Uid = "NO";
    private String Choosed = "NO";
    private String Extra = "NO";
    private String ParticipantUID = "NO";
    private long Result = 0;
    private long LiveRank = 0;


    public RankModel() {
    }

    public RankModel(String uid, String choosed, String extra, String participantUID, long result, long liveRank) {
        Uid = uid;
        Choosed = choosed;
        Extra = extra;
        ParticipantUID = participantUID;
        Result = result;
        LiveRank = liveRank;
    }

    public String getUid() {
        return Uid;
    }

    public String getChoosed() {
        return Choosed;
    }

    public String getExtra() {
        return Extra;
    }

    public String getParticipantUID() {
        return ParticipantUID;
    }

    public long getResult() {
        return Result;
    }

    public long getLiveRank() {
        return LiveRank;
    }
}


