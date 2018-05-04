package com.tencent.living.dataHelper;
import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;

public class UserHelper {
    public static ResultData<Post> postLogin(String phone_number, String password) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_LOGIN);
        lsa.putData("phone_number", phone_number);
        lsa.putData("password", password);
        return  lsa.execAndGetResult(new TypeToken<ResultData<Post>>() {
        }.getType());
    }
    public static ResultData<Post> postRegister(String phone_number, String nickname, String password, String avatar) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_REGISTER);
        lsa.putData("phone_number", phone_number);
        lsa.putData("password", password);
        lsa.putData("nickname", nickname);
        lsa.putData("avatar", avatar);
        lsa.putData("qq_number", phone_number);
        return  lsa.execAndGetResult(new TypeToken<ResultData<Post>>() {
        }.getType());
    }
    public static ResultData<User> getUserInfo(String token) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_GET_USER);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("token", token);
        return  lsa.execAndGetResult(new TypeToken<ResultData<User>>() {
        }.getType());
    }
}
