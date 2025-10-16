package com.sk.iwara.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by 25140 on 2025/10/16 .
 */
public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> loginData = new MutableLiveData<>();

    public void setLogin(boolean isLogin) {
        loginData.postValue(isLogin);
    }

    public LiveData<Boolean> getLoginData() {
        return loginData;
    }
}