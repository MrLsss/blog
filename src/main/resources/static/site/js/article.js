$(document).ready(function () {
    initMarkdown();
    initComments();
});

function initMarkdown() {
    $(function () {
        editormd.markdownToHTML("content", {
            htmlDecode: "style,script,iframe", //可以过滤标签解码
            emoji: true,
            taskList: true,
            tex: true,               // 默认不解析
            flowChart: true,         // 默认不解析
            sequenceDiagram: true, // 默认不解析
            codeFold: true
        });
    });
}

//根节点
var rootNode;
//子节点
var childNodes = [];
//所有评论
var comments = [];

function initComments() {
    let id = getUrlLastParam();

    getAJax(site.articleComment(id), function (res) {
        //处理评论信息
        for (let i = 0; i < res.data.length; i++) {
            rootNode = res.data[i].comment;
            if (res.data[i].nodes.length > 0) {
                tree(res.data[i]);
            }
            let comment = {
                node: rootNode,
                childNodes: childNodes
            };
            comments.push(comment);
            rootNode = 0;
            childNodes = [];
        }
    });
}

function tree(data) {
        if (data.nodes.length > 0) {
            for (let i = 0; i < data.nodes.length; i++) {
                childNodes.push(data.nodes[i].comment);
                tree(data.nodes[i]);
            }
        }
}

new Vue({
    el: "#comment",
    data() {
        return {
            commentsList: comments,
            replyBoxShow: 0,
            commentForm: {
                nickname: '',
                email: '',
                content: '',
            },
            replyForm: {
                pid: '',
                nickname: '',
                email: '',
                content: '',
            },
        }
    },
    created: function () {
    },
    mounted: function () {

    },
    methods: {
        clickReply(id) {
            this.replyBoxShow = id;
            this.replyForm.pid = id;
        },
        close() {
            this.replyBoxShow = 0;
            this.replyForm.pid = '';
            this.replyForm.nickname = '';
            this.replyForm.email = '';
            this.replyForm.content = '';
            this.replyForm.comment_from = '';
        },
        submitReply: function () {
            if (this.replyForm.pid !== '' && this.replyForm.nickname !== '' && this.replyForm.email !== '' && this.replyForm.content !== '') {
                if (isEmail(this.replyForm.email)) {
                    let data = {
                        pid: this.replyForm.pid,
                        nickname: this.replyForm.nickname,
                        email: this.replyForm.email,
                        content: this.replyForm.content,
                        comment_from: '2',
                        comment_for: getUrlLastParam(),
                        address: returnCitySN["cname"]
                    };
                    var _this = this;
                    postAJax(site.replyArticle, data, function (res) {
                        if (res.code === 200) {
                            comments = [];
                            _this.close();
                            initComments();
                            _this.commentsList = comments;
                        } else {
                            alert(res.msg);
                        }
                    })
                } else {
                    alert("邮箱不合法");
                }
            } else {
                alert("填好相应的数据后再点提交！");
            }
        },
        submitComment() {
            if (this.commentForm.nickname !== '' && this.commentForm.email !== '' && this.commentForm.content !== '') {
                if (isEmail(this.commentForm.email)) {
                    let data = {
                        nickname: this.commentForm.nickname,
                        email: this.commentForm.email,
                        content: this.commentForm.content,
                        comment_from: '2',
                        comment_for: getUrlLastParam(),
                        address: returnCitySN["cname"]
                    };
                    var _this = this;
                    postAJax(site.commentArticle, data, function (res) {
                        if (res.code === 200) {
                            comments = [];
                            _this.close();
                            initComments();
                            _this.commentsList = comments;
                            _this.commentForm = {
                                nickname: '',
                                email: '',
                                content: ''
                            };
                        } else {
                            alert(res.msg);
                        }
                    })
                } else {
                    alert("邮箱不合法");
                }
            } else {
                alert("填好相应的数据后再点提交！");
            }

        }
    }
});

