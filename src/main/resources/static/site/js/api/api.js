const site = {
    addArticleView: '/addArticleView',
    mainArticle: '/getMainArticleList',
    logo: '/getSiteLogo',
    masterInfo: '/getMasterInfo',
    topAndRecArticle: '/getRecAndTopArticle',
    classifyList: '/getClassifyList',
    articleList(current, classify) {
        return '/getArticleList?currentPage=' + current + '&classify=' + classify;
    },
    tagArticleList(current, id) {
        return '/getTagArticle?id=' + id + '&current=' + current;
    },
    getTagName(id) {
        return '/getTagName?id=' + id;
    },
    articleComment(id) {
        return '/getArticleComment?id=' + id;
    },
    archives: '/getArchives',
    links: '/getLinks',
    replyArticle: '/addArticleReply',
    commentArticle: '/addArticleComment',
    msgboard: '/getMsgboardComment',
    msgboardComment: '/addMsgboardComment',
    msgboardReply: '/addMsgboardReply'
};