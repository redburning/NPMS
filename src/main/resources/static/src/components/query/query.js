define(function(require){
	var vue = require("vue");
	return vue.extend({
		template: require('text!/src/components/query/query.html'),
		data () {
			return {
				keyword: '',
				searchResult: []
			}
		},
		mounted() {
			this.keyword = this.$route.query.keyword;
			this.onSearchBtnClick();
			// enter按键自动触发搜索
			var self = this;
        	document.onkeydown = function(e) {
        		let key = window.event.keyCode;
        		if (key == 13) {
        			self.onSearchBtnClick();
        		}
        	};
		},
		methods: {
			onSearchBtnClick() {
				var self = this;
				self.searchResult = [];
				this.$http.get('/docs/search?keyword=' + this.keyword)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							// 遍历每个结果项
							responseData.forEach(item => {
							    // 遍历 highlightFields 中的每个字段
							    for (const [field, highlightedValues] of Object.entries(item.highlightFields)) {
							        // 使用高亮内容替换原来的内容
							        item.content[field] = highlightedValues[0];
							    }
							    self.searchResult.push(item.content);
							});
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			truncateMessage(message) {
				if (message.indexOf('span') != -1) {
					return message.substring(0, message.lastIndexOf('</span>') + 100) + '...';
				} else if (message.length > 100) {
					return message.substring(0, 100) + '...';
				}
				return message;
			},
		    handleImageError(event) {
		    	event.target.src = '/assets/img/no-img.svg'; // 使用默认占位图
		    	event.target.style = 'width:40px;';
		    },
		    downloadMolFile(molFile) {
		    	if (!molFile.endsWith('.mol')) {
		    		molFile = molFile + '.mol';
		    	}
		        const url = '/download/mol/' + molFile;
		     	// 使用 fetch 进行预检
		        fetch(url, { method: 'HEAD' })
		            .then(response => {
		                if (response.ok) {
		                    // 文件存在，开始下载
		                    const link = document.createElement('a');
		                    link.href = url;
		                    link.setAttribute('download', molFile);
		                    document.body.appendChild(link);
		                    link.click();
		                    document.body.removeChild(link);
		                } else {
		                    // 文件不存在或下载失败，提示用户
		                    alert('File Not Found');
		                }
		            })
		            .catch(error => {
		                console.error('预检失败:', error);
		                alert('预检失败，请稍后再试。');
		            });
		    },
		    backHome() {
				this.$router.push({ path: '/home'});
			},
			gotoDetails(item) {
				//this.$router.push({ path: '/details', query: item, append: true });
				const query = new URLSearchParams(item).toString();
				const url = `/#/details?${query}`;
				window.open(url, '_blank');
			}
		}
	});
})