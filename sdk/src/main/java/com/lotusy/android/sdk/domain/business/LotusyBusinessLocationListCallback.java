package com.lotusy.android.sdk.domain.business;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lotusy.android.sdk.task.LotusyCallback;
import com.lotusy.android.sdk.task.LotusyTaskResult;
import com.lotusy.android.sdk.utility.LotusyUtility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pshen on 2014-07-17.
 */
abstract public class LotusyBusinessLocationListCallback extends LotusyCallback {

    @Override
    protected void doCallback(LotusyCallbackStatus status, JsonObject response) {
        LotusyTaskResult result = null;
        Map<Double, LotusySimpleBusiness> businesses = null;

        if (status == LotusyCallbackStatus.SUCCESS) {
            businesses = new HashMap<Double, LotusySimpleBusiness>();

            JsonArray businessArr = response.get("businesses").getAsJsonArray();

            Iterator<JsonElement> itr = businessArr.iterator();
            while(itr.hasNext()) {
                JsonObject element = itr.next().getAsJsonObject();

                LotusySimpleBusiness business = LotusyUtility.parseSimpleBusinessJson(element);
                int businessId = element.get("id").getAsInt();
                business.id = businessId;

                double distance = element.get("distance").getAsDouble();

                businesses.put(distance, business);
            }

            result = new LotusyTaskResult();
            result.setStatusCode(0);
            result.setSuccess(true);
        }
        else if (status == LotusyCallbackStatus.ERROR) {
            result = new LotusyTaskResult();
            result.setStatusCode(1);
            result.setSuccess(false);

            String description = response.get("description").getAsString();
            result.addError(description);
        }
        else if (status == LotusyCallbackStatus.FAILURE) {
            result = new LotusyTaskResult();
            result.setStatusCode(2);
            result.setSuccess(false);

            String description = response.get("description").getAsString();
            result.addError(description);
        }

        this.callback(result, businesses);
    }

    abstract public void callback(LotusyTaskResult result, Map<Double, LotusySimpleBusiness> businesses);
}
