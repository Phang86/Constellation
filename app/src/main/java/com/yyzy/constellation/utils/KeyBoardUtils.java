package com.yyzy.constellation.utils;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yyzy.constellation.R;

public class KeyBoardUtils{
    private final Keyboard k1;//自定义的键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnSureListener{
        void onEnSure();
    }

    OnEnSureListener onEnSureListener;

    public void setOnEnSureListener(OnEnSureListener onEnSureListener) {
        this.onEnSureListener = onEnSureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);   //取消弹出系统键盘
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);
        this.keyboardView.setKeyboard(k1);      //显示自定义的键盘
        this.keyboardView.setEnabled(true);     //可使用
        this.keyboardView.setPreviewEnabled(false);   //可进行预览
        this.keyboardView.setOnKeyboardActionListener(listener);  //设置各个按钮监听
    }


    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable text = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                //点击了删除
                case Keyboard.KEYCODE_DELETE:
                    if (text.length() > 0 && !TextUtils.isEmpty(text)) {
                        if (start > 0) {
                            text.delete(start-1,start);
                        }
                    }
                    break;
                //点击了清零
                case Keyboard.KEYCODE_CANCEL:
                    text.clear();
                    break;
                //点击了确定
                case Keyboard.KEYCODE_DONE:
                    //通过回调函数进行判断
                    onEnSureListener.onEnSure();
                    break;
                default:    //其他数字直接插入
                    text.insert(start,Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    //显示键盘的方法
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    //隐藏键盘的方法
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

}
