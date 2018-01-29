
system = require('system') 
var url = system.args[1]; 
var encode = system.args[2];
  
var page = require('webpage').create();
phantom.outputEncoding=encode; 
page.viewportSize = {
  width: 1920,
  height: 1080
};  
  
  page.open(url, function (status) {   
    //Page is loaded!   
    if (status !== 'success') {   
         console.log('Unable to post!');
		 phantom.exit();   
    } else {
		 //console.log('start do task.');
		 doTask();
    }      
   
}); 
   
 function doTask(){
	 //page.evaluate(function(){  
        //此函数在目标页面执行的，上下文环境非本phantomjs，所以不能用到这个js中其他变量         
        //window.scrollTo(0,10000);//滚动到底部  
    //});  
   //var scrollHeight=getScrollHeight();
	 	 
	   // console.log("start scroll...");
        page.evaluate(function(){  
        //此函数在目标页面执行的，上下文环境非本phantomjs，所以不能用到这个js中其他变量
           var scrollHeight= document.body.scrollHeight;		
           window.scrollTo(0,scrollHeight);//滚动到底部  
        }); 
　 
   // console.log("sleep 5s.");
    window.setTimeout(check, 5000);      
 }
 
 function check(){    
       // console.log("after 5s."); 
        if(isBottom()){
			// console.log("已经到最底部了！");
			 console.log(page.content);   
			 phantom.exit(); 
		}else{
			doTask();
		}
       
 }
 
 
 function isBottom(){
	var scrollTop=getScrollTop();
	var clientHeight =getClientHeight();
	var scrollHeight=getScrollHeight();
	
	//console.log('scrollTop='+scrollTop);
	//console.log('clientHeight='+clientHeight);
	//console.log('scrollHeight='+scrollHeight);
	
	if(scrollTop + clientHeight == scrollHeight){
		//console.log("why");
		return true;
	}
	//console.log("why false");
	return false;
 }
 
 function getScrollTop(){
	 return page.evaluate(function(){
          var h= document.body.scrollTop;
          return h
    });
 }
 function getClientHeight(){
	 return 1080;
 }
function getScrollHeight(){
	 return page.evaluate(function(){
          var h= document.body.scrollHeight;
          return h
    });
 }