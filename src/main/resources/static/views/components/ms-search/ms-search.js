define(function(require){
	var vue = require("vue");
	require('css!/views/components/ms-search/ms-search.css');
	return vue.extend({
		template: require('text!/views/components/ms-search/ms-search.html'),
		data () {
			return {
				showSearchPanel: true,
				searchParams: {
					precursorMz: undefined,
					uploadContent: '',
					uploadFormat: "100.321  30.0 \n300.321  100.0\n...",
					peaks: []
				},
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
				},
				spectrumComparationDialogVisible: false
			}
		},
		components: {
		    'file-upload': require('/views/components/ms-search/file-upload/file-upload.js')
		},
		watch: {
			'searchParams.uploadContent': function(newContent) {
				this.handleUploadSuccess(newContent);
			}
		},
		methods: {
			handleUploadSuccess (data) {
				this.searchParams.uploadContent = data;
				this.parseFileContent(data);
				this.renderSpectrumPlot();
			},
			parseFileContent(content) {
			    // 将内容按行分割
			    const lines = content.trim().split('\n');
			    this.searchParams.peaks = [];
				
			    // 遍历每一行
			    for (let i = 0; i < lines.length; i++) {
			        const line = lines[i].trim();
			        // 跳过空行
			        if (line === '') continue;
					
			        // 使用正则表达式匹配行中的两个数值，数值之间可以用空格、制表符或逗号分隔
			        const regex = /^([+-]?\d+(\.\d+)?([eE][+-]?\d+)?)\s*[ ,\t]\s*([+-]?\d+(\.\d+)?([eE][+-]?\d+)?)$/;
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
			        const num2 = parseFloat(match[4]);
			        this.searchParams.peaks.push([num1, num2]);
			    }
			},
			renderSpectrumPlot () {
				let traces = [];
				let mzMax = -Infinity;
				let mzMin = Infinity;
				let intensityMax = -Infinity;
				for (let i = 0; i < this.searchParams.peaks.length; i++) {
					if (this.searchParams.peaks[i][0] > mzMax) mzMax = this.searchParams.peaks[i][0];
					if (this.searchParams.peaks[i][0] < mzMin) mzMin = this.searchParams.peaks[i][0];
					if (this.searchParams.peaks[i][1] > intensityMax) intensityMax = this.searchParams.peaks[i][1];
		            traces.push({
		                x: [this.searchParams.peaks[i][0], this.searchParams.peaks[i][0]],
		                y: [0, this.searchParams.peaks[i][1]],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#3854a5',
		                    width: 3
		                },
		                showlegend: false,
		                name: `peak ${i + 1}`
		            });
		        }
				
				// append precursor
				if (this.searchParams.precursorMz != null) {
					traces.push({
		                x: [this.searchParams.precursorMz, this.searchParams.precursorMz],
		                y: [0, intensityMax],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#ed1f24',
		                    width: 3
		                },
		                showlegend: false,
		                name: `precursor`
		            });
					if (this.searchParams.precursorMz > mzMax) mzMax = this.searchParams.precursorMz;
					if (this.searchParams.precursorMz < mzMin) mzMin = this.searchParams.precursorMz;
				}
				this.plotLayout.xaxis.range = [mzMin - 20, mzMax + 20];
				Plotly.newPlot('plotly-upload-spectrum', traces, this.plotLayout, this.plotOptions);
			},
			renderPrecursor() {
				if (this.searchParams.peaks.length > 0) {
					let traces = [];
					let mzMax = -Infinity;
					let mzMin = Infinity;
					let intensityMax = -Infinity;
					for (let i = 0; i < this.searchParams.peaks.length; i++) {
						if (this.searchParams.peaks[i][0] > mzMax) mzMax = this.searchParams.peaks[i][0];
						if (this.searchParams.peaks[i][0] < mzMin) mzMin = this.searchParams.peaks[i][0];
						if (this.searchParams.peaks[i][1] > intensityMax) intensityMax = this.searchParams.peaks[i][1];
			            traces.push({
			                x: [this.searchParams.peaks[i][0], this.searchParams.peaks[i][0]],
			                y: [0, this.searchParams.peaks[i][1]],
			                mode: 'lines',
			                type: 'scatter',
			                line: {
			                    color: '#3854a5',
			                    width: 3
			                },
			                showlegend: false,
			                name: `peak ${i + 1}`
			            });
			        }
					// append precursor
					traces.push({
		                x: [this.searchParams.precursorMz, this.searchParams.precursorMz],
		                y: [0, intensityMax],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#ed1f24',
		                    width: 3
		                },
		                showlegend: false,
		                name: `precursor`
		            });
					if (this.searchParams.precursorMz > mzMax) mzMax = this.searchParams.precursorMz;
					if (this.searchParams.precursorMz < mzMin) mzMin = this.searchParams.precursorMz;
					this.plotLayout.xaxis.range = [mzMin - 20, mzMax + 20];
					Plotly.newPlot('plotly-upload-spectrum', traces, this.plotLayout, this.plotOptions);
				}
			},
			searchSpectrum() {
				const requestBody = {
					precursor_mz: this.searchParams.precursorMz,
					peaks: this.searchParams.peaks
				};
				var self = this;
				this.$http.post('/msentropy/search', requestBody)
					.then(response => {
						if (!!response.data) {
							var responseData = response.data;
							if (responseData.code === 200) {
								const result = JSON.parse(responseData.data);
								self.searchResult = result.data;
								if (self.searchResult.length > 0) {
									self.showSearchPanel = false;
								}
							}
						}
					})
					.catch(error => {
						console.log(error);
					});
			},
			formatMetaboliteName(row, column, cellValue) {
				const englishName = cellValue.englishName;
			    const chineseName = cellValue.chineseName;
			    return `${englishName}\n${chineseName}`;
			},
			formatNumber(row, column, cellValue) {
				return cellValue.toFixed(6);
			},
			viewPeaks(row) {
				let traces = [];
				let mzMax = -Infinity;
				let mzMin = Infinity;
				for (let i = 0; i < this.searchParams.peaks.length; i++) {
					if (this.searchParams.peaks[i][0] > mzMax) mzMax = this.searchParams.peaks[i][0];
					if (this.searchParams.peaks[i][0] < mzMin) mzMin = this.searchParams.peaks[i][0];
		            traces.push({
		                x: [this.searchParams.peaks[i][0], this.searchParams.peaks[i][0]],
		                y: [0, this.searchParams.peaks[i][1]],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#ec2124',
		                    width: 3
		                },
		                showlegend: false,
		                name: `peak ${i + 1}`
		            });
		        }
				
				for (let i = 0; i < row.metadata.peaks.length; i++) {
					if (row.metadata.peaks[i][0] > mzMax) mzMax = row.metadata.peaks[i][0];
					if (row.metadata.peaks[i][0] < mzMin) mzMin = row.metadata.peaks[i][0];
					traces.push({
		                x: [row.metadata.peaks[i][0], row.metadata.peaks[i][0]],
		                y: [0, -row.metadata.peaks[i][1]],
		                mode: 'lines',
		                type: 'scatter',
		                line: {
		                    color: '#3854a5',
		                    width: 3
		                },
		                showlegend: false,
		                name: `peak ${i + 1}`
		            });
				}
				
				this.spectrumComparationDialogVisible = true;
				plotLayout = {
					title: `Uploaded Spectrum vs. ${row.metadata.englishName} ${row.metadata.ion}`,
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
				plotLayout.xaxis.range = [mzMin - 20, mzMax + 20];
				
				this.$nextTick(() => {
		        	Plotly.newPlot('plotly-spectrum-mirror', traces, plotLayout, this.plotOptions); 
		        });
			},
			getTagClass(type) {
				switch (type) {
			        case '[M+H]':
			          	return 'tag1';
			        case '[M-H]':
			          	return 'tag2';
			        default:
			          	return 'tag3';
			    }
			}
		}
	});
})