package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/8/2017.
 */
public class Answer {
    @SerializedName("option")
    private String option;

    @SerializedName("user_input")
    private String userInput;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
