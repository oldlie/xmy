module.exports = {
    "publicPath":"/mam",
    "outputDir":"D:/nginx-1.18.0/html/mam",
    /*
    "devServer": {
        proxy: 'http://localhost:8088'
    }
    */
   chainWebpack: config => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title= '门诊预约管理系统'
        return args
      })
  }
}