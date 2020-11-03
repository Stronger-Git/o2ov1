$(function () {
    var bindUrl = '/o2o/local/bindlocalauth'
    var userType = getParameter("userType");
    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        var verifyCode = $('#verify-code').val();
        if (!verifyCode) {
            $.toast('验证码为空！');
            return;
        }
        $.ajax({
            type:'POST',
            async:false,
            cache:false,
            url:bindUrl,
            dataType:'json',
            data:{username:username,password:password,verifyCode:verifyCode},
            success:function (data) {
                if (data.success) {
                    $.toast('绑定成功！');
                    // 绑定成功后，根据用户类型跳转到不同的页面
                    // 若用户在前端展示系统则则回退到前端展示系统页面
                    // 若用户是在店家管理系统页面则自动会退到店铺列表页中
                    if (userType == 1) {
                        window.location.href='/o2o/frontend/index';
                    } else {
                        window.location.href='/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast('绑定失败！');
                    alert(data.errorMsg);
                    $('#verify-code-img').click();
                }
            }
        })

    })

})