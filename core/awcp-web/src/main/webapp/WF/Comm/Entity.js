﻿
/*
说明:
1.  围绕该文件工作的有两个js文件，分别是。 FrmFool.js FrmFree.js
2.  对于傻瓜表单自由表单的，展现方式不同以外其他的都相同.
3.  相同的部分写入到了该文件里，不同的部分分别在不同的两个js文件里.
4.  MapExt2016.js 文件是一个公用的文件，用于处理扩展业务逻辑的，它在多个地方别调用了.
*/
var frmData = {};

//从表在新建或者在打开行的时候，如果 rowShowModel 配置了使用卡片的模式显示一行数据的时候，就调用此方法.
function DtlFrm(ensName, refPKVal, pkVal, frmType) {
    // model=1 自由表单, model=2傻瓜表单.
    var url = 'DtlFrm.htm?EnsName=' + ensName + '&RefPKVal=' + refPKVal + "&FrmTyp=" + frmType + '&OID=' + pkVal;
    alert('@代国强，这里需要弹出一url:' + url);
}

var frmData = null;
//将v1版本表单元素转换为v2 杨玉慧  silverlight 自由表单转化为H5表单.
function GenerFrm() {

    var href = window.location.href;
    var urlParam = href.substring(href.indexOf('?') + 1, href.length);
    urlParam = urlParam.replace('&DoType=', '&DoTypeDel=xx');

    //隐藏保存按钮.
    if (href.indexOf('&IsReadonly=1') > 1 || href.indexOf('&IsEdit=0') > 1) {
        $("#Btn").hide();
    }

    $.ajax({
        type: 'post',
        async: true,
        data: pageData,
        url: Handler + "?DoType=Entity_Init&m=" + Math.random() + "&" + urlParam,
        dataType: 'html',
        success: function (data) {

            if (data.indexOf('err@') == 0) {
                alert('装载表单出错..');
                return;
            }

            try {
                frmData = JSON.parse(data);
            }
            catch (err) {
                alert(" frmData数据转换JSON失败:" + data);

                return;
            }

            //获得sys_mapdata.
            var mapData = frmData["Sys_MapData"][0];

            //初始化Sys_MapData
            var h = mapData.FrmH;
            var w = mapData.FrmW;
            if (h <= 1200)
                h = 1200;

            //表单名称.
            document.title = mapData.Name;

            $('#divCCForm').height(h);

            $('#topContentDiv').height(h);
            $('#topContentDiv').width(w);
            $('.Bar').width(w + 15);

            var marginLeft = $('#topContentDiv').css('margin-left');
            marginLeft = parseFloat(marginLeft.substr(0, marginLeft.length - 2)) + 50;
            $('#topContentDiv i').css('left', marginLeft.toString() + 'px');
            $('#CCForm').html('');

            //根据表单类型不同生成表单.

            var isTest = GetQueryString("IsTest");

            if (isTest == "1") {

                var frmType = GetQueryString("FrmType");
                if (frmType == 'FreeFrm')
                    GenerFreeFrm(mapData, frmData); //自由表单.
                else
                    GenerFoolFrm(mapData, frmData); //生成傻瓜表单.

            } else {
                if (mapData.FrmType == 0)
                    GenerFoolFrm(mapData, frmData); //生成傻瓜表单.
                else
                    GenerFreeFrm(mapData, frmData); //自由表单.
            }

            //原有的。
            //为 DISABLED 的 TEXTAREA 加TITLE 
            var disabledTextAreas = $('#divCCForm textarea:disabled');
            $.each(disabledTextAreas, function (i, obj) {
                $(obj).attr('title', $(obj).val());
            })


            //根据NAME 设置ID的值
            var inputs = $('[name]');
            $.each(inputs, function (i, obj) {
                if ($(obj).attr("id") == undefined || $(obj).attr("id") == '') {
                    $(obj).attr("id", $(obj).attr("name"));
                }
            })


            // 加载JS文件 改变JS文件的加载方式 解决JS在资源中不显示的问题.
            var enName = frmData.Sys_MapData[0].No;
            try {
                ////加载JS文件
                //jsSrc = "<script language='JavaScript' src='/DataUser/JSLibData/" + enName + "_Self.js' ></script>";
                //$('body').append($('<div>' + jsSrc + '</div>'));

                var s = document.createElement('script');
                s.type = 'text/javascript';
                s.src = "../DataUser/JSLibData/" + enName + "_Self.js";
                var tmp = document.getElementsByTagName('script')[0];
                tmp.parentNode.insertBefore(s, tmp);
            }
            catch (err) {

            }

            var jsSrc = '';
            try {

                var s = document.createElement('script');
                s.type = 'text/javascript';
                s.src = "../DataUser/JSLibData/" + enName + "_Self.js";
                var tmp = document.getElementsByTagName('script')[0];
                tmp.parentNode.insertBefore(s, tmp);
            }
            catch (err) {

            }

            Common.MaxLengthError();

            //处理下拉框级联等扩展信息
            AfterBindEn_DealMapExt(frmData);

            //设置默认值
            for (var j = 0; j < frmData.Sys_MapAttr.length; j++) {

                var mapAttr = frmData.Sys_MapAttr[j];

                //添加 label
                //如果是整行的需要添加  style='clear:both'

                var defValue = ConvertDefVal(frmData, mapAttr.DefVal, mapAttr.KeyOfEn);
                if ($('#TB_' + mapAttr.KeyOfEn).length == 1) {
                    $('#TB_' + mapAttr.KeyOfEn).val(defValue);
                }

            }

            ShowNoticeInfo();

            ShowTextBoxNoticeInfo();


            //初始化复选下拉框 
            var selectPicker = $('.selectpicker');
            $.each(selectPicker, function (i, selectObj) {
                var defVal = $(selectObj).attr('data-val');
                var defValArr = defVal.split(',');
                $(selectObj).selectpicker('val', defValArr);
            });

            //给富文本 创建编辑器
            var editor = document.activeEditor = UM.getEditor('editor', {
                'autoHeightEnabled': false,
                'fontsize': [10, 12, 14, 16, 18, 20, 24, 36]
            });
            if (document.BindEditorMapAttr) {
                editor.MaxLen = document.BindEditorMapAttr.MaxLen;
                editor.MinLen = document.BindEditorMapAttr.MinLen;
                editor.BindField = document.BindEditorMapAttr.KeyOfEn;
                editor.BindFieldName = document.BindEditorMapAttr.Name;
            }
            //调整样式,让必选的红色 * 随后垂直居中
            editor.$container.css({ "display": "inline-block", "margin-right": "10px", "vertical-align": "middle" });
        }
    })
}

