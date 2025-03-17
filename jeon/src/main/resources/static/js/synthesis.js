import { httpRequest } from "./httpRequest.js";

document.addEventListener("DOMContentLoaded", () => {
    // 모든 삭제 버튼 가져오기
    const deleteButtons = document.querySelectorAll("#delete-btn");

    deleteButtons.forEach(button => {
        button.addEventListener("click", event => {
            let id = button.getAttribute("data-id"); // 클릭된 버튼의 data-id 값 가져오기

            if (!id) {
                alert("삭제할 ID를 찾을 수 없습니다.");
                return;
            }

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
        });
    });
});