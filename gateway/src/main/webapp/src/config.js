export default {
  appName: 'Project Manager',
  apiBaseUrl: 'http://localhost:4040/',
  registrationUrl: '/accounts/registration'
};
module: {
  rules: [
    {
      test: /\.css$/,
      use: [ 'style-loader', 'css-loader' ]   //  <=  Order is very important
    }
  ]
}