//打开从表的从表
function DtlFoolFrm(dtl, refPK, refOID) {

    var url = 'DtlFoolFrm.htm?EnsDtl=' + dtl + '&RefPK=' + refPK + '&RefOID=' + refOID;
    alert('这里没有实现打开iurl ' + url);

    //引入了刘贤臣写的东西，一直缺少东西.可否改进一下，弄个稳定的？ @代国强.
    OpenEasyUiDialog(url, "eudlgframe", "编辑", 600, 450, "icon-edit", true, null, null, null, function () {
        //   window.location.href = window.location.href;
    });

    // window.open(url);
    //alert('打开从表卡片');
}





//保存
function Save() {

    //必填项和正则表达式检查
    var formCheckResult = true;
    if (!CheckBlanks()) {
        formCheckResult = false;
    }
    if (!CheckReg()) {
        formCheckResult = false;
    }
    if (!formCheckResult) {
        //alert("请检查表单必填项和正则表达式");
        return;
    }

    // setToobarDisiable();

    $.ajax({
        type: 'post',
        async: true,
        data: GetFormData(true, true),
        url: Handler + "?DoType=Entity_Save&OID=" + pageData.OID,
        dataType: 'html',
        success: function (data) {

            if (data.indexOf('err@') == 0) {
                $('#Message').html(data.substring(4, data.length));
                $('.Message').show();
                return;
            }
            window.location.href = window.location.href;
            //alert(data);
        }
    });
}


//以下是软通写的
//初始化网页URL参数
function initPageParam() {
    //新建独有
    pageData.UserNo = GetQueryString("UserNo");
    pageData.DoWhat = GetQueryString("DoWhat");
    pageData.IsMobile = GetQueryString("IsMobile");

    pageData.FK_Flow = GetQueryString("FK_Flow");
    pageData.FK_Node = GetQueryString("FK_Node");
    //FK_Flow=004&FK_Node=402&FID=0&WorkID=232&IsRead=0&T=20160920223812&Paras=
    pageData.FID = GetQueryString("FID") == null ? 0 : GetQueryString("FID");

    var oid = GetQueryString("WorkID");
    if (oid == null)
        oid = GetQueryString("OID");
    pageData.OID = oid;

    pageData.IsRead = GetQueryString("IsRead");
    pageData.T = GetQueryString("T");
    pageData.Paras = GetQueryString("Paras");
    pageData.IsReadOnly = GetQueryString("IsReadOnly"); //如果是IsReadOnly，就表示是查看页面，不是处理页面
    pageData.IsStartFlow = GetQueryString("IsStartFlow"); //是否是启动流程页面 即发起流程

    pageData.DoType1 = GetQueryString("DoType")//View
    pageData.FK_MapData = GetQueryString("FK_MapData")//View

    //$('#navIframe').attr('src', 'Admin/CCBPMDesigner/truck/centerTrakNav.html?FK_Flow=' + pageData.FK_Flow + "&FID=" + pageData.FID + "&WorkID=" + pageData.OID);
}
//将获取过来的URL参数转成URL中的参数形式  &
function pageParamToUrl() {
    var paramUrlStr = '';
    for (var param in pageData) {
        paramUrlStr += '&' + (param.indexOf('@') == 0 ? param.substring(1) : param) + '=' + pageData[param];
    }
    return paramUrlStr;
}

//设置附件为只读
function setAttachDisabled() {
    //附件设置
    var attachs = $('iframe[src*="AttachmentUpload.aspx"]');
    $.each(attachs, function (i, attach) {
        if (attach.src.indexOf('IsReadOnly') == -1) {
            $(attach).attr('src', $(attach).attr('src') + "&IsReadOnly=1");
        }
    })
}


//设置表单元素不可用
function setFormEleDisabled() {
    //文本框等设置为不可用
    $('#divCCForm textarea').attr('disabled', 'disabled');
    $('#divCCForm select').attr('disabled', 'disabled');
    $('#divCCForm input[type!=button]').attr('disabled', 'disabled');
}


//FK_Flow=005&UserNo=zhwj&DoWhat=StartClassic&=&IsMobile=&FK_Node=501
var pageData = {};
var globalVarList = {};

//刷新子流程
function refSubSubFlowIframe() {
    var iframe = $('iframe[src*="SubFlow.aspx"]');
    //iframe[0].contentWindow.location.reload();
    iframe[0].contentWindow.location.href = iframe[0].src;
}
//回填扩展字段的值
function SetAth(data) {
    var atParamObj = $('#iframeAthForm').data();
    var tbId = atParamObj.tbId;
    var divId = atParamObj.divId;
    var athTb = $('#' + tbId);
    var athDiv = $('#' + divId);

    $('#athModal').modal('hide');
    //不存在或来自于viewWorkNodeFrm
    if (atParamObj != undefined && atParamObj.IsViewWorkNode != 1 && divId != undefined && tbId != undefined) {
        if (atParamObj.AthShowModel == "1") {
            athTb.val(data.join('*'));
            athDiv.html(data.join(';&nbsp;'));
        } else {
            athTb.val('@AthCount=' + data.length);
            athDiv.html("附件<span class='badge' >" + data.length + "</span>个");
        }
    } else {
        $('#athModal').removeClass('in');
    }
    $('#athModal').hide();
    var ifs = $("iframe[id^=track]").contents();
    if (ifs.length > 0) {
        for (var i = 0; i < ifs.length; i++) {
            $(ifs[i]).find(".modal-backdrop").hide();
        }
    }
}

