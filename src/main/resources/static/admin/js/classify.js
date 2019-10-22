new Vue({
    el: '#container',
    data() {
        //表单验证
        var checkClassifyName = (rule, value, callback) => {
            if (!value) {
                return callback('分类名称不能为空');
            } else {
                callback();
            }
        };
        var checkClassifyDec = (rule, value, callback) => {
            if (!value) {
                return callback('分类描述不能为空');
            } else {
                callback();
            }
        };
        return {
            showID: false,
            keyword: '',//搜索框输入的值
            multipleSelection: [],//批量删除选中的值
            //添加分类
            dialogFormAdd: false,
            dialogFormEdit: false,
            addForm: {//弹出框的表单数据
                classify_name: '',
                classify_dec: '',
            },
            editForm: {
                id: '',
                classify_name: '',
                classify_dec: '',
            },
            formLabelWidth: '80px',
            tableData: [],
            page: {
                pageCode: 1, //当前页
                pageSize: 10, //每页显示的记录数
                totalPage: 0, //总记录数
                pageOption: [10, 20, 50], //分页选项
            },
            rules: {
                classify_name: [
                    {validator: checkClassifyName, trigger: 'blur'}
                ],
                classify_dec: [
                    {validator: checkClassifyDec, trigger: 'blur'}
                ]
            },
            index: 3
        };

    },
    mounted: function () {

    },
    created: function () {
        this.getClassifyPage(this.page.pageCode, this.page.pageSize, this.keyword);
    },
    computed: {},
    methods: {
        getClassifyPage(current, size, keyword) {
            var _this = this;
            axios.get(api.classify.getPage(current, size, keyword)).then(function (res) {
                _this.tableData = res.data.data.records;
                _this.page.totalPage = res.data.data.totalPage;
            });
        },
        refresh() {
            this.getClassifyPage(this.page.pageCode, this.page.pageSize, this.keyword);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(id) {
            var _this = this;
            this.$confirm('确定删除该分类信息?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {//此处调用ajax
                axios.delete(api.classify.delete(id)).then(function (res) {
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
                ids.push(arr[i].ID);
            }
            var length = ids.length;
            if (length <= 1) {
                this.$message.error('请至少选择两条数据');
            } else {
                this.$confirm('确定删除这些分类信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//此处调用ajax
                    axios.delete(api.classify.batchDelete(ids)).then(function (res) {
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
        //添加按钮
        handleAdd() {
            this.dialogFormAdd = true;
        },
        //编辑按钮
        handleEdit(id, name, dec) {
            this.dialogFormEdit = true;
            this.editForm.classify_name = name;
            this.editForm.classify_dec = dec;
            this.editForm.id = id;
        },
        //表单的取消按钮，清空输入框的值
        cancel(formName) {
            this.dialogFormAdd = false;
            this.dialogFormEdit = false;
            this.$refs[formName].resetFields();
        },
        //提交添加的表单
        submitForm(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.post(api.classify.add, {
                        data: JSON.stringify(_this.addForm)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            _this.$message({
                                message: '添加成功',
                                type: 'success'
                            });
                            _this.cancel(formName);
                        } else {
                            _this.$message.error('添加失败');
                        }
                        _this.refresh();
                    });
                } else {//验证没通过
                    return false;
                }
            });
        },
        save(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.patch(api.classify.save, {
                        data: JSON.stringify(_this.editForm)
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            _this.$message({
                                message: '修改成功',
                                type: 'success'
                            });
                            _this.cancel(formName);
                        } else {
                            _this.$message.error('修改失败');
                        }
                        _this.refresh();
                    });
                } else {//验证没通过
                    return false;
                }
            });
        },
        setting() {
            window.location.href = '/admin/setting';
        }
    }
})