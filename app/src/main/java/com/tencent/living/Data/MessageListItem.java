package com.tencent.living.Data;

public class MessageListItem {


    private String messageContent;
    private int headImageId;
    private boolean isPraised;
//    private String praiseNum;

    public MessageListItem(){

    }

    public MessageListItem(String messageContent,int headImageId,boolean isPraised){
        super();
        this.messageContent = messageContent;
        this.headImageId = headImageId;
        this.isPraised = isPraised;

    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String newsContent) {
        this.messageContent = newsContent;
    }

    public int getHeadImageId() {
        return headImageId;
    }

    public void setHeadImageId(int headImageId) {
        this.headImageId = headImageId;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setPraised(boolean praised) {
        isPraised = praised;
    }



}

