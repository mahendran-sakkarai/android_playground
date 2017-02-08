package test.mahendran.testing;

import com.google.gson.JsonElement;

import test.mahendran.testing.model.Question;

/**
 * Created by user on 2/8/2017.
 */

public interface InteractionListener {
    Question getQuestion();

    void updateAnswer(String id, Object answer);
}
