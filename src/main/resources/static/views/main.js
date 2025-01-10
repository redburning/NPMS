define(function (require) {
	var domReady = require("domReady");
	var	vue = require("vue");
	var	VueRouter = require("vue-router");
	var Vuex = require("vuex");
	var	VueResource = require("vue-resource");
	var Element = require("ELEMENT");
	vue.use(VueRouter);
	vue.use(Vuex);
	vue.use(VueResource);
	vue.use(Element);
	
	domReady(function() {
		new vue({
			el: '#app',
			data() {
				return {
					isScrolled: false
				}
			},
			computed: {
				navLinkColor() {
			      	if (this.$route.path === '/' || this.$route.path === '/home') {
						if (this.isScrolled) {
							return '#005537';
						} else {
							return 'white';
						}
			      	} else {
			        	return '#005537';
			      	}
			    },
				logoSrc() {
					if (this.$route.path === '/' || this.$route.path === '/home') {
						if (this.isScrolled) {
							return '/assets/image/logo-dark.svg';
						} else {
							return '/assets/image/logo.svg';
						}
			      	} else {
			        	return '/assets/image/logo-dark.svg';
			      	}
				},
				navbarLight() {
					if (this.$route.path === '/' || this.$route.path === '/home') {
						if (this.isScrolled) {
							return true;
						} else {
							return false;
						}
			      	} else {
			        	return true;
			      	}
				},
				navLinkActiveClass() {
			      	if (this.$route.path === '/' || this.$route.path === '/home') {
						if (this.isScrolled) {
							return 'nav-link-active-light';
						} else {
							return 'nav-link-active';
						}
			      	} else {
			        	return 'nav-link-active-light';
			      	}
			    }
			},
			router: new VueRouter({
				routes: [
					{
						path: '/',
						redirect: '/home'
					},
					{
						path: '/home',
						component: require('/views/components/home/home.js')
					},
					{
						path: '/search',
						component: require('/views/components/search/search.js')
					},
					{
						path: '/details',
						component: require('/views/components/details/details.js')
					}
				]
			}),
			store: new Vuex.Store({
				state: {
					searchHistory: []
				},
				mutations: {
					addSearchHistory(state, keyword) {
						if (!state.searchHistory.includes(keyword)) {
							state.searchHistory.unshift(keyword);
						}
						if (state.searchHistory.length > 10) {
							state.searchHistory.length = 10;
						}
					},
					clearSearchHistory(state) {
						state.searchHistory = [];
					}
				},
				actions: {
					addSearchHistory({ commit }, keyword) {
      					commit('addSearchHistory', keyword);
    				},
    				clearSearchHistory({ commit }) {
						commit('clearSearchHistory');
					}
				},
				getters: {
					searchHistory: state => state.searchHistory
				}
			}),
			mounted() {
			    window.addEventListener('scroll', this.handleScroll);
			},
			beforeDestroy() {
			    window.removeEventListener('scroll', this.handleScroll);
			},
			methods: {
				handleScroll() {
			    	this.isScrolled = window.scrollY > 0;
			    }
			}
		});
	})
})