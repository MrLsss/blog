const api = {
    index: {
        count: '/admin/index/counts',
        top10: '/admin/index/top10',
        pie: '/admin/index/pie',
        cloud: '/admin/index/tagCloud',
        operationTime: '/admin/index/operationTime',
        webInfo: '/admin/index/getWebInfo'
    },
    article: {
        getPage(currentPage, size, classify, keyword) {
            return '/admin/articles?currentPage=' + currentPage + '&size=' + size + '&classify=' + classify + '&keyword=' + keyword
        },
        getClassifySelect: '/admin/articles/classifies',
        changeRec: '/admin/article/rec',
        changeMain: '/admin/article/main',
        delete(id) {
            return '/admin/article/' + id
        },
        batchDelete(ids) {
            return '/admin/articles/' + ids
        },
        getClassify: '/admin/publish/classifies',
        getTag: '/admin/publish/tags',
        coverImgUrl: '/admin/article/cover',
        contentImgUrl(type) {
            return '/admin/article/content/' + type;
        },
        addArticle: '/admin/article',
        saveArticle: '/admin/saveArticle',
        getArticleInfo(id) {
            return '/admin/article/' + id;
        },
        editArticle: '/admin/article',

    },
    classify: {
        getPage(currentPage, size, keyword) {
            return '/admin/classifies?currentPage=' + currentPage + '&size=' + size + '&keyword=' + keyword
        },
        add: '/admin/classify',
        save: '/admin/classify',
        delete(id) {
            return '/admin/classify/' + id
        },
        batchDelete(ids) {
            return '/admin/classifies/' + ids
        }
    },
    tag: {
        getPage(currentPage, size, keyword) {
            return '/admin/tags?currentPage=' + currentPage + '&size=' + size + '&keyword=' + keyword
        },
        add: '/admin/tag',
        save: '/admin/tag',
        delete(id) {
            return '/admin/tag/' + id
        },
        batchDelete(ids) {
            return '/admin/tags/' + ids
        }
    },
    comment: {
        getPage(currentPage, size, select, keyword) {
            return '/admin/comments?currentPage=' + currentPage + '&size=' + size + '&select=' + select + '&keyword=' + keyword;
        },
        audit: '/admin/comment',
        reply: '/admin/comment',
        delete(id) {
            return '/admin/comment/' + id
        },
        batchDelete(ids) {
            return '/admin/comments/' + ids
        }
    },
    link: {
        getPage(currentPage, size, keyword) {
            return '/admin/links?currentPage=' + currentPage + '&size=' + size + '&keyword=' + keyword;
        },
        delete(id) {
            return '/admin/link/' + id
        },
        batchDelete(ids) {
            return '/admin/links/' + ids
        },
        add: '/admin/link',
        save: '/admin/link'
    },
    log: {
        getPage(currentPage, size, keyword, startTime, endTime) {
            return '/admin/logs?currentPage=' + currentPage + '&size=' + size + '&keyword=' + keyword + '&startTime=' + startTime + '&endTime=' + endTime
        },
        delete(id) {
            return '/admin/log/' + id
        },
        batchDelete(ids) {
            return '/admin/logs/' + ids
        }
    },
    setting: {
        getSiteBaseInfo: '/setting/getSiteBaseInfo',
        getMasterInfo: '/setting/getMasterInfo',
        modifyPwd: '/setting/modifyPwd'
    }
};