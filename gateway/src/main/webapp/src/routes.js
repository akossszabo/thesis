export default [
  { path: '/',                        component: require('./pages/HomePage.vue') },
  { path: '/catalog',                 redirect: 'catalog/projects' },
  { path: '/catalog/projects',        component: require('./pages/ProjectPage.vue') },
  { path: '/catalog/issues',      component: require('./pages/TodoPage.vue') },
  { path: '/catalog/media',           component: require('./pages/TodoPage.vue') },
  { path: '/catalog/registration',           component: require('./pages/RegistrationPage.vue')},
  { path: '*',                        redirect: '/'}
]