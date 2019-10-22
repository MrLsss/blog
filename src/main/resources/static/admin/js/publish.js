new Vue({
    el: '#container',
    data() {
        return {
            coverUrl: api.article.coverImgUrl,
            articleForm: {
                articleCoverImg: [],
                articleTitle: '',
                articleAuthor: '',
                isOwn: true,
                articleFrom: '',
                articleTag: [],
                articleClassify: '',
                articleDec: '',//简介
                articleContent: '',
                articleContentType: '',// 1=markdown，2=富文本
                articleStatus: '',//1=发表，2=草稿
            },
            tags: [],
            classify: [],
            editorTool: '1',//1=markdown，2=富文本
            editor: '',  // 存放实例化的wangEditor对象，在多个方法中使用
            index: 2,
        }
    },
    mounted: function () {
        $(".el-upload__input").css('display', 'none');
        //初始化编辑器配置
        // markdown
        editormd("content-editormd", {
            width: "100%",
            height: "500px",
            syncScrolling: "single",
            path: "/admin/lib/markdown/lib/",
            saveHTMLToTextarea: true, // 保存HTML到Textarea
            //上传图片
            imageUpload: true,
            imageFormats: ["jpg", "JPG", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL: api.article.contentImgUrl("markdown")//图片上传地址
        });

        // wangEditor
        var E = window.wangEditor;
        this.editor = new E('#editor');
        // 配置服务器端地址,也就是controller的请求路径，不带项目路径，前面没有/
        this.editor.customConfig.uploadImgServer = api.article.contentImgUrl("wangEditor");//图片上传地址
        this.editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024;
        //配置属性名称，绑定请求的图片数据
        //controller会用到，可以随便设置，但是一定要与controller一致
        this.editor.customConfig.uploadFileName = 'img';
        //配置字
        this.editor.customConfig.fontNames = [
            '宋体',
            '微软雅黑',
            'Arial',
            'Tahoma',
            'Verdana'
        ];
        this.editor.customConfig.fontSize = [
            {2: '小'},
            {3: '中'},
            {5: '大'},
            {7: '特大'},
        ];
        this.editor.create();

        //初始化tagsinput
        var input = $("#tagsinputval");
        input.tagsinput({
            itemValue: 'id',
            itemText: 'text',
            maxTags: 3
        });
    },
    created: function () {
        this.init();
    },
    methods: {
        //初始化数据
        init() {
            this.articleForm.isOwn = true;
            this.articleForm.articleContentType = '1';
            this.articleForm.articleStatus = '-1';
            var _this = this;
            //初始化标签列表
            axios.get(api.article.getTag).then(function (res) {
                _this.tags = res.data.data;
            });
            //初始化分类下拉框
            axios.get(api.article.getClassify).then(function (res) {
                _this.classify = res.data.data;
            });
        },
        //选择编辑器
        selectEditorTool(value) {
            this.editorTool = value;
            this.articleForm.articleContentType = value;
        },
        selectClassify(event) {
            this.articleForm.articleClassify = event;
        },
        //标签点击添加
        selectTag(id, tag) {
            var input = $("#tagsinputval");
            input.tagsinput('add',  {
                id: id,
                text: tag,
            }); //赋值
        },
        publish() {
            var _this = this;
            this.articleForm.articleStatus = '1';
            if (this.editorTool === '1') {//markdown
                this.articleForm.articleContent = $("#content-editormd-markdown-doc").val();
            } else {//富文本
                this.articleForm.articleContent = this.editor.txt.html();
            }
            this.articleForm.articleTag = $("#tagsinputval").val();
            axios.post(api.article.addArticle, {
                data: JSON.stringify(_this.articleForm)
            }).then(function (res) {
                if (res.data.code === 200) {
                    _this.$message({
                        message: "发表成功",
                        type: 'success'
                    });
                    setInterval("window.location.href = '/admin/article'", 1000);
                } else {
                    _this.$message.error(res.data.msg);
                }
            });
        },
        save() {//草稿
            var _this = this;
            this.articleForm.articleStatus = '0';
            if (this.editorTool === '1') {//markdown
                this.articleForm.articleContent = $("#content-editormd-markdown-doc").val();
            } else {//富文本
                this.articleForm.articleContent = this.editor.txt.html();
            }
            this.articleForm.articleTag = $("#tagsinputval").val();
            axios.post(api.article.saveArticle, {
                data: JSON.stringify(_this.articleForm)
            }).then(function (res) {
                if (res.data.code === 200) {
                    _this.$message({
                        message: "发表成功",
                        type: 'success'
                    });
                    setInterval("window.location.href = '/admin/article'", 1000);
                } else {
                    _this.$message.error(res.data.msg);
                }
            });
        },
        setting() {
            window.location.href = '/admin/setting';
        },
        //图片上传
        //限制
        onExceed(files, fileList) {
            this.$message.error("只能上传一张图片");
        },
        beforeUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isGIF = file.type === 'image/gif';
            const isPNG = file.type === 'image/png';
            const isBMP = file.type === 'image/bmp';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG && !isGIF && !isPNG && !isBMP) {
                this.$message.error('上传图片必须是JPG/GIF/PNG/BMP 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 2MB!');
            }
            return (isJPG || isBMP || isGIF || isPNG) && isLt2M;
        },
        //成功回调
        handleSuccess(res, file, fileList) {
            if (res.code === 200) {
                this.$message({
                    message: "上传成功",
                    type: 'success'
                })
                this.articleForm.articleCoverImg = [{name: res.data.name, url:
                res.data.url}];
            } else {
                this.$message.error("上传失败");
            }
        },
        //点击图片
        handlePreview(file) {
            this.$notify.info({
                title: '消息',
                message: file.url
            });
        },
        //删除图片
        handleRemove(file, fileList) {
            this.$message({
                message: "删除封面",
                type: "info"
            });
            this.articleForm.articleCoverImg = [];
        },

    }
})