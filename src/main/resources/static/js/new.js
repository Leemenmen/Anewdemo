// import '../ace-builds-master/src-noconflict/ace';
document.write("<script language=javascript src='/ace-builds-master/src-noconflict/ace.js'></script>");
// document.write("<script language=javascript src='/js/jquery.min.js'></script>");
// @import('jquery.min')
// document.write("<script language=javascript src='/lib/layui/layui.all.js'></script>");

function initEditor(editorid, codepath) {


    var editor = ace.edit(editorid);
    //设置主题
    editor.setTheme("ace/theme/chrome");
    // 设置编辑语言
    editor.getSession().setMode("ace/mode/python");
    // 设置字体大小
    editor.setFontSize(20);
    // 设置行高;
    document.getElementById("editor").style.lineHeight = "30px";
    // console.log("初始化完成，开始赋值。。。。。");
    // 获取编辑内容
    editor.getValue();
    // console.log("现在的值："+editor.getValue());
    // 移动光标至第0行，第0列
    editor.moveCursorTo(1, 0);
    //设置只读（true时只读，用于展示代码）
    editor.setReadOnly(true);
    //自动换行,设置为off关闭
    editor.setOption("wrap", "free");

    // console.log(codepath)
    // console.log(encodeURIComponent(encodeURIComponent(codepath)))
    $.post("/readCode?codePath=" + encodeURIComponent(encodeURIComponent(codepath)), function (res) {
        if (res.code == 200) {
            // console.log("a")
            editor.setValue(res.data);
            // console.log(res)
        } else {
            editor.setValue("读取失败");
        }
    }, "json");

}

function getQtoa(qtoa, element) {
    html = '';

    $.get("/qToa", qtoa, function (res) {
        // console.log(res);
        if (res.code == 200) {
            // console.log(res.datas)
            // console.log(res.datas.length);
            if (res.datas.length > 0) {
                $.each(res.datas, function (i, item) {
                    if (item.answer === '') {
                        answer = '未回答'
                    } else {
                        answer = item.answer
                    }

                    html +=
                        ' <div class="layui-colla-item">' +
                        '      <h4 class="layui-colla-title">问题' + (i + 1) + ':&nbsp' + item.question + '</h4>' +
                        '      <div class="layui-colla-content" id="answer" style="white-space: pre-line;font-size: 16px;">' + answer +
                        '      </div>' +
                        '</div>';

                });

                $("#qtoa").html(html);
                element.render('layui-collapse');
            }
            // console.log(html)


        }
    }, "json");
}

