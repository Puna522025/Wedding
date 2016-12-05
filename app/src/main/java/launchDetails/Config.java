package launchDetails;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by DELL on 11/29/2016.
 */
public class Config {

    public static final String URL_FETCH = "http://vnnps.esy.es/getData.php?unique_wed_code=";
    public static final String TAG_JSON_ARRAY = "result";
    public static final String unique_wed_code = "unique_wed_code";
    public static final String name_bride = "name_bride";
    public static final String name_groom = "name_groom";
    public static final String date_marriage = "date_marriage";
    public static final String blessUs_para = "blessUs_para";
    public static final String event_two_tobe = "event_two_tobe";
    public static final String event_Two_date = "event_two_date";
    public static final String event_Two_time = "event_two_time";
    public static final String event_Two_location = "event_two_location";
    public static final String marriage_tobe = "marriage_tobe";
    public static final String marriage_date = "marriage_date";
    public static final String marriage_time = "marriage_time";
    public static final String marriage_location = "marriage_location";
    public static final String rsvp_tobe = "rsvp_tobe";
    public static final String rsvp_name1 = "rsvp_name1";
    public static final String rsvp_name2 = "rsvp_name2";
    public static final String rsvp_phone_one = "rsvp_phone_one";
    public static final String rsvp_phone_two = "rsvp_phone_two";
    public static final String event_two_name = "event_two_name";
    public static final String rsvp_text = "rsvp_text";
    public static final String setToolbarMenuIcons = "setToolbarMenuIcons";
    public static final String setLatestViewedId = "setLatestViewedId";
    public static final  String TYPE_WED_CREATED = "created";
    public static final  String TYPE_WED_VIEWED = "viewed";
    public static final String ONLY_SHARE = "only_Share";

    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void checkEditTextNullandSetError(EditText editText) {
        if (editText != null && TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Please fill the details..");
        }
    }

    public static void changeDateTextcolor(TextView toCheck, TextView toChaneColor, int colorRed, int colorNormal) {
        if (toCheck != null && TextUtils.isEmpty(toCheck.getText().toString())) {
            toChaneColor.setTextColor(colorRed);
        } else {
            toChaneColor.setTextColor(colorNormal);
        }
    }

    public static boolean checkDateText(TextView toCheck) {
        if (toCheck != null && TextUtils.isEmpty(toCheck.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEditTextEmpty(EditText editText) {
        if (editText != null && TextUtils.isEmpty(editText.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideKeyboard(View currentFocus, Context context) {
        if (currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
