$(function () {
    var updateUrl = '/o2o/local/updatelocalpwd';
    var userType = getParameter('userType');
    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        var newPassword = $('#newPassword').val();
        var confirmPwd = $('#confirmPwd').val();
        var verifyCode = $('#verify-code').val();
        if (newPassword != confirmPwd) {
            $.toast('新密码和确认密码不一致！');
            return;
        }
        $.ajax({
            type:'POST',
            url:updateUrl,
            cache:false,
            async:false,
            dataType:'json',
            data:{username:username,password:password,newPassword:newPassword,verifyCode:verifyCode},
            success:function (data) {
                if (data.success) {
                    $.toast('更新成功！');
                    if (userType == 1) {
                        window.location.href='/o2o/frontend/index';
                    } else {
                        window.location.href='/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast(data.errorMsg);
                    $('#verify-code-img').click();
                }

            }
        });
    });
})