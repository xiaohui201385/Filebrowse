$(document).ready(function() {

	// 初始化时获取服务器上的文件信息
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
			console.log(data);
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
					+ data[i].name + "\",\"" + i + "\")'>" + data[i].name
					+ "</a></li>"
		} else {
			ul += "<li class='color text-center' id=\"li_"
					+ i
					+ "\" style='list-style:none'><a  href='#' type='button' onclick='clickaction(\""
					+ data[i].name + "\",\"" + i + "\")'>" + data[i].name
					+ "</a></li>"
		}
		/*
		 * if(i==0){ typeName = data[i].name; ul +="<li class='  active text-center' style='list-style:none'><button
		 * class='btn btn-link active'
		 * onclick='clickaction(\""+data[i].name+"\")'>"+data[i].name+"</button></li>"
		 * }else{ ul +="<li class=' text-center' style='list-style:none'><button
		 * class='btn btn-link '
		 * onclick='clickaction(\""+data[i].name+"\")'>"+data[i].name+"</button></li>" }
		 */
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

			var tablethead = "<tr><th width='50%'>文件名</th><th width='25%'>上传日期</th><th width='25%'>操作</th></tr>"
			var table_html = "";
			if (data.list.length == 0) {
				table_html = "<span>该分类下未有文件</span>"
			} else {
				var lookFileId = "lookfile_"
				for (var i = 0; i < data.list.length; i++) {
					
					var url =lookfile(data.list[i].fileName);
					table_html += "<tr><td style='vertical-align: middle'>"
							+ data.list[i].fileName + "</td>";
					
					var datetime = new Date(data.list[i].createTime).format('yyyy-MM-dd h:m:s');
					
					table_html += "<td style='vertical-align: middle'>"
							+ datetime + "</td>";
					
					table_html += "<td style='vertical-align: middle'><form action='downloadFile' method='post'><a type='button' href='"+url+"' target='"+data.list[i].id+"' class='btn btn-warning'  style='margin-right:20px;'>浏览</a>"
					table_html += "<button type='submit'  class='btn btn-primary'>下载</button><input type='text' style='display:none' value=\""
							+ data.list[i].fileName
							+ "\" name='fileName'/> <input type='text' style='display:none' value=\""
							+ datetime
							+ "\" name='createTime'/></form></td></tr>";

				}

			}
			$("#tablethead").html(tablethead);
			$("#tableContent").html(table_html);
		} else {
			var tablethead = "<tr><th width='50%'>文件名</th><th width='10%'>类型</th><th width='20%'>上传日期</th><th width='20%'>操作</th></tr>"
			var table_html = "";
			if (data.list.length == 0) {
				table_html = "<span>找不到任何相关文件</span>"
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
					
					table_html += "<td style='vertical-align: middle'><form action='downloadFile' method='post'><a type='button' href='"+url+"' target='"+data.list[i].id+"' class='btn btn-warning'  style='margin-right:20px;'>浏览</a>"
					
					table_html += "<button type='submit'  class='btn btn-primary'>下载</button><input type='text' style='display:none' value=\""
							+ data.list[i].fileName
							+ "\" name='fileName'/> <input type='text' style='display:none' value=\""
							+ datetime
							+ "\" name='createTime'/></form></td></tr>";

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

//$(function () {
//	$("#mySelect").select({
//		width: "200px"
//	});
//});

// 上传时显示类型
function showTypeUp(data) {
//	class='form-control'
	/*var str = "<select class='selectpicker' data-size='6' name='type' id='select_id' > <option selected='selected' >请选择</option><option >自定义</option><option onclick='selectClick()'>自定义</option><option >自定义</option><option >自定义</option>";
	for (var i = 0; i < data.length; i++) {
		str += "<option  value=\"" + data[i].name + "\">" + data[i].name
				+ "</option>"
	}*/
	
	var str = "<select class='form-control' data-size='6' name='type' id='select_id' > <option selected='selected' >请选择</option><option >自定义</option>";
	for (var i = 0; i < data.length; i++) {
		str += "<option  value=\"" + data[i].name + "\">" + data[i].name
				+ "</option>"
	}
	
	str += "</select>";
	$("#showoclassselect").html(str);
	
//	$("#select_id").select({
//		width: "200px"
//	});
	
	
	
	$("#select_id").change(function(){
		
	       var selected=$(this).children('option:selected').val()
	       if(selected=="自定义"){
	    	   $('#addclass').modal();
	       }
	   });
}

// 点击左边
function clickaction(name, li_id) {
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
			pageNow = data.pageNum;
			pages = data.pages;
			judgePage(pageNow, pages);
			ShowTable(data);
		},
		error : function(data) {
		}
	});

}

// 搜索文件
function searchfile() {
	$('li').removeClass('fristcolor');
	$('li').removeClass('color');
	$('li').addClass('color');
	searchname = $("#searchname").val();
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

			console.log(data);
			pageNow = data.pageNum;
			pages = data.pages;
			judgePage(pageNow, pages);
			ShowTable(data, 1);
		},
		error : function(data) {

		}
	});
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

// 浏览文件
function lookfile(name) {
	var name = name; // 获取文件

	var type = typeName; // 获取文件类型

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

// 上传文件
function inputFile() {
	var select = $("#select_id").val();
	
	var file = $("#filename").val();
	if(select =="请选择" || select =="自定义"){
		alert("请选择要上传文档的类型！");
	}else{
		if (file == '' || file == null) {
			alert("请选择所要上传的文件！");
		} else {
			var index = file.lastIndexOf(".");
			if (index < 0) {
				alert("上传的文件格式不正确，请上传Excel、Word、PDF文件");
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
					alert("请上传Excel、Word、PDF文件");
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
	var name = $("#className").val();
	if(name==null||name==""){
		alert("请输入要添加的类型名称!");
	}else{
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

			}
		});
		$.ajax({
			type : "post",
			url : "typeList",
			async : true,
			dataType : "json",
			success : function(data) {
				console.log(data);
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
//关闭自定义添加按钮
function closeModal(){
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
function selectFocus(){  
    document.getElementById("select_id").setAttribute("size","5");  
}  
function selectClick(){  
    document.getElementById("select_id").removeAttribute("size");  
    document.getElementById("select_id").blur();  
    this.setAttribute("select_id","");  
}  
