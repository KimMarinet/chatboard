var commonLib = {
    // ajax 공통 기능
    ajaxLoad(url, successCallback, failureCallback, method = 'GET', body, header, isText = false) {
        url = commonLib.getUrl(url);

        const options = {
            method,
        }

        // body 데이터 처리, POST, PUT, PATCH 일때만 추가
        method = method.toUpperCase();
        if (typeof body === 'string') body = body.trim();

        if (['POST', 'PUT', 'PATCH'].includes(method) && body) {
            options.body = body;
        }

        // 요청 헤더 처리
        header = header ?? {};

        // csrf 토큰 처리
        // 세션에 저장된 토큰과 요청에 포함된 토큰을 비교하여 보안강화
        const csrfToken = document.querySelector("meta[name='csrf_token']").content;
        const csrfHeader = document.querySelector("meta[name='csrf_header']").content;
        header[csrfHeader] = csrfToken;
        options.headers = header;

        // ajax 요청 처리
        fetch(url, options)
            .then(res => isText ? res.text() : res.json())
            .then(data => {
                // 성공시 후속 처리
                if (typeof successCallback === 'function') {
                    // successCallback은 콜백함수 : 성공 후 바로 실행하기 위해 사용 (자바스크립트가 비동기기 때문에)
                    successCallback(data);
                }
            })
            .catch(e => {
                // 실패시 후속 처리
                if (typeof failureCallback === 'function') {
                    // failureCallback도 콜백함수 : 실패 후 바로 실행하기 위해 사용
                    failureCallback(e);
                }
            });
    },
    /**
    * ContextPath 기준 경로
    * 잘못된 주소로 요청하지 않기 위해 사용
    */
    getUrl(url) {
        let baseUrl = document.querySelector("meta[name='base_url']").content;
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));

        return url ? baseUrl + url : baseUrl;
    }
}


// 에디터 공통처리 부분
commonLib.loadEditor = function(el, height = 350) {
    if (!ClassicEditor || !el) {
        return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
        (async () => {
            try {
                const editor = await ClassicEditor.create(el);
                resolve(editor);

                editor.editing.view.change((writer) => {
                    writer.setStyle(
                        "height", `${height}px`,
                        editor.editing.view.document.getRoot()
                    );
                });

            } catch (err) {
                console.error(err);
                reject(err);
            }
        })();
    });
};

// source -> 이미지 경로
commonLib.insertEditorImage = function(source, editor) {
    editor = editor ?? window.editor;
    if (!editor || !source || (Array.isArray(source) && source.length === 0)) return;

    source = Array.isArray(source) ? source : [source];

    editor.execute('insertImage', { source })

};

// SweetAlert2 처리
window.alert = function(message, callback) {
    parent.Swal.fire({
      title: message,
      icon: "warning"
    }).then(() => {
        if (typeof callback === 'function') {
            callback();
        }
    })
};