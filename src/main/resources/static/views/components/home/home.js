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
				suggestOptions: [],
				carouselIntervalId: null,
				carouselScrollLeft: 0
			}
		},
		computed: {
			...Vuex.mapState(['searchHistory'])
		},
		mounted () {
			// enter按键自动触发搜索
			document.onkeydown = (e) => {
				if (e.keyCode === 13) {
					this.onSearchBtnClick();
				}
			};
		},
		beforeRouteEnter(to, from, next) {
			next(vm => {
				vm.carouselIntervalId = vm.startCarouselGallery();
				vm.handleResize();
			});
		},
		beforeRouteLeave(to, from, next) {
			this.stopCarouselGallery();
			window.removeEventListener('resize', this.handleResize);
			next();
		},
		beforeDestroy() {
			this.stopCarouselGallery();
			window.removeEventListener('resize', this.handleResize);
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
			startCarouselGallery() {
				if (this.carouselIntervalId) {
					clearInterval(this.carouselIntervalId);
				}
				this.carouselScrollLeft = 0;
				return setInterval(() => this.scrollGallery('next'), 5000);
			},
			stopCarouselGallery() {
				if (this.carouselIntervalId) {
					clearInterval(this.carouselIntervalId);
					this.carouselIntervalId = null;
				}
			},
			handleResize() {
				this.startCarouselGallery();
			},
			scrollGallery(direction) {
				const gap = 20;
				const imgWidthKey = 'clientWidth';
				const containerId = 'imgContainer';
				const multiplier = 4;
				
				const container = document.getElementById(containerId);
				if (container) {
					const imgWidth = container.querySelector('img')[imgWidthKey];
					const offsetThreshold = imgWidth * multiplier + gap * (multiplier - 1);
					
					if (direction === 'next') {
						this.carouselScrollLeft -= (imgWidth + gap);
						if (this.carouselScrollLeft < -offsetThreshold) {
							this.carouselScrollLeft = 0;
							container.style.transition = 'unset';
							setTimeout(() => {
								container.style.transition = 'left 0.5s ease-in-out';
							}, 50);
						}
					}
					container.style.left = this.carouselScrollLeft + 'px';
				}
			}
		}
	});
})