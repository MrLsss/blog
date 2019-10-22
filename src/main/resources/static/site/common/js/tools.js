//get方法的ajax
function getAJax(url, callback) {
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        async: true,
        success: function (res) {
            callback(res);
        }
    });
}

//post方法的ajax
function postAJax(url, data, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: 'json',
        async: true,
        success: function (res) {
            callback(res);
        }
    });
}

//获取url中最后一位中的参数
function getUrlLastParam() {
    let url = window.location.href;
    return url.substring(url.lastIndexOf("/") + 1);
}

//邮箱验证
function isEmail(email) {
    const reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    return reg.test(email);
}