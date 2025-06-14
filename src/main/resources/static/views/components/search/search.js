define(function(require){
	var vue = require("vue");
	require('css!/views/components/search/search.css');
	return vue.extend({
		template: require('text!/views/components/search/search.html'),
		data () {
			return {
				type: '',
				keyword: '',
				field: '',
				value: '',
				page: {
					currentPage: 1,
				    pageSize: 10,
					total: 0,
				},
				searchResult: [],
				showFixedSearchBanner: false
			}
		},
		mounted() {
			this.type = this.$route.query.type;
			this.keyword = this.$route.query.keyword;
			this.field = this.$route.query.field;
			this.value = this.$route.query.value;
			
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
		beforeRouteEnter(to, from, next) {
			next(vm => {
				window.addEventListener('scroll', vm.handleScroll);
			});
		},
		beforeRouteLeave(to, from, next) {
		    window.removeEventListener('scroll', this.handleScroll);
		    next();
		},
		methods: {
			handleScroll() {
			    this.showFixedSearchBanner = window.scrollY > 180;
			},
			onSearchBtnClick() {
				// search by keyword
				if (this.type === 'text') {
					this.getTotlaCount(this.keyword);
					this.getSearchHits(this.keyword, this.page.currentPage - 1, this.page.pageSize);
				} 
				// term query
				else if (this.type === 'term') {
					this.getTermCount(this.field, this.value);
					this.getTermQueryHits(this.field, this.value, this.page.currentPage - 1, this.page.pageSize);
				}
				// browse
				else {
					this.getTotlaCount('*');
					this.getSearchHits('*', this.page.currentPage - 1, this.page.pageSize);
				}
			},
			getTotlaCount(keyword) {
				var self = this;
				this.$http.get('/docs/count?keyword=' + keyword)
				    .then(response => {
					    if (!!response.data) {
						    var responseData = response.data;
							self.page.total = responseData.data;
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			getTermCount(field, value) {
				var self = this;
				this.$http.get('/docs/termcount?field=' + field + '&value=' + value)
				    .then(response => {
					    if (!!response.data) {
						    var responseData = response.data;
							self.page.total = responseData.data;
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			getSearchHits(keyword, page, size) {
				var self = this;
				self.searchResult = [];
				this.$http.get('/docs/search?keyword=' + keyword + '&page=' + page + '&size=' + size)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							// 遍历每个结果项
							responseData.data.forEach(item => {
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
			getTermQueryHits(field, value, page, size) {
				var self = this;
				self.searchResult = [];
				this.$http.get('/docs/term?field=' + field + '&value=' + value + '&page=' + page + '&size=' + size)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							// 遍历每个结果项
							responseData.data.forEach(item => {
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
			handleCurrentPageChange(current) {
				this.page.currentPage = current;
				if (this.keyword !== undefined) {
					this.getSearchHits(this.keyword, this.page.currentPage - 1, this.page.pageSize);
				} else {
					this.getSearchHits('*', this.page.currentPage - 1, this.page.pageSize);
				}
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
		      	event.target.onerror = () => {
		        	// 如果 .png 也加载失败，使用默认占位图
		        	event.target.src = '/assets/img/no-img.svg';
		        	event.target.style = 'width:40px;';
		      	};
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
				this.$router.push({
					path: '/details',
					query: { ipt: item.ipt }
				});
			}
		}
	});
})