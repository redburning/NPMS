define(function(require){
	var vue = require("vue");
	var Vuex = require("vuex");
	return vue.extend({
		template: require('text!/src/components/home/home.html'),
		data () {
			return {
				keyword: '',
				showHistory: false,		// 显示历史搜索项
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
		},
		methods: {
			onSearchBtnClick() {
				this.keyword = this.keyword.trim();
				if (this.keyword) {
					// Add new search to the beginning of search history
					this.$store.dispatch('addSearchHistory', this.keyword);
					
					this.$router.push({
						path: '/query',
						query: {
							keyword: this.keyword
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
				}, 200);
			},
			clearSearchHistory() {
				this.$store.dispatch('clearSearchHistory');
				this.showHistory = false;
			}
		}
	});
})