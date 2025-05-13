define(function(require){
	var vue = require("vue");
	require('css!/views/components/details/details.css');
	return vue.extend({
		template: require('text!/views/components/details/details.html'),
		data () {
			return {
				searchHit: {},
				directoryItems: [
					{ id: 'overview', label: 'ID and Overview', active: true, icon: '/icon/icon-id.svg' },
					{ id: 'identifiers', label: 'Chemical Identifiers', active: false, icon: '/icon/icon-chemical.svg' },
					{ id: 'taxonomy', label: 'Chemical Taxonomy', active: false, icon: '/icon/icon-taxonomy.svg' },
					{ id: 'ms1', label: 'Experimental MS1 Spectra', active: false, icon: '/icon/icon-spectra.svg' },
					{ id: 'ms2', label: 'Experimental MS2 Spectra', active: false, icon: '/icon/icon-spectra.svg' }
				]
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
				this.$http.get('/docs/searchIpt?ipt=' + ipt)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							self.searchHit = responseData.data[0].content;
							self.searchHit.ipt = responseData.data[0].highlightFields.ipt[0]
							
							this.renderPlot();
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			renderPlot() {
				function prepareData(data, color) {
					let traces = [];
					for (let i = 0; i < data.intensity.length; i++) {
			            traces.push({
			                x: [data['m/z'][i], data['m/z'][i]],
			                y: [0, data.intensity[i]],
			                mode: 'lines',
			                type: 'scatter',
			                line: {
			                    color: color ? color : '#636EFA',
			                    width: 2
			                },
			                showlegend: false,
			                name: `trace ${i + 1}`
			            });
			        }
					return traces;
				}
		        
				var layout = {
					title: '',
					xaxis: { 
						title: 'm/z', 
						showgrid: false
					},
					yaxis: {
						title: 'intensity', 
						//fixedrange: true, 
						showgrid: false, 
						exponentformat: 'E', 
						showline: true 
					}
				};
				
				var options = {
					displayModeBar: false,
					displaylogo: false
				};
				
				// ms1
				if (this.searchHit.ms1PositiveData && this.searchHit.ms1PositiveData.hasOwnProperty('intensity')) {
					let ms1PositiveData = prepareData(this.searchHit.ms1PositiveData, '#636EFA');
					let xmin = Math.min(...this.searchHit.ms1PositiveData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms1PositiveData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
					Plotly.newPlot('plotly-ms1-positive', ms1PositiveData, layout, options);
				} 
				if (this.searchHit.ms1NegativeData && this.searchHit.ms1NegativeData.hasOwnProperty('intensity')) {
					let ms1NegativeData = prepareData(this.searchHit.ms1NegativeData, '#FF7F0E');
					let xmin = Math.min(...this.searchHit.ms1NegativeData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms1NegativeData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
					Plotly.newPlot('plotly-ms1-negative', ms1NegativeData, layout, options);
				}
				
				// ms2
				if (this.searchHit.ms2PositiveData && this.searchHit.ms2PositiveData.hasOwnProperty('intensity')) {
					let ms2PositiveData = prepareData(this.searchHit.ms2PositiveData, '#2CA02C');
					let xmin = Math.min(...this.searchHit.ms2PositiveData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms2PositiveData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
					Plotly.newPlot('plotly-ms2-positive', ms2PositiveData, layout, options);
				}
				if (this.searchHit.ms2NegativeData && this.searchHit.ms2NegativeData.hasOwnProperty('intensity')) {
					let ms2NegativeData = prepareData(this.searchHit.ms2NegativeData, '#9467BD');
					let xmin = Math.min(...this.searchHit.ms2NegativeData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms2NegativeData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
					Plotly.newPlot('plotly-ms2-negative', ms2NegativeData, layout, options);
				}
			},
			toggleDirectoryItemActive(key) {
				for (var i = 0; i < this.directoryItems.length; i++) {
					if (this.directoryItems[i].id === key) {
						this.directoryItems[i].active = true;
					} else {
						this.directoryItems[i].active = false;
					}
				}
			},
			navigateToSection(sectionId) {
	            const element = this.$refs[sectionId];
	            if (element) {
	                element.scrollIntoView({ behavior: 'smooth', block: 'start' });
	            }
				this.toggleDirectoryItemActive(sectionId);
	        }
		}
	});
})