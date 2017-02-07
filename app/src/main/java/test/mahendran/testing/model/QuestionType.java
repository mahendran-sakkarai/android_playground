package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/7/2017.
 */

public enum QuestionType {
    @SerializedName("text")
    TEXT,
    @SerializedName("options")
    OPTION,
    @SerializedName("option_with_user_input")
    OPTION_WITH_USER_INPUT,
    @SerializedName("sub_questions")
    SUB_QUESTIONS
}
