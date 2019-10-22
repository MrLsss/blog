$(document).ready(function () {
    initComments();
});

//根节点
var rootNode;
//子节点
var childNodes = [];
//所有评论
var comments = [];

function initComments() {
    getAJax(site.msgboard, function (res) {
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
    el: "#content-wrapper",
    data: {
        commentsList: comments,
        replyBoxShow: 0,
        my_dec: [],
        my_qq: '',
        my_email: '',
        replyForm: {
            nickname: '',
            email: '',
            content: '',
            pid: ''
        },
        commentForm: {
            nickname: '',
            email: '',
            content: ''
        },
    },
    created: function () {
        this.getMasterInfo();
    },
    mounted: function () {

    },
    methods: {
        getMasterInfo() {
            let _this = this;
            axios.get(site.masterInfo).then(function (res) {
                if (res.data.code === 200) {
                    _this.my_dec = res.data.data.wmDec.split(",");
                    _this.my_qq = res.data.data.wmQq;
                    _this.my_email = res.data.data.wmEmail;
                }
            });
        },
        submitComment() {
            if (this.commentForm.nickname !== '' && this.commentForm.email !== '' && this.commentForm.content !== '') {
                if (isEmail(this.commentForm.email)) {
                    let _this = this;
                    let data = {
                        nickname: _this.commentForm.nickname,
                        email: _this.commentForm.email,
                        content: _this.commentForm.content,
                        comment_from: '1',
                        address: returnCitySN["cname"]
                    };
                    axios.post(site.msgboardComment, {
                        data: JSON.stringify(data)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            comments = [];
                            _this.close();
                            initComments();
                            _this.commentsList = comments;
                            _this.commentForm = {
                                nickname: '',
                                email: '',
                                content: ''
                            };
                        }
                    })
                } else {
                    alert("邮箱不合法");
                }
            } else {
                alert("填好相应的数据后再点提交！");
            }

        },
        submitReply() {
            if (this.replyForm.nickname !== '' && this.replyForm.email !== '' && this.replyForm.content !== '' && this.replyForm.pid !== '') {
                if (isEmail(this.replyForm.email)) {
                    let _this = this;
                    let data = {
                        pid: _this.replyForm.pid,
                        nickname: _this.replyForm.nickname,
                        email: _this.replyForm.email,
                        content: _this.replyForm.content,
                        comment_from: '1',
                        address: returnCitySN["cname"],
                    };
                    axios.post(site.msgboardReply, {
                        data: JSON.stringify(data)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            comments = [];
                            _this.close();
                            initComments();
                            _this.commentsList = comments;
                        } else {
                            alert(res.data.msg);
                        }
                    });
                } else {
                    alert("邮箱不合法");
                }
            } else {
                alert("填好相应的数据后再点提交！");
            }

        },
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
        }

    }
});