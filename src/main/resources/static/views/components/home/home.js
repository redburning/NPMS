define(function(require){
	var vue = require("vue");
	var Vuex = require("vuex");
	require('css!/views/components/home/home.css');
	return vue.extend({
		template: require('text!/views/components/home/home.html'),
		data () {
			return {
				keyword: '',
				showHistory: false,		// 显示历史搜索项
				pageNum: 0,
				pageSize: 10,
				suggestOptions: []
			}
		},
		computed: {
			...Vuex.mapState(['searchHistory'])
		},
		mounted () {
			// enter按键自动触发搜索
			var self = this;
        	document.onkeydown = function(e) {
        		let key = window.event.keyCode;
        		if (key == 13) {
        			self.onSearchBtnClick();
        		}
        	};
			
			this.carouselGallery();
			window.addEventListener('resize', this.carouselGallery());
		},
		beforeDestory() {
			window.removeEventListener('resize', this.carouselGallery());
		},
		methods: {
			onSearchBtnClick() {
				this.keyword = this.keyword.trim();
				if (this.keyword) {
					// Add new search to the beginning of search history
					this.$store.dispatch('addSearchHistory', this.keyword);
					
					this.$router.push({
						path: '/search',
						query: {
							keyword: this.keyword,
							page: this.pageNum,
							size: this.pageSize
						}
					});
				}
			},
			quickSearch(keyword) {
				this.keyword = keyword;
				this.onSearchBtnClick();
			},
			onSearchInputFocus() {
				if (this.searchHistory.length > 0) {
					this.showHistory = true;
				} else {
					this.showHistory = false;
				}
			},
			onSearchInputBlur() {
				setTimeout(() => {
					this.showHistory = false;
					this.suggestOptions = [];
				}, 200);
			},
			onKeywordSuggest() {
				var self = this;
				this.$http.get('/docs/suggest?keyword=' + self.keyword)
				    .then(response => {
					    if (!!response.data) {
						    var responseData = response.data;
							self.suggestOptions = responseData.data;
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			clearSearchHistory() {
				this.$store.dispatch('clearSearchHistory');
				this.showHistory = false;
			},
			carouselGallery() {
			    let currentLeft = 0;
			    const gap = 20;
			    const imgWidthKey = 'clientWidth';
			    const containerId = 'imgContainer';
			    const multiplier = 4;
			    function scrollGallery(direction) {
			        const container = document.getElementById(containerId);
					if (container !== null) {
						const imgWidth = container.querySelector('img')[imgWidthKey];
				        const offsetThreshold = imgWidth * multiplier + gap * (multiplier - 1);
						
				        if (direction === 'next') {
				            currentLeft -= (imgWidth + gap);
				            if (currentLeft < -offsetThreshold) {
				                currentLeft = 0;
				                container.style.transition = 'unset';
				                setTimeout(() => {
				                    container.style.transition = 'left 0.5s ease-in-out';
				                }, 50);
				            }
				        }
				        container.style.left = currentLeft + 'px';
					}
			    }
			    // Auto scroll every 5 seconds
			    setInterval(() => scrollGallery('next'), 5000);
			}
		}
	});
})