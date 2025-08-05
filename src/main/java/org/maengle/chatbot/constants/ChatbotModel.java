package org.maengle.chatbot.constants;

public enum ChatbotModel {
    MODEL1(1, "범죄와의 전쟁 - 최익현"),
    MODEL2(2, "태극기 휘날리며 - 이진태"),
    MODEL3(3, "시크릿 가든 - 김주원"),
    MODEL4(4, "너의 결혼식 - 황우연");

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
