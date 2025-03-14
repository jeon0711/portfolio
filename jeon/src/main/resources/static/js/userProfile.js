document.addEventListener("DOMContentLoaded", function () {
    const modifyButton = document.getElementById("modify-btn");
    const editForm = document.getElementById("edit-form");
    const saveButton = document.getElementById("save-btn");
    const addSkillButton = document.getElementById("add-skill");
    const newSkillInput = document.getElementById("new-skill");
    const skillsList = document.getElementById("skills-list");

    // "프로필 수정" 버튼 클릭 시 폼 표시
    if (modifyButton) {
        modifyButton.addEventListener("click", function () {
            editForm.classList.toggle("hidden");
        });
    }
  // 기술 스택 업데이트 함수
    function updateSkills() {
        let skills = Array.from(document.querySelectorAll(".skill-item span"))
                          .map(span => span.innerText.trim()) // 공백 제거
                          .filter(skill => skill.length > 0); // 빈 값 제거

        console.log("✅ 최신 skills 목록:", skills);
        return skills; // 저장 시 활용 가능
    }

    // 새로운 기술을 리스트에 추가하는 함수
    function addSkill(skillName) {
       if (!skillName.trim()) return; // 빈 값 입력 방지

           // 🔹 입력값을 공백과 쉼표(`,`)를 기준으로 분리하고, 공백 제거 후 필터링
           let skillArray = skillName.split(/\s*,\s*|\s+/).map(skill => skill.trim()).filter(skill => skill.length > 0);

           skillArray.forEach(skill => {
               // 중복 추가 방지
               if (!Array.from(document.querySelectorAll(".skill-item span")).some(span => span.innerText === skill)) {
                   const li = document.createElement("li");
                   li.className = "skill-item";
                   li.innerHTML = `<span>${skill}</span>
                                   <button type="button" class="btn btn-danger btn-sm ml-2 remove-skill">X</button>`;

                   skillsList.appendChild(li);

                   // 삭제 버튼 기능 추가
                   li.querySelector(".remove-skill").addEventListener("click", function () {
                       li.remove();
                       updateSkills();
                   });
               }
           });

           updateSkills();
    }

    // "기술 스택 추가" 버튼 클릭 시 입력창 표시
    if (addSkillButton) {
        addSkillButton.addEventListener("click", function () {
            newSkillInput.classList.remove("hidden");
            newSkillInput.focus();
        });

        // Enter 키 입력 시 새로운 기술 추가
        newSkillInput.addEventListener("keypress", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                addSkill(newSkillInput.value);
                newSkillInput.value = ""; // 입력창 초기화
            }
        });
    }

    // 기존 기술 스택 삭제 버튼 이벤트 추가
    document.querySelectorAll(".remove-skill").forEach(button => {
        button.addEventListener("click", function () {
            this.parentElement.remove();
            updateSkills();
        });
    });

    // "저장" 버튼 클릭 시 프로필 업데이트 요청
    if (saveButton) {
        saveButton.addEventListener("click", function () {


            let formData = new FormData();
            formData.append("title", document.getElementById("title").value.trim());
            formData.append("content", document.getElementById("content").value.trim());
            formData.append("name", document.getElementById("name").value.trim());
         let id = document.getElementById("profile-author").innerText.trim();
                        console.log("id:",id);
           formData.append("author", id);
            // 파일이 선택된 경우 추가
            let imageFile = document.getElementById("image").files[0];
            if (imageFile) {
                formData.append("image", imageFile);
            }

            // 최신 기술 스택 가져와 추가 (문자열 변환 시 JSON 포맷 유지)
       let skillElements = document.querySelectorAll(".skill-item span");
                   let skills = Array.from(skillElements)
                                     .map(span => span.textContent.trim()) // 공백 제거
                                     .filter(skill => skill.length > 0); // 빈 값 제거

                   console.log("✅ 변환된 skills 배열:", skills);

                   // 🔹 배열 데이터를 FormData에 추가 (배열로 전송)
                   skills.forEach(skill => formData.append("skills", skill));

                   console.log("📤 최종 전송될 FormData:", Array.from(formData.entries()));

            function success() {
                alert("수정 완료되었습니다.");
                location.replace(`/profile/${id}`);
            }

            function fail() {
                alert("수정 실패했습니다.");
                location.replace(`/profile/${id}`);
            }

            // HTTP 요청 실행 (PUT 요청)
            httpRequest("PUT", `/api/profile/`, formData, success, fail);
        });
    }
    // HTTP 요청을 보내는 함수
    function httpRequest(method, url, body, success, fail) {
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

    // 요청 헤더 설정 함수
    function getHeaders(body) {
        let headers = {};
        const token = localStorage.getItem("access_token");

        if (token) {
            headers["Authorization"] = "Bearer " + token;
        }

        if (!(body instanceof FormData)) {
            headers["Content-Type"] = "application/json";
        }

        return headers;
    }

    // 쿠키를 가져오는 함수
    function getCookie(key) {
        return document.cookie.split(";").map(item => item.trim()).reduce((acc, item) => {
            const [k, v] = item.split("=");
            return k === key ? v : acc;
        }, null);
    }

    // 인증 실패 시 토큰 갱신 함수
    function handleAuthFailure(response, method, url, body, success, fail) {
        const refreshToken = getCookie("refresh_token");

        if (response.status === 401 && refreshToken) {
            return fetch("/api/token", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ refreshToken }),
            })
            .then(res => res.ok ? res.json() : Promise.reject())
            .then(result => {
                localStorage.setItem("access_token", result.accessToken);
                return httpRequest(method, url, body, success, fail);
            })
            .catch(() => fail());
        }

        return fail();
    }
});
