export default [
  { path: '/',                        component: require('./pages/HomePage.vue') },
  { path: '/catalog',                 redirect: 'catalog/projects' },
  { path: '/catalog/projects',        component: require('./pages/ProjectPage.vue') },
  { path: '/catalog/issues',      component: require('./pages/IssuePage.vue') },
  { path: '/catalog/media',           component: require('./pages/IssuePage.vue') },
  { path: '/catalog/registration',           component: require('./pages/RegistrationPage.vue')},
  { path: '*',                        redirect: '/'},
  { path: '/login',                        component: require('./pages/LandingPage.vue') }
]