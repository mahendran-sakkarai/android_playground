package test.mahendran.testing;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipkart.android.proteus.builder.LayoutBuilder;
import com.flipkart.android.proteus.builder.LayoutBuilderFactory;
import com.flipkart.android.proteus.view.ProteusView;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import test.mahendran.testing.model.Question;

/**
 * Created by user on 2/7/2017.
 */

public class QuestionFragment extends Fragment {
    private static final String QUESTION_KEY = "question";
    private Question mQuestion;

    public QuestionFragment() {

    }

    public QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    public void setQuestion(Question question) {
        this.mQuestion = question;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutBuilder layoutBuilder = new LayoutBuilderFactory().getDataParsingLayoutBuilder();
        ProteusView view = layoutBuilder.build(container, Utils.createView(container.getContext(), mQuestion), null, 0, null);

        return (View) view;
    }
}
