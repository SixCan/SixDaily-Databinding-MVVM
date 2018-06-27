package ca.six.daily.data;


import android.support.annotation.IntDef;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CheckUpdateResponse {
    private static final int NO_UPDATE = 0;
    private static final int HAS_UPDATE = 1;

    @AppUpdateStatus
    public int status;
    public String message;
    public String url;
    public String latest;

    public CheckUpdateResponse(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                status = json.optInt("status");
                message = json.optString("msg");
                url = json.optString("url");
                latest = json.optString("latest");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNoUpdate(){
        return status == NO_UPDATE;
    }


    @IntDef({NO_UPDATE, HAS_UPDATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppUpdateStatus {
    }

}
