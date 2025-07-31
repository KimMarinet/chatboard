const emotionToEmoticon = {
    "기쁨": "😊",
    "분노": "😠",
    "불안": "😰",
    "당황": "😳",
    "슬픔": "😢",
    "상처": "💔"
};

function convertEmotionToEmoticon(emotion) {
    if (!emotion) return "";
    const keyword = emotion.split(" ")[0]; // 예: "기쁨 만족스러운" → "기쁨"
    const emoji = emotionToEmoticon[keyword];
    return emoji ? `${emoji}` : emotionText;
}


window.addEventListener("DOMContentLoaded", function() {
    const { ajaxLoad } = commonLib;

    const tpl = document.getElementById("chat-tpl").innerHTML;
    const targetEl = document.querySelector(".chat-history");
    const domParser = new DOMParser();

    frmChat.addEventListener("submit", function(e) {

        e.preventDefault();
        const formData = new FormData(frmChat);

        const message = formData.get('message');

        let html = tpl.replace(/\[addClass\]/g, 'user')
                        .replace(/\[message\]/g, message);

        const dom = domParser.parseFromString(html, "text/html");

        targetEl.append(dom.querySelector(".message"));

        frmChat.message.value = ""; // 메세지 입력 초기화

        const url = `/chat/api?model=${formData.get('model')}&roomId=${formData.get('roomId')}&message=${message}`;

        ajaxLoad(url, ({ sysMessage, emotion }) => {
            let emotionHtml = "";
            if (emotion && emotion.trim() !== "") {
                const emoticon = convertEmotionToEmoticon(emotion.trim());
                emotionHtml = `감정: ${emoticon}`;
            }

            let html = tpl
                .replace(/\[addClass\]/g, 'system')
                .replace(/\[message\]/g, sysMessage)
                .replace(/\[emotionHtml\]/g, emotionHtml);

            const dom = domParser.parseFromString(html, "text/html");
            targetEl.append(dom.querySelector(".message"));
        });
    });
});