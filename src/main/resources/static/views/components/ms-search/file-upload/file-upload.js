define(function(require){
	var vue = require("vue");
	require('css!/views/components/ms-search/file-upload/file-upload.css');
	return vue.extend({
		template: require('text!/views/components/ms-search/file-upload/file-upload.html'),
		data () {
			return {
				loading: false
			}
		},
		props: {
		    onSuccess: Function
		},
		methods: {
			handleDrop (e) {
				e.stopPropagation();
				e.preventDefault();
				if (this.loading) return;
		      	const files = e.dataTransfer.files;
		      	if (files.length !== 1) {
		        	this.$message.error('Only support uploading one file!');
		        	return;
		      	}
		      	const rawFile = files[0]; // only use files[0]
				
		      	if (!this.fileFormatCheck(rawFile)) {
		        	this.$message.error('Only supports upload .txt suffix files');
		        	return false;
		      	}
		      	this.upload(rawFile);
		      	e.stopPropagation();
		      	e.preventDefault();
			},
			handleDragover (e) {
				e.stopPropagation();
				e.preventDefault();
				e.dataTransfer.dropEffect = 'copy';
			},
			handleClick (e) {
				const files = e.target.files;
				const rawFile = files[0]; // only use files[0]
				if (!rawFile) return;
				this.upload(rawFile);
			},
			handleUpload () {
			    this.$refs['file-upload-input'].click();
			},
			upload (rawFile) {
			    this.$refs['file-upload-input'].value = null; // fix can't select the same excel
			    if (!this.beforeUpload) {
			        this.readerData(rawFile);
			        return;
			    }
			    const before = this.beforeUpload(rawFile);
			    if (before) {
			        this.readData(rawFile);
			    }
			},
			fileFormatCheck (file) {
			    return /\.(txt|mgf)$/.test(file.name)
			},
			beforeUpload (file) {
			    const isLt1M = file.size / 1024 / 1024 < 1;
			    if (isLt1M) {
			        return true;
			    }
			    this.$message({
			        message: 'Please do not upload files larger than 1m in size.',
			        type: 'warning'
			    });
			    return false;
			},
			readData (rawFile) {
			    this.loading = true
			    return new Promise((resolve, reject) => {
			        const reader = new FileReader()
			        reader.onload = e => {
			          	const fileContent = e.target.result;
						this.onSuccess(fileContent);
			          	this.loading = false;
			          	resolve();
			        }
			        reader.readAsText(rawFile)
			    })
			},
		}
	});
})