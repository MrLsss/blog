new Vue({
    el: '#container',
    data: {
        showID: false,
        allClassify: [],//下拉框的值
        select: '0',//下拉框默认选中的值
        keyword: '',//搜索框输入的值
        multipleSelection: [],//批量删除选中的值
        tableData: [],
        page: {
            pageCode: 1, //当前页
            pageSize: 10, //每页显示的记录数
            totalPage: 0, //总记录数
            pageOption: [10, 20, 50], //分页选项
        },
        index: 2
    },
    mounted: function () {

    },
    created: function () {
        this.getArticlePage(this.page.pageCode, this.page.pageSize, this.select, this.keyword);
        this.getClassifySelect();
    },
    methods: {
        //初始化文章分页数据
        getArticlePage(current, size, classify, keyword) {
            var _this = this;
            axios.get(api.article.getPage(current, size, classify, keyword)).then(function (res) {
                res.data.data.records.forEach(item => {
                    if (item.isRec === '1') {
                        item.isRec = true;
                    } else {
                        item.isRec = false;
                    }
                    if (item.isMain === '1') {
                        item.isMain = true;
                    } else {
                        item.isMain = false;
                    }
                });
                _this.tableData = res.data.data.records;
                _this.page.totalPage = res.data.data.totalPage;
            });
        },
        refresh() {
            this.getArticlePage(this.page.pageCode, this.page.pageSize, this.select, this.keyword);
        },
        //初始化下拉框
        getClassifySelect() {
            var _this = this;
            axios.get(api.article.getClassifySelect).then(function (res) {
                _this.allClassify = res.data.data;
            });
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(id) {
            var _this = this;
            this.$confirm('确定删除该文章信息?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {//此处调用ajax
                axios.delete(api.article.delete(id)).then(function (res) {
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
                this.$message.error('你只删一条干嘛不点后面的删除？');
            } else {
                this.$confirm('确定删除这些文章信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//此处调用ajax
                    axios.delete(api.article.batchDelete(ids)).then(function (res) {
                        if (res.data.code === 200) {
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
        handleSizeChange(val) {//val=改变后的每页记录数
            this.page.pageSize = val;
            this.refresh();
        },
        //当前页改变时触发的函数
        handleCurrentChange(val) {//val=改变后的当前页码
            this.page.pageCode = val; //为了保证刷新后不跳转到第一页
            this.refresh();
        },
        //分类下拉框的值发生变化时，event=value
        selectOne(event) {
            this.select = event;
            //初始化分页配置，避免改变页码后再去分类选择出现当前页码没有数据的情况
            this.page.pageCode = 1;
            this.page.pageSize = 10;
            this.refresh();
        },
        changeRec(id) {
            axios.patch(api.article.changeRec, {
                id: id
            }).then(function (res) {
                console.log(res.data.msg);
            });
        },
        changeMain(id) {
            axios.patch(api.article.changeMain, {
                id: id
            }).then(function (res) {
                console.log(res.data.msg);
            });
        },
        setting() {
            window.location.href = '/admin/setting';
        }
    }
})