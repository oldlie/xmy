<template>
  <div>
    <van-nav-bar title="湘美雅皮肤门诊预约" />
    <van-grid square :column-num="2">
      <van-grid-item @click="navigate('/doctor')">
        <van-icon name="label-o" size="3rem" color="#7cb305" />
        <p>开始预约</p>
      </van-grid-item>

      <van-grid-item @click="navigate('/book-info')">
        <van-icon name="info-o" size="3rem" color="#91d5ff" />
        <p>预约记录</p>
      </van-grid-item>
      
      <!--van-grid-item @click="navigate('/book-info')">
        <van-icon name="smile-o" size="3rem" color="#87e8de" />
        <p>我的信息</p>
      </van-grid-item-->

    </van-grid>

    <p style="text-align:left;margin: 10px 0; padding: 15px">地址：湖南省长沙市开福区清水塘街道芙蓉中路一段319号绿地中心T2栋1801号房。
      <a href="https://map.baidu.com/search/%E9%95%BF%E6%B2%99%E5%BC%80%E7%A6%8F%E6%B9%98%E7%BE%8E%E9%9B%85%E7%9A%AE%E8%82%A4%E7%A7%91%E9%97%A8%E8%AF%8A%E9%83%A8/@12578514.245,3255979.73,19.16z?querytype=s&da_src=shareurl&wd=%E9%95%BF%E6%B2%99%E5%BC%80%E7%A6%8F%E6%B9%98%E7%BE%8E%E9%9B%85%E7%9A%AE%E8%82%A4%E7%A7%91%E9%97%A8%E8%AF%8A%E9%83%A8&c=158&src=0&wd2=%E9%95%BF%E6%B2%99%E5%B8%82%E5%BC%80%E7%A6%8F%E5%8C%BA&pn=0&sug=1&l=12&b=(12528895.333468135,3228447.5703921565;12618612.995232843,3280569.2596078436)&from=webmap&biz_forward=%7B%22scaler%22:2,%22styles%22:%22pl%22%7D&sug_forward=5af579758e892e7fc7f50d28&device_ratio=2">百度地图</a>
      </p>
  </div>
</template>

<script>
export default {
    methods: {
        navigate (url) {
            this.$router.push(url);
        }
    }
};
</script>

<style>
</style>