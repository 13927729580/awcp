﻿//点击右边的下载标签.
function Down(fk_ath, pkVal, delPKVal) {
    window.location.href = 'AttachmentUpload.aspx?DoType=Down&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + '&FK_Node=<%=FK_Node %>&FK_Flow = <%=FK_Flow %>&FK_MapData=<%=FK_MapData %>&Ath=<%=Ath %>';
}

//点击文件名称执行的下载.
function Down2017(mypk) {

    //$("#Msg").html("<img src=../Img/loading.gif />正在加载,请稍后......");

    //组织url.
    var url = Handler + "?DoType=AttachmentUpload_Down&MyPK=" + mypk + "&m=" + Math.random();

    $.ajax({
        type: 'post',
        async: true,
        url: url,
        dataType: 'html',
        success: function (data) {

            if (data.indexOf('err@') == 0) {
                alert(data); //如果是异常，就提提示.
                return;
            }

            if (data.indexOf('url@') == 0) {

                data = data.replace('url@', ''); //如果返回url，就直接转向.

                var i = data.indexOf('\DataUser');
                var str = '/' + data.substring(i);
                str = str.replace('\\\\', '\\');
                window.open(str, "_blank", "width=800, height=600,toolbar=yes");
                return;
            }
            alert(data);
            return;
        }
    });
}

/* 一下的方法从网上找到的，都不适用 . */

function Down3(str) {

    alert(str);
    var a;
    a = window.open(str, "_blank", "width=0, height=0,status=0");
    a.document.execCommand("SaveAs");
    a.close();
}

function Down2(imgURL) {
    
    var oPop = window.open(imgURL, "", "width=1, height=1, top=5000, left=5000");

    for (; oPop.document.readyState != "complete"; ) {
        if (oPop.document.readyState == "complete") 
        break;
    }

    oPop.document.execCommand("SaveAs");
    oPop.close();

} 

function Down(url) {

    var $eleForm = $("<form method='get'></form>");

    $eleForm.attr("action", url);

    $(document.body).append($eleForm);

    //提交表单，实现下载
    $eleForm.submit();
}

function downloadFile(url) {
    try {
        var elemIF = document.createElement("iframe");
        elemIF.src = url;
        elemIF.style.display = "none";
        document.body.appendChild(elemIF);
    } catch (e) {

    }
}