new Vue({
    el: '#container',
    data() {
        //表单验证
        var checkTagName = (rule, value, callback) => {
            if (!value) {
                return callback('标签名称不能为空');
            } else {
                callback();
            }
        };
        return {
            showID: false,
            keyword: '',//搜索框输入的值
            multipleSelection: [],//批量删除选中的值
            dialogFormAdd: false,
            dialogFormEdit: false,
            addForm: {//弹出框的表单数据
                tag_name: ''
            },
            editForm: {
                id: '',
                tag_name: ''
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
                tag_name: [
                    {validator: checkTagName, trigger: 'blur'}
                ]
            },
            index: 4
        };

    },
    mounted: function () {

    },
    created: function () {
        this.getTagPage(this.page.pageCode, this.page.pageSize, this.keyword);
    },
    computed: {},
    methods: {
        getTagPage(current, size, keyword) {
            var _this = this;
            axios.get(api.tag.getPage(current, size, keyword)).then(function (res) {
                _this.tableData = res.data.data.records;
                _this.page.totalPage = res.data.data.totalPage;
            });
        },
        refresh() {
            this.getTagPage(this.page.pageCode, this.page.pageSize, this.keyword);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(id) {
            var _this = this;
            this.$confirm('确定删除该标签信息?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {//此处调用ajax
                axios.delete(api.tag.delete(id)).then(function (res) {
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
            var length = arr.length;
            for (var i = 0; i < length; i++) {
                ids.push(arr[i].ID);
            }
            if (ids.length <= 1) {
                this.$message.error('请至少选择两条数据');
            } else {
                this.$confirm('确定永久删除这些标签信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//此处调用ajax
                    axios.delete(api.tag.batchDelete(ids)).then(function (res) {
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
            this.refresh()
        },
        //当前页改变时触发的函数
        handleCurrentChange(val) {
            this.page.pageCode = val; //为了保证刷新列表后页面还是在当前页，而不是跳转到第一页
            this.refresh()
        },
        //添加按钮
        handleAdd() {
            this.dialogFormAdd = true;
        },
        //编辑按钮
        handleEdit(id, name) {
            this.dialogFormEdit = true;
            this.editForm.tag_name = name;
            this.editForm.id = id;
        },
        //表单的取消按钮，清空输入框的值
        cancel(formName) {
            this.dialogFormAdd = false;
            this.dialogFormEdit = false;
            this.$refs[formName].resetFields();
        },
        //提交添加的表单
        add(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.post(api.tag.add, {
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
        //提交编辑的表单
        save(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.patch(api.tag.save, {
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