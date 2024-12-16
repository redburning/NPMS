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
			router: new VueRouter({
				routes: [
					{
						path: '/',
						redirect: '/home'
					},
					{
						path: '/home',
						component: require('/src/components/home/home.js')
					},
					{
						path: '/query',
						component: require('/src/components/query/query.js')
					},
					{
						path: '/details',
						component: require('/src/components/details/details.js')
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
					},
					
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
			})
		});
	})
})