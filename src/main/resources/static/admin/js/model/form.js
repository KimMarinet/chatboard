window.addEventListener("DOMContentLoaded", function () {
    const el = document.querySelector(".file-images .remove");
    if (el) {
        el.addEventListener("click", function () {
            const { seq } = this.dataset;

            Swal.fire({
                title: '정말 삭제하시겠습니까?',
                text: '삭제된 파일은 복구할 수 없습니다.',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: '삭제',
                cancelButtonText: '취소',
                reverseButtons: true,
                customClass: {
                    confirmButton: 'swal2-confirm-red',
                    cancelButton: 'swal2-cancel-gray'
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    const { fileManager } = commonLib;
                    fileManager.delete(seq);

                    Swal.fire({
                        title: '삭제 완료',
                        text: '파일이 삭제되었습니다.',
                        icon: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    });
                }
            });
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
    const targetEl = document.querySelector(".main-image");
    const imageUrl = commonLib.getUrl(`/file/thumb?seq=${seq}&width=250&height=250&crop=true`);
    html = html.replace(/\[seq\]/g, seq)
                .replace(/\[imageUrl\]/g, imageUrl);

    const domParser = new DOMParser();
    const dom = domParser.parseFromString(html, "text/html");
    const el = dom.querySelector(".file-images");

    targetEl.append(el);

    const removeEl = el.querySelector(".remove");
    const { fileManager } = commonLib;

    removeEl.addEventListener("click", function () {
        const { seq } = this.dataset;

        Swal.fire({
            title: '정말 삭제하시겠습니까?',
            text: '삭제된 이미지는 복구할 수 없습니다.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소',
            reverseButtons: true,
            customClass: {
                confirmButton: 'swal2-confirm-red',
                cancelButton: 'swal2-cancel-gray'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                fileManager.delete(seq);

                Swal.fire({
                    title: '삭제 완료',
                    text: '프로필 이미지가 삭제되었습니다.',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                });
            }
        });
    });

    const uploadBtn = document.querySelector(".file-upload-btn");
    uploadBtn.classList.remove("dn");
    uploadBtn.classList.add("dn");
}

/**
* 파일 삭제 후속 처리
*
*/
function fileDeleteCallback(item) {
   const targetEl = document.querySelector(".main-image");
   targetEl.innerHTML = "";

   const uploadBtn = document.querySelector(".file-upload-btn");
   uploadBtn.classList.remove("dn");
}