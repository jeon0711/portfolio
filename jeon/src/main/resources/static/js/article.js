const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles/');
        }

        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/articles/');
        }

        httpRequest('DELETE',`/api/articles/${id}`, null, success, fail);
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
                let id = params.get('id');

                // 1. FormData 객체 생성
                let formData = new FormData();
                formData.append('title', document.getElementById('title').value);
                formData.append('content', document.getElementById('content').value);

                // 2. 이미지 파일(복수 가능)을 FormData에 추가
                let imagesInput = document.getElementById('images');
                for (let file of imagesInput.files) {
                    // 'images'라는 필드 이름으로 여러 파일 첨부
                    formData.append('images', file);
                }

                // 3. 요청 성공/실패 콜백
                function success() {
                    alert('수정 완료되었습니다.');
                    location.replace(`/articles/${id}`);
                }

                function fail() {
                    alert('수정 실패했습니다.');
                    location.replace(`/articles/${id}`);
                }

                // 4. 멀티파트 요청 전송
                httpRequestMultipart('PUT', `/api/articles/${id}`, formData, success, fail);
            });

}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    createButton.addEventListener('click', event => {
        let formData = new FormData();
                       formData.append('title', document.getElementById('title').value);
                       formData.append('content', document.getElementById('content').value);

                       // 2. 이미지 파일(복수 가능)을 FormData에 추가
                       let imagesInput = document.getElementById('images');
                       for (let file of imagesInput.files) {
                           // 'images'라는 필드 이름으로 여러 파일 첨부
                           formData.append('images', file);
                       }
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles/');
        };
        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles/');
        };
        console.log("before send")
         httpRequestMultipart('POST', `/api/articles/`, formData, success, fail);
    });
}


// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
    console.log("url",url);
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}
function httpRequestMultipart(method, url, formData, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token')
            // 'Content-Type': 'multipart/form-data' 직접 지정 X (FormData 사용 시 자동 생성)
        },
        body: formData
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }

        // 401인 경우 리프레시 토큰 시도
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: refresh_token,
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => {
                    // 새로 받은 accessToken 저장 후, 다시 요청
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestMultipart(method, url, formData, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}