window.addEventListener("DOMContentLoaded", function() {
    const el = document.querySelector(".uploaded_images .remove");
    if (el) {
        el.addEventListener("click", function() {
            if (!confirm('정말 삭제하겠습니까?')) {
                return;
            }

            const { seq } = this.dataset;
            const { fileManager } = commonLib;
            fileManager.delete(seq);
        });
    }
});

/**
* 파일 업로드 후속 처리
*
*/
function fileUploadCallback(items) {
    if (!items || items.length === 0) return;

    const { seq } = items[0];
    let html = document.getElementById("image-tpl").innerHTML;
    const targetEl = document.querySelector(".uploaded_images .inner");
    const imageUrl = commonLib.getUrl(`/file/thumb?seq=${seq}&width=250&height=250&crop=true`);
    html = html.replace(/\[seq\]/g, seq)
                .replace(/\[imageUrl\]/g, imageUrl);

    const domParser = new DOMParser();
    const dom = domParser.parseFromString(html, "text/html");
    const el = dom.querySelector(".file-images");

    targetEl.append(el);
    targetEl.parentElement.classList.add("uploaded")

    const removeEl = el.querySelector(".remove");
    const { fileManager } = commonLib;
    removeEl.addEventListener("click", function() {
        if (confirm("정말 삭제하겠습니까?")) {
            fileManager.delete(seq);
        }
    });
}

/**
* 파일 삭제 후속 처리
*
*/
function fileDeleteCallback(item) {
   const targetEl = document.querySelector(".uploaded_images .inner");
   targetEl.innerHTML = "";
   targetEl.parentElement.classList.remove("uploaded");
}