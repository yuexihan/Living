package com.tencent.living;

import com.tencent.living.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MessageItemApi {

    public static ArrayList<Message> parseData(String content) throws JSONException {
        ArrayList<Message> messageList = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray array = object.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Message messageListItem = new Message();
            messageListItem.setComment(results.getString("comment"));
            messageListItem.setAvatar(results.getString("avatar"));
            messageListItem.setNickname(results.getString("nickname"));
            messageListItem.setType(results.getInt("type"));
            messageListItem.setPoster(results.getInt("poster"));
            messageList.add(messageListItem);
        }
        return messageList;
    }

}


