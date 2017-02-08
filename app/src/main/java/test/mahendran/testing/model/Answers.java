package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/8/2017.
 */

public class Answers {
    @SerializedName("type")
    private AnswerType answerType;

    @SerializedName("answer")
    private Answer answer;

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
