$(function () {
    var productId = getParameter('productId');
    var infoUrl = '/o2o/shopadmin/getproduct?productId=' + productId;
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
    var productPostUrl = '/o2o/shopadmin/modifyproduct';
    // 商品添加和编辑使用的是同一个页面，由该标识符来标明本次是添加还是编辑操作
    var isEdit = false;
    if (productId) {
        // 若有productId则为编辑操作
        getInfo(productId);
        isEdit = true;
    } else {
        // 添加 初始化 获取该店铺下的所有产品类别 并渲染到页面上
        getCategory();
        productPostUrl = '/o2o/shopadmin/addproduct';
    }
    // 获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                // 获取原本的商品类别以及该店铺的所有商品类别列表
                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                optionArr.map(function (item, index) {
                    var isSelect = optionSelected == item.productCategoryId?'selected':'';
                    optionHtml += '<option data-value="' + item.productCategoryId+'"' + isSelect + '>' + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }
    // 为商品添加操作提供该店铺下的所有商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId +'">'
                        +item.productCategoryName +'</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }
    // 商品详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片）
    // 且控件总数未达到6个，则生成新的一个文件上传控件
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    })
    $('#submit').click(function () {
        var product = {};
        product.productId=productId;
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        product.productCategory = {
            productCategoryId: $('#category').find('option').not(
                function () {
                    return !this.selected;
                }
            ).data('value')
        };
        // 获取缩略图文件流
        var thumbnail = $('#small-img')[0].files[0];
        var formData = new FormData();
        formData.append('thumbnail', thumbnail);
        // 遍历商品详情图控件，获取里面的文件流
        $('.detail-img').map(function (index, value){
            if ($('.detail-img')[index].files.length > 0) {
                // 将第i个文件流赋值给key为productImg的表单键值对里
                formData.append('productImg' + index,
                    $('.detail-img')[index].files[0]);
            }
        });
        formData.append('product', JSON.stringify(product));
        var verifyCode = $('#verify-code').val();
        if (!verifyCode) {
            $.toast('验证码为空!');
            return;
        }
        formData.append('verifyCode', verifyCode);
        $.ajax({
            url:productPostUrl,
            type:'post',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    $('#verify-code-img').click();
                } else {
                    $.toast('提交失败！');
                    $('#verify-code-img').click();
                }
            }
        })
    });
})