//查看页面的附件展示  查看页面调用
function ShowViewNodeAth(athLab, atParamObj, src) {
    var athForm = $('iframeAthForm');
    var athModal = $('athModal');
    var athFormTitle = $('#athModal .modal-title');
    athFormTitle.text("上传附件：" + athLab);
    athModal.modal().show();
}

//处理MapExt
function AfterBindEn_DealMapExt(frmData) {

    var mapExtArr = frmData.Sys_MapExt;
    for (var i = 0; i < mapExtArr.length; i++) {
        var mapExt = mapExtArr[i];
        switch (mapExt.ExtType) {
            case "PopVal": //PopVal窗返回值
            case "PopFullCtrl": //PopFullCtrl.
                var tb = $('[name$=' + mapExt.AttrOfOper + ']');
                //tb.attr("placeholder", "请双击选择。。。");
                tb.attr("onclick", "ShowHelpDiv('TB_" + mapExt.AttrOfOper + "','','" + mapExt.MyPK + "','" + mapExt.FK_MapData + "','returnvalccformpopval');");
                //  tb.attr("ondblclick", "ReturnValCCFormPopValGoogle(this,'" + mapExt.MyPK + "','" + mapExt.FK_MapData + "', " + mapExt.W + "," + mapExt.H + ",'" + GepParaByName("Title", mapExt.AtPara) + "');");

                tb.attr('readonly', 'true');
                tb.attr('disabled', 'true');
                var icon = '';
                var popWorkModelStr = '';
                var popWorkModelIndex = mapExt.AtPara != undefined ? mapExt.AtPara.indexOf('@PopValWorkModel=') : -1;
                if (popWorkModelIndex >= 0) {
                    popWorkModelIndex = popWorkModelIndex + '@PopValWorkModel='.length;
                    popWorkModelStr = mapExt.AtPara.substring(popWorkModelIndex, popWorkModelIndex + 1);
                }
                switch (popWorkModelStr) {
                    /// <summary> 
                    /// 自定义URL 
                    /// </summary> 
                    //SelfUrl =1, 
                    case "1":
                        icon = "glyphicon glyphicon-th";
                        break;
                    /// <summary> 
                    /// 表格模式 
                    /// </summary> 
                    // TableOnly, 
                    case "2":
                        icon = "glyphicon glyphicon-list";
                        break;
                    /// <summary> 
                    /// 表格分页模式 
                    /// </summary> 
                    //TablePage, 
                    case "3":
                        icon = "glyphicon glyphicon-list-alt";
                        break;
                    /// <summary> 
                    /// 分组模式 
                    /// </summary> 
                    // Group, 
                    case "4":
                        icon = "glyphicon glyphicon-list-alt";
                        break;
                    /// <summary> 
                    /// 树展现模式 
                    /// </summary> 
                    // Tree, 
                    case "5":
                        icon = "glyphicon glyphicon-tree-deciduous";
                        break;
                    /// <summary> 
                    /// 双实体树 
                    /// </summary> 
                    // TreeDouble 
                    case "6":
                        icon = "glyphicon glyphicon-tree-deciduous";
                        break;
                    default:
                        icon = "glyphicon glyphicon-th";
                        break;
                }
                var eleHtml = ' <div class="input-group form_tree">' + tb.parent().html() +
                '<span class="input-group-addon" onclick="' + "ReturnValCCFormPopValGoogle('TB_" + mapExt.AttrOfOper + "','" + mapExt.MyPK + "','" + mapExt.FK_MapData + "', " + mapExt.W + "," + mapExt.H + ",'" + GepParaByName("Title", mapExt.AtPara) + "','" + mapExt.ExtType + "');" + '"><span class="' + icon + '"></span></span></div>';
                tb.parent().html(eleHtml);
                break;
            case "RegularExpression": //正则表达式  统一在保存和提交时检查
                var tb = $('[name$=' + mapExt.AttrOfOper + ']');
                //tb.attr(mapExt.Tag, "CheckRegInput('" + tb.attr('name') + "'," + mapExt.Doc.replace(/【/g, '[').replace(/】/g, ']').replace(/（/g, '(').replace(/）/g, ')').replace(/｛/g, '{').replace(/｝/g, '}') + ",'" + mapExt.Tag1 + "')");

                if (tb.attr('class') != undefined && tb.attr('class').indexOf('CheckRegInput') > 0) {
                    break;
                } else {
                    tb.addClass("CheckRegInput");
                    tb.data(mapExt)
                    //tb.data().name = tb.attr('name');
                    //tb.data().Doc = mapExt.Doc;
                    //tb.data().Tag1 = mapExt.Tag1;
                    //tb.attr("data-name", tb.attr('name'));
                    //tb.attr("data-Doc", tb.attr('name'));
                    //tb.attr("data-checkreginput", "CheckRegInput('" + tb.attr('name') + "'," + mapExt.Doc.replace(/【/g, '[').replace(/】/g, ']').replace(/（/g, '(').replace(/）/g, ')').replace(/｛/g, '{').replace(/｝/g, '}') + ",'" + mapExt.Tag1 + "')");
                }
                break;
            case "InputCheck": //输入检查
                //var tbJS = $("#TB_" + mapExt.AttrOfOper);
                //if (tbJS != undefined) {
                //    tbJS.attr(mapExt.Tag2, mapExt.Tag1 + "(this)");
                //}
                //else {
                //    tbJS = $("#DDL_" + mapExt.AttrOfOper);
                //    if (ddl != null)
                //        ddl.attr(mapExt.Tag2, mapExt.Tag1 + "(this);");
                //}
                break;
            case "TBFullCtrl": //自动填充  先不做
                var tbAuto = $("#TB_" + mapExt.AttrOfOper);
                if (tbAuto == null)
                    continue;

                tbAuto.attr("ondblclick", "ReturnValTBFullCtrl(this,'" + mapExt.MyPK + "');");
                tbAuto.attr("onkeyup", "DoAnscToFillDiv(this,this.value, '" + 'TB_' + mapExt.AttrOfOper + "', '" + mapExt.MyPK + "');");
                tbAuto.attr("AUTOCOMPLETE", "OFF");
                if (mapExt.Tag != "") {
                    /* 处理下拉框的选择范围的问题 */
                    var strs = mapExt.Tag.split('$');
                    for (var str in strs) {
                        var str = strs[k];
                        if (str = "" || str == null) {
                            continue;
                        }

                        var myCtl = str.Split(':');
                        var ctlID = myCtl[0];
                        var ddlC1 = $("#DDL_" + ctlID);
                        if (ddlC1 == null) {
                            continue;
                        }

                        //如果文本库数值为空，就让其返回.
                        var txt = tbAuto.val();
                        if (txt == '')
                            continue;

                        //获取要填充 ddll 的SQL.
                        var sql = myCtl[1].Replace("~", "'");
                        sql = sql.Replace("@Key", txt);
                        //sql = BP.WF.Glo.DealExp(sql, en, null);  怎么办
                    }
                }

                break;
            case "ActiveDDL": /*自动初始化ddl的下拉框数据. 下拉框的级联操作 已经 OK*/
                var ddlPerant = $("#DDL_" + mapExt.AttrOfOper);
                var ddlChild = $("#DDL_" + mapExt.AttrsOfActive);
                if (ddlPerant == null || ddlChild == null)
                    continue;
                ddlPerant.attr("onchange", "DDLAnsc(this.value,\'" + "DDL_" + mapExt.AttrsOfActive + "\', \'" + mapExt.MyPK + "\')");
                // 处理默认选择。
                //string val = ddlPerant.SelectedItemStringVal;
                var valClient = ConvertDefVal(frmData, '', mapExt.AttrsOfActive); // ddlChild.SelectedItemStringVal;

                //ddlChild.select(valClient);  未写
                break;
            case "AutoFullDLL": // 自动填充下拉框.
                continue; //已经处理了。
            case "DDLFullCtrl": // 自动填充其他的控件..  先不做
                break;
                var ddlOper = $("#DDL_" + mapExt.AttrOfOper);
                if (ddlOper == null)
                    continue;

                ddlOper.attr("onchange", "Change('" + frmData.Sys_MapData[0].No + "');DDLFullCtrl(this.value,\'" + "DDL_" + mapExt.AttrOfOper + "\', \'" + mapExt.MyPK + "\')");
                if (me.Tag != "") {
                    /* 下拉框填充范围. */
                    var strs = me.Tag.split('$');
                    for (var k = 0; k < strs.length; k++) {
                        var str = strs[k];
                        if (str == "")
                            continue;

                        var myCtl = str.split(':');
                        var ctlID = myCtl[0];
                        var ddlC1 = $("#DDL_" + ctlID);
                        if (ddlC1 == null) {
                            //me.Tag = "";
                            //me.Update();
                            continue;
                        }

                        //如果触发的dll 数据为空，则不处理.
                        if (ddlOper.val() == "")
                            continue;

                        var sql = myCtl[1].Replace("~", "'");
                        sql = sql.Replace("@Key", ddlOper.val());

                        //需要执行SQL语句
                        //sql = BP.WF.Glo.DealExp(sql, en, null);

                        //dt = DBAccess.RunSQLReturnTable(sql);
                        //string valC1 = ddlC1.SelectedItemStringVal;
                        //if (dt.Rows.Count != 0)
                        //{
                        //    foreach (DataRow dr in dt.Rows)
                        //{
                        //        ListItem li = ddlC1.Items.FindByValue(dr[0].ToString());
                        //    if (li == null)
                        //    {
                        //        ddlC1.Items.Add(new ListItem(dr[1].ToString(), dr[0].ToString()));
                        //    }
                        //    else
                        //    {
                        //        li.Attributes["visable"] = "false";
                        //    }
                        //}

                        var items = [{ No: 1, Name: '测试1' }, { No: 2, Name: '测试2' }, { No: 3, Name: '测试3' }, { No: 4, Name: '测试4' }, { No: 5, Name: '测试5'}];
                        var operations = '';
                        $.each(items, function (i, item) {
                            operations += "<option  value='" + item.No + "'>" + item.Name + "</option>";
                        });
                        ddlC1.children().remove();
                        ddlC1.html(operations);
                        //ddlC1.SetSelectItem(valC1);
                    }
                }
                break;
        }
    }
}
//AtPara  @PopValSelectModel=0@PopValFormat=0@PopValWorkModel=0@PopValShowModel=0
function GepParaByName(name, atPara) {
    var params = atPara.split('@');
    var result = $.grep(params, function (value) {
        return value != '' && value.split('=').length == 2 && value.split('=')[0] == value;
    })
    return result;
}

