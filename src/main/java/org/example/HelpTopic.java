package org.example;

public class HelpTopic {
    private String question;
    private String answer;

    public HelpTopic(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
}