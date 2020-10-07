<template>
  <div>
    <van-nav-bar title="预约系统管理" />
    <van-grid square :column-num="3">
      <van-grid-item @click="navigate('/book-info')">
        <van-icon name="label-o" size="3rem" color="#7cb305" />
        <p>预约列表</p>
      </van-grid-item>

      <!--van-grid-item>
        <van-icon name="friends-o" size="3rem" color="#91d5ff" />
        <p>客户列表</p>
      </van-grid-item-->

      <van-grid-item @click="navigate('/doctor')">
          <van-icon name="manager-o" size="3rem" color="#69c0ff" />
          <p>员工设置</p>
      </van-grid-item>

      <van-grid-item @click="navigate('/appointment')">
        <van-icon name="orders-o" size="3rem" color="#bfbfbf" />
        <p>预约设置</p>
      </van-grid-item>

      <van-grid-item>
        <van-icon name="setting-o" size="3rem" color="#bfbfbf" />
        <p>系统设置</p>
      </van-grid-item>

      <!--van-grid-item>
        <van-icon name="close" size="3rem" color="#ffa39e" />
        <p>退出</p>
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