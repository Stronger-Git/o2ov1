$(function () {
    var loading =false;
    // 分页允许返回的最大条数，超过此数则禁止访问后台；实际以后台为准
    var maxItems = 999;
    // 一页返回的最大条数
    var pageSize =10;
    // 获取商品列表的URL
    var listUrl = '/o2o/frontend/listproductbyshop';
    // 页码
    var pageNum = 1;
    // 从地址栏URL里尝试获取 shopId
    var shopId = getParameter("shopId");
    var productCategoryId = '';
    var productName = '';
    // 获取本店铺信息以及商品类别信息列表的URL
    var getShopDetailsUrl = '/o2o/frontend/listshopdetailpageinfo?shopId='+shopId;
    getShopDetails();
    // 预先加载10条店铺信息
    addItems(pageNum, pageSize);
    function getShopDetails() {
        // 及时没有parent也无妨，后台捕获异常
        $.getJSON(getShopDetailsUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-cover-pic').attr('src', getContextPath() + shop.shopImg);
                $('#shop-update-time').html(
                    new Date(shop.lastEditTime).Format('yyyy-MM-dd')
                );
                $('#shop-name').html(shop.shopName);
                $('#shop-desc').html(shop.shopDesc);
                $('#shop-addr').html(shop.shopAddr);
                $('#shop-phone').html(shop.phone);
                var productCategories = data.productCategories;
                var html = '';
                productCategories.map(function (value, index) {
                    html += '<a href="#" class="button col-33" data-product-category-id="'
                            + value.productCategoryId + '">'
                            + value.productCategoryName + '</a>'
                });
                $('#shop-detail-button-div').html(html);
            }
        });
    }
    /**
     * 获取分页展示的产品列表信息
     * @param pageSize
     * @param pageIndex
     */
    function addItems(pageIndex, pageSize) {
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&shopId=' + shopId + '&productCategoryId='
            + productCategoryId + '&productName=' + productName;
        // 设定加载符，若还在后台取数据咋不能再次访问后台，避免多次重复加载
        loading = true;
        // 访问后台获取相应查询条件下的产品列表
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取当前查询条件下产品的总数
                maxItems = data.count;
                var html = '';
                data.products.map(function (value, index) {
                    html += '<div class="card" data-product-id="'
                        + value.productId + '">' + '<div class="card-header">'
                        + value.productName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">'
                        + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">'
                        + '<img src="' + getContextPath() + value.imgAddr + '" width="44px">'
                        + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + value.productDesc
                        + '</div></div></li></ul></div></div><div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(value.lastEditTime).Format('yyyy-MM-dd')
                        + '更新</p>' + '<span>点击查看</span></div>'
                });
                // 将卡片集合添加到目标HTML组件里
                $('.list-div').append(html);
                // 获取目前为止已显示的卡片总数，包含之前已经加载的
                var total = $('.list-div .card').length;
                // 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
                if (total >= maxItems)
                    $('.infinite-scroll-preloader').hide();
                else
                    $('.infinite-scroll-preloader').show();
                // 否则页码加1，继续load出新的店铺卡片
                pageNum += 1;
                // 加载结束，可以再次加载了
                loading = false;
                // 刷新页面显示新加载的店铺
                $.refreshScroller();
            }
        });
    }
    // 下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageNum, pageSize);
    });

    // 点击商品的卡片进入该商品的详情页
    $('.list-div').on('click', '.card', function (e) {
        var productId = e.currentTarget.dataset.productId;
        window.location.href = '/o2o/frontend/productdetail?productId=' + productId;
    });
    // 选择新的产品类别或者区域之后 重置页码 清空原先的产品列表 按照新的信息去查询
    $('#shop-detail-button-div').on('click', '.button',function (e) {
        productCategoryId = e.target.dataset.productCategoryId;
        if (productCategoryId) {
            // 若之前已选定了别的category，则移除其选定效果，改成选定新的
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                productCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            // 由于查询条件改变，清空店铺列表在进行查询
            $('.list-div').empty();
            // 重置页码
            pageNum = 1;
            addItems(pageNum ,pageSize);
            parentId ='';
        }
    });
    $('#search').on('change', function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageNum, pageSize);
    });
    $('#area-search').on('change', function () {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageNum, pageSize);
    });
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });
    $.init();
});


