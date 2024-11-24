import './assets/main.css';

import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.full'


library.add(fas);

createApp(App)
    .use(router)
    .use(ElementPlus)
    .component('fa', FontAwesomeIcon)
    .mount('#app');