//codes.js   
system = require('system')   
address = system.args[1];//���������еڶ������� ���������õ�   
//console.log('Loading a web page');   
var page = require('webpage').create();   
var url = address;   
//console.log(url);   


page.onResourceRequested = function(requestData, request) {
    if ((/http:\/\/.+?\.css/gi).test(requestData['url']) || requestData.headers['Content-Type'] == 'text/css') {
        // console.log('The url of the request is matching. Aborting: ' + requestData['url']);
        request.abort();
    }
};



page.open(url, function (status) {   
    //Page is loaded!   
    if (status !== 'success') {   
        console.log('Unable to post!');   
    } else {   
        //console.log(page.content);   
        //var title = page.evaluate(function() {   
        //  return document.title;//ʾ��������ʹ��ҳ����jsapiȥ����ҳ����  www.oicqzone.com 
        //  });   
        //console.log(title);   
           
        console.log(page.content);   
    }      
    phantom.exit();   
}); 