define(function(require){
	var vue = require("vue");
	require("3dmol");
	return vue.extend({
		template: require('text!/src/components/details/details.html'),
		data () {
			return {
				queryParams: {},
				struct2dScale: 1
			}
		},
		beforeRouteEnter(to, from, next) {
		    document.body.style.backgroundColor = '#f4f5f5';
		    next();
		},
		beforeRouteLeave(to, from, next) {
		    document.body.style.backgroundColor = 'white';
		    next();
		},
		mounted() {
	        const url = new URL(window.location.href);
	        const params = new URLSearchParams(url.href);
	        const queryParams = {};
	        params.forEach((value, key) => {
	            queryParams[key] = value;
	        });
	        this.queryParams = queryParams;
	        
	        this.draw3dStructure();
		},
		methods: {
			downloadStructure2d() {
				var name = this.queryParams.ipt;
				if (!name.endsWith('.svg')) {
		    		name = name + '.svg';
		    	}
		        const url = '/download/structure2d/' + name;
		     	// 使用 fetch 进行预检
		        fetch(url, { method: 'HEAD' })
		            .then(response => {
		                if (response.ok) {
		                    // 文件存在，开始下载
		                    const link = document.createElement('a');
		                    link.href = url;
		                    link.setAttribute('download', name);
		                    document.body.appendChild(link);
		                    link.click();
		                    document.body.removeChild(link);
		                } else {
		                    // 文件不存在或下载失败，提示用户
		                    alert('File Not Found');
		                }
		            })
		            .catch(error => {
		                console.error('download error:', error);
		                alert('Download Error');
		            });
			},
			zoomin() {
				this.struct2dScale += 0.1;
				const image = document.getElementById('structure-2d');
				image.style.transform = `scale(${this.struct2dScale})`;
			},
			zoomout() {
				this.struct2dScale -= 0.1;
				if (this.struct2dScale < 0.1) {
					this.struct2dScale = 0.1;
				}
				const image = document.getElementById('structure-2d');
				image.style.transform = `scale(${this.struct2dScale})`;
			},
			draw3dStructure() {
				let element = document.querySelector('#container-01');
			    let config = { backgroundColor: 'white' };
			    let viewer = $3Dmol.createViewer( element, config );
			    let pdbUri = '/assets/pdb/1ycr.pdb';
			    jQuery.ajax(pdbUri, {
			        success: function(data) {
			            let v = viewer;
			            v.addModel(data, "pdb");
			            v.setStyle({}, {cartoon: {color: 'spectrum'}});
			            //v.setStyle({}, {sphere: {color: "element"}, stick: {color: "black"}});
			            v.zoomTo();
			            v.render();
			            v.zoom(1.2, 1000);
			        },
			        error: function(hdr, status, err) {
			            console.error( "Failed to load PDB " + pdbUri + ": " + err );
			        },
			    });
			}
		}
	});
})