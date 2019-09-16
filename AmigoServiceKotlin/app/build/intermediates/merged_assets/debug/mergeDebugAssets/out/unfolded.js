(function(){
    document.getElementsByClassName("Unfolded-btn")[0].onclick=function(){
       window.AndroidWebView.unfold("Unfolded-btn");
       //alert('clientHeight:'+document.body.clientHeight+";"+'contentHeight:'+document.getElementsByTagName('article').item(0).offsetHeight);
    };
}())