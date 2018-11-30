/**
 * 调用函数，传入url的参数名称，返回对应的参数值
 * @param {Object} key
 */
function getParam(key) {
	
	var url = location.href;

	var index = url.indexOf('?');
	
	url = url.substr(index + 1);
	
	var arr = url.split('&');
	
	for (var i = 0;i < arr.length;i ++) {
		
		var s = arr[i].split('=');
		
		if (s[0] == key)
			return s[1];
		
	}		
}	
