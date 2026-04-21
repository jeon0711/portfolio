import { httpRequest } from "./httpRequest.js";

document.addEventListener("DOMContentLoaded", function () {
    const addSkillButton = document.getElementById("add-skill");
    const newSkillInput = document.getElementById("new-skill");
    const skillsList = document.getElementById("skills-list");
    const deleteImagesContainer = document.getElementById("delete-images-container");

    // [추가] 삭제할 이미지 ID를 저장할 배열
    let deletedImageIds = [];

    // 기술 스택 업데이트 함수
    function updateSkills() {
        let skills = Array.from(document.querySelectorAll(".skill-item span"))
            .map(span => span.innerText.trim())
            .filter(skill => skill.length > 0);
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

    if (addSkillButton) {
        addSkillButton.addEventListener("click", () => {
            newSkillInput.classList.remove("hidden");
            newSkillInput.focus();
        });
        newSkillInput.addEventListener("keypress", (event) => {
            if (event.key === "Enter") {
                event.preventDefault();
                addSkill(newSkillInput.value);
                newSkillInput.value = "";
            }
        });
    }

    // 기존 기술 삭제 버튼 이벤트
    document.querySelectorAll(".remove-skill").forEach(button => {
        button.addEventListener("click", function () {
            this.parentElement.remove();
            updateSkills();
        });
    });

    // [추가] 기존 업로드된 이미지 삭제 이벤트 바인딩
    document.querySelectorAll('.delete-existing-img').forEach(button => {
        button.addEventListener('click', function() {
            const imageId = this.getAttribute('data-id');
            // 리스트에 추가
            deletedImageIds.push(imageId);
            // 화면에서 제거
            this.closest('.existing-img-wrapper').remove();
            console.log("🗑️ 삭제 대기 이미지 ID:", deletedImageIds);
        });
    });

    // 삭제 기능 (게시글 전체 삭제)
    const deleteButton = document.getElementById('delete-btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', () => {
            let id = document.getElementById('article-id').value;
            if (confirm("정말 삭제하시겠습니까?")) {
                const success = () => { alert("삭제가 완료되었습니다."); location.replace("/synthesis/"); };
                const fail = () => { alert("삭제 실패했습니다."); location.replace("/synthesis/"); };
                httpRequest("DELETE", `/api/articles/${id}`, null, success, fail);
            }
        });
    }

    // 수정 기능
    const modifyButton = document.getElementById('modify-btn');
    if (modifyButton) {
        modifyButton.addEventListener('click', () => {
            let id = document.getElementById('article-id').value;
            let formData = new FormData();

            formData.append('title', document.getElementById('title').value);
            formData.append('content', document.getElementById('content').value);

            // 새 이미지 파일 추가
            let imagesInput = document.getElementById('images');
            for (let file of imagesInput.files) {
                formData.append('images', file);
            }

            // 기술 스택 추가
            updateSkills().forEach(skill => formData.append("skills", skill));

            // [추가] 삭제할 이미지 ID 리스트를 FormData에 추가
            deletedImageIds.forEach(id => formData.append("deleteImageIds", id));

            const success = () => {
                alert('수정 완료되었습니다.');
                location.replace(`/articles/${id}`);
            };
            const fail = () => {
                alert('수정 실패했습니다.');
                location.replace(`/articles/${id}`);
            };

            httpRequest('PUT', `/api/articles/${id}`, formData, success, fail);
        });
    }

    // 생성 기능
    const createButton = document.getElementById('create-btn');
    if (createButton) {
        createButton.addEventListener('click', () => {
            let formData = new FormData();
            formData.append('title', document.getElementById('title').value);
            formData.append('content', document.getElementById('content').value);

            let imagesInput = document.getElementById('images');
            for (let file of imagesInput.files) {
                formData.append('images', file);
            }
            updateSkills().forEach(skill => formData.append("skills", skill));

            const success = () => { alert('등록 완료되었습니다.'); location.replace('/articles/'); };
            const fail = () => { alert('등록 실패했습니다.'); location.replace('/articles/'); };

            httpRequest('POST', `/api/articles/`, formData, success, fail);
        });
    }
});