//初始化下拉列表框的OPERATION
function InitDDLOperation(frmData, mapAttr, defVal) {

    var operations = '';
    //外键类型
    if (mapAttr.LGType == 2) {
        if (frmData[mapAttr.KeyOfEn] != undefined) {
            $.each(frmData[mapAttr.KeyOfEn], function (i, obj) {
                operations += "<option " + (obj.No == defVal ? " selected='selected' " : "") + " value='" + obj.No + "'>" + obj.Name + "</option>";
            });
        }
        else if (frmData[mapAttr.UIBindKey] != undefined) {
            $.each(frmData[mapAttr.UIBindKey], function (i, obj) {
                operations += "<option " + (obj.No == defVal ? " selected='selected' " : "") + " value='" + obj.No + "'>" + obj.Name + "</option>";
            });
        }
    } else {
        var enums = frmData.Sys_Enum;
        enums = $.grep(enums, function (value) {
            return value.EnumKey == mapAttr.UIBindKey;
        });


        $.each(enums, function (i, obj) {
            operations += "<option " + (obj.IntKey == defVal ? " selected='selected' " : "") + " value='" + obj.IntKey + "'>" + obj.Lab + "</option>";
        });
    }

    return operations;
}

//填充默认数据
function ConvertDefVal(frmData, defVal, keyOfEn) {
    //计算URL传过来的表单参数@TXB_Title=事件测试

    var pageParams = getQueryString();
    var pageParamObj = {};
    $.each(pageParams, function (i, pageParam) {
        if (pageParam.indexOf('@') == 0) {
            var pageParamArr = pageParam.split('=');
            pageParamObj[pageParamArr[0].substring(1, pageParamArr[0].length)] = pageParamArr[1];
        }
    });

    var result = defVal;

    //通过MAINTABLE返回的参数
    for (var ele in frmData.MainTable[0]) {
        if (keyOfEn == ele && frmData.MainTable[0] != '') {
            result = frmData.MainTable[0][ele];
            break;
        }
    }

    //通过URL参数传过来的参数 后台处理到MainTable 里面
    //for (var pageParam in pageParamObj) {
    //    if (pageParam == keyOfEn) {
    //        result = pageParamObj[pageParam];
    //        break;
    //    }
    //}

    if (result != undefined && typeof (result) == 'string') {
        //result = result.replace(/｛/g, "{").replace(/｝/g, "}").replace(/：/g, ":").replace(/，/g, ",").replace(/【/g, "[").replace(/】/g, "]").replace(/；/g, ";").replace(/~/g, "'").replace(/‘/g, "'").replace(/‘/g, "'");
    }
    return result = unescape(result);
}

