var main ={
    init : function(){
        var _this = this;
        $('#btn-save').on('click',function(){
            _this.save();
        });
    },
    save : function(){
        var data = {
            title:$('#title').val(),
            author:$('#author').val(),
            content:$('#content').val()
        };

        $.ajax({
            type:"POST",
            url:'/posts',
            dataType:'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function(){
                alert('글이 등록되었습니다.');
                location.reload();
            },
            error :function(error){
                alert(error);
            }
        })
    }
};

main.init();

/*
    JS 첫 문장에 var main = {...}를 선언했습니다.
    굳이 main이라는 변수의 속성으로 function을 추가한 이유는 뭘까요?
    예를 들어 보겠습니다.
    main.js가 아래와 같이 function을 작성한 상황에서
    main.hbs에서 a.js가 추가 된다고 가정하겠습니다.

    var init = function () { .... }; var save = function () { ... }; init();

    여기서 a.js가 init, save function이 없을땐 괜찮은데, a.js도 a.js만의 init function과 save function이 있다면 어떻게 될까요?

    브라우저의 scope는 공용으로 쓰이기 때문에 나중에 불려진 js의 init, save가 먼저 불려진 js의 function을 덮어쓰게 됩니다.

    이런 문제를 피하려고 main.js만의 변수,function 영역으로 var main이란 객체 안에서 function을 선언합니다.
    이렇게 되면 main 객체 안에서만 이름이 유효하기 때문에 다른 JS와 겹칠 위험이 사라집니다.

    프론트엔드의 이런 의존성관리, scope관리 등등의 문제들 때문에 최근에는 JS 개발환경이 급변했습니다.
    Angular, React등은 이미 이런 기능을 프레임워크 레벨에서 지원하고 있습니다.

 */