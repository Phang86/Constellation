package com.yyzy.constellation.user;

public interface CallBackOkhttp {
    void onSuccess(String resultStr);
    void onError(Exception e);
}
