package com.bakhtiar.quizmate.View;

public class QuestionModel {

    private String QuesUID = "NO";
    private String UserSelectedAnswer = "NO";
    private int AnswerOnIndex = 1;
    private String Question = "NO";
    private String Answer = "NO";
    private String Fake2 = "NO";
    private String Fake3 = "NO";
    private String Fake4 = "NO";
    private String Suggestion = "NO";
    private String PhotoUrl = "NO";
    private String Extra = "NO";

    private long Serial = 0;

    public QuestionModel() {
    }

    public QuestionModel(String quesUID, String userSelectedAnswer, int answerOnIndex, String question, String answer, String fake2, String fake3, String fake4, String suggestion, String photoUrl, String extra, long serial) {
        QuesUID = quesUID;
        UserSelectedAnswer = userSelectedAnswer;
        AnswerOnIndex = answerOnIndex;
        Question = question;
        Answer = answer;
        Fake2 = fake2;
        Fake3 = fake3;
        Fake4 = fake4;
        Suggestion = suggestion;
        PhotoUrl = photoUrl;
        Extra = extra;
        Serial = serial;
    }

    public String getQuesUID() {
        return QuesUID;
    }

    public String getUserSelectedAnswer() {
        return UserSelectedAnswer;
    }

    public String getQuestion() {
        return Question;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getFake2() {
        return Fake2;
    }

    public String getFake3() {
        return Fake3;
    }

    public String getFake4() {
        return Fake4;
    }

    public String getSuggestion() {
        return Suggestion;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public String getExtra() {
        return Extra;
    }

    public long getSerial() {
        return Serial;
    }

    public int getAnswerOnIndex() {
        return AnswerOnIndex;
    }

    public void setAnswerOnIndex(int answerOnIndex) {
        AnswerOnIndex = answerOnIndex;
    }

    public void setUserSelectedAnswer(String userSelectedAnswer) {
        UserSelectedAnswer = userSelectedAnswer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public void setFake2(String fake2) {
        Fake2 = fake2;
    }

    public void setFake3(String fake3) {
        Fake3 = fake3;
    }

    public void setFake4(String fake4) {
        Fake4 = fake4;
    }
}
