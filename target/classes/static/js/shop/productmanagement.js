$(function () {
    // 获取此店铺下的商品列表的URL
    var listUrl = '/o2o/shopadmin/getallproducts?pageIndex=1&pageSize=999';
    var statusUrl = '/o2o/shopadmin/modifyproduct';
    getList();
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.products;
                var tempHtml = '';
                /*
                    遍历每条商品信息，拼接成一行显示，列信息包括：
                    商品名称，优先级，上架\下架 编辑按钮 预览
                 */
                productList.map(function (value, index) {
                    var textOp = '下架';
                    var contraryStatus = 0;
                    if (value.enableStatus == 0) {
                        textOp = '上架';
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '<div class="row row-product">'
                        + '<div class="col-33">'
                        + value.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + value.priority
                        + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="'
                        + value.productId
                        + '" data-status="'
                        + value.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="status" data-id="'
                        + value.productId
                        + '" data-status="'
                        + contraryStatus
                        + '" >'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + value.productId
                        + '" data-status="'
                        + value.enableStatus
                        + '" >预览</a>'
                        + '</div>'
                        + '</div>';
                });
            }
            $('.product-wrap').html(tempHtml);
        })
    }
    $('.product-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            // 如果有类属性值 edit则点击就进入某店铺下商品为指定ID的商品信息编辑界面
            window.location.href = '/o2o/shopadmin/productoperation?productId='
                +e.currentTarget.dataset.id;
        } else if (target.hasClass('status')) {
            // 如果有类属性值 status则调用后台功能上下架相关商品
            changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status);
        } else if(target.hasClass('preview')) {
            // 如果有类属性值 preview则去前台展示系统该商品详情页预览商品情况
            window.location.href = '/o2o/frontend/productdetail?productId='
                +e.currentTarget.dataset.id;
        }
    });
    function changeItemStatus(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定下架？', function () {
            $.ajax({
                url:statusUrl,
                type:'post',
                data:{
                    product:JSON.stringify(product),
                    statusChange:true
                },
                dataType:'json',
                success:function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            })
        })
    }

})