//获取表单数据
function GetFormData(isCotainTextArea, isCotainUrlParam) {
    var formss = $('#divCCForm').serialize();
    var formArr = formss.split('&');
    var formArrResult = [];
    //获取CHECKBOX的值
    $.each(formArr, function (i, ele) {
        if (ele.split('=')[0].indexOf('CB_') == 0) {
            if ($('#' + ele.split('=')[0] + ':checked').length == 1) {
                ele = ele.split('=')[0] + '=1';
            } else {
                ele = ele.split('=')[0] + '=0';
            }
        }
        formArrResult.push(ele);
    });

    //获取表单中禁用的表单元素的值
    var disabledEles = $('#divCCForm :disabled');
    $.each(disabledEles, function (i, disabledEle) {
        var name = $(disabledEle).attr('name');
        switch (disabledEle.tagName.toUpperCase()) {
            case "INPUT":
                switch (disabledEle.type.toUpperCase()) {
                    case "CHECKBOX": //复选框
                        formArrResult.push(name + '=' + $(disabledEle).is(':checked') ? 1 : 0);
                        break;
                    case "TEXT": //文本框
                        formArrResult.push(name + '=' + $(disabledEle).val());
                        break;
                    case "RADIO": //单选钮
                        var eleResult = name + '=' + $('[name="' + name + ':checked"]').val();
                        if (!$.inArray(formArrResult, eleResult)) {
                            formArrResult.push();
                        }
                        break;
                }
                break;
            //下拉框  
            case "SELECT":
                formArrResult.push(name + '=' + $(disabledEle).children('option:checked').val());
                break;

            //对于复选下拉框获取值得方法  
            //                if ($('[data-id=' + name + ']').length > 0) { 
            //                    var val = $(disabledEle).val().join(','); 
            //                    formArrResult.push(name + '=' + val); 
            //                } else { 
            //                    formArrResult.push(name + '=' + $(disabledEle).children('option:checked').val()); 
            //                } 
            //                break; 
            //文本区域  
            case "TEXTAREA":
                formArrResult.push(name + '=' + $(disabledEle).val());
                break;
        }
    });

    //获取表单中隐藏的表单元素的值
    var hiddens = $('input[type=hidden]');
    $.each(hiddens, function (i, hidden) {
        if ($(hidden).attr("name").indexOf('TB_') == 0) {
            //formArrResult.push($(hidden).attr("name") + '=' + $(hidden).val());
        }
    });

    if (!isCotainTextArea) {
        formArrResult = $.grep(formArrResult, function (value) {
            return value.split('=').length == 2 ? value.split('=')[1].length <= 50 : true;
        });
    }

    formss = formArrResult.join('&');
    var dataArr = [];
    //加上URL中的参数
    if (pageData != undefined && isCotainUrlParam) {
        var pageDataArr = [];
        for (var data in pageData) {
            pageDataArr.push(data + '=' + pageData[data]);
        }
        dataArr.push(pageDataArr.join('&'));
    }
    if (formss != '')
        dataArr.push(formss);
    var formData = dataArr.join('&');


    //为了复选框  合并一下值  复选框的值以  ，号分割
    //用& 符号截取数据
    var formDataArr = formData.split('&');
    var formDataResultObj = {};
    $.each(formDataArr, function (i, formDataObj) {
        //计算出等号的INDEX
        var indexOfEqual = formDataObj.indexOf('=');
        var objectKey = formDataObj.substr(0, indexOfEqual);
        var objectValue = formDataObj.substr(indexOfEqual + 1);
        if (formDataResultObj[objectKey] == undefined) {
            formDataResultObj[objectKey] = objectValue;
        } else {
            formDataResultObj[objectKey] = formDataResultObj[objectKey] + ',' + objectValue;
        }
    });

    var formdataResultStr = '';
    for (var ele in formDataResultObj) {
        formdataResultStr = formdataResultStr + ele + '=' + formDataResultObj[ele] + '&';
    }
    return formdataResultStr;
}

$(function () {

    setAttachDisabled();
    //setToobarDisiable();
    setFormEleDisabled();

})


