package test.mahendran.testing.parser;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.flipkart.android.proteus.parser.Attributes;
import com.flipkart.android.proteus.parser.Parser;
import com.flipkart.android.proteus.parser.WrappableParser;
import com.flipkart.android.proteus.processor.DrawableResourceProcessor;
import com.flipkart.android.proteus.processor.StringAttributeProcessor;
import com.flipkart.android.proteus.toolbox.Styles;
import com.flipkart.android.proteus.view.ProteusView;
import com.google.gson.JsonObject;

import test.mahendran.testing.views.ProteusRadioButton;

/**
 * Created by user on 2/8/2017.
 */

public class RadioButtonParser<T extends RadioButton> extends WrappableParser<T> {
    public RadioButtonParser(Parser<T> wrappedParser) {
        super(wrappedParser);
    }

    @Override
    public ProteusView createView(ViewGroup viewGroup, JsonObject jsonObject, JsonObject jsonObject1, Styles styles, int i) {
        return new ProteusRadioButton(viewGroup.getContext());
    }

    @Override
    protected void prepareHandlers() {
        super.prepareHandlers();

        addHandler(Attributes.CheckBox.Button, new DrawableResourceProcessor<T>() {
            @Override
            public void setDrawable(T view, Drawable drawable) {
                view.setButtonDrawable(drawable);
            }
        });

        addHandler(Attributes.CheckBox.Checked, new StringAttributeProcessor<T>() {
            @Override
            public void handle(String attributeKey, String attributeValue, T view) {
                view.setChecked(Boolean.parseBoolean(attributeValue));
            }
        });

        addHandler(Attributes.TextView.Text, new StringAttributeProcessor<T>() {
            @Override
            public void handle(String attributeKey, String attributeValue, T view) {
                view.setText(attributeValue);
            }
        });
    }
}
