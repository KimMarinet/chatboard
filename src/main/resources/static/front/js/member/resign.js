document.addEventListener("DOMContentLoaded", function () {
  const personResign = document.getElementById("personResign");
  const resignForm = document.getElementById("resignForm");

  if (personResign && resignForm) {
    personResign.addEventListener("click", function () {
      Swal.fire({
        title: "정말 탈퇴하시겠습니까?",
        text: "탈퇴시 재가입이 어려울 수 있습니다.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#aaa",
        confirmButtonText: "탈퇴",
        cancelButtonText: "취소"
      }).then((result) => {
        if (result.isConfirmed) {
          resignForm.submit();
        }
      });
    });
  }
});