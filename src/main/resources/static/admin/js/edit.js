new Vue({
    el: '#container',
    data: {
        coverUrl: api.article.coverImgUrl,
        articleForm: {
            articleCoverImg: [],
            articleTitle: '',
            articleAuthor: '',
            isOwn: '',
            articleFrom: '',
            articleTag: [],
            articleClassify: '',
            articleDec: '',//简介
            articleContent: '',
            articleContentType: '',// 1=markdown，2=富文本
        },
        articleTag: [],
        tags: [],
        classify: [],
        editorTool: '1',//1=markdown，2=富文本
        editor: '',  // 存放实例化的wangEditor对象，在多个方法中使用
        index: 2
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
        this.editor.customConfig.uploadImgMaxLength = 5;
        //配置属性名称，绑定请求的图片数据
        //controller会用到，可以随便设置，但是一定要与controller一致
        this.editor.customConfig.uploadFileName = 'img';
        //配置字体
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

        this.init(this.getUrlParam());
    },
    created: function () {
    },
    methods: {
        init(id) {
            var _this = this;
            //加载标签列表
            axios.get(api.article.getTag).then(function (res) {
                _this.tags = res.data.data;
            });
            //加载分类下拉框
            axios.get(api.article.getClassify).then(function (res) {
                _this.classify = res.data.data;
            });
            //加载文章信息
            axios.get(api.article.getArticleInfo(id)).then(function (res) {
                if (res.data.data.isOwn === '0') {
                    res.data.data.isOwn = false;
                } else {
                    res.data.data.isOwn = true;
                }
                //给文章form赋值
                _this.articleForm = res.data.data;
                //文章内容类型
                _this.editorTool = res.data.data.articleContentType;
                //将文章标签初始化
                var input = $("#tagsinputval");
                res.data.data.articleTag.forEach(item => {
                    input.tagsinput('add', item);
                    _this.articleTag.push(item.id);
                });
                //如果是富文本，在这里给文章内容初始化
                if (_this.editorTool === '2') {
                    _this.editor.txt.html(res.data.data.articleContent);
                }
                //封面初始化
                _this.articleForm.articleCoverImg = [
                    {
                        name: res.data.data.articleCoverImg.substring(res.data.data.articleCoverImg.lastIndexOf("/") + 1),
                        url: res.data.data.articleCoverImg
                    }
                ]
                //分类默认选中，注意列表中的value类型和双向绑定的value类型要一致
                _this.articleForm.articleClassify = parseInt(res.data.data.articleClassifyID);
            });
        },
        //根据url获取文章id
        getUrlParam() {
            var path = window.location.href;
            var id = '';
            if (path.indexOf("?") === -1 && path.indexOf("#") === -1) {
                id = path.substring(path.lastIndexOf("/") + 1);
            } else {
                if (path.indexOf('?') === -1 || path.indexOf('#') === -1) {
                    if (path.indexOf('?') > path.indexOf('#')) {
                        //说明 ？在 # 前
                        id = path.substring(path.lastIndexOf('/') + 1, path.indexOf('?'));
                    } else {
                        id = path.substring(path.lastIndexOf('/') + 1, path.indexOf('#'));
                    }
                } else {
                    if (path.indexOf('?') > path.indexOf('#')) {
                        //说明 ？在 # 前
                        id = path.substring(path.lastIndexOf('/') + 1, path.indexOf('#'));
                    } else {
                        id = path.substring(path.lastIndexOf('/') + 1, path.indexOf('?'));
                    }
                }
            }
            return id;
        },
        selectClassify(event) {
            console.log(event);
            this.articleForm.articleClassify = event;
        },
        //选择编辑器
        selectEditorTool(value) {
            this.editorTool = value;
            this.articleForm.articleContentType = value;
        },
        //标签点击添加
        selectTag(id, tag) {
            var input = $("#tagsinputval");
            input.tagsinput('add', {
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
            axios.put(api.article.editArticle, {
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
        handleSuccess(res, file, fileList) {
            if (res.code === 200) {
                this.$message({
                    message: "上传成功",
                    type: 'success'
                });
                this.articleForm.articleCoverImg = [{
                    name: res.data.name, url:
                    res.data.url
                }];
            } else {
                this.$message.error("上传失败");
            }
        },
        handlePreview(file) {
            this.$notify.info({
                title: '消息',
                message: file.url
            });
        },
        handleRemove(file, fileList) {
            this.$message({
                message: "删除封面",
                type: "info"
            });
            this.articleForm.articleCoverImg = [];
        },

    }
})