define(function(require){
	var vue = require("vue");
	require('css!/views/components/ms-search/ms-search.css');
	return vue.extend({
		template: require('text!/views/components/ms-search/ms-search.html'),
		data () {
			return {
				precursorMz: undefined,
				uploadContent: '',
				uploadFormat: "100.321  30.0 \n300.321  100.0\n...",
				peaks: [],
				searchResult: [],
				plotOptions: {
					displayModeBar: false,
					displaylogo: false
				},
				plotLayout: {
					title: 'Uploaded Spectrum',
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
				}
			}
		},
		components: {
		    'file-upload': require('/views/components/ms-search/file-upload/file-upload.js')
		},
		watch: {
			'uploadContent': function(newContent) {
				this.handleUploadSuccess(newContent);
			}
		},
		methods: {
			handleUploadSuccess (data) {
				this.uploadContent = data;
				this.parseFileContent(data);
				this.renderSpectrumPlot();
			},
			parseFileContent(content) {
			    // 将内容按行分割
			    const lines = content.trim().split('\n');
			    this.peaks = [];
				
			    // 遍历每一行
			    for (let i = 0; i < lines.length; i++) {
			        const line = lines[i].trim();
			        // 跳过空行
			        if (line === '') continue;
					
			        // 使用正则表达式匹配行中的两个数值，数值之间可以用空格、制表符或逗号分隔
			        const regex = /^([+-]?\d+(\.\d+)?)\s*[ ,\t]\s*([+-]?\d+(\.\d+)?)$/;
			        const match = line.match(regex);

			        if (!match) {
						this.$message({
					        message: 'Please check your data format.',
					        type: 'warning'
					    });
						return;
			        }
					
			        // 将匹配到的两个数值转换为数字并添加到结果数组中
			        const num1 = parseFloat(match[1]);
			        const num2 = parseFloat(match[3]);
			        this.peaks.push([num1, num2]);
			    }
			},
			renderSpectrumPlot () {
				let traces = [];
				let mzMax = -Infinity;
				let mzMin = Infinity;
				let intensityMax = -Infinity;
				for (let i = 0; i < this.peaks.length; i++) {
					if (this.peaks[i][0] > mzMax) mzMax = this.peaks[i][0];
					if (this.peaks[i][0] < mzMin) mzMin = this.peaks[i][0];
					if (this.peaks[i][1] > intensityMax) intensityMax = this.peaks[i][1];
		            traces.push({
		                x: [this.peaks[i][0], this.peaks[i][0]],
		                y: [0, this.peaks[i][1]],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#636EFA',
		                    width: 2
		                },
		                showlegend: false,
		                name: `peak ${i + 1}`
		            });
		        }
				
				// append precursor
				if (this.precursorMz != null) {
					traces.push({
		                x: [this.precursorMz, this.precursorMz],
		                y: [0, intensityMax],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#FF7F0E',
		                    width: 2
		                },
		                showlegend: false,
		                name: `precursor`
		            });
					if (this.precursorMz > mzMax) mzMax = this.precursorMz;
					if (this.precursorMz < mzMin) mzMin = this.precursorMz;
				}
				this.plotLayout.xaxis.range = [mzMin - 20, mzMax + 20];
				Plotly.newPlot('plotly-upload-spectrum', traces, this.plotLayout, this.plotOptions);
			},
			renderPrecursor() {
				if (this.peaks.length > 0) {
					let traces = [];
					let mzMax = -Infinity;
					let mzMin = Infinity;
					let intensityMax = -Infinity;
					for (let i = 0; i < this.peaks.length; i++) {
						if (this.peaks[i][0] > mzMax) mzMax = this.peaks[i][0];
						if (this.peaks[i][0] < mzMin) mzMin = this.peaks[i][0];
						if (this.peaks[i][1] > intensityMax) intensityMax = this.peaks[i][1];
			            traces.push({
			                x: [this.peaks[i][0], this.peaks[i][0]],
			                y: [0, this.peaks[i][1]],
			                mode: 'lines',
			                type: 'scatter',
			                line: {
			                    color: '#636EFA',
			                    width: 2
			                },
			                showlegend: false,
			                name: `peak ${i + 1}`
			            });
			        }
					// append precursor
					traces.push({
		                x: [this.precursorMz, this.precursorMz],
		                y: [0, intensityMax],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#FF7F0E',
		                    width: 2
		                },
		                showlegend: false,
		                name: `precursor`
		            });
					if (this.precursorMz > mzMax) mzMax = this.precursorMz;
					if (this.precursorMz < mzMin) mzMin = this.precursorMz;
					this.plotLayout.xaxis.range = [mzMin - 20, mzMax + 20];
					Plotly.newPlot('plotly-upload-spectrum', traces, this.plotLayout, this.plotOptions);
				}
			},
			searchSpectrum() {
				const requestBody = {
					precursor_mz: this.precursorMz,
					peaks: this.peaks
				};
				var self = this;
				this.$http.post('/msentropy/search', requestBody)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							if (responseData.code === 200) {
								const result = JSON.parse(responseData.data);
								self.searchResult = result.data;
								console.log(self.searchResult);
							}
						}
					})
					.catch(error => {
						console.log(error);
					});
			}
		}
	});
})