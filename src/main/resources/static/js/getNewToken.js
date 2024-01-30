$(document).ready(function() {
    refreshAccessToken();
});

function refreshAccessToken() {
    if(localStorage.getItem("jwtToken") || localStorage.getItem("account"))
    $.ajax({
        type: "POST",
        url: "/api/users/refresh",
        xhrFields: {
            withCredentials: true // 쿠키를 포함하도록 설정
        },
        success: function(response, textStatus, xhr) {
            // JWT 토큰을 응답 헤더에서 추출
            var token = xhr.getResponseHeader("Authorization");
            if (token) {
                localStorage.setItem("jwtToken", token);
            }
        },
        error: function(xhr, status, error) {
        }
    });
}

