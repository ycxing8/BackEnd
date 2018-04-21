/**
 * Created by Intellij IDEA.
 * @Author LUOLIANG
 * @Date 2016/8/2
 * @Comment js文件，用于页面发送ajax请求
 */
function getComponent() {
    $.ajax({
        type: "post",
        url: "/api/component/get_by_machine_identifier",    //向springboot请求数据的url
        data: {"machine_identifier":"01"},
        success: function (data) {
            viewmodel.datalist = data;
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

    request:function () {
        getComponent();
        setInterval("getComponent()","1000");
    }
});