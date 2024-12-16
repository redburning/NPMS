require = {
  	baseUrl: '/src',
  	paths: {
	    'vue': '/js/vue/vue@2.6.20.min',
	    'vue-router': '/js/vue/vue-router@3.0.1',
	    'vue-resource': '/js/vue/vue-resource@1.5.3.min',
	    'vuex': '/js/vue/vue-vuex@2.1.1',
	    'ELEMENT': '/js/elementui/element-ui@2.15.14.min',
	    'jquery': '/js/jquery/jquery@2.1.4.min',
	    'domReady': '/js/require/domReady@2.0.1',
	    "text": "/js/require/text@2.0.15",
	    "3dmol": "/js/3dmol/3Dmol-min"
  	},
  	shim: {
	    'vue-resource': {
	      	deps: ['vue']
	    },
	    'vue-router': {
	      	deps: ['vue']
	    },
	    'vuex': {
	      	deps: ['vue']
	    },
	    'ELEMENT': {
	      	deps: ['vue']
	    },
	    '3dmol': {
			deps: ['jquery']
		}
  	}
};