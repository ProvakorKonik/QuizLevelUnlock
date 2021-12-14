package com.konik.quizlevelunlock.Model;

public class SubjectModel {
    private String SubjectUID= "NO";
    private String SubjectName = "NO";
    private String SubjectPhotoUrl = "NO";
    private String SubjectBio = "NO";
    private String SubjectCreator = "NO";
    private String SubjectExtra = "NO";
    private String SubjectPassword = "NO";
    private int SubjectiPriority = 0;
    private int SubjectiViewCount= 0;
    private int SubjectiTotalLevel= 0;

    public SubjectModel() {
    }

    public SubjectModel(String subjectUID, String subjectName, String subjectPhotoUrl, String subjectBio, String subjectCreator, String subjectExtra, String subjectPassword, int subjectiPriority, int subjectiViewCount, int subjectiTotalLevel) {
        SubjectUID = subjectUID;
        SubjectName = subjectName;
        SubjectPhotoUrl = subjectPhotoUrl;
        SubjectBio = subjectBio;
        SubjectCreator = subjectCreator;
        SubjectExtra = subjectExtra;
        SubjectPassword = subjectPassword;
        SubjectiPriority = subjectiPriority;
        SubjectiViewCount = subjectiViewCount;
        SubjectiTotalLevel = subjectiTotalLevel;
    }

    public String getSubjectUID() {
        return SubjectUID;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public String getSubjectPhotoUrl() {
        return SubjectPhotoUrl;
    }

    public String getSubjectBio() {
        return SubjectBio;
    }

    public String getSubjectCreator() {
        return SubjectCreator;
    }

    public String getSubjectExtra() {
        return SubjectExtra;
    }

    public String getSubjectPassword() {
        return SubjectPassword;
    }

    public int getSubjectiPriority() {
        return SubjectiPriority;
    }

    public int getSubjectiViewCount() {
        return SubjectiViewCount;
    }

    public int getSubjectiTotalLevel() {
        return SubjectiTotalLevel;
    }
}