//根据下拉框选定的值，弹出提示信息  绑定那个元素显示，哪个元素不显示  
function ShowNoticeInfo() {

    var rbs = frmData.Sys_FrmRB;
    data = rbs;
    $("input[type=radio],select").bind('change', function (obj) {
        var needShowDDLids = [];
        var methodVal = obj.target.value;

        for (var j = 0; j < data.length; j++) {
            var value = data[j].IntKey;
            var noticeInfo = data[j].Tip;
            var drdlColName = data[j].KeyOfEn;

            if (obj.target.tagName == "SELECT") {
                drdlColName = 'DDL_' + drdlColName;
            } else {
                drdlColName = 'RB_' + drdlColName;
            }
            //if (methodVal == value &&  obj.target.name.indexOf(drdlColName) == (obj.target.name.length - drdlColName.length)) {
            if (methodVal == value && (obj.target.name == drdlColName)) {
                //高级JS设置;  设置表单字段的  可用 可见 不可用 
                var fieldConfig = data[j].FieldsCfg;
                var fieldConfigArr = fieldConfig.split('@');
                for (var k = 0; k < fieldConfigArr.length; k++) {
                    var fieldCon = fieldConfigArr[k];
                    if (fieldCon != '' && fieldCon.split('=').length == 2) {
                        var fieldConArr = fieldCon.split('=');
                        var ele = $('[name$=' + fieldConArr[0] + ']');
                        if (ele.length == 0) {
                            continue;
                        }
                        var labDiv = undefined;
                        var eleDiv = undefined;
                        if (ele.css('display').toUpperCase() == "NONE") {
                            continue;
                        }

                        if (ele.parent().attr('class').indexOf('input-group') >= 0) {
                            labDiv = ele.parent().parent().prev();
                            eleDiv = ele.parent().parent();
                        } else {
                            labDiv = ele.parent().prev();
                            eleDiv = ele.parent();
                        }
                        switch (fieldConArr[1]) {
                            case "1": //可用
                                if (labDiv.css('display').toUpperCase() == "NONE" && ele[0].id.indexOf('DDL_') == 0) {
                                    needShowDDLids.push(ele[0].id);
                                }

                                labDiv.css('display', 'block');
                                eleDiv.css('display', 'block');
                                ele.removeAttr('disabled');


                                break;
                            case "2": //可见
                                if (labDiv.css('display').toUpperCase() == "NONE" && ele[0].id.indexOf('DDL_') == 0) {
                                    needShowDDLids.push(ele[0].id);
                                }

                                labDiv.css('display', 'block');
                                eleDiv.css('display', 'block');
                                break;
                            case "3": //不可见
                                labDiv.css('display', 'none');
                                eleDiv.css('display', 'none');
                                break;
                        }
                    }
                }
                //根据下拉列表的值选择弹出提示信息
                if (noticeInfo == undefined || noticeInfo.trim() == '') {
                    break;
                }
                noticeInfo = noticeInfo.replace(/\\n/g, '<br/>')
                var selectText = '';
                if (obj.target.tagName.toUpperCase() == 'INPUT' && obj.target.type.toUpperCase() == 'RADIO') {//radio button
                    selectText = obj.target.nextSibling.textContent;
                } else {//select
                    selectText = $(obj.target).find("option:selected").text();
                }
                $($('#div_NoticeInfo .popover-title span')[0]).text(selectText);
                $('#div_NoticeInfo .popover-content').html(noticeInfo);

                var top = obj.target.offsetHeight;
                var left = obj.target.offsetLeft;
                var current = obj.target.offsetParent;
                while (current !== null) {
                    left += current.offsetLeft;
                    top += current.offsetTop;
                    current = current.offsetParent;
                }


                if (obj.target.tagName.toUpperCase() == 'INPUT' && obj.target.type.toUpperCase() == 'RADIO') {//radio button
                    left = left - 40;
                    top = top + 10;
                }
                if (top - $('#div_NoticeInfo').height() - 30 < 0) {
                    //让提示框在下方展示
                    $('#div_NoticeInfo').removeClass('top');
                    $('#div_NoticeInfo').addClass('bottom');
                    top = top;
                } else {
                    $('#div_NoticeInfo').removeClass('bottom');
                    $('#div_NoticeInfo').addClass('top');
                    top = top - $('#div_NoticeInfo').height() - 30;
                }
                $('#div_NoticeInfo').css('top', top);
                $('#div_NoticeInfo').css('left', left);
                $('#div_NoticeInfo').css('display', 'block');
                //$("#btnNoticeInfo").popover('show');
                //$('#btnNoticeInfo').trigger('click');
                break;
            }
        }

        $.each(needShowDDLids, function (i, ddlId) {
            $('#' + ddlId).change();
        });
    });


    $('#span_CloseNoticeInfo').bind('click', function () {
        $('#div_NoticeInfo').css('display', 'none');
    })

    $("input[type=radio]:checked,select").change();
    $('#span_CloseNoticeInfo').click();
}

