            var url = location.search.substring(1);

            PDFJS.cMapUrl = 'https://unpkg.com/pdfjs-dist@1.9.426/cmaps/';
            PDFJS.cMapPacked = true;

			function createPage(viewport) {
				var container = document.createElement("div");
				container.className = "container";
				container.width = viewport.width;
				container.height = viewport.height;

				var loading = document.createElement("div");
				loading.className = "loading";

			    var canvas = document.createElement("canvas");
			    canvas.width = viewport.width;
			    canvas.height = viewport.height;

			    container.appendChild(canvas);
			    container.appendChild(loading);

			    document.body.appendChild(container);
			    return canvas;
			}

			function buildPage(page,index){
				page.then(function(page){
					var viewport = page.getViewport(2.0);
			        var canvas = createPage(viewport);
			        var ctx = canvas.getContext('2d');

			        page.render({
			            canvasContext: ctx,
			            viewport: viewport
			        }).then(function(){
			        	var loadings = document.body.getElementsByClassName('loading');
			        	var loading = loadings[index];
			        	loading.style.display = "none";
			        });
				});
			}

            PDFJS.getDocument(url).then(function (pdf) {
              window.control.end();
              for (var i = 1; i <= pdf.numPages; i++) {
                buildPage(pdf.getPage(i),i-1);
              }
            });