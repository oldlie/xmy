<template>
  <div style="margin: 60px 0 0 0">
    <van-form @submit="onSubmit">
      <van-field
        v-model="username"
        name="用户名"
        label="用户名"
        placeholder="用户名"
        :rules="usernameRule"
      />
      <van-field
        v-model="password"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules="passwordRule"
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
      username: "",
      usernameRule: [{ required: true, message: "请填写用户名" }],
      password: "",
      passwordRule: [{ required: true, message: "请填写密码" }],
      loading: false,
    };
  },
  mounted() {},
  methods: {
    onSubmit() {
      const url = "/login";
      const fd = new FormData();
      fd.append("username", this.username);
      fd.append("password", this.password);
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            const token = data.item;
            this.$cookie.set("token", token);
            this.$router.push('/dashboard');
          } else {
            this.$notify({type: 'danger', message: decodeURI(data['message'])});
          }
        })
        .fcb(() => { this.loading = false })
        .req();
    },
  },
};
</script>

<style>
</style>