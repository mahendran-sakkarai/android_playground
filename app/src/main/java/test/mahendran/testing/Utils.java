package test.mahendran.testing;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import test.mahendran.testing.model.Question;
import test.mahendran.testing.model.QuestionType;

/**
 * Created by user on 2/7/2017.
 */

public class Utils {
    public static JsonObject createView(Question question) {
        JsonObject response = new JsonObject();
        if (question != null) {
            JsonObject idList = new JsonObject();
            JsonObject baseViewObject = getBaseView("ScrollView", "match_parent", "match_parent", null);
            baseViewObject.addProperty("background", "#FFFFFF");
            baseViewObject.addProperty("clickable", "true");
            JsonElement questions = getChildLayouts(question, idList);
            if (questions != null) {
                JsonArray scrollViewChildren = new JsonArray();
                scrollViewChildren.add(questions);
                baseViewObject.add("children", scrollViewChildren);
            }

            response.add("view", baseViewObject);
        }
        return response;
    }

    private static JsonElement getChildLayouts(Question question, JsonObject idList) {
        JsonObject baseLayout = getBaseView("LinearLayout", "match_parent", "match_parent", "vertical");
        JsonArray childrenLayout = new JsonArray();
        if (question.getQuestion() != null) {
            JsonObject questionView = getBaseView("TextView", "match_parent", "wrap_content", null);
            questionView.addProperty("text", question.getQuestion());
            childrenLayout.add(questionView);
        }

        if (question.getQuestionType() != QuestionType.SUB_QUESTIONS && question.getId() == null) {
            try {
                throw new IdRequiredException();
            } catch (IdRequiredException e) {
                e.printStackTrace();
                return null;
            }
        } else {

        }

        switch (question.getQuestionType()) {
            case TEXT:
                JsonObject editTextLayout = getBaseView("EditText", "match_parent", "wrap_content", null);
                childrenLayout.add(editTextLayout);
                break;
            case OPTION:
                addOptionsRelatedView(childrenLayout, question, false);
                break;
            case OPTION_WITH_USER_INPUT:
                addOptionsRelatedView(childrenLayout, question, true);
                break;
            case SUB_QUESTIONS:
                addSubQuestions(childrenLayout, question, idList);
                break;
        }

        baseLayout.add("children", childrenLayout);

        return baseLayout;
    }

    private static void addSubQuestions(JsonArray container, Question baseQuestion, JsonObject idList) {
        for (int i = 0; i < baseQuestion.getQuestions().size(); i++) {
            Question question = baseQuestion.getQuestions().get(i);
            JsonElement questionLayout = getChildLayouts(question, idList);
            if (questionLayout != null)
                container.add(questionLayout);
        }
    }

    private static void addOptionsRelatedView(JsonArray container, Question question, boolean user_input) {
        JsonArray options = new JsonArray();
        Iterator iterations = ((HashMap) question.getOptions()).entrySet().iterator();
        while (iterations.hasNext()) {
            Map.Entry pair = (Map.Entry) iterations.next();
            options.add(pair.getValue().toString());
        }
        switch (question.getSelectionType()) {
            case RADIO:
                generateRadioGroup(options, container, user_input);
                break;
            case CHECK_BOX:
                generateCheckBox(options, container, user_input);
                break;
            case DROPDOWN:
                generateSpinner(options, container, user_input);
                break;
        }
    }

    private static void generateSpinner(JsonArray options, JsonArray container, boolean user_input) {
        JsonObject optionsLayout = getBaseView("Spinner", "match_parent", "wrap_content", null);
        //optionsLayout.addProperty("entries", "$options");
        container.add(optionsLayout);
        if (user_input)
            addUserInputView(container);
    }

    private static void generateCheckBox(JsonArray options, JsonArray container, boolean user_input) {
        for (int i = 0; i < options.size(); i++) {
            JsonObject optionsLayout = getBaseView("CheckBox", "wrap_content", "wrap_content", null);
            optionsLayout.addProperty("text", options.get(i).toString());
            container.add(optionsLayout);
            if (user_input)
                addUserInputView(container);
        }
    }

    private static void generateRadioGroup(JsonArray options, JsonArray container, boolean user_input) {
        JsonObject radioGroup = getBaseView("RadioGroup", "wrap_content", "wrap_content", "horizontal");
        JsonArray radioButtons = new JsonArray();
        for (int i = 0; i < options.size(); i++) {
            JsonObject optionsLayout = getBaseView("RadioButton", "wrap_content", "wrap_content", null);
            optionsLayout.addProperty("text", options.get(i).toString());
            radioButtons.add(optionsLayout);
        }
        radioGroup.add("children", radioButtons);
        container.add(radioGroup);
        if (user_input)
            addUserInputView(container);
    }

    private static void addUserInputView(JsonArray container) {
        JsonObject reasonViewLayout = getBaseView("LinearLayout", "match_parent", "wrap_content", "vertical");
        JsonArray layoutChildrens = new JsonArray();
        JsonObject reasonView = getBaseView("TextView", "match_parent", "wrap_content", null);
        reasonView.addProperty("text", "Reason:");
        layoutChildrens.add(reasonView);
        layoutChildrens.add(getBaseView("EditText", "match_parent", "wrap_content", null));
        reasonViewLayout.add("children", layoutChildrens);
        container.add(reasonViewLayout);
    }

    private static JsonObject getBaseView(String view, String width, String height, String orientation) {
        JsonObject viewToReturn = new JsonObject();
        viewToReturn.addProperty("type", view);
        viewToReturn.addProperty("layout_width", width);
        viewToReturn.addProperty("layout_height", height);
        if (orientation != null)
            viewToReturn.addProperty("orientation", orientation);
        return viewToReturn;
    }

    public static String getQuestion(Context context) throws IOException {
        InputStream is = context.getResources().openRawResource(R.raw.questions);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return writer.toString();
    }
}
