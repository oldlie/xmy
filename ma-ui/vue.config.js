module.exports = {
    "publicPath":"/ma",
    "outputDir":"./dist/ma",
    /*
    "devServer": {
        proxy: 'http://localhost:8088'
    }
    */
   chainWebpack: config => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title= '湘美雅门诊预约'
        return args
      })
  }

}