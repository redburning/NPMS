define(function(require){
	var vue = require("vue");
	require('css!/views/components/details/details.css');
	return vue.extend({
		template: require('text!/views/components/details/details.html'),
		data () {
			return {
				ipt: '',
				searchHit: {},
				selectedMS1Spectra: 'positive-ms1',
				ms1SpectraOptions: [
					{ label: 'Positive MS1', value: 'positive-ms1' },
					{ label: 'Negative MS1', value: 'negative-ms1' }
				],
				selectedMS2Spectra: '',
				ms2SpectraOptions: [],
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
			const ipt = this.$route.query.ipt;
			this.ipt = ipt;
			this.getDetails(ipt);
			
			// 监听 window.resize 事件
	        window.addEventListener('resize', function () {
				Plotly.relayout('plotly-ms1', {
	                width: window.innerWidth - 610,
	                height: 450
	            });
				Plotly.relayout('plotly-ms2', {
	                width: window.innerWidth - 610,
	                height: 450
	            });
	        });
		},
		computed: {
			formatedEnglishSynonyms() {
				if (this.searchHit.englishSynonyms !== undefined) {
					// 按分号分隔原始文本
			      	const parts = this.searchHit.englishSynonyms.split(';');
			      	// 去除每个部分的首尾空格并使用 <br> 连接
			      	return parts.map(part => part.trim()).join('<br>');
				}
				return null;
			},
			formatedChineseSynonyms() {
				if (this.searchHit.chineseSynonyms !== undefined) {
					// 按分号分隔原始文本
			      	const parts = this.searchHit.chineseSynonyms.split(';');
			      	// 去除每个部分的首尾空格并使用 <br> 连接
			      	return parts.map(part => part.trim()).join('<br>');
				}
				return null;
			}
		},
		methods: {
			getDetails (ipt) {
				var self = this;
				// get meta data
				this.$http.get('/docs/searchIpt?ipt=' + ipt)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							const searchHit = responseData.data[0].content;
							self.searchHit = searchHit;
							if (searchHit.reference.includes('PMID:')) {
								self.searchHit.pmid = (searchHit.reference.match(/PMID:(\d+)/) || [])[1];
							}
							if (searchHit.reference.includes('PMCID:')) {
								self.searchHit.pmcid = (searchHit.reference.match(/PMCID:(.*)/) || [])[1];
							}
							if (searchHit.reference.includes('DOI:')) {
								self.searchHit.doi = (searchHit.reference.match(/DOI:(.*)/) || [])[1];
							}
						}
					})
					.catch(error => {
						console.log(error);
					});
				
				// get ms1 data
				this.$http.get('/ms1/searchIpt?ipt=' + ipt)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							self.searchHit.ms1PositiveData = responseData.data[0].content.positiveData;
							self.searchHit.ms1NegativeData = responseData.data[0].content.negativeData;
							if (self.searchHit.ms1PositiveData == null) {
								self.selectedMS1Spectra = 'negative-ms1';
							}
							vue.set(self.searchHit, 'ms1PositiveAnnotation', responseData.data[0].content.positiveAnnotation);
							vue.set(self.searchHit, 'ms1NegativeAnnotation', responseData.data[0].content.negativeAnnotation);
							self.renderMs1Plot();
						}
					})
					.catch(error => {
						console.log(error);
					});
					
				// get ms2 data
				this.$http.get('/ms2/searchIpt?ipt=' + ipt)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							self.ms2SpectraOptions = Object.keys(responseData.data[0].content.data).map(key => (
								{ label: key, value: key }
							));
							self.selectedMS2Spectra = self.ms2SpectraOptions[0].value;
							self.searchHit.ms2Data = responseData.data[0].content.data;
							self.renderMs2Plot();
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			prepareData(data, color) {
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
			},
			renderMs1Plot() {
				if (this.selectedMS1Spectra === 'positive-ms1') {
					this.renderMs1PositivePlot();
				} else {
					this.renderMs1NegativePlot();
				}
			},
			renderMs1PositivePlot() {
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
				
				// ms1 positive
				let ms1PositiveData = [];
				if (this.searchHit.ms1PositiveData && this.searchHit.ms1PositiveData.hasOwnProperty('intensity')) {
					ms1PositiveData = this.prepareData(this.searchHit.ms1PositiveData, 'rgb(44, 123, 182)');
					let xmin = Math.min(...this.searchHit.ms1PositiveData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms1PositiveData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
				}
				Plotly.newPlot('plotly-ms1', ms1PositiveData, layout, options);
			},
			renderMs1NegativePlot() {
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
				
				// ms1 negative
				let ms1NegativeData = [];
				if (this.searchHit.ms1NegativeData && this.searchHit.ms1NegativeData.hasOwnProperty('intensity')) {
					ms1NegativeData = this.prepareData(this.searchHit.ms1NegativeData, 'rgb(255, 127, 80)');
					let xmin = Math.min(...this.searchHit.ms1NegativeData['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms1NegativeData['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
				}
				Plotly.newPlot('plotly-ms1', ms1NegativeData, layout, options);
			},
			renderMs2Plot() {
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
				
				// ms2
				let ms2Data = [];
				if (this.searchHit.ms2Data && this.searchHit.ms2Data.hasOwnProperty(this.selectedMS2Spectra)) {
					ms2Data = this.prepareData(this.searchHit.ms2Data[this.selectedMS2Spectra], '#2CA02C');
					let xmin = Math.min(...this.searchHit.ms2Data[this.selectedMS2Spectra]['m/z']) - 20;
					let xmax = Math.max(...this.searchHit.ms2Data[this.selectedMS2Spectra]['m/z']) + 20;
					layout.xaxis.range = [xmin, xmax];
				}
				Plotly.newPlot('plotly-ms2', ms2Data, layout, options);
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
	        },
			handleImageError(event) {
		      	event.target.onerror = () => {
		        	// 如果 .png 也加载失败，使用默认占位图
		        	event.target.src = '/assets/img/no-img.svg';
		        	event.target.style = 'width:40px;';
		      	};
		    },
		}
	});
})