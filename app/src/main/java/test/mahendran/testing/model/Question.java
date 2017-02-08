package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2/7/2017.
 */

public class Question{
    @SerializedName("id")
    private int id;

    @SerializedName("question")
    private String question;

    @SerializedName("questionType")
    private QuestionType questionType;

    @SerializedName("selectionType")
    private SelectionType selectionType;

    @SerializedName("options")
    private HashMap<String, String> options;

    @SerializedName("selectionCount")
    private int selectionCount;

    @SerializedName("answer")
    private ArrayList<Answer> answer;

    @SerializedName("customView")
    private String customView;

    @SerializedName("questions")
    private ArrayList<Question> questions;

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

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(int selectionCount) {
        this.selectionCount = selectionCount;
    }

    public ArrayList<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Answer> answer) {
        this.answer = answer;
    }

    public String getCustomView() {
        return customView;
    }

    public void setCustomView(String customView) {
        this.customView = customView;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
