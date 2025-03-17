import { httpRequest } from "./httpRequest.js";
document.addEventListener("DOMContentLoaded", function () {
    const modifyButton = document.getElementById("modify-btn");
    const editForm = document.getElementById("edit-form");
    const saveButton = document.getElementById("save-btn");

    const addSkillButton = document.getElementById("add-skill");
    const newSkillInput = document.getElementById("new-skill");
    const skillsList = document.getElementById("skills-list");

    const addLinkButton = document.getElementById("add-link");
    const newLinkInput = document.getElementById("new-link");
    const linksList = document.getElementById("links-list");

    // "프로필 수정" 버튼 클릭 시 폼 표시
    if (modifyButton) {
        modifyButton.addEventListener("click", function () {
            editForm.classList.toggle("hidden");
        });
    }

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

    // 외부 링크 업데이트 함수
    function updateLinks() {
        let links = Array.from(document.querySelectorAll(".link-item a"))
                         .map(link => link.href.trim())
                         .filter(link => link.length > 0);

        console.log("✅ 최신 links 목록:", links);
        return links;
    }

    // 새로운 링크 추가 함수
    function addLink(linkURL) {
        if (!linkURL.trim()) return;

        if (!/^https?:\/\//.test(linkURL)) {
            alert("올바른 URL 형식을 입력해주세요 (http:// 또는 https:// 필요)");
            return;
        }

        if (!Array.from(document.querySelectorAll(".link-item a")).some(a => a.href === linkURL)) {
            const li = document.createElement("li");
            li.className = "link-item";
            li.innerHTML = `<a href="${linkURL}" target="_blank">${linkURL}</a>
                            <button type="button" class="btn btn-danger btn-sm ml-2 remove-link">X</button>`;

            linksList.appendChild(li);

            li.querySelector(".remove-link").addEventListener("click", function () {
                li.remove();
                updateLinks();
            });
        }

        updateLinks();
    }

    // "링크 추가" 버튼 클릭 시 입력창 표시
    if (addLinkButton) {
        addLinkButton.addEventListener("click", function () {
            newLinkInput.classList.remove("hidden");
            newLinkInput.focus();
        });

        newLinkInput.addEventListener("keypress", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                addLink(newLinkInput.value);
                newLinkInput.value = "";
            }
        });
    }

    // 기존 링크 삭제 버튼 이벤트 추가
    document.querySelectorAll(".remove-link").forEach(button => {
        button.addEventListener("click", function () {
            this.parentElement.remove();
            updateLinks();
        });
    });

    // "저장" 버튼 클릭 시 프로필 업데이트 요청
    if (saveButton) {
        saveButton.addEventListener("click", function () {
            let formData = new FormData();
            formData.append("title", document.getElementById("title").value.trim());
            formData.append("content", document.getElementById("content").value.trim());
            formData.append("name", document.getElementById("name").value.trim());
            formData.append("phone", document.getElementById("phone").value.trim());

            let id = document.getElementById("profile-author").innerText.trim();
            console.log("id:", id);
            formData.append("author", id);

            let imageFile = document.getElementById("image").files[0];
            if (imageFile) {
                formData.append("image", imageFile);
            }

            // 최신 기술 스택 추가
            updateSkills().forEach(skill => formData.append("skills", skill));

            // 최신 외부 링크 추가
            updateLinks().forEach(link => formData.append("externalUrls", link));

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

});
