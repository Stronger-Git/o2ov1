$(function () {
    var loading =false;
    // 分页允许返回的最大条数，超过此数则禁止访问后台；实际以后台为准
    var maxItems = 999;
    // 一页返回的最大条数
    var pageSize =3;
    // 获取店铺列表的URL
    var listUrl = '/o2o/frontend/listshops';
    // 获取店铺类别列表以及区域列表的URL
    var categoryAndArea = '/o2o/frontend/listshoppageinfo';
    // 页码
    var pageNum = 1;
    // 从地址栏URL里尝试获取 parent_id shopcategory_id
    var parentId = getParameter("parentId");
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';
    // 渲染出店铺类别列表以及区域列表以供搜索
    getCategoryAndArea();
    // 预先加载10条店铺信息
    addItems(pageNum, pageSize);
    function getCategoryAndArea() {
        // 及时没有parent也无妨，后台捕获异常
        var url = categoryAndArea + '?parentId=' + parentId;
        $.getJSON(url, function (data) {
            if (data.success) {
                var shopCategoryList = data.shopCategoryList;
                var html = '';
                // 某个一级类别下的全部类别其实就是显示全部商店
                html += '<a href="#" class="button col-33" data-category-id="">全部类别</a>';
                shopCategoryList.map(function (value, index) {
                    html += '<a href="#" class="button col-33" data-category-id='
                        + value.shopCategoryId +'>'
                        + value.shopCategoryName + '</a>';
                });
                $('#shop-category').html(html);
                var selectOptions = '<option value="">全部街道</option>';
                var areaList = data.areaList;
                areaList.map(function (value, index) {
                    selectOptions += '<option value="'
                            + value.areaId + '">'
                            +value.areaName + '</option>'
                });
                $('#area-search').html(selectOptions);
            }
        });
    }
    /**
     * 获取分页展示的店铺列表信息
     * @param pageSize
     * @param pageIndex
     */
    function addItems(pageIndex, pageSize) {
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&parentId=' + parentId + '&areaId='
            + areaId + '&shopCategoryId=' + shopCategoryId
            + '&shopName=' + shopName;
        // 设定加载符，若还在后台取数据咋不能再次访问后台，避免多次重复加载
        loading = true;
        // 访问后台获取相应查询条件下的店铺列表
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取当前查询条件下的店铺总数
                maxItems = data.count;
                var html = '';
                data.shops.map(function (value, index) {
                    html += '<div class="card" data-shop-id="'
                        + value.shopId + '">' + '<div class="card-header">'
                        + value.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">'
                        + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">'
                        + '<img src="' + getContextPath() + value.shopImg + '" width="44px">'
                        + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + value.shopDesc
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

    // 点击店铺的卡片进入该店铺的详情页
    $('.shop-list').on('click', '.card', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });
    // 选择新的店铺类别或者区域之后 重置页码 清空原先的店铺列表 按照新的信息去查询
    $('#shop-category').on('click', '.button',function (e) {
        // 如果传递过来的是一个父类下的子类
        if (parentId) {
            shopCategoryId = e.target.dataset.categoryId;
            // 若之前已选定了别的category，则移除其选定效果，改成选定新的
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                shopCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            // 由于查询条件改变，清空店铺列表在进行查询
            $('.list-div').empty();
            // 重置页码
            pageNum = 1;
            addItems(pageNum, pageSize);
        } else {
            // 若果传递过来的父类为空，则按照父类查询
            parentId = e.target.dataset.categoryId;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                parentId = '';
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
        shopName = e.target.value;
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


