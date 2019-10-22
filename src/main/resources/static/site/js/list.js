$(document).ready(function () {
    $("#classifyList").load(site.classifyList, function (res, status) {
        if (status === 'success') {
            $("#classify-0").attr("class", "active");
        }
    });
    //加载文章列表，默认第一页，分类为全部
    $("#articleList").load(site.articleList(1, '0'), function (res, status) {
        if (status === 'success') {
            $("#page-1").attr("class", "active");
        }
    });
});

//保存当前分类，默认为0（分类为全部）
var currentClassify = '0';

//选择分类
function selectClassify(id) {
    currentClassify = id;
    //样式处理
    $("#classifyList li a").removeClass("active");
    $("#classify-" + id).addClass("active");

    //选择分类默认是从第一页查，避免出现指定的页码没有数据的情况
    $("#articleList").load(site.articleList(1, id), function (res, status) {
        if (status === 'success') {
            $("#pagination li a").removeClass("active");
            $("#page-1").addClass("active");
        }
    });
}

//选择页码
function selectPage(page) {
    $("#articleList").load(site.articleList(page, currentClassify), function (res, status) {
        if (status === 'success') {
            $("#pagination li a").removeClass("active");
            $("#page-" + page).addClass("active");
        }
    });
}