
function getReportForm() {
    $.ajax({
        type: "post",
        url: "/api/report_form/get_by_month",    //向springboot请求数据的url
        data: {"machine_identifier":"01","date":"2018-04"},
        success: function (data) {
            $('button').removeClass("btn-primary").addClass("btn-success").attr('disabled', true);

            viewmodel.datalist = data;

            viewmodel.text = "数据请求成功，已渲染";
        }
    });
}

function cycleGetComponent() {
    getComponent();
    setInterval("getComponent()","1000");
}
//定义一个avalonjs的控制器
var viewmodel = avalon.define({
    //id必须和页面上定义的ms-controller名字相同，否则无法控制页面
    $id: "viewmodel",
    datalist: {},
    text: "请求数据",

    request:function () {
        getReportForm();
        setInterval("getReportForm()","1000");
    }
});