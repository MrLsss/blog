$(document).ready(function () {
    //获取logo
    getAJax(site.logo, function (res) {
        if (res.code === 200) {
            $(".logo_30").attr("src", res.data);
        }
    });
    //不是首页，就获取热门文章和推荐文章
    if (!isIndex()) {
        $("#topAndRec").load(site.topAndRecArticle);
    }
    getUrlParam();
});

//判断是不是首页
function isIndex() {
    let path = getUrlLastParam();
    if (path === '') {
        return true;
    } else {
        return false;
    }
}
//菜单默认选中
function getUrlParam() {
    let path = getUrlLastParam();
    if (path === '') {//首页
        $("#index a").addClass("active")
    } else {
        $("#"+path).addClass("active");
    }
}

function addView(id) {
    postAJax(site.addArticleView, {
        id: id
    }, function (res) {
        console.log(res);
    })
}