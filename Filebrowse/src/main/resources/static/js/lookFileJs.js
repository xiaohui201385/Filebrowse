$(document).ready(function() {
	
	//初始化时获取服务器上的文件信息
	/*$.ajax({
		type : "get",
		url : "",
		async : true,
		dataType : "json",
		success : function(data) {
			
		},
		error : function(data) {
			
		}
	});*/
	ShowTable(tableData);
});
//获取数据
var tableData=[
	{
		name:"平凡的世界",
		date:"2017-09-04",
		clickmun:"23",
		url:"C:\learn"
	},
	{
		name:"挪威的森林",
		date:"2017-09-03",
		clickmun:"20",
		url:"C:\learn"
	},
	{
		name:"上下五千年",
		date:"2017-09-02",
		clickmun:"18",
		url:"C:\learn"
	}
]
//根据获得的数据填充表格
function ShowTable(data){
	var table_html = "";
	var lookFileId="lookfile_"
	for(var i=0;i<data.length;i++){
		table_html +="<tr><td style='vertical-align: middle'>"+data[i].name+"</td>";
		table_html +="<td style='vertical-align: middle'>"+data[i].date+"</td>";
		table_html +="<td style='vertical-align: middle'>"+data[i].clickmun+"</td>";
		//table_html +="<td style='vertical-align: middle'><button type='button' onclick='lookfile(\""+data[i].url+"\",\""+data[i].clickmun+"\")' class='btn btn-warning' id='lookfile'>浏览</button></td></tr>";
		//table_html +="<td style='vertical-align: middle'><button type='button' onclick='lookfile(\""+data[i].url+"\",\""+data[i].clickmun+"\",\""+lookFileId+i+"\")' class='btn btn-warning' id='lookfile_0'>浏览</button></td></tr>";
	    table_html +="<td style='vertical-align: middle'><button type='button' onclick='lookfile(\""+data[i].url+"\",\""+data[i].clickmun+"\",\""+lookFileId+i+"\")' class='btn btn-warning' id='\""+lookFileId+i+"\"'>浏览</button></td></tr>";
	
	}
	
	$("#tableContent").html(table_html);
	$("th,td").addClass("text-center");
}

//搜索文件
function searchfile(){
	var searchname = $("#searchname").val();
	alert(searchname);
	$.ajax({
		type : "post",
		url : "",
		async : true,
		data:{name:searchname},
		dataType : "json",
		success : function(data) {
			ShowTable(data);
		},
		error : function(data) {
			
		}
	});
}

//浏览文件
function lookfile(url,clickmun,id){
	var url = url;    //获取文件地址
	var clickmun = clickmun;  //获取点击数
	console.log(url);
	console.log(clickmun);
	console.log(id);
	/*$.ajax({
		type : "get",
		url : "",
		async : true,
		data:{url:url,clickmun:clickmun},
		dataType : "",
		success : function(data) {
		},
		error : function(data) {
			
		}
	});*/
	wait_load(id);
}


//上传文件
function inputFile() {
	var file = $("#filename").val();
	if(file == '' || file == null) {
		alert("请选择所要上传的文件！");
	} else {
		var index = file.lastIndexOf(".");
		if(index < 0) {
			alert("上传的文件格式不正确，请上传Excel、Word、PDF文件");
		} else {
			var ext = file.substring(index + 1, file.length);
			if(ext == "xls" || ext == "xlsx" ||ext == "pdf"||ext == "docx"||ext == "doc") {
				//加载等待时转圈圈
				wait_load("foo");
				//$("#uploadFile").submit();
				$("#filename").text("");
			} else {
				alert("请上传Excel、Word、PDF文件");
			}
		}
	}
}

//加载等待时转圈圈
function wait_load(id){
	//alert(id)
	var spinnerOpts={
						lines: 13, // 共有几条线组成
					    length: 7, // 每条线的长度
					    width: 4, // 每条线的粗细
					    radius: 10, // 内圈的大小
					    corners: 1, // 圆角的程度
					    rotate: 0, // 整体的角度（因为是个环形的，所以角度变不变其实都差不多）
					    color: '#000', // 颜色 #rgb 或 #rrggbb
					    speed: 1, // 速度：每秒的圈数
					    trail: 60, // 高亮尾巴的长度
					    shadow: false, // 是否要阴影
					    hwaccel: false, // 是否用硬件加速
					    className: 'spinner', // class的名字
					    zIndex: 7, // z-index的值 2e9（默认为2000000000）
					    top: 'auto', // 样式中的top的值（以像素为单位，写法同css）
					    left: 'auto' // 样式中的left的值（以像素为单位，写法同css）	
					};
				var target=document.getElementById(id);				
				var spinner = new Spinner(spinnerOpts);
				spinner.spin(target);
}
