package org.maengle.chatbot.constants;

public enum ChatbotModel {
    MODEL1(1),
    MODEL2(2),
    MODEL3(3);

    private final int num;
    ChatbotModel(int num){
        this.num = num;
    }

    public int getNum(){
        return num;
    }
}
