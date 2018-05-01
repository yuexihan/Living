package com.tencent.living;

import com.tencent.living.Data.MessageListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MessageItemApi {

    public ArrayList<MessageListItem> parseData(String content) throws JSONException {
        ArrayList<MessageListItem> newsListItems = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray array = object.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            MessageListItem newsListItem = new MessageListItem();
            newsListItem.setMessageContent(results.getString("content"));
            newsListItem.setHeadImageId(R.id.headImage);
            newsListItem.setPraised(results.getBoolean("praise"));
            newsListItems.add(newsListItem);
        }
        return newsListItems;
    }

}


