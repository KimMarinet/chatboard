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
        ajaxLoad(url, ({sysMessage, emotion}) => {

             let html = tpl.replace(/\[addClass\]/g, 'system')
                                    .replace(/\[message\]/g, sysMessage);

             const dom = domParser.parseFromString(html, "text/html");
             targetEl.append(dom.querySelector(".message"));

        });
    });
});