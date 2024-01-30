// 모달을 가져옵니다
var modal = document.getElementById("userModal");
var span = document.getElementsByClassName("close")[0];

// 모달을 닫는 기능
span.onclick = function() {
    modal.style.display = "none";
}

// 모달 바깥쪽을 클릭하면 모달을 닫는 기능
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// AJAX 요청을 통해 사용자 프로필을 가져오는 함수
function fetchUserProfile(account) {
    console.log("account : " + account)
    $.ajax({
        url: '/api/user/' + account,
        type: 'GET',
        success: function(data) {
            updateModalWithProfileData(data);
        },
        error: function(error) {
            console.error('Error fetching profile data:', error);
        }
    });
}

// 모달에 사용자 프로필 데이터를 적용하는 함수
function updateModalWithProfileData(profile) {
    document.querySelector('#userModal .nickname').innerHTML = '<b>닉네임:</b> ' + profile.nickname;
    document.querySelector('#userModal .introduction').innerHTML = '<b>자기소개:</b> ' + profile.description;
    // document.querySelector('#userModal img').src = profile.file || 'profile.jpg'; // 기본 이미지 경로를 설정할 수 있음
    modal.style.display = 'block';
}
