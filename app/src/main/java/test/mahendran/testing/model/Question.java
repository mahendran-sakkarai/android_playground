package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2/7/2017.
 */

public class Question<ANSWER_TYPE, OPTION_TYPE>{
    @SerializedName("id")
    private int id;

    @SerializedName("question")
    private String question;

    @SerializedName("questionType")
    private QuestionType questionType;

    @SerializedName("selectionType")
    private SelectionType selectionType;

    @SerializedName("options")
    private OPTION_TYPE options;

    @SerializedName("selectionCount")
    private int selectionCount;

    @SerializedName("answer")
    private ANSWER_TYPE answer;

    @SerializedName("customView")
    private String customView;

    @SerializedName("questions")
    private List<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectionType) {
        this.selectionType = selectionType;
    }

    public OPTION_TYPE getOptions() {
        return options;
    }

    public void setOptions(OPTION_TYPE options) {
        this.options = options;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(int selectionCount) {
        this.selectionCount = selectionCount;
    }

    public ANSWER_TYPE getAnswer() {
        return answer;
    }

    public void setAnswer(ANSWER_TYPE answer) {
        this.answer = answer;
    }

    public String getCustomView() {
        return customView;
    }

    public void setCustomView(String customView) {
        this.customView = customView;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