//给出文本框输入提示信息
function ShowTextBoxNoticeInfo() {

    var mapAttr = frmData.Sys_MapAttr;
    mapAttr = $.grep(mapAttr, function (attr) {
        var atParams = attr.AtPara;
        return atParams != undefined && AtParaToJson(atParams).Tip != undefined && AtParaToJson(atParams).Tip != '' && $('#TB_' + attr.KeyOfEn).length > 0 && $('#TB_' + attr.KeyOfEn).css('display') != 'none';
    })

    $.each(mapAttr, function (i, attr) {
        $('#TB_' + attr.KeyOfEn).bind('focus', function (obj) {
            var frmData = JSON.parse(jsonStr);
            var mapAttr = frmData.Sys_MapAttr;

            mapAttr = $.grep(mapAttr, function (attr) {
                return 'TB_' + attr.KeyOfEn == obj.target.id;
            })
            var atParams = AtParaToJson(mapAttr[0].AtPara);
            var noticeInfo = atParams.Tip;

            if (noticeInfo == undefined || noticeInfo == '')
                return;

            //noticeInfo = noticeInfo.replace(/\\n/g, '<br/>')

            $($('#div_NoticeInfo .popover-title span')[0]).text(mapAttr[0].Name);
            $('#div_NoticeInfo .popover-content').html(noticeInfo);

            var top = obj.target.offsetHeight;
            var left = obj.target.offsetLeft;
            var current = obj.target.offsetParent;
            while (current !== null) {
                left += current.offsetLeft;
                top += current.offsetTop;
                current = current.offsetParent;
            }

            if (top - $('#div_NoticeInfo').height() - 30 < 0) {
                //让提示框在下方展示
                $('#div_NoticeInfo').removeClass('top');
                $('#div_NoticeInfo').addClass('bottom');
                top = top;
            } else {
                $('#div_NoticeInfo').removeClass('bottom');
                $('#div_NoticeInfo').addClass('top');
                top = top - $('#div_NoticeInfo').height() - 30;
            }
            $('#div_NoticeInfo').css('top', top);
            $('#div_NoticeInfo').css('left', left);
            $('#div_NoticeInfo').css('display', 'block');
        });
    })
}
//必填项检查   名称最后是*号的必填  如果是选择框，值为'' 或者 显示值为 【*请选择】都算为未填 返回FALSE 检查必填项失败
function CheckBlanks() {
    var checkBlankResult = true;
    //获取所有的列名 找到带* 的LABEL mustInput
    //var lbs = $('[class*=col-md-1] label:contains(*)');
    var lbs = $('.mustInput');
    $.each(lbs, function (i, obj) {
        if ($(obj).parent().css('display') != 'none' && $(obj).parent().next().css('display')) {
            var keyofen = $(obj).data().keyofen
            var ele = $('[id$=_' + keyofen + ']');
            if (ele.length == 1) {
                switch (ele[0].tagName.toUpperCase()) {
                    case "INPUT":
                        if (ele.attr('type') == "text") {
                            if (ele.val() == "") {
                                checkBlankResult = false;
                                ele.addClass('errorInput');
                            } else {
                                ele.removeClass('errorInput');
                            }
                        }
                        break;
                    case "SELECT":
                        if (ele.val() == "" || ele.children('option:checked').text() == "*请选择") {
                            checkBlankResult = false;
                            ele.addClass('errorInput');
                        } else {
                            ele.removeClass('errorInput');
                        }
                        break;
                    case "TEXTAREA":
                        if (ele.val() == "") {
                            checkBlankResult = false;
                            ele.addClass('errorInput');
                        } else {
                            ele.removeClass('errorInput');
                        }
                        break;
                }
            }
        }
    });


    return checkBlankResult;
}

//正则表达式检查
function CheckReg() {
    var CheckRegResult = true;
    var regInputs = $('.CheckRegInput');
    $.each(regInputs, function (i, obj) {
        var name = obj.name;
        var mapExtData = $(obj).data();
        if (mapExtData.Doc != undefined) {
            var regDoc = mapExtData.Doc.replace(/【/g, '[').replace(/】/g, ']').replace(/（/g, '(').replace(/）/g, ')').replace(/｛/g, '{').replace(/｝/g, '}').replace(/，/g, ',');
            var tag1 = mapExtData.Tag1;
            if ($(obj).val() != undefined && $(obj).val() != '') {

                var result = CheckRegInput(name, regDoc, tag1);
                if (!result) {
                    $(obj).addClass('errorInput');
                    CheckRegResult = false;
                } else {
                    $(obj).removeClass('errorInput');
                }
            }
        }
    });
    return CheckRegResult;
}

function addLoadFunction(id, eventName, method) {
    var js = "";
    js = "<script type='text/javascript' >";
    js += "function F" + id + "load() { ";
    js += "if (document.all) {";
    js += "document.getElementById('F" + id + "').attachEvent('on" + eventName + "',function(event){" + method + "('" + id + "');});";
    js += "} ";

    js += "else { ";
    js += "document.getElementById('F" + id + "').contentWindow.addEventListener('" + eventName + "',function(event){" + method + "('" + id + "');}, false); ";
    js += "} }";

    js += "</script>";
    return $(js);
}
var appPath = "/";
if (plant == "JFlow")
    appPath = "./../../";
var DtlsCount = " + dtlsCount + "; //应该加载的明细表数量


//处理URL，MainTable URL 参数 替换问题
function dealWithUrl(src) {
    var src = src.replace(new RegExp(/(：)/g), ':');

    //替换
    src = src.replace('@OID', pageData.OID);
    src = src.replace('@WorkID', pageData.OID);

    var params = '&FID=' + pageData.FID;
    params += '&WorkID=' + pageData.OID;
    if (src.indexOf("?") > 0) {
        var params = getQueryStringFromUrl(src);
        if (params != null && params.length > 0) {
            $.each(params, function (i, param) {
                if (param.indexOf('@') == 0) {//是需要替换的参数
                    paramArr = param.split('=');
                    if (paramArr.length == 2 && paramArr[1].indexOf('@') == 0) {
                        if (paramArr[1].indexOf('@WebUser.') == 0) {
                            params[i] = paramArr[0].substring(1) + "=" + frmData.MainTable[0][paramArr[1].substr('@WebUser.'.length)];
                        }
                        if (frmData.MainTable[0][paramArr[1].substr(1)] != undefined) {
                            params[i] = paramArr[0].substring(1) + "=" + frmData.MainTable[0][paramArr[1].substr(1)];
                        }

                        //使用URL中的参数
                        var pageParams = getQueryString();
                        var pageParamObj = {};
                        $.each(pageParams, function (i, pageParam) {
                            if (pageParam.indexOf('@') == 0) {
                                var pageParamArr = pageParam.split('=');
                                pageParamObj[pageParamArr[0].substring(1, pageParamArr[0].length)] = pageParamArr[1];
                            }
                        });
                        var result = "";
                        //通过MAINTABLE返回的参数
                        for (var ele in frmData.MainTable[0]) {
                            if (paramArr[0].substring(1) == ele) {
                                result = frmData.MainTable[0][ele];
                                break;
                            }
                        }
                        //通过URL参数传过来的参数
                        for (var pageParam in pageParamObj) {
                            if (pageParam == paramArr[0].substring(1)) {
                                result = pageParamObj[pageParam];
                                break;
                            }
                        }

                        if (result != '') {
                            params[i] = paramArr[0].substring(1) + "=" + unescape(result);
                        }
                    }
                }
            });
            src = src.substr(0, src.indexOf('?')) + "?" + params.join('&');
        }
    }
    else {
        src += "?q=1";
    }
    return src;
}

