//禁止所有ajax缓存
$(function(){
    $.ajaxSetup ({
        cache: false //false为关闭，ture为打开
    });
});

$(document).ready(function() {

	// 初始化时获取服务器上的文档信息
	$.ajax({
		type : "get",
		url : "types",
		async : true,
		dataType : "json",
		success : function(data) {
			showType(data);
			showTypeUp(data);
		},
		error : function(data) {

		}
	});
	$.ajax({
		type : "post",
		url : "typeList",
		async : true,
		dataType : "json",
		success : function(data) {
			
			pageNow = data.pageNum;
			pages = data.pages;
			judgePage(pageNow, pages);
			ShowTable(data);
		},
		error : function(data) {
		}
	});
	$("#searchname").val("");
//	ShowTable(showdata);

});
// 存放搜索时的数据
var searchname = "";
// 获取分类值
var typeName = "";
// 设置是搜索还是分类显示
var state = 0;
// 当前页码
var pageNow = "";
// 总页码
var pages = "";

//新添加类型值
var newTypeName ="";

// var menudata = ["通知","会议","政策","活动"]
// 显示左边菜单
function showType(data) {
	
	var ul = "<ul id='treeMenu' class='' >";
	for (var i = 0; i < data.length; i++) {
		
		if (i == 0) {
			typeName = data[i].name;
			ul += "<li class=' fristcolor  text-center' id=\"li_"
					+ i
					+ "\" style='list-style:none'><a href='#' type='button'  onclick='clickaction(\""
					+ data[i].name + "\",\"" + i + "\",\"" + data[i].id + "\")'>" + data[i].name
					+ "</a></li>"
		} else {
			ul += "<li class='color text-center' id=\"li_"
					+ i
					+ "\" style='list-style:none'><a  href='#' type='button' onclick='clickaction(\""
					+ data[i].name + "\",\"" + i + "\",\"" + data[i].id + "\")'>" + data[i].name
					+ "</a></li>"
		}
		
	}
	;
	ul += "</ul>";
	$("#showMen").html(ul);
}

