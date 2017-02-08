package test.mahendran.testing.parser;

import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.flipkart.android.proteus.parser.Attributes;
import com.flipkart.android.proteus.parser.ParseHelper;
import com.flipkart.android.proteus.parser.Parser;
import com.flipkart.android.proteus.parser.WrappableParser;
import com.flipkart.android.proteus.processor.StringAttributeProcessor;
import com.flipkart.android.proteus.toolbox.Styles;
import com.flipkart.android.proteus.view.ProteusLinearLayout;
import com.flipkart.android.proteus.view.ProteusView;
import com.google.gson.JsonObject;

import test.mahendran.testing.views.ProteusRadioGroup;

/**
 * Created by user on 2/8/2017.
 */

public class RadioGroupParser<T extends RadioGroup> extends WrappableParser<T> {
    public RadioGroupParser(Parser<T> wrappedParser) {
        super(wrappedParser);
    }

    @Override
    public ProteusView createView(ViewGroup viewGroup, JsonObject jsonObject, JsonObject jsonObject1, Styles styles, int i) {
        return new ProteusRadioGroup(viewGroup.getContext());
    }

    @Override
    protected void prepareHandlers() {
        super.prepareHandlers();
        addHandler(Attributes.LinearLayout.Orientation, new StringAttributeProcessor<T>() {
            @Override
            public void handle(String attributeKey, String attributeValue, T view) {
                if ("horizontal".equals(attributeValue)) {
                    view.setOrientation(ProteusLinearLayout.HORIZONTAL);
                } else {
                    view.setOrientation(ProteusLinearLayout.VERTICAL);
                }
            }
        });

        addHandler(Attributes.View.Gravity, new StringAttributeProcessor<T>() {
            @Override
            public void handle(String attributeKey, String attributeValue, T view) {

                view.setGravity(ParseHelper.parseGravity(attributeValue));

            }
        });
    }
}
