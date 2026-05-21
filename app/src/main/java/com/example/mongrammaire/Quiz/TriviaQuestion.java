package com.example.mongrammaire.Quiz;

public class TriviaQuestion {
    private int id;
    private String question;
    private String opta;
    private String optb;
    private String optc;
    private String optd;
    private String answer;
    
    // Spaced Repetition Fields
    private long nextReviewTime; // Unix timestamp
    private int interval;        // Days
    private float easeFactor;    // Default 2.5
    private int repetitions;     // Number of times reviewed

    public TriviaQuestion(String q, String oa, String ob, String oc, String od, String ans) {
        question = q;
        opta = oa;
        optb = ob;
        optc = oc;
        optd = od;
        answer = ans;
        this.nextReviewTime = 0;
        this.interval = 0;
        this.easeFactor = 2.5f;
        this.repetitions = 0;
    }

    public TriviaQuestion() {
        id = 0;
        question = "";
        opta = "";
        optb = "";
        optc = "";
        optd = "";
        answer = "";
        this.nextReviewTime = 0;
        this.interval = 0;
        this.easeFactor = 2.5f;
        this.repetitions = 0;
    }

    public int getId() { return id; }
    public String getQuestion() { return question; }
    public String getOptA() { return opta; }
    public String getOptB() { return optb; }
    public String getOptC() { return optc; }
    public String getOptD() { return optd; }
    public String getAnswer() { return answer; }

    public long getNextReviewTime() { return nextReviewTime; }
    public void setNextReviewTime(long nextReviewTime) { this.nextReviewTime = nextReviewTime; }

    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }

    public float getEaseFactor() { return easeFactor; }
    public void setEaseFactor(float easeFactor) { this.easeFactor = easeFactor; }

    public int getRepetitions() { return repetitions; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }

    public void setId(int i) { id = i; }
    public void setQuestion(String q1) { question = q1; }
    public void setOptA(String o1) { opta = o1; }
    public void setOptB(String o2) { optb = o2; }
    public void setOptC(String o3) { optc = o3; }
    public void setOptD(String o4) { optd = o4; }
    public void setAnswer(String ans) { answer = ans; }
}
