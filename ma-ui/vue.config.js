module.exports = {
    "publicPath":"/ma",
    "outputDir":"D:/nginx-1.18.0/html/ma",
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