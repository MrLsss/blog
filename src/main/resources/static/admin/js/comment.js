new Vue({
    el: '#container',
    data() {
        return {
            showID: false,
            keyword: '',//搜索框输入的值
            multipleSelection: [],//批量删除选中的值
            select: '0',
            //回复按钮
            dialogFormReply: false,
            //审核按钮
            dialogFormAudit: false,
            replyForm: {
                commentNickName: '',
                commentContent: '',
                replyContent: '',
                id: '',
                address: '',
                from: '',
                for: ''
            },
            auditForm: {
                id: '',
                radio: '',
                commentRemark: '',
                isEmail: false,
                show: false
            },
            formLabelWidth: '80px',
            tableData: [],
            page: {
                pageCode: 1, //当前页
                pageSize: 5, //每页显示的记录数
                totalPage: 0, //总记录数
                pageOption: [5, 10], //分页选项
            },
            index: 5
        };

    },
    mounted: function () {

    },
    created: function () {
        this.getCommentPage(this.page.pageCode, this.page.pageSize, this.select, this.keyword);
    },
    computed: {},
    methods: {
        getCommentPage(current, size, select, keyword) {
            var _this = this;
            axios.get(api.comment.getPage(current, size, select, keyword)).then(function (res) {
                _this.tableData = res.data.data.records;
                _this.page.totalPage = res.data.data.totalPage;
            });
        },
        refresh() {
            this.getCommentPage(this.page.pageCode, this.page.pageSize, this.select, this.keyword);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(id) {
            var _this = this;
            this.$confirm('确定删除该评论信息?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {//此处调用ajax
                axios.delete(api.comment.delete(id)).then(function (res) {
                    if (res.data.code === 200) {
                        _this.$message({
                            type: 'success',
                            message: '删除成功'
                        });
                        if ((_this.page.totalPage - 1) / _this.page.pageSize === _this.page.pageCode - 1) {
                            _this.page.pageCode = _this.page.pageCode - 1;
                        }
                        _this.refresh();
                    } else {
                        _this.$message.error('删除失败');
                        _this.refresh();
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        handleDeleteSome() {
            var _this = this;
            var arr = this.multipleSelection;
            var ids = [];
            for (var i = 0; i < arr.length; i++) {
                ids.push(arr[i].id);
            }
            var length = ids.length;
            if (length <= 1) {
                this.$message.error('请至少选择两条数据');
            } else {
                this.$confirm('确定删除这些评论信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//此处调用ajax
                    axios.delete(api.comment.batchDelete(ids)).then(function (res) {
                        console.log(res);
                        if(res.data.code === 200) {
                            _this.$message({
                                type: 'success',
                                message: '删除成功'
                            });
                            if ((_this.page.totalPage - length) / _this.page.pageSize === _this.page.pageCode - 1) {
                                _this.page.pageCode = _this.page.pageCode - 1;
                            }
                            _this.refresh();
                        } else {
                            _this.$message.error('删除失败');
                            _this.refresh();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
        search() {
            this.page.pageSize = 10;
            this.page.pageCode = 1;
            this.refresh();
        },
        //pageSize改变时触发的函数
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.refresh();
        },
        //当前页改变时触发的函数
        handleCurrentChange(val) {
            this.page.pageCode = val; //为了保证刷新列表后页面还是在当前页，而不是跳转到第一页
            this.refresh();
        },
        //表单的取消按钮，清空输入框的值
        cancel(formName) {
            this.dialogFormReply = false;
            this.dialogFormAudit = false;
            this.$refs[formName].resetFields();
        },
        //提交回复的表单
        submitFormReply(formName) {
            this.replyForm.address = returnCitySN["cname"];
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.post(api.comment.reply, {
                        data: JSON.stringify(_this.replyForm)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            _this.$message({
                                message: '回复成功',
                                type: 'success'
                            });
                            _this.cancel(formName);
                        } else {
                            _this.$message.error('回复失败');
                        }
                        _this.refresh();
                    });
                } else {//验证没通过
                    return false;
                }
            });
        },
        //提交审核的表单
        submitFormAudit(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.patch(api.comment.audit, {
                        data: JSON.stringify(_this.auditForm)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            _this.$message({
                                message: '审核成功',
                                type: 'success'
                            });
                            _this.cancel(formName);
                        } else {
                            _this.$message.error('审核失败');
                        }
                        _this.refresh();
                    });
                } else {//验证没通过
                    return false;
                }
            });
        },
        //审核结果：通过/不通过按钮显示事件
        auditResult(value) {
            if (value === 'yes') {
                this.auditForm.show = false;
                this.auditForm.isEmail = false;
                this.auditForm.commentRemark = '';
            } else {
                this.auditForm.show = true;
            }
        },
        //回复按钮
        handleReply(id, nickname, content, from, forWhere) {
            this.dialogFormReply = true;
            this.replyForm.id = id;
            this.replyForm.commentNickName = nickname;
            this.replyForm.commentContent = content;
            this.replyForm.from = from;
            this.replyForm.for = forWhere;
        },
        //审核按钮
        handleAudit(status, remark, id) {
            this.dialogFormAudit = true;
            this.auditForm.id = id;
            if (status === '1') {
                this.auditForm.radio = 'yes';
                this.auditForm.show = false;
                this.auditForm.commentRemark = '';
            } else if (status === '0') {
                this.auditForm.radio = 'no';
                this.auditForm.show = true;
                this.auditForm.commentRemark = remark;
            } else {
                this.auditForm.radio = '';
                this.auditForm.show = false;
                this.auditForm.commentRemark = '';
            }
        },
        selectOne(event) {
            this.select = event;
            //初始化分页配置，避免改变页码后再去分类选择出现当前页码没有数据的情况
            this.page.pageCode = 1;
            this.page.pageSize = 10;
            this.refresh();
        },
        setting() {
            window.location.href = '/admin/setting';
        }
    }
})