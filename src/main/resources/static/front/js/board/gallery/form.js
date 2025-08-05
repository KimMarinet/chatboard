window.addEventListener("DOMContentLoaded", function() {
    // 위지윅 에디터 로드 S
    const { loadEditor } = commonLib;
    const el = document.getElementById("content");
    loadEditor(el)
        .then(editor => {
            window.editor = editor;
        })
    // 위지윅 에디터 로드 E

    // 파일 삭제 이벤트 처리
    const { fileManager, insertEditorImage } = commonLib;
    const removeEls = document.querySelectorAll(".file-items .remove");
    removeEls.forEach(el => {
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
    });

    // 이미지를 에디터 본문 추가 이벤트 처리
    const insertEditorEls = document.querySelectorAll(".file-items .insert-editor");
    insertEditorEls.forEach(el => {
        el.addEventListener("click", function() {
            const { fileUrl } = this.dataset;
            insertEditorImage(fileUrl);
        });
    });
});


/**
* 파일 업로드 후속 처리
*
*/
function fileUploadCallback(items) {
    if (!items || items.length === 0) return;

    const { insertEditorImage } = commonLib;

    const editorTarget = document.querySelector(".editor-files");
    const attachTarget = document.querySelector(".attach-files");

    const editor_tpl = document.getElementById("editor-tpl").innerHTML;
    const attach_tpl = document.getElementById("attach-tpl").innerHTML;

    const domParser = new DOMParser();

    const editorImages = [];
    for (const {seq, fileUrl, location, fileName } of items) {
        let targetEl, tpl;
        switch (location) {
            case "editor":
                editorImages.push(fileUrl);
                targetEl = editorTarget;
                tpl = editor_tpl;
                break;
            default:
                targetEl = attachTarget;
                tpl = attach_tpl;
        }

        insertEditorImage(editorImages); // 에디터에 이미지 추가

        // 템플릿 치환
        tpl = tpl.replace(/\[seq\]/g, seq)
                .replace(/\[fileName\]/g, fileName)
                .replace(/\[fileUrl\]/g, fileUrl);
        const dom = domParser.parseFromString(tpl, "text/html");
        const fileItem = dom.querySelector(".file-items");

        targetEl.append(fileItem);

        const removeEl = fileItem.querySelector(".remove");
        if (removeEl) {
            const { fileManager } = commonLib;

            removeEl.addEventListener("click", function () {
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

        const insertEditorEl = fileItem.querySelector(".insert-editor");
        if (insertEditorEl) {
            insertEditorEl.addEventListener("click", () => insertEditorImage(fileUrl));
        }
    }
}

// 파일 삭제 후 후속처리
function fileDeleteCallback({seq}) {
    const el = document.getElementById(`file-${seq}`);
    if (el) el.parentElement.removeChild(el);
}