//设置时间格式
Date.prototype.format = function(format) {
    var date = {
           "M+": this.getMonth() + 1,
           "d+": this.getDate(),
           "h+": this.getHours(),
           "m+": this.getMinutes(),
           "s+": this.getSeconds(),
           "q+": Math.floor((this.getMonth() + 3) / 3),
           "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
           format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
           if (new RegExp("(" + k + ")").test(format)) {
                  format = format.replace(RegExp.$1, RegExp.$1.length == 1
                         ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
           }
    }
    return format;
}
// 根据获得的数据填充表格
function ShowTable(data, state) {
	if (data.list) {
		if (state == "" || state == null) {
			var tablethead = "<tr><th width='50%'>文档名</th><th width='25%'>上传日期</th><th width='25%'>操作</th></tr>"
			var table_html = "";
			if (data.list.length == 0) {
				table_html = "<span>该分类下未有文档</span>"
			} else {
				var lookFileId = "lookfile_"
				for (var i = 0; i < data.list.length; i++) {
					
					var url =lookfile(data.list[i].fileName);
					table_html += "<tr><td style='vertical-align: middle'>"
							+ data.list[i].fileName + "</td>";
					
					var datetime = new Date(data.list[i].createTime).format('yyyy-MM-dd h:m:s');
					
					table_html += "<td style='vertical-align: middle'>"
							+ datetime + "</td>";
					
					table_html += "<td style='vertical-align: middle'><form action='downloadFile' method='post'><a type='button' href='"+url+"' target='"+data.list[i].id+"' class='btn btn-warning'  style='margin-right:20px;'>预览</a>"
					table_html += "<button type='submit'  class='btn btn-primary'>下载</button><input type='text' style='display:none' value=\""
							+ data.list[i].fileName
							+ "\" name='fileName'/> <input type='text' style='display:none' value=\""
							+ data.list[i].id
							+ "\" name='id'/></form></td></tr>";

				}

			}
			$("#tablethead").html(tablethead);
			$("#tableContent").html(table_html);
		} else {
			var tablethead = "<tr><th width='50%'>文档名</th><th width='10%'>类型</th><th width='20%'>上传日期</th><th width='20%'>操作</th></tr>"
			var table_html = "";
			if (data.list.length == 0) {
				table_html = "<span>找不到任何相关文档</span>"
			} else {
				var lookFileId = "lookfile_"
				for (var i = 0; i < data.list.length; i++) {
					var url =lookfile(data.list[i].fileName);
					table_html += "<tr><td style='vertical-align: middle'>"
							+ data.list[i].fileName + "</td>";
					var datetime = new Date(data.list[i].createTime).format('yyyy-MM-dd h:m:s');
					
					table_html += "<td style='vertical-align: middle'>"
							+ data.list[i].typeName + "</td>";
					table_html += "<td style='vertical-align: middle'>"
							+ datetime + "</td>";
					
					table_html += "<td style='vertical-align: middle'><form action='downloadFile' method='post'><a type='button' href='"+url+"' target='"+data.list[i].id+"' class='btn btn-warning'  style='margin-right:20px;'>预览</a>"
					
					table_html += "<button type='submit'  class='btn btn-primary'>下载</button><input type='text' style='display:none' value=\""
							+ data.list[i].fileName
							+ "\" name='fileName'/> <input type='text' style='display:none' value=\""
							+ data.list[i].id
							+ "\" name='id'/></form></td></tr>";
				}
			}

			$("#tablethead").html(tablethead);
			$("#tableContent").html(table_html);
			$('#fristcolor').removeClass('fristcolor');
			$('#fristcolor').addClass('color');

		}

		$("th,td").addClass("text-center");
	}

}

// 下载
function downfile(name, time) {
	$.ajax({
		type : "post",
		url : "downloadFile",
		async : false,
		data : {
			fileName : name,
			createTime : time
		},
		success : function() {
			console.log(111);
		},
		error : function(data) {
			console.log(2222);
		}
	});
	return false;
}


// 上传时显示类型
function showTypeUp(data) {

	var t = 0;
	var index = -1;
	
	for(var i=0;i<data.length ;i++){
		if(data[i].name == newTypeName){
			t=1;
			index = i;
		}
	}
	if(t==1){
		$('#select_id.selectpicker').append("<option >自定义</option>");
		$.each(data, function (i) {
			if(index == i){
				$('#select_id.selectpicker').append("<option selected='selected' value=" + data[i].name + ">" + data[i].name + "</option>");

			}else{
				$('#select_id.selectpicker').append("<option value=" + data[i].name + ">" + data[i].name + "</option>");
			}
		  });
	}else{
		$('#select_id.selectpicker').append("<option selected='selected' >请选择</option><option >自定义</option>");
		$.each(data, function (i) {
		      $('#select_id.selectpicker').append("<option value=" + data[i].name + ">" + data[i].name + "</option>");

		  });
	}
	
	
	


  $('#select_id').selectpicker('refresh');
	
	
	$("#select_id").change(function(){
		
	       var selected=$(this).children('option:selected').val()
	       if(selected=="自定义"){
	    	   $('#addclass').modal();
	       }
	   });
}

// 点击左边
function clickaction(name, li_id,id) {
	typeName = name;
	state = 0;
	$('li').removeClass('fristcolor');
	$('li').removeClass('color');
	$('li').addClass('color');
	$("#li_" + li_id + "").addClass('fristcolor');
	$("#searchname").val("");
	$.ajax({
		type : "post",
		url : "typeList",
		async : true,
		data : {
			type : name
		},
		dataType : "json",
		success : function(data) {
			//console.log(data);
			pageNow = data.pageNum;
			pages = data.pages;
			judgePage(pageNow, pages);
			ShowTable(data);
		},
		error : function(data) {
			alert("该类型已经被修改，请刷新页面!")
		}
	});

}

// 搜索文档
function searchfile() {
	
	searchname = $("#searchname").val();
	if(searchname==null || searchname==""){
		alert("请输入要搜索的文档名");
	}else{
		$('li').removeClass('fristcolor');
		$('li').removeClass('color');
		$('li').addClass('color');
		
		state = 1;
		$.ajax({
			type : "post",
			url : "search",
			async : true,
			data : {
				string : searchname
			},
			dataType : "json",
			success : function(data) {

				
				pageNow = data.pageNum;
				pages = data.pages;
				judgePage(pageNow, pages);
				ShowTable(data, 1);
			},
			error : function(data) {

			}
		});
	}
	
}

// 判断是否是第一页或者最后一页
function judgePage(pageNow, pages) {
	if (pageNow > 1)
		$('#lastpage').removeClass('disabled');
	else
		$('#lastpage').addClass('disabled');

	if (pageNow < pages)
		$('#nextpage').removeClass('disabled');
	else
		$('#nextpage').addClass('disabled');

}

// 分页
function page(e) {
	if (state == 0) {
		if (e == 0) {
			$.ajax({
				type : "post",
				url : "typeList",
				async : true,
				data : {
					type : typeName,
					pageNum : 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data);
				},
				error : function(data) {
				}
			});
		}
		if (e == 1) {
			$.ajax({
				type : "post",
				url : "typeList",
				async : true,
				data : {
					type : typeName,
					pageNum : pageNow - 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data);
				},
				error : function(data) {
				}
			});
		}
		if (e == 2) {
			$.ajax({
				type : "post",
				url : "typeList",
				async : true,
				data : {
					type : typeName,
					pageNum : pageNow + 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data);
				},
				error : function(data) {
				}
			});
		}
	}
	if (state == 1) {
		$('li').removeClass('fristcolor');
		$('li').removeClass('color');
		$('li').addClass('color');
		if (e == 0) {
			$.ajax({
				type : "post",
				url : "search",
				async : true,
				data : {
					string : searchname,
					pageNum : 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data, 1);
				},
				error : function(data) {
				}
			});
		}
		if (e == 1) {
			$.ajax({
				type : "post",
				url : "search",
				async : true,
				data : {
					string : searchname,
					pageNum : pageNow - 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data, 1);
				},
				error : function(data) {
				}
			});
		}
		if (e == 2) {
			$.ajax({
				type : "post",
				url : "search",
				async : true,
				data : {
					string : searchname,
					pageNum : pageNow + 1
				},
				dataType : "json",
				success : function(data) {
					pageNow = data.pageNum;
					pages = data.pages;
					judgePage(pageNow, pages);
					ShowTable(data, 1);
				},
				error : function(data) {
				}
			});
		}
	}
	return false;

}

