$(function () {
    var loginUrl='/o2o/local/logincheck';
    var userType = getParameter("userType");
    var errorCount = 0;
    var needVerify = false;
    $('#submit').click(function () {
        var verifyCode = null;
        if (needVerify == true) {
            verifyCode = $('#verify-code').val();
        }
        var username=$('#username').val();
        var password=$('#password').val();
        $.ajax({
            type:'post',
            url:loginUrl,
            cache:false,
            async:false,
            data:{username:username,password:password,needVerify:needVerify,verifyCode:verifyCode},
            dataType:'json',
            success:function (data) {
                if (data.success) {
                    if (userType == 1) {
                        window.location.href='/o2o/frontend/index';
                    } else {
                        window.location.href='/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast(data.errorMsg);
                    errorCount++;
                    console.log(errorCount);
                    if (errorCount >= 3) {
                        $('#verify-code-img').click();
                        $('#li-verify-code').show();
                        needVerify = true;
                    }
                }
            }
        });
    });
})