/*关闭弹出框口*/
function x_admin_close() {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

function init() {
    console.log("1");
    var active = {
        //在这里给active绑定事件，后面可通过active调用这些事件
        tabDeleteAll: function (ids) {//删除所有
            $.each(ids, function (i, item) {
                element.tabDelete("xbs_tab", item);//ids是一个数组，里面存放了多个id，调用tabDelete方法分别删除
            })
        }
    };
    // console.log("active===" + active);
    var tabtitle = $(".layui-tab-title li");
    // console.log("tabtitle===" + tabtitle.dataset);
    var ids = new Array();
    $.each(tabtitle, function (i) {
        ids[i] = $(this).attr("lay-id");
        // console.log("==ids==" + ids)
    });
    active.tabDeleteAll(ids);

    sessionStorage.clear();
    window.location.replace("/");
}


// 执行编辑器中代码
function runEditorTest(context, expId) {
    // console.log(context);
    // console.log(expId);
    // if($("#print").length>0){
    //     $("#print").remove();
    // }
    // if($("#figure").length>0){
    //     $("#figure").remove();
    // }
    if($("#resultButton").length>0){
        $("#resultButton").remove();
    }

    if (context === "") {
        layer.open({
            content: "执行代码不能为空",
            title: false,
            closeBtn: false,
            skin: 'layui-layer-molv',
        })
    } else {

        $("#run").html("代码执行中，请耐心等待，不要关闭该页面！！！").addClass("layui-btn-disabled").prop("disabled", true)
        // window.encodeURI("/runeditor?context=" + context)
        $.post("/runEditor?context=" + encodeURIComponent(encodeURIComponent(context)) + "&expId=" + expId,/* data.field,*/ function (res) {
            // console.log(res);
            // '<span class="text">创建时间：' + item.createtime + '</span>' +
            // '  <a onclick="xadmin.open(\'查看结果\',\'simple_allResult.html?resultUrl=' + item.resulturl + '&figCount='+item.figCount+'&csvCount='+item.csvCount+'\',800,600)" title="查看结果"  href="javascript:;">' +
            // '          <i class="iconfont" style="font-size: 12px;">&nbsp;&nbsp; &#xe6e6;查看结果</i>' +
            // '   </a>' +'</div>';
//             runResult.setTag(String.valueOf(tag));
//             runResult.setFigCount(Integer.valueOf(figCount));
//             runResult.setCsvCount(Integer.valueOf(csvCout));
//
// //            i = 0;
// //            runResult.setFigName(RandomFilename + "_fig");
//             runResult.setFigName(RandomFilename);
//             runResult.setCodeName("stuCode/" + user.getUsername() + "/" + RandomFilename + "_result.py");
//             console.log(res.data);
            let print;
            let figure;
            let resultButton;
            if (res.code === 200) {


                resultButton = "<button class=\"layui-btn layui-bg-blue\"\n" +
                    "                        style=\"margin-left: 30px;\"\n" +
                    "                        onClick=\"xadmin.open('查看结果','/pages/simple_allResult.html?csvCount="+res.data.csvCount+"&figCount="+res.data.figCount+"&resulturl="+res.data.figName+"',850,650)\"\n" +
                    "                        id=\"resultButton\">查看结果" +
                    "                </button>" ;
                layer.msg("运行成功", {icon: 6, time: 900})
                $("#buttons").append(resultButton)

                $("#run").html("代码执行").removeClass("layui-btn-disabled").prop("disabled", false)

            } else {
                layer.open({
                    title: '运行失败',
                    content: '请稍后重新运行！！！'
                })
                $("#run").html("代码执行").removeClass("layui-btn-disabled").prop("disabled", false)

            }
        }, "json");
        return false;
    }
}



// 编辑器中填充内容
// editorid: 编辑器所属于的id名
// html: 像编辑器中填充的内容
function codeEditor(editorid, html) {


    var editor = ace.edit(editorid);
    //设置主题twilight
    editor.setTheme("ace/theme/chrome");
    editor.setTheme("ace/theme/twilight");

    // 设置编辑语言
    // editor.getSession().setMode("ace/mode/python");
    editor.setOption("mode", "ace/mode/python");
    // 设置字体大小
    editor.setFontSize(20);
    // 设置行高;
    document.getElementById(editorid).style.lineHeight = "30px";
    // console.log("初始化完成，开始赋值。。。。。");
    // 获取编辑内容
    editor.getValue();
    // console.log("现在的值："+editor.getValue());
    // 移动光标至第0行，第0列
    editor.moveCursorTo(1, 0);
    //设置只读（true时只读，用于展示代码）
    editor.setReadOnly(false);

    //自动换行,设置为off关闭
    editor.setOption("wrap", "free");

    editor.setValue(html);


    // console.log("赋的值："+editorValue);

    // console.log("设置完毕。。。："+editor.getValue());
}


// 保存编辑器中的代码在session中
function saveCode(value) {
    if (value) {
        // console.log("给session存值=" + value)
        if (value === sessionStorage.getItem(name)) {
            // console.log("保存的代码和session的值一致")
        } else {
            // console.log("这个是editor里的值得"+value)
            // console.log("不一致")
            sessionStorage.setItem(name, value);
            // console.log("这个是session里的值得"+sessionStorage.getItem(name))
            // console.log(sessionStorage.getItem(name))
        }
    } else {
        // console.log("不给session存值")
    }
}



// 填充实验代码内容
function initDiv(path, id) {
    $.post("/readCode?codePath=" + path, function (res) {
        if (res.code == 200) {
            // console.log("===代码内容==" + res.data)
            $("#" + id).html(res.data);
        } else {
            layer.msg("读取失败！");
        }
    }, "json");
}



//
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 获取文件名后缀
 * @param filename string 文件名，可以带路径
 * @return string/false 后缀，没有后缀时返回false
 */
function getSuffix(filename) {
    var index = filename.lastIndexOf('.');
    return index === -1 ? false : filename.substr(index + 1);
}


/**
 * 文件内容预览
 * @param file 文件
 * @param id  将文件内容显示的位置
 */
function readBlob(file, id) {

    var start = 0;
    var stop = file.size - 1;

    var reader = new FileReader();

    reader.onloadend = function (evt) {
        if (evt.target.readyState === FileReader.DONE) { // DONE == 2
            var content = evt.target.result;
            $("#"+id).html(content)
            // alert(content);
        }
    };
    var blob = file.slice(start, stop + 1);
    reader.readAsText(blob, "utf-8");
}

function changeRecord() {
    $("#upForm").css('display', 'block')
    $("#look").css('display', 'none')
}

function submitRecord() {

    id = $("#recordSubmit").val()
    console.log(id)
    if (id==='0'){
        $('#hideUpload').trigger('click');
    }else {
        flag = 1
        $.post("/submitRecord/"+id,function (res){
            if (res.code == 200){
                layer.msg("提交成功")
            }else {
                layer.msg("提交失败，请稍后重试")
            }
        })
    }
}

function prelook() {

    if ($('#expprocess').val() && $('#expresult').val() && files[0]) {

        $("#upForm").css('display', 'none')
        $("#look").css('display', 'block')
        $("#preProcess").html($('#expprocess').val())
        $("#preResult").html($('#expresult').val())
        readBlob(files[0], 'preRecordcode')
    }

}


/**
 *
 * @param name 多选框的name
 * @param id  多选框id
 * @param data 多选框数据
 */
function xms(name, id, data) {
    xmSelect.render({
        name: '' + name,
        el: '#' + id,
        filterable: true,
        layVerify: 'required',
        clickClose: true,
        filterMethod: function (val, item, index, prop) {
            if (val == item.value) {//把value相同的搜索出来
                return true;
            }
            if (item.name.indexOf(val) != -1) {//名称中包含的搜索出来
                return true;
            }
            return false;//不知道的就不管了
        },
        data: data
    })
}

/**
 *
 * @param obj id名称
 */

function wordLeg  (obj) {

    var currleg = $(obj).val().length;

    var length = $(obj).attr('maxlength');
    var cou = $(obj).attr('id');
    // console.log(cou)

    if (currleg > length) {

        layer.msg('字数请在' + length + '字以内');

    } else {

        $('.'+cou+'_count').text(currleg);

    }

}

// 分页获取实验数据
function getCsvData(csvPath,mPageSize,pageNo,lineId,colId,headId,bodyId,pagerId) {

    pageNo = pageNo || 1
    $.get("/readCsvLiu?csvPath=" + csvPath, function (res) {
        if (res.code === 200) {
            // console.log(res)
            start = ''
            end = ''
            head = ''
            if (res.datas.length > 0) {

                $("#"+lineId).html(res.line - 1)
                $("#"+colId).html(res.datas[0].split(',').length)
                $.each(res.datas[0].split(','), function (i, item) {
                    head += '<th>' + item + '</th>'
                })
                $("#"+headId).html(head)

                if (res.line > mPageSize) {
                    $("#"+pagerId).css('display', 'block')
                    fillDataTable(res.datas.slice(1), 0, mPageSize,bodyId);

                } else {
                    fillDataTable(res.datas.slice(1), 0, res.line - 1,bodyId)
                }

                layui.use('laypage', function () {
                    var laypage = layui.laypage;

                    //执行一个laypage实例
                    laypage.render({
                        elem: pagerId, //注意，这里的 test1 是 ID，不用加 # 号
                        count: res.line - 1, //数据总数，从服务端得到
                        limit: mPageSize,
                        curr: pageNo,
                        prev: "<",
                        next: ">",
                        groups: 2,
                        limits: [mPageSize],
                        layout: ['prev', 'page', 'next', 'limit', 'refresh'],
                        skip: false, //是否开启跳页
                        jump: function (obj, isfirst) {
                            if (!isfirst) {

                                start = (obj.curr - 1) * obj.limit
                                end = start + mPageSize
                                fillDataTable(res.datas.slice(1), start, end, bodyId);
                                // getCsvData(obj.curr, obj.limit)
                            }
                        }
                    });
                });
            } else {
                $("#"+bodyId).html("数据集为空");
            }
        } else {
            // console.log("失败")
            $("#"+bodyId).html(res.msg)
        }
    })

}


function fillDataTable(datas, start, end, id) {


    html = "<tr>";

    if (end > datas.length) {
        end = datas.length
    }
    for (i = start; i < end; i++) {
        $.each(datas[i].split(','), function (i, data) {
            html += '<td>' + data + '</td>'
        })
        html += "</tr>"
    }

    $('#'+id).html(html);
}


function getXlsxOrXlsData(dataPath,mPageSize,pageNo,headNum, headId,bodyId,pagerId){
    $.get("/readExcel?uploadPath=" + encodeURIComponent(encodeURIComponent(dataPath)), function (res) {
        // $.get("/readCsvLiu?csvPath=" + encodeURIComponent(encodeURIComponent(html2)), function (res) {

        if (res.code === 200) {


            row = parseInt(res.datas[0]) + 1;// 行
            colum = (res.datas.length - 1) / (row);//列

            // colum = Math.trunc((res.datas.length - 1) / (row));//列
            // console.log("==行==" + row)
            // console.log("==列==" + colum)
            html1 = '<tr>' +
                '<td>行数</td>' +
                '<td>' + (row-1) + '</td>' +
                '<td>列数</td>' +
                '<td>' + colum + '</td>' +
                +'</tr>'
            // console.log(dataPath)
            // console.log(html1)
            $("#"+headNum).html(html1);

            if ((row-1)  > mPageSize) {
                $("#"+pagerId).css('display', 'block')
                contentTable(0,1,res.datas, headId)
                contentTable(1,mPageSize+1,res.datas, bodyId)
            } else {
                contentTable(0,1,res.datas, headId)
                contentTable(1,row,res.datas, bodyId)
            }




            layui.use('laypage', function () {
                var laypage = layui.laypage;

                //执行一个laypage实例
                laypage.render({
                    elem: pagerId, //注意，这里的 test1 是 ID，不用加 # 号
                    count: row-1, //数据总数，从服务端得到
                    limit: mPageSize,
                    curr: pageNo,
                    prev: "<",
                    next: ">",
                    groups: 2,
                    limits: [mPageSize],
                    layout: ['prev', 'page', 'next', 'limit', 'refresh'],
                    skip: false, //是否开启跳页
                    jump: function (obj, isfirst) {
                        if (!isfirst) {
                            // $("#shares").html("")
                            start = (obj.curr - 1) * obj.limit+1
                            end = start + mPageSize
                            if (end>row){
                                end = row
                            }
                            contentTable(start,end,res.datas,bodyId)
                        }
                    }
                });
            });

        } else {
            layer.msg("读取失败！");
        }
    }, "json");

}

function contentTable(start,end,contents,id){
    html = ''
    for (i = start; i < end; i++) {
        html += '<tr>'
        for (j = 1; j <= colum; j++) {
            html +=
                '<td>' + contents[j + i * colum] + '</td>'
        }
        html += '</tr>'
    }
    // console.log(html)
    $("#"+id).html(html);
}



