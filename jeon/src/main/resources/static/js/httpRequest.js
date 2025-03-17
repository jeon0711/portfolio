// httpRequest.js

/**
 * 쿠키 값 가져오기
 */
function getCookie(key) {
  return document.cookie
    .split(";")
    .map(item => item.trim())
    .reduce((acc, item) => {
      const [k, v] = item.split("=");
      return k === key ? v : acc;
    }, null);
}

/**
 * 요청 헤더 설정
 */
function getHeaders(body) {
  let headers = {};
  const token = localStorage.getItem("access_token");

  if (token) {
    headers["Authorization"] = "Bearer " + token;
  }

  // FormData가 아닐 경우에만 JSON 헤더 설정
  if (!(body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }

  return headers;
}

/**
 * 인증 실패 시 토큰 갱신 처리
 */
function handleAuthFailure(response, method, url, body, success, fail) {
  const refreshToken = getCookie("refresh_token");

  // 401 에러 && refreshToken 존재 시 토큰 갱신
  if (response.status === 401 && refreshToken) {
    return fetch("/api/token", {
      method: "POST",
      headers: {
        "Authorization": "Bearer " + localStorage.getItem("access_token"),
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ refreshToken }),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(result => {
        // 새 accessToken 저장 후, 원래 요청 재시도
        localStorage.setItem("access_token", result.accessToken);
        return httpRequest(method, url, body, success, fail);
      })
      .catch(() => fail());
  }

  // 그 외 실패
  return fail();
}

/**
 * 메인 httpRequest 함수
 */
export function httpRequest(method, url, body, success, fail) {
  fetch(url, {
    method: method,
    headers: getHeaders(body),
    body: body instanceof FormData ? body : JSON.stringify(body),
  })
    .then(response => {
      if (response.ok) {
        return success();
      }
      return handleAuthFailure(response, method, url, body, success, fail);
    })
    .catch(() => fail());
}
