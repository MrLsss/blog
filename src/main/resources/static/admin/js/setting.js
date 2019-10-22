new Vue({
    el: '#container',
    data() {
        var checkInfo = (rule, value, callback) => {
            if (!value) {
                return callback('不能为空');
            } else {
                callback();
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.pass) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        var validatePass3 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入原密码'));
            } else {
                callback();
            }
        };
        return {
            headImg: '',
            baseInfo: {},
            webMasterInfo: {},
            ruleForm: {
                pass: '',
                checkPass: '',
                oldPass: ''
            },
            rules: {
                siteName: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteDec: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteBlogAddress: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteAddress: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteImg: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteLogo: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteCopyright: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                siteNumber: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmName: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmEmail: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmWeixin: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmAlipay: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmQq: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmGithub: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                wmDec: [
                    {validator: checkInfo, trigger: 'blur'}
                ],
                pass: [
                    {validator: validatePass, trigger: 'blur'}
                ],
                checkPass: [
                    {validator: validatePass2, trigger: 'blur'}
                ],
                oldPass: [
                    {validator: validatePass3, trigger: 'blur'}
                ]
            },
            showBaseInfo: true,//控制网站基本信息的显示
            showPersonInfo: false,//控制个人基本信息的显示
            showModifyPwd: false,//控制修改密码的显示
            current: 1,
            index: 8
        };
    },
    mounted: function () {

    },
    created: function () {
        this.initInfo();
    },
    computed: {},
    methods: {
        initInfo() {
            var _this = this;
            axios.get(api.setting.getSiteBaseInfo).then(function (res) {
               _this.baseInfo = res.data.data;
            });
            axios.get(api.setting.getMasterInfo).then(function (res) {
                _this.webMasterInfo = res.data.data;
                _this.headImg = res.data.data.wmHeadImg;
            });
        },
        submitBaseInfo(formName) {
            console.log(this.baseInfo.siteBlogAddress);
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //看后台修改成功没
                    this.$message({
                        message: '成功',
                        type: 'success'
                    });
                    this.$message.error('失败');
                } else {//验证没通过
                    return false;
                }
            });
        },
        submitPersonInfo(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //看后台修改成功没
                    this.$message({
                        message: '成功',
                        type: 'success'
                    });
                    this.$message.error('失败');
                } else {//验证没通过
                    return false;
                }
            });
        },
        clickBaseInfo() {
            this.current = 1;
            this.showBaseInfo = true;
            this.showPersonInfo = false;
            this.showModifyPwd = false;
        },
        clickPersonInfo() {
            this.current = 2;
            this.showBaseInfo = false;
            this.showPersonInfo = true;
            this.showModifyPwd = false;
        },
        modifyPwd() {
            this.current = 3;
            this.showBaseInfo = false;
            this.showPersonInfo = false;
            this.showModifyPwd = true;
        },
        setting() {
            window.location.href = '/admin/setting';
        },
        submitModifyPwd(formName) {
            var _this = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    axios.post(api.setting.modifyPwd, {
                        data: _this.ruleForm
                    }).then(function (res) {
                        if (res.data.code === 200) {
                            _this.$message({
                                message: '修改成功',
                                type: 'success'
                            });
                        } else {
                            _this.$message.error(res.data.msg);
                        }
                    });
                } else {
                    _this.$message.error('修改失败');
                    return false;
                }
                _this.ruleForm.checkPass = '';
                _this.ruleForm.oldPass = '';
                _this.ruleForm.pass = '';
            });
        },
    }
})