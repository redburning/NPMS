define(function(require){
	var vue = require("vue");
	require('css!/views/components/details/details.css');
	return vue.extend({
		template: require('text!/views/components/details/details.html'),
		data () {
			return {
				searchHit: {}
			}
		},
		mounted () {
			const ipt = this.$route.query.ipt
			this.getDetails(ipt);
		},
		methods: {
			getDetails (ipt) {
				var self = this;
				self.searchResult = [];
				this.$http.get('/docs/search?keyword=ipt:' + ipt)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							console.log(responseData);
							self.searchHit = responseData.data[0].content;
							self.searchHit.ipt = responseData.data[0].highlightFields.ipt[0]
						}
					})
					.catch(error => {
						console.log(error);
					});
			}
		}
	});
})