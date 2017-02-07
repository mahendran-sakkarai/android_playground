package test.mahendran.testing;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import test.mahendran.testing.model.Question;

/**
 * Created by user on 2/7/2017.
 */

public class Utils {
    public static JsonObject createView(Context context, Question question) {
        JsonObject baseViewObject = new JsonObject();
        if (question != null) {
            JsonObject baseLayout = getBaseView("LinearLayout", "match_parent", "match_parent", "vertical");

            baseLayout.add("children", getChildLayouts(question));

            baseViewObject = getBaseView("ScrollView", "match_parent", "match_parent", null);
            baseViewObject.add("children", baseLayout);
        }
        return baseViewObject;
    }

    private static JsonElement getChildLayouts(Question question) {
        JsonArray childrenLayout = new JsonArray();
        switch (question.getQuestionType()) {
            case TEXT:
                JsonObject editTextLayout = getBaseView("EditText", "match_parent", "wrap_content", null);
                childrenLayout.add(editTextLayout);
                break;
            case OPTION:
                childrenLayout.add(addOptionsRelatedView(question));
                break;
            case OPTION_WITH_USER_INPUT:

                break;
            case SUB_QUESTIONS:

                break;
        }

        return childrenLayout;
    }

    private static JsonElement addOptionsRelatedView(Question question) {
        JsonObject optionsLayout = new JsonObject();
        switch (question.getSelectionType()) {
            case RADIO:

                break;
            case CHECK_BOX:

                break;
            case DROPDOWN:

                break;
        }
        return optionsLayout;
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
}
