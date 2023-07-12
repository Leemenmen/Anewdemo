//获取url中的参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);

    // if (r != null) return unescape(r[2]);
    return null;
}

//
function encrpty(param) {
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(sessionStorage.pubKey);
    // console.log(encrypt.encrypt("123"))
    return encrypt.encrypt(param);
}

// 获取实验班级授权列表
function getTgrade() {
    html = '';
    expId = getQueryString("expid");
    // console.log(expId)
    // var exp = {
    //     "id": [[${session.loginUser.getId()}]],
    //     "expid": getQueryString("expid"),
    // }
    // $.get("/shares?flag="+flag, function (res) {
    $.get("/tGrades/" + expId, function (res) {
        // console.log(res);
        if (res.code == 200) {
            $.each(res.datas, function (i, item) {
                if (item != null) {
                    // console.log("实验id:"+item.id)
                    var staus = '';
                    var grant = '';
                    if (item.expid) {
                        staus = '已授权';
                        grant = '取消授权';

                    } else {
                        staus = '未授权';
                        grant = '点击授权';
                    }
                    path = encodeURIComponent(encodeURIComponent(item.storagePath))
                    html +=
                        '<tr>' +
                        '<td>' + (i + 1) + '</td>' +
                        '<td>' + item.gname + '</td>' +
                        '<td>' + staus + '</td>' +
                        '<td>' +
                        ' <button class="layui-btn"' +
                        '        style="margin-left: 30px; "' +
                        '                 onclick="updateTgrade(' + item.geid + ',' + item.gradeid + ',' + getQueryString("expid") + ')">' + grant +
                        ' </button>' +
                        '</td>';
                }
            });
            $("#grant").html(html);

        }
    }, "json");
}

// 授权更改
function updateTgrade(geid, gradeid, expid) {
    if (geid != null) {
        // console.log("===geid===:"+geid)
        layer.confirm('确认要取消吗？', {title: false, closeBtn: false, skin: 'layui-layer-molv'}, function (index) {
            $.post("/cancel?id=" + geid, function (res) {
                if (res.code == 200) {
                    layer.msg('取消成功!', {icon: 1, time: 1000});
                    getTgrade();
                } else {
                    layer.msg('取消失败!', {icon: 1, time: 1000});
                }
            });

        });
    } else {
        // console.log("===geid===:"+geid)
        var data = {
            "gradeid": gradeid,
            "expid": expid,
        }
        // console.log(data);
        layer.confirm('确认要授权吗？', {title: false, closeBtn: false, skin: 'layui-layer-molv'}, function (index) {
            $.post("/addGtoe", data, function (res) {
                if (res.code == 200) {
                    layer.msg('授权成功!', {icon: 1, time: 1000});
                    getTgrade();
                } else {
                    layer.msg('授权失败!', {icon: 1, time: 1000});
                }
            });

        });
    }
}


// 下载示例代码
function Download(path) {
    layer.confirm('是否看懂？', {title: false, closeBtn: false, skin: 'layui-layer-molv'}, function (index) {
        layer.confirm('是否下载？', {title: false, closeBtn: false, skin: 'layui-layer-molv'}, function (index) {
            location.href = '/downCode?uploadpath=null&flag=2&count=0&id=0&codepath=' + path;
            layer.close(layer.index)
        })
    })

}


// 开始实验
function StartExp(expId, expWork) {
    layer.confirm('是否看懂？', {title: false, closeBtn: false, skin: 'layui-layer-molv'}, function (index) {
        location.href = '/pages/exp_expwork?expid=' + expId + '&expwork=' + expWork;
    })
}

// 清空
function resetForm(files) {
    // let fileBox = $('#test3').parent();
    // fileBox.find('.layui-upload-choose').remove();

    files = []
    $("#fileTable").css('display', 'none');
    $("#demoList").empty();
    $("#resetForm")[0].reset();
    layui.form.render();
}

