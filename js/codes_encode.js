system = require('system')
//var fs = require('fs')   
var url  = system.args[1];//��������еڶ������� ���������õ� 
var encode = system.args[2];
var page = require('webpage').create();
//phantom.outputEncoding="GBK"; 
phantom.outputEncoding=encode;    
page.open(url, function (status) {   
    //Page is loaded!   
    if (status !== 'success') {   
        console.log('Unable to post!');
        phantom.exit();   		
    } else {   
           
        //save to file
		//try {
        //  fs.write("star.txt", page.content, 'w');
        //} catch(e) {
        //    console.log(e);
        //}
		window.setTimeout(wait, 5000);
    }
   
}); 
 
 function wait(){    
	console.log(page.content);
    phantom.exit();   		
 }