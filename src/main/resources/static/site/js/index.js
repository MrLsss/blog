new Vue({
    el: "#content-wrapper",
    data: {
        articleList: []
    },
    created: function () {
        this.init();
    },
    mounted: function () {

    },
    methods: {
        init() {
            var _this = this;
            axios.get(site.mainArticle).then(function (res) {
                console.log(res);
                _this.articleList = res.data.data;
            })
        },
        addView(id) {
            postAJax(site.addArticleView, {
                id: id
            }, function (res) {
                console.log(res);
            })
        }
    }

});