// 教师获取学生上传分享文件
function getTestShare(flag, pageNo, mPageSize, id) {
    pageNo = pageNo || 1
    html = '';
    $.get("/testShare/" + pageNo + "/" + mPageSize + "?flag=" + flag + "&id=" + id, function (res) {
        // console.log(res);
        if (res.code == 200) {
            // console.log(res.datas);
            $.each(res.datas, function (i, item) {

                if (item != null) {
                    // console.log("实验id:"+item.id)
                    if (flag === '0') {
                        path = 'code/' + item.storagePath;
                    } else {
                        path = 'data/' + item.storagePath;
                    }

                    status = ''
                    if (item.tid === 3) {
                        status = '<td style="color: red">未审批</td>'
                    }
                    if (item.tid === 0) {
                        status = '<td style="color: grey">审批未通过</td>'
                    }
                    if (item.tid === 1) {
                        status = '<td style="color: green">审批通过</td>'
                    }
                    html +=
                        '<tr>' +
                        '<td>' + (i + 1) + '</td>' +
                        '<td>' + item.username + '</td>' +
                        '<td>' + item.realname + '</td>' +
                        '<td>' + item.gname + '</td>' +
                        status +
                        '<td>' + item.uploadDes + '</td>' +
                        '<td>' +
                        '   <a onclick="xadmin.open(\'点击预览\',\'add_lookshare.html?storagePath=shareFile/' + path + '&flag=' + flag + '\',1200,800)" title="查看"  href="javascript:;">' +
                        '          <i class="iconfont" style="font-size: 15px;">&#xe6e6;</i>' +
                        '   </a>&nbsp' +
                        '   <a onclick="xadmin.open(\'审批\',\'add_checkshare.html?id=' + item.id + '&tid=' + item.tid + '&teachScore=' + item.teachScore + '&flag=' + flag + '&flagDes=' + item.flagDes + '\',500,300)" title="审批"  href="javascript:;">' +
                        '          <i class="iconfont" style="cfont-size: 15px;">&#xe71c;</i>' +
                        '   </a>&nbsp' +
                        '   <a href="/downCode?codepath=null&id=0&count=null&uploadpath=' + encodeURIComponent(encodeURIComponent(item.storagePath)) + '&flag=' + flag + '" title="下载">' +
                        '          <i class="iconfont" style="font-size: 15px;">&#xe714;</i>' +
                        '   </a>' +
                        '</tr>';
                }
            });
            $("#shares").html(html);
            $("#num").html(res.total);
            //执行一个laypage实例
            layui.use('laypage', function () {
                var laypage = layui.laypage;

                //执行一个laypage实例
                laypage.render({
                    elem: 'pager', //注意，这里的 test1 是 ID，不用加 # 号
                    count: res.total, //数据总数，从服务端得到
                    limit: mPageSize,
                    curr: pageNo,
                    prev: "<",
                    next: ">",
                    groups: 2,
                    limits: [mPageSize, 10, 30, 50, 100],
                    layout: ['prev', 'page', 'next', 'limit', 'refresh', 'skip'],
                    skip: true, //是否开启跳页
                    jump: function (obj, isfirst) {
                        if (!isfirst) {
                            mPageSize = obj.limit;
                            getTestShare(flag, obj.curr, obj.limit, id)
                        }
                    }
                });
            });


        }
    }, "json");
}

