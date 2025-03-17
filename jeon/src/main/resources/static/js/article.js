import { httpRequest } from "./httpRequest.js";
document.addEventListener("DOMContentLoaded", function () {

    const addSkillButton = document.getElementById("add-skill");
    const newSkillInput = document.getElementById("new-skill");
    const skillsList = document.getElementById("skills-list");

    const addLinkButton = document.getElementById("add-link");



    // 기술 스택 업데이트 함수
    function updateSkills() {
        let skills = Array.from(document.querySelectorAll(".skill-item span"))
                          .map(span => span.innerText.trim())
                          .filter(skill => skill.length > 0);

        console.log("✅ 최신 skills 목록:", skills);
        return skills;
    }

    // 새로운 기술 추가 함수
    function addSkill(skillName) {
        if (!skillName.trim()) return;

        let skillArray = skillName.split(/\s*,\s*|\s+/).map(skill => skill.trim()).filter(skill => skill.length > 0);

        skillArray.forEach(skill => {
            if (!Array.from(document.querySelectorAll(".skill-item span")).some(span => span.innerText === skill)) {
                const li = document.createElement("li");
                li.className = "skill-item";
                li.innerHTML = `<span>${skill}</span>
                                <button type="button" class="btn btn-danger btn-sm ml-2 remove-skill">X</button>`;

                skillsList.appendChild(li);

                li.querySelector(".remove-skill").addEventListener("click", function () {
                    li.remove();
                    updateSkills();
                });
            }
        });

        updateSkills();
    }

    // "기술 추가" 버튼 클릭 시 입력창 표시
    if (addSkillButton) {
        addSkillButton.addEventListener("click", function () {
            newSkillInput.classList.remove("hidden");
            newSkillInput.focus();
        });

        newSkillInput.addEventListener("keypress", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                addSkill(newSkillInput.value);
                newSkillInput.value = "";
            }
        });
    }

    // 기존 기술 삭제 버튼 이벤트 추가
    document.querySelectorAll(".remove-skill").forEach(button => {
        button.addEventListener("click", function () {
            this.parentElement.remove();
            updateSkills();
        });
    });

const deleteButton = document.getElementById('delete-btn');//첫번째 삭제버튼 가져오게된다.

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        if (confirm("정말 삭제하시겠습니까?")) {
                       function success() {
                           alert("삭제가 완료되었습니다.");
                           location.replace("/synthesis/");
                       }

                       function fail() {
                           alert("삭제 실패했습니다.");
                           location.replace("/synthesis/");
                       }

                       httpRequest("DELETE", `/api/articles/${id}`, null, success, fail);
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
                updateSkills().forEach(skill => formData.append("skills", skill));
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
                httpRequest('PUT', `/api/articles/${id}`, formData, success, fail);
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
                        updateSkills().forEach(skill => formData.append("skills", skill));
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles/');
        };
        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles/');
        };
        console.log("before send")
         httpRequest('POST', `/api/articles/`, formData, success, fail);
    });
}


    });