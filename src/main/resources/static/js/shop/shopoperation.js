/*
从后台得到数据初始化店铺信息 比如area category
为表单提交按钮添加点击事件
*/
$(function () {
    var shopId = getParameter('shopId');
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var modifyShopUrl = '/o2o/shopadmin/modifyshop?shopId=' + shopId;
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    var isEdit = !!shopId;
    if (!isEdit) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function (value, index) {
                    if (value.areaId == shop.area.areaId) {
                        tempAreaHtml += '<option data-id="' + value.areaId + '" selected>'
                            + value.areaName + '</option>';
                    } else {
                        tempAreaHtml += '<option data-id="' + value.areaId + '">'
                            + value.areaName + '</option>';
                    }
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tempAreaHtml);
            }
        });
    }
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if(data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' +item.shopCategoryId + '">'
                        +item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (value, index) {
                    tempAreaHtml += '<option data-id="' +value.areaId + '">'
                        +value.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }
    $('#submit').click(function () {
        var shop = {};
        if (shopId) {
            shop.shopId =shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {
            shopCategoryId:$('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId:$('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var verifyCode = $('#verify-code').val();
        var formData = new FormData();
        if (!verifyCode) {
            $.toast('验证码为空!');
            return;
        }
        formData.append('verifyCode', verifyCode);
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));
        $.ajax({
           url:isEdit?modifyShopUrl:registerShopUrl,
           type:'POST',
           data:formData,
           contentType:false,
           processData:false,
           cache:false,
           success:function (data) {
               if (data.success) {
                   $.toast('提交成功！');
               } else {
                   $.toast('提交失败！' + data.errMsg);
               }
               $('#verify-code-img').click();
           }
        });
    });
})