// 学生查看教师审核通过的共享文件
function getShares(flag, pageNo, mPageSize) {
    pageNo = pageNo || 1
    html = '';
    uploadDes = $("#uploadDes").val()
    // $.get("/shares?flag="+flag, function (res) {
    $.get("/shares/" + flag + "/" + pageNo + "/" + mPageSize + "?uploadDes=" + uploadDes, function (res) {
        // console.log(res);
        if (res.code === 200) {
            // console.log(res.datas);
            $.each(res.datas, function (i, item) {

                if (item != null) {
                    // console.log("实验id:"+item.id)
                    path = encodeURIComponent(encodeURIComponent(item.storagePath))
                    html +=
                        '<tr>' +
                        '<th>' + (i + 1) + '</th>' +
                        '<th>' + item.username + '</th>' +
                        '<th>' + item.uploadDes + '</th>' +
                        '<th>' + item.downloadCount + '</th>' +
                        '<th>' + item.teachScore + '</th>' +
                        '<th><a href="/downCode?codepath=null&id=' + item.id + '&count=' + item.downloadCount + '&uploadpath=' + path + '&flag=' + flag + '">点击下载</a></th>' +
                        // '<th><a href="/download?codepath=null&id='+item.id+'&count='+item.downloadCount+'&uploadpath=' + item.storagePath + '">下载</a></th>' +
                        '</tr>';
                }
            });
            $("#num").html(res.total)
            $("#shares").html(html);
            layui.use('laypage', function () {
                var laypage = layui.laypage;

                //执行一个laypage实例
                laypage.render({
                    elem: 'pager', //注意，这里的 test1 是 ID，不用加 # 号
                    count: res.total, //数据总数，从服务端得到
                    limit: mPageSize,
                    curr: pageNo,
                    prev: "<",
                    next: ">",
                    groups: 2,
                    limits: [10, 30, 50, 100],
                    layout: ['prev', 'page', 'next', 'limit', 'refresh', 'skip'],
                    skip: true, //是否开启跳页
                    jump: function (obj, isfirst) {
                        if (!isfirst) {
                            mPageSize = obj.limit;
                            getShares(flag, obj.curr, obj.limit)
                        }
                    }
                });
            });


        }
    }, "json");
}


// 教师获取所有实验
function getExp() {
    html = '';
    var exp = {
        "expname": $('input[name="expname"]').val(),
    }
    // $.get("/shares?flag="+flag, function (res) {
    $.post("/getExps", exp, function (res) {
        // console.log(res);
        if (res.code === 200) {
            // console.log(res);
            if (res.datas !== '') {
                $.each(res.datas, function (i, item) {

                    // console.log("实验id:"+item.id)
                    path = "dmspCode/" + item.expcodeurl
                    html +=
                        '<tr>' +
                        '<td>' + (i + 1) + '</td>' +
                        '<td>' + item.expname + '</td>' +
                        '<td>' + item.exptarget + '</td>' +
                        '<td>' + item.expwork + '</td>' +
                        '<td>' +
                        '   <a onclick="xadmin.open(\'点击预览\',\'add_lookshare.html?storagePath=' + path + '&flag=0\',800,600)" title="点击预览"  href="javascript:;">' +
                        '          <span>' + item.expcodeurl + '</span>' +
                        '   </a>&nbsp' +
                        '</td>' +
                        // '<td>' + item.expcodeurl + '</td>' +
                        '<td>' +
                        '   <a onclick="xadmin.open(\'授权情况\',\'add_expgranted.html?expid=' + item.id + '\',600,400)" title="授权情况"  href="javascript:;">' +
                        '          <i class="iconfont" style="font-size: 15px;">&#xe6e6;</i>' +
                        '   </a>' +
                        '</td>' +
                        // '<th><a href="/download?codepath=null&id='+item.id+'&count='+item.downloadCount+'&uploadpath=' + item.storagePath + '">下载</a></th>' +
                        '</tr>';

                });
                $("#num").html(res.datas.length)

            } else {
                html += '还没有实验';
            }
            $("#exps").html(html);

        }
    }, "json");
}

