//公共通用代码
//共通代码
var baseUrl = 'http://localhost:7777/hr-student';

//全局ajax事件回调，处理ajax请求session超时
$.ajaxSetup({
	complete:function(xhr){
		var sessionStatus = xhr.getResponseHeader('sessionStatus');
		if(sessionStatus == 'timeout'){
			alert('登录超时，请重新登录');
			location.href = baseUrl + '/logout.jsp';
		}
	}
	
	
});
