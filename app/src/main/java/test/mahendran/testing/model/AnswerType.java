package test.mahendran.testing.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/8/2017.
 */
public enum AnswerType {
    @SerializedName("string")
    STRING_TYPE,
    @SerializedName("int")
    INT_TYPE,
    @SerializedName("array")
    ARRAY_TYPE,
    @SerializedName("map")
    MAP_TYPE;
}
