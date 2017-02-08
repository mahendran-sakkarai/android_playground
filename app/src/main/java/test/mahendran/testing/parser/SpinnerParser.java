package test.mahendran.testing.parser;

import android.view.ViewGroup;
import android.widget.Spinner;

import com.flipkart.android.proteus.parser.Parser;
import com.flipkart.android.proteus.parser.WrappableParser;
import com.flipkart.android.proteus.toolbox.Styles;
import com.flipkart.android.proteus.view.ProteusView;
import com.google.gson.JsonObject;

import test.mahendran.testing.views.ProteusSpinner;

/**
 * Created by user on 2/8/2017.
 */

public class SpinnerParser<T extends Spinner> extends WrappableParser<T> {
    public SpinnerParser(Parser<T> wrappedParser) {
        super(wrappedParser);
    }

    @Override
    public ProteusView createView(ViewGroup viewGroup, JsonObject jsonObject, JsonObject jsonObject1, Styles styles, int i) {
        return new ProteusSpinner(viewGroup.getContext());
    }
}
