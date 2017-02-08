package test.mahendran.testing;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipkart.android.proteus.builder.LayoutBuilder;
import com.flipkart.android.proteus.builder.LayoutBuilderFactory;
import com.flipkart.android.proteus.parser.ViewParser;
import com.flipkart.android.proteus.parser.custom.ViewGroupParser;
import com.flipkart.android.proteus.view.ProteusView;

import java.util.ArrayList;

import test.mahendran.testing.parser.RadioButtonParser;
import test.mahendran.testing.parser.RadioGroupParser;
import test.mahendran.testing.parser.SpinnerParser;

/**
 * Created by user on 2/7/2017.
 */

public class QuestionFragment extends Fragment {
    private InteractionListener mListener;
    private ArrayList<String> mIds = new ArrayList<>();

    public QuestionFragment() {

    }

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutBuilder layoutBuilder = new LayoutBuilderFactory().getSimpleLayoutBuilder();
        layoutBuilder.registerHandler("Spinner", new SpinnerParser(new ViewParser()));
        layoutBuilder.registerHandler("RadioGroup", new RadioGroupParser(new ViewGroupParser(new ViewParser())));
        layoutBuilder.registerHandler("RadioButton", new RadioButtonParser(new ViewParser()));
        ProteusView view = layoutBuilder.build(container, Utils.createView(mListener.getQuestion()), null, 0, null);

        return (View) view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setQuestionListener(InteractionListener listener) {
        this.mListener = listener;
    }
}
