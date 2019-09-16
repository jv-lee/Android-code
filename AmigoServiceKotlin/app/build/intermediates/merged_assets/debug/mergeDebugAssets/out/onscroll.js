(function(){
//    document.getElementById("J_article").onscroll=function(){
//       window.AndroidWebView.onscroll();
//       //alert('clientHeight:'+document.body.clientHeight+";"+'contentHeight:'+document.getElementsByTagName('article').item(0).offsetHeight);
//    };
//        document.getElementsByTagName('article').item(0).onscroll=function(){
//           window.AndroidWebView.onscroll();
//        };
          let t1 = 0;
          let t2 = 0;
          let timer = null; // 定时器
          document.onscroll=function(){
            clearTimeout(timer);
            timer = setTimeout(isScrollEnd, 1000);
             t1 = document.documentElement.scrollTop || document.body.scrollTop;
            };

            function isScrollEnd() {
              t2 = document.documentElement.scrollTop || document.body.scrollTop;
              if(t2 == t1){
                window.AndroidWebView.onscroll();
              }
            }
}())