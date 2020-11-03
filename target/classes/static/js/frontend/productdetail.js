$(function () {
    var productId = getParameter('productId');
    var productUrl = '/o2o/frontend/listproductdetail?productId=' + productId;
    $.getJSON(productUrl, function (data) {
        if (data.success) {
            var product = data.product;
            $('#product-img').attr('src', getContextPath() + product.imgAddr);
            $('#product-time').text(new Date(product.lastEditTime).Format('yyyy-MM-dd'));
            $('#product-name').text(product.productName);
            $('#product-desc').text(product.productDesc);
            // 商品价格展示逻辑，主要判断原价现价是否为空，所有都为空则不显示价格栏目
            if (product.normalPrice != undefined && product.promotionPrice != undefined) {
                // 如果现价和原价都不为空则都显示，并给原价添加个删除符号
                $('#price').show();
                $('#normal-price').html('<del>￥' + product.normalPrice + '</del>');
                $('#promotion-price').text('￥' + product.promotionPrice);
            } else if (product.normalPrice != undefined && product.promotionPrice == undefined) {
                // 如果原价不为空而现价为空shop则只展示原价
                $('#price').show();
                $('#promotion-price').text('￥' + product.normalPrice);
            } else if (product.normalPrice == undefined && product.promotionPrice != undefined) {
                $('#promotion-price').text('￥' + product.promotionPrice);
            }
            var imgListHtml = '';
            product.productImgList.map(function (value, index) {
                imgListHtml += '<div> <img src="' + getContextPath() + value.imgAddr
                        + '"width="100%" /></div>';
            });
            $('#img-list').html(imgListHtml);
        }
    });
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });
    $.init();
})