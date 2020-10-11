<template>
  <div>
    <van-nav-bar title="修改密码" left-text="返回" @click-left="onClickLeft" />

    <van-form @submit="onSubmit">
      <van-field
        v-model="password"
        type="password"
        name="旧密码"
        label="旧密码"
        placeholder="密码"
        :rules="passwordRule"
      />
      <van-field
        v-model="newpass"
        name="新密码"
        label="新密码"
        placeholder="新密码"
        :rules="newpassRule"
      />
    <div style="margin: 16px">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          :loading="loading"
          >提交</van-button
        >
      </div>
    </van-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      password: "",
      passwordRule: [{ required: true, message: "请填写密码" }],
      newpass: '',
      newpassRule: [{ required: true, message: "请填写密码" }],
      loading: false
    };
  },
  methods: {
    onClickLeft() {
      this.$router.push("/system");
    },
    onSubmit() {
        const url = '/api/system/modify-password';
        const fd = new FormData();
        fd.append('op', this.password);
        fd.append('np', this.newpass);
        this.loading = true;
        this.$h.post(url, fd)
        .cb(data => {
            if(data.status === 0) {
                this.$notify({type: 'success', message: "已修改"});
            }else {
                this.$notify({type: 'danger', message: decodeURI(data.message)});
            }
        })
        .fcb(() => this.loading = false)
        .req();
    },
  },
};
</script>

<style>
</style>