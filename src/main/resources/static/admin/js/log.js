new Vue({
    el: '#container',
    data() {
        return {
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            dataTime: '',
            startTime: '',
            endTime: '',
            showID: false,
            keyword: '',//搜索框输入的值
            multipleSelection: [],//批量删除选中的值
            formLabelWidth: '80px',
            tableData: [],
            page: {
                pageCode: 1, //当前页
                pageSize: 10, //每页显示的记录数
                totalPage: 0, //总记录数
                pageOption: [10, 20, 50], //分页选项
            },
            index: 7
        };
    },
    mounted: function () {

    },
    created: function () {
        this.getLogPage(this.page.pageCode, this.page.pageSize, this.keyword, this.startTime, this.endTime);
    },
    computed: {},
    methods: {
        getLogPage(currentPage, size, keyword, startTime, endTime) {
            var _this = this;
            axios.get(api.log.getPage(currentPage, size, keyword, startTime, endTime)).then(function (res) {
                _this.tableData = res.data.data.records;
                _this.page.totalPage = res.data.data.totalPage;
            })
        },
        refresh() {
            this.getLogPage(this.page.pageCode, this.page.pageSize, this.keyword, this.startTime, this.endTime);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(id) {
            var _this = this;
            this.$confirm('确定删除该系统日志?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {//此处调用ajax
                axios.delete(api.log.delete(id)).then(function (res) {
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
                this.$confirm('确定删除这些系统日志?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//此处调用ajax
                    axios.delete(api.log.batchDelete(ids)).then(function (res) {
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
        //根据时间戳查询
        selectDateTime(value) {
            this.datetime = value;
            if (this.datetime != null) {
                var start = this.datetime[0];
                var end = this.datetime[1];
                this.startTime = this.formatTime(start);
                this.endTime = this.formatTime(end);
            } else {
                this.startTime = '';
                this.endTime = '';
            }
            this.refresh();
        },
        formatTime(time) {
            var date = new Date(time);
            var year = date.getFullYear();
            if (year < 10) {
                year = "0" + year;
            }
            var month = date.getMonth() + 1;
            if (month < 10) {
                month = "0" + month;
            }
            var day = date.getDate();
            if (day < 10) {
                day = "0" + day;
            }
            var hours = date.getHours();
            if (hours < 10) {
                hours = "0" + hours;
            }
            var minutes = date.getMinutes();
            if (minutes < 10) {
                minutes = "0" + minutes;
            }
            var second = date.getSeconds();
            if (second < 10) {
                second = "0" + second;
            }
            return date_str = year + "-" + month + "-" + day + "- " + hours + ":" + minutes + ":" + second;
        },
        setting() {
            window.location.href = '/admin/setting';
        }
    }
})