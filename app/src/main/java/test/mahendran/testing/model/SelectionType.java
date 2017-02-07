package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/7/2017.
 */

public enum SelectionType {
    @SerializedName("check_box")
    CHECK_BOX,
    @SerializedName("radio")
    RADIO,
    @SerializedName("drop_down")
    DROPDOWN
}
