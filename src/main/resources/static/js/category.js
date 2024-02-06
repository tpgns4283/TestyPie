function fetchCategories() {
    fetch('/api/categories')
        .then(response => response.json())
        .then(data => {
            const categoryList = document.getElementById('categoryList');
            categoryList.appendChild(createCategoryTree(data));
        })
        .catch(error => console.error('Error:', error));
}

function createCategoryTree(categories, parentName = '') {
    let ul = document.createElement('ul');
    for (let category of categories.filter(c => (c.parentName === parentName) || (!parentName && !c.parentName))) {
        let li = document.createElement('li');

        if (!parentName) { // 최상위 카테고리 확인
            li.textContent = category.name; // 텍스트만 추가
        } else {
            // 하위 카테고리에 링크 추가
            let a = document.createElement('a');
            a.textContent = category.name;
            a.href = `/api/category/${encodeURIComponent(parentName)}/${category.id}`; // 링크 설정
            li.appendChild(a);
        }

        // 재귀적으로 하위 카테고리 처리
        let childrenUl = createCategoryTree(categories, category.name);
        if (childrenUl.children.length > 0) {
            li.appendChild(childrenUl);
        }
        ul.appendChild(li);
    }
    return ul;
}

document.addEventListener('DOMContentLoaded', function() {
    fetchCategories();
});

$(document).ready(function() {
    $('#submitBtn').click(function () {
        var token = localStorage.getItem('jwtToken');
        if (token) {
            fetch('/api/user/profile', {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            })
                .then(response => response.json())
                .then(data => {
                    // 서버로부터 받은 사용자의 account 정보를 사용하여 URL을 설정
                    console.log("DATA ::::::::::::::::::::"+ data.account)
                    if (data && data.account) {
                        window.location.href = '/api/users/' + data.account; // 사용자를 해당 URL로 리다이렉트
                    }
                })
        } else {
            alert("로그인 후 이용해주세요");
            window.location.href = '/login';

        }
    });
});
function checkLoginStatusAndUpdateLink() {
    var loginLink = document.getElementById("loginLink");

    loginLink.onclick = function() {
        var jwtToken = localStorage.getItem("jwtToken");

        // 로그인 상태가 아닐 경우 로그인 페이지로 이동
        if (!jwtToken) {
            window.location.href = "/login";
            return false;
        }

        // 로그인 상태일 경우 서버에 로그아웃 요청
        $.ajax({
            type: "DELETE",
            url: "/api/users/logout",
            xhrFields: {
                withCredentials: true // 쿠키를 포함하도록 설정
            },
            success: function(response) {
                // 로그아웃 성공 시 로컬 스토리지에서 계정 정보 제거
                localStorage.removeItem("account");
                localStorage.removeItem("jwtToken");

                alert("로그아웃 되었습니다");
                window.location.href = "/";
                loginLink.textContent = "로그인";
                loginLink.href = "/login";
            },
            error: function(xhr, status, error) {
                console.log("로그아웃 실패:", error);
            }
        });

        return false;
    };

    updateLinkText();
}

function updateLinkText() {
    var loginLink = document.getElementById("loginLink");
    var jwtToken = localStorage.getItem("jwtToken");

    if (jwtToken) {
        loginLink.textContent = "로그아웃";
        loginLink.href = "#";
    } else {
        loginLink.textContent = "로그인";
        loginLink.href = "/login";
    }
}
function checkUserhasAccessToken(){

    if (!localStorage.getItem("jwtToken")){
        alert("로그인 후 이용해주세요")
        window.location.href = '/login';
    }
}

window.onload = checkLoginStatusAndUpdateLink;