// 预览文档
function lookfile(name) {
	var name = name; // 获取文档

	var type = typeName; // 获取文档类型

	var ViewUrlMask = "http:\u002f\u002fdocviewserver.docview.com\u002fop\u002fview.aspx?src=WACFILEURL";
	var EmbedCodeMask = "\u003ciframe src=\u0027http:\u002f\u002fcdz.read.com\u002fop\u002fembed.aspx?src=WACFILEURL\u0027 width=\u0027476px\u0027 height=\u0027288px\u0027 frameborder=\u00270\u0027\u003eThis is an embedded \u003ca target=\u0027_blank\u0027 href=\u0027http:\u002f\u002foffice.com\u0027\u003eMicrosoft Office\u003c\u002fa\u003e document, powered by \u003ca target=\u0027_blank\u0027 href=\u0027http:\u002f\u002foffice.com\u002fwebapps\u0027\u003eOffice Web Apps\u003c\u002fa\u003e.\u003c\u002fiframe\u003e";
	var UrlPlaceholder = "WACFILEURL";

	var OriginalUrlElementId = "OriginalUrl";
	var GeneratedViewUrlElementId = "GeneratedViewUrl";
	var GeneratedEmbedCodeElementId = "GeneratedEmbedCode";
	var CopyViewUrlLinkId = "CopyViewUrl";
	var CopyEmbedCodeLinkId = "CopyEmbedCode";

	var originalUrl = "http://docviewserver.docview.com/op/docview/" + type
			+ "/" + name;

	var generatedViewUrl = ViewUrlMask.replace(UrlPlaceholder,
			encodeURIComponent(originalUrl));

	return generatedViewUrl;

}

// 上传文档
function inputFile() {
	var select = $("#select_id").val();
	
	var file = $("#filename").val();
	if(select =="请选择" || select =="自定义"){
		alert("请选择要上传文档的类型！");
	}else{
		if (file == '' || file == null) {
			alert("请选择所要上传的文档！");
		} else {
			var index = file.lastIndexOf(".");
			if (index < 0) {
				alert("上传的文档格式不正确，请上传Excel、Word、PDF文档");
			} else {
				var ext = file.substring(index + 1, file.length);
				if (ext == "xls" || ext == "xlsx" || ext == "pdf" || ext == "docx"
						|| ext == "doc" || ext == "wps" || ext == "ppt"
						|| ext == "pptx") {
					// 加载等待时转圈圈
					wait_load("foo");
					$("#uploadFile").submit();
					$("#filename").text("");
				} else {
					alert("请上传Excel、Word、PDF文档");
				}
			}
		}
	}
	
}

// 加载等待时转圈圈
function wait_load(id) {
	// alert(id)
	var spinnerOpts = {
		lines : 13, // 共有几条线组成
		length : 7, // 每条线的长度
		width : 4, // 每条线的粗细
		radius : 10, // 内圈的大小
		corners : 1, // 圆角的程度
		rotate : 0, // 整体的角度（因为是个环形的，所以角度变不变其实都差不多）
		color : '#000', // 颜色 #rgb 或 #rrggbb
		speed : 1, // 速度：每秒的圈数
		trail : 60, // 高亮尾巴的长度
		shadow : false, // 是否要阴影
		hwaccel : false, // 是否用硬件加速
		className : 'spinner', // class的名字
		zIndex : 7, // z-index的值 2e9（默认为2000000000）
		top : 'auto', // 样式中的top的值（以像素为单位，写法同css）
		left : 'auto' // 样式中的left的值（以像素为单位，写法同css）
	};
	var target = document.getElementById(id);
	var spinner = new Spinner(spinnerOpts);
	spinner.spin(target);
}

//添加自定义类型
function addclass(){
	$('#select_id.selectpicker').empty();
	var name = $("#className").val();
	if(name==null||name==""){
		alert("请输入要添加的类型名称!");
	}else{
		newTypeName =name;
		$.ajax({
			type : "get",
			url : "types-after",
			async : true,
			data:{typeName:name},
			dataType : "json",
			success : function(data) {
				console.log(data);
				showType(data);
				showTypeUp(data);
				$("#addclass").modal('hide');
				
			},
			error : function(data) {
				alert("添加失败！")
			}
		});
		
	}
	
}
//关闭自定义添加按钮
function closeModal(){
	$('#select_id.selectpicker').empty();

	$.ajax({
		type : "get",
		url : "types",
		async : true,
		dataType : "json",
		success : function(data) {
			showType(data);
			showTypeUp(data);
			$("#addclass").modal('hide');

		},
		error : function(data) {

		}
	});
}



