(function(){
//    document.getElementsByTagName('article').item(0).onscroll=function(){
//       //window.AndroidWebView.unfold("Unfolded-btn");
//       //alert('clientHeight:'+document.body.clientHeight+";"+'contentHeight:'+document.getElementsByTagName('article').item(0).offsetHeight);
//       alert('scrollHeight:'+document.body.scrollHeight)
//    };
//    document.getElementById("J_article").onscroll=function(){
//    alert('scrollHeight:'+document.getElementById("J_article").scrollTop)
//    };
  return document.getElementById("J_article").scrollTop || document.body.scrollTop
}())