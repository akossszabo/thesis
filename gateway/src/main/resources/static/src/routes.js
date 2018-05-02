export default [
  { path: '/',                        component: require('./pages/HomePage.vue') },
  { path: '/catalog',                 redirect: 'catalog/projects' },
  { path: '/catalog/projects',        component: require('./pages/ProjectsPage.vue') },
  { path: '/catalog/issues',      component: require('./pages/IssuesPage.vue') },
  { path: '/catalog/media',           component: require('./pages/IssuesPage.vue') },
  { path: '/catalog/registration',           component: require('./pages/RegistrationPage.vue')},
  { path: '/catalog/issues/:id', name: "toissue", component: require('./pages/IssuePage.vue') },
  { path: '/catalog/project/:id', name: "toproject", component: require('./pages/ProjectPage.vue') },
  { path: '/loggingout',                        redirect: '/logout'},
  { path: '*',                        redirect: '/'}
]