// 教师获取班级学生提交的实验报告
function getRecord(gname, expname) {
    user = {
        expid: getQueryString("expid"),
        gid: getQueryString("gradeid"),
    }
    html = '';
    submitCount = 0
    unSubCount = 0
    checkedCount = 0
    $.get("/getGradeRecordsByExp", user, function (res) {
        // console.log(res.datas)
        statusSub = ''
        score = ''
        items = res.datas
        htmlbtn = ''
        statusflag = 0
        scoreflag = 0
        // console.log(res.datas)
        $.each(res.datas, function (i, item) {
            if (item.recordcode && item.flag === 2) {

                // if (item.recordcode && item.flag === 1) {
                submitCount += 1
                statusflag = 1
                if (item.score){
                    htmlbtn = '<td><button class="layui-btn" onclick="xadmin.open(\'评阅\',\'/pages/step_teachStepCheck.html?index=' + (i + 2) + '&expname=' + expname + '&gname=' + gname + '\',1200,700)">查看打分</button></td>'
                }else {
                    htmlbtn = '<td><button class="layui-btn" onclick="xadmin.open(\'评阅\',\'/pages/step_teachStepCheck.html?index=' + (i + 2) + '&expname=' + expname + '&gname=' + gname + '\',1200,700)">点击评阅</button></td>'
                }

                // htmlbtn = '<td><button class="layui-btn" onclick="xadmin.open(\'评阅\',\'/pages/simple_teacheck?index=' + (i + 2) + '&expname=' + expname + '&gname=' + gname + '\',1200,700)">点击评阅</button></td>'
            } else {
                unSubCount += 1
                htmlbtn = '<td><button style="background-color: grey" class="layui-btn" onclick="layer.msg(\'学生还未提交报告\',{time:1000})">点击评阅</button></td>'

            }
            if (item.score) {
                checkedCount += 1
                score = item.score
                scoreflag = 1
            }
            // } else {
            //     score = '未打分'
            // }

            if (statusflag === 0) {
                statusSub = '<td style="color: red">未提交</td><td style="color: red">未打分</td>'
            } else {
                if (scoreflag === 0) {
                    statusSub = '<td style="color: goldenrod">已提交</td><td style="color: goldenrod">未打分</td>'
                } else {
                    statusSub = '<td style="color: green">已提交</td><td style="color: green">' + item.score + '</td>'
                }
            }

            // console.log(data)
            html += '<tr>' +
                '<td>' + (i + 1) + '</td>' +
                '<td>' + item.username + '</td>' +
                '<td>' + item.realname + '</td>'
                + statusSub + score +
                '<td style="display: none">' + item.expprocess + '</td>' +
                '<td style="display: none">' + item.expresult + '</td>' +
                '<td style="display: none">' + item.recordcode + '</td>' +
                '<td style="display: none">' + item.id + '</td>' +
                '<td style="display: none">' + item.score + '</td>' +
                '<td style="display: none">' + item.experience + '</td>' +
                '<td style="display: none">' + item.png + '</td>' +
                '<td style="display: none">' + item.csv + '</td>' +
                '<td style="display: none">' + item.videoDesc + '</td>' +
                '<td style="display: none">' + item.dataDesc + '</td>' +
                '<td style="display: none">' + item.totalDesc + '</td>' +
                htmlbtn +
                '</tr>'

            // console.log(statusSub)
            // console.log(score)
            statusflag = 0
            scoreflag = 0
        })
        $("#getusers").html(html);

        if (checkedCount === submitCount) {
            htmlCount = "<tr>" +
                "                            <td>已评阅</td><td style=\"color: green\">" + checkedCount + "</td>" +
                "                            <td>已提交</td><td style=\"color: green\">" + submitCount + "</td>";
            if (unSubCount === 0) {
                htmlCount += "  <td>未提交</td><td style=\"color: green\">" + unSubCount + "</td></tr>"
            } else {
                htmlCount += "  <td>未提交</td><td style=\"color: red\">" + unSubCount + "</td></tr>"
            }
            $("#statistic").html(htmlCount)
        } else {
            $("#statistic").html("<tr>" +
                "                            <td>已评阅</td><td style=\"color: green\">" + checkedCount + "</td>" +
                "                            <td>已提交</td><td style=\"color: goldenrod\">" + submitCount + "</td>" +
                "                            <td>未提交</td><td style=\"color: red\">" + unSubCount + "</td>" +
                "                        </tr>")
        }

    })
}

