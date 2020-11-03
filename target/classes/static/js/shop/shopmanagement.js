$(function () {
    var shopId = getParameter('shopId');
    $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' +shopId);
    // 将特定ID下的商铺信息提前保存到session中
    $.get('/o2o/shopadmin/saveShopInfoToSession',{shopId:shopId},function (data) {
        // 因为进入类别管理后点击返回会重新加载页面 导致下面设置属性为null
        // $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' +shopId);
        // 两种办法：一种是get请求返回后重新设置属性 另一种是在后端session中取 采取第一种办法
        if (data.success) {
            $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' + data.data);
        }
    });
});