var colVisibleJsonStr = ''
var jsonStr = '';



$(function () {
    SetHegiht();
    //打开表单检查正则表达式
    if (typeof FormOnLoadCheckIsNull != 'undefined' && FormOnLoadCheckIsNull instanceof Function) {
        FormOnLoadCheckIsNull();
    }
});

function SetHegiht() {
    var screenHeight = document.documentElement.clientHeight;

    var messageHeight = $('#Message').height();
    var topBarHeight = 40;
    var childHeight = $('#childThread').height();
    var infoHeight = $('#flowInfo').height();

    var allHeight = messageHeight + topBarHeight + childHeight + childHeight + infoHeight;
    try {

        var BtnWord = $("#BtnWord").val();
        if (BtnWord == 2)
            allHeight = allHeight + 30;

        var frmHeight = $("#FrmHeight").val();
        if (frmHeight == NaN || frmHeight == "" || frmHeight == null)
            frmHeight = 0;

        if (screenHeight > parseFloat(frmHeight) + allHeight) {
            // $("#divCCForm").height(screenHeight - allHeight);

            $("#TDWorkPlace").height(screenHeight - allHeight - 10);

        }
        else {
            //$("#divCCForm").height(parseFloat(frmHeight) + allHeight);
            $("#TDWorkPlace").height(parseFloat(frmHeight) + allHeight - 10);
        }
    }
    catch (e) {
    }
}

function Change() {
    var btn = document.getElementById('Btn_Save');
    if (btn != null) {
        if (btn.value.valueOf('*') == -1)
            btn.value = btn.value + '*';
    }
}

// ccform 为开发者提供的内置函数. 
// 获取DDL值 
function ReqDDL(ddlID) {
    var v = document.getElementById('DDL_' + ddlID).value;
    if (v == null) {
        alert('没有找到ID=' + ddlID + '的下拉框控件.');
    }
    return v;
}

//20160106 by 柳辉
//获取页面参数
//sArgName表示要获取哪个参数的值
function GetPageParas(sArgName) {

    var sHref = window.location.href;
    var args = sHref.split("?");
    var retval = "";
    if (args[0] == sHref) /*参数为空*/{
        return retval; /*无需做任何处理*/
    }
    var str = args[1];
    args = str.split("&");
    for (var i = 0; i < args.length; i++) {
        str = args[i];
        var arg = str.split("=");
        if (arg.length <= 1) continue;
        if (arg[0] == sArgName) retval = arg[1];
    }
    return retval;
}

//获取Dtl中TB的值 20160106 from 柳辉
function ReqDtlBObj(dtlTable, DtlColumn, onValue) {

    var getworkid = $('#HidWorkID').val(); //hiddenValue

    $.ajax({

        url: "../../DataUser/Do.aspx",
        data: { getworkid: getworkid, dtlTable: dtlTable, DtlColumn: DtlColumn, onValue: onValue },
        success: function (arr) {
            alert(arr);
            if (arr == "true") {
                return true;
            }
            else {
                return false;
            }
        }

    });
}
// 获取TB值
function ReqTB(tbID) {
    var v = document.getElementById('TB_' + tbID).value;
    if (v == null) {
        alert('没有找到ID=' + tbID + '的文本框控件.');
    }
    return v;
}
// 获取CheckBox值
function ReqCB(cbID) {
    var v = document.getElementById('CB_' + cbID).value;
    if (v == null) {
        alert('没有找到ID=' + cbID + '的 CheckBox （单选）控件.');
    }
    return v;
}
// 获取附件文件名称,如果附件没有上传就返回null.
function ReqAthFileName(athID) {
    var v = document.getElementById(athID);
    if (v == null) {
        return null;
    }
    var fileName = v.alt;
    return fileName;
}

/// 获取DDL Obj
function ReqDDLObj(ddlID) {
    var v = document.getElementById('DDL_' + ddlID);
    if (v == null) {
        alert('没有找到ID=' + ddlID + '的下拉框控件.');
    }
    return v;
}
// 获取TB Obj
function ReqTBObj(tbID) {
    var v = document.getElementById('TB_' + tbID);
    if (v == null) {
        alert('没有找到ID=' + tbID + '的文本框控件.');
    }
    return v;
}
// 获取CheckBox Obj值
function ReqCBObj(cbID) {
    var v = document.getElementById('CB_' + cbID);
    if (v == null) {
        alert('没有找到ID=' + cbID + '的单选控件(获取CheckBox)对象.');
    }
    return v;
}
// 设置值.
function SetCtrlVal(ctrlID, val) {
    document.getElementById('TB_' + ctrlID).value = val;
    document.getElementById('DDL_' + ctrlID).value = val;
    document.getElementById('CB_' + ctrlID).value = val;
}

function To(url) {
    //window.location.href = url;
    window.name = "dialogPage"; window.open(url, "dialogPage")
}



//然浏览器最大化.
function ResizeWindow() {
    if (window.screen) {  //判断浏览器是否支持window.screen判断浏览器是否支持screen     
        var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽     
        var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高     
        window.moveTo(0, 0);           //把window放在左上角     
        window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh     
    }
}
 