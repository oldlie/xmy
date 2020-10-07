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