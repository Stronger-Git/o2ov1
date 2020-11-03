/*通用js组件*/
function changeVerifyCode(img) {
    img.src = "../kaptcha";
}

function getContextPath() {
    return "/o2o/";
}
/*根据传递过来的参数name获取对应的值*/
function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
    var r = location.search.substr(1).match(reg);
    if (r!=null) return (r[2]); return null;
}
Date.prototype.Format = function (fmt) {
    var o = {
        'M+':this.getMonth()+1,
        'd+':this.getDate(),
        'h+':this.getHours(),
        'm+':this.getMinutes(),
        's+':this.getSeconds(),
        'q+':Math.floor((this.getMonth() + 3)/3),// 季度
        'S':this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1)?(o[k])
                :(("00" + o[k]).substr((""+o[k]).length)));
    }
    return fmt;
}