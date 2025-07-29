window.addEventListener("DOMContentLoaded", function() {
<script>
    function toggleDropdown(id){
        const e1 = document.getElementById(id);
        if (e1.style.display === "none"){
            e1.style.display = "block";
        }else {
            e1.style.display = "none";
        }
    }
    });
    function se (id){

    // select 요소 참조
    const sc = document.querySelect('searchType');

    // 값을 가지고 오기

    const value =  element.vlaue;

    // 상태 화면에 표시 하기
    const log = `선택된 값은 ${value} 입니다`;
    document.querySelect('.log').innerHTML = log;


    }
</script>