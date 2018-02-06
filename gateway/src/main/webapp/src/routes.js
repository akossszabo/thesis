export default [
  { path: '/',                        component: require('./pages/HomePage.vue') },
  { path: '/catalog',                 redirect: 'catalog/products' },
  { path: '/catalog/products',        component: require('./pages/ProductPage.vue') },
  { path: '/catalog/categories',      component: require('./pages/TodoPage.vue') },
  { path: '/catalog/media',           component: require('./pages/TodoPage.vue') },
  { path: '*',                        redirect: '/'}
]