<template>
  <div>
    <van-nav-bar title="系统设置" left-text="返回" @click-left="navigate('/dashboard')" />
    <van-grid square :column-num="3">
      <van-grid-item @click="navigate('/modify-pass')">
        <van-icon name="closed-eye" size="3rem"/>
        <p>修改密码</p>
      </van-grid-item>
      <van-grid-item @click="navigate('/system-setting')">
        <van-icon name="setting-o" size="3rem" />
        <p>系统设置</p>
      </van-grid-item>
      <van-grid-item @click="navigate('/sms-setting')">
        <van-icon name="comment-circle-o" size="3rem" />
        <p>短信设置</p>
      </van-grid-item>
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