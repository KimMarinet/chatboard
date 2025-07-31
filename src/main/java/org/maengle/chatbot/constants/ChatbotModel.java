package org.maengle.chatbot.constants;

public enum ChatbotModel {
    MODEL1(1, "모델1"),
    MODEL2(2, "모델2"),
    MODEL3(3, "모델3");

    private final int num;
    private final String title;

    ChatbotModel(int num, String title){
        this.num = num;
        this.title = title;
    }

    public int getNum(){
        return num;
    }
    public String getTitle() {
        return title;
    }
}
