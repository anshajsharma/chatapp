package com.example.chatapp.NotInUse;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading , String notificationKey)
    {
        try {
            JSONObject jsonObject =  new JSONObject("{'contents': {'en':'"+message+"'}," + "'include_player_ids':['" + notificationKey + "']," +
                    "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(jsonObject,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
