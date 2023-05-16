package com.example.example1;

import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


//ViewModel本质上还是model
public class TextViewModel extends ViewModel {

//    private String text="old";
    public MutableLiveData<String> textLiveData;

    public TextViewModel() {
        this.textLiveData=new MutableLiveData<>();
        textLiveData.setValue("old");
    }


    public void switchText(){
        if(textLiveData.getValue().equals("new")){
            textLiveData.setValue("old");
        }else{
            textLiveData.setValue("new");
        }
    }


    public MutableLiveData<String> getTextLiveData() {

        return textLiveData;
    }

    public void setTextLiveData(MutableLiveData<String> textLiveData) {
        this.textLiveData = textLiveData;
    }
    public void display(TextView t){
        t.setText(getTextLiveData().getValue());
    }
/*    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }*/
}
