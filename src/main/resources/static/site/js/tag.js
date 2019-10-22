$(document).ready(function () {
    let id = getUrlLastParam();
    $("#articleList").load(site.tagArticleList(1, id), function (res, status) {
        if (status === 'success') {
            $("#page-1").attr("class", "active");
        }
    });
    getAJax(site.getTagName(id), function (res) {
        $("#tagName").html("#"+res.data.tagName);
    });
});

//选择页码
function selectPage(page) {
    $("#articleList").load(site.tagArticleList(page, getUrlLastParam()), function (res, status) {
        if (status === 'success') {
            $("#pagination li a").removeClass("active");
            $("#page-" + page).addClass("active